package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.iface.domain.HasValidatingValue;
import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.core.base.iface.domain.ValidationHandler;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public class MutableBinder<X> extends LabelledBinder<X>
    implements ValueChangeHandler<X>, ValidationHandler
{
    private final HasValidatingValue<X> validatingValue;
    private final ValidatableMeta validatableMeta;
    private HandlerRegistration widgetHandlerRegistration;
    private X originalMetaValue;
    private HandlerRegistration validationHandlerRegistration;
    private boolean modified = false;

    public MutableBinder(HasValidatingValue<X> validatingValue,
                         ValidatableMeta validatableMeta,
                         String metaFieldName)
    {
        super(validatingValue, validatableMeta, metaFieldName);

        this.validatingValue = validatingValue;
        this.validatableMeta = validatableMeta;
        this.originalMetaValue = (X)validatableMeta.get(metaFieldName);

        restoreOriginalValue();

        widgetHandlerRegistration = validatingValue.addValueChangeHandler(this);
        validationHandlerRegistration = validatableMeta.registerValidationHandler(this, metaFieldName);
    }

    public void restoreOriginalValue()
    {
        validatableMeta.set(getMetaFieldName(), originalMetaValue);
        validatingValue.setValue(originalMetaValue, false);
        if (originalMetaValue != null && !originalMetaValue.equals(validatingValue.getValue()))
            handleUnsettableValue();
        modified = false;
    }

    // may be overridden for custom binder behavior, if setting to null is unacceptable
    protected void handleUnsettableValue()
    {
        GWT.log("Warning: widget bound to propertyName: " + getMetaFieldName() +
            " refuses original value, perhaps due to invalid combo selection: " + originalMetaValue, null);
    }

    public void onValueChange(ValueChangeEvent<X> xValueChangeEvent)
    {
        X value = xValueChangeEvent.getValue();
        validatableMeta.set(getMetaFieldName(), value);
        modified = true;
        forceRevalidate();
    }

    public void handleValidationResults(List<ValidationResult> resultList)
    {
        for (ValidationResult validationResult : resultList)
            validationResult.setFieldLabel(getFieldLabel());
        validatingValue.handleValidationResults(resultList);
    }

    public boolean isModified()
    {
        return modified;
    }

    public void unbind()
    {
        getTracker().removeBinder(this);
        if (widgetHandlerRegistration != null)
            widgetHandlerRegistration.removeHandler();
        if (validationHandlerRegistration != null)
            validationHandlerRegistration.removeHandler();
    }

    public void undoModifications()
    {
        restoreOriginalValue();
    }

    public void forceRevalidate()
    {
        validatableMeta.validateAndNotify(Option.some(getMetaFieldName()));
    }

    public boolean isValueValid()
    {
        forceRevalidate();
        List<ValidationResult> validateResults =
            validatableMeta.validate(Option.some(getMetaFieldName()));
        return validateResults == null || validateResults.size() == 0;
    }

    public List<ValidationResult> getValidationResults()
    {
        return validatableMeta.validate(Option.some(getMetaFieldName()));
    }

    public void setModified(boolean modified)
    {
        this.modified = modified;
    }

    public HasValidatingValue<X> getValidatingValue()
    {
        return validatingValue;
    }

    public ValidatableMeta getValidatableMeta()
    {
        return validatableMeta;
    }

    public HandlerRegistration getWidgetHandlerRegistration()
    {
        return widgetHandlerRegistration;
    }

    protected void setOriginalMetaValue(X value)
    {
        this.originalMetaValue = value;
    }
    public X getOriginalMetaValue()
    {
        return originalMetaValue;
    }

    public HandlerRegistration getValidationHandlerRegistration()
    {
        return validationHandlerRegistration;
    }

    public void saveValue()
    {
        modified = false;
        originalMetaValue = validatingValue.getValue();
        validatableMeta.set(getMetaFieldName(), originalMetaValue);
    }
}
