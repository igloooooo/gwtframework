package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.iface.domain.HasValidatingValue;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class TitledValidatingFormField implements HasValidatingValue<String>
{
    private final Label title;
    private final TextBox field;
    private final Label errors;

    public TitledValidatingFormField(String titleString)
    {
        field = new TextBox();
        title = new Label(titleString);
        errors = new Label("");
    }

    public Widget asWidget()
    {
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(title);
        hp.add(field);
        hp.add(errors);
        return hp;
    }

    public void handleValidationError(List<ValidationResult> validationResultList)
    {
        if (validationResultList.size() == 0)
            handleValidationSuccess();
        else
            errors.setText(validationResultList.get(0).getReason());
    }

    public void handleValidationSuccess()
    {
        errors.setText("");
    }

    public String getValue()
    {
        return field.getValue();
    }

    public void setValue(String s)
    {
        field.setValue(s);
    }

    public void setValue(String s, boolean b)
    {
        field.setValue(s, b);
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> stringValueChangeHandler)
    {
        return field.addValueChangeHandler(stringValueChangeHandler);
    }

    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        field.fireEvent(gwtEvent);
    }

    public Label getTitle()
    {
        return title;
    }

    public TextBox getField()
    {
        return field;
    }

    public Label getErrors()
    {
        return errors;
    }

    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        if (validationResultList.size() == 0)
            errors.setText("");
        else
            errors.setText(validationResultList.get(0).getReason());
    }

    public void valueExternallyChangedFrom(String oldLocalValue)
    {
        errors.setText("Value changed from external event: " + oldLocalValue);
    }

    public String getFieldLabel()
    {
        return StringUtil.emptyStringIfNull(title.getText());
    }

    public void setFieldLabel(String fieldLabel)
    {
        title.setText(fieldLabel);
    }

    public Option<String> getUsageHint()
    {
        return Option.none();
    }

    public void setUsageHint(String usageHint)
    {

    }
}
