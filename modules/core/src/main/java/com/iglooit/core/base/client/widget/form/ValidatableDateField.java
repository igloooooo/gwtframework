package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.DelayedTimeValidationResultHandler;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.OutputDateFormats;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.Date;
import java.util.List;

public class ValidatableDateField extends ClarityDateField implements ClarityField<Date, DateField>
{
    public static final String TRANSFER_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String TRANSFER_DATE_FORMAT_PLUS = "dd-MM-yyyy HH:mm:ss.0";
    public static final String META_DATE_FORMAT_PLUS = "yyyy-MM-dd HH:mm:ss.0";
    public static final String DISPLAY_DATE_FORMAT = "EEE dd-MMMM-yyyy";

    private boolean fireChangeEvents = true;
    private final DelayedTimeValidationResultHandler<Date> timeErrorHandler;

    public ValidatableDateField()
    {
        this(OutputDateFormats.DATE_FRIENDLY);
    }

    //  display validatebleDateField in a specific format.
    public ValidatableDateField(OutputDateFormats format)
    {
        super();
        this.timeErrorHandler = new DelayedTimeValidationResultHandler<Date>(this, new Runnable()
        {
            public void run()
            {
                fireValidationChangeEvent();
            }
        });

        getImages().setInvalid(Resource.ICONS.exclamationRed());
        getPropertyEditor().setFormat(format.toDateTimeFormat());
        setEditable(false);
    }

    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        timeErrorHandler.handleValidationResults(validationResultList);
    }

    public void valueExternallyChangedFrom(Date oldLocalValue)
    {
        // override this
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        timeErrorHandler.fireEvent(gwtEvent);
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Date> dValueChangeHandler)
    {
        return timeErrorHandler.addValueChangeHandler(dValueChangeHandler);
    }

    private void fireValidationChangeEvent()
    {
        if (fireChangeEvents)
        {
            Date value = getValue();
            ValueChangeEvent.fire(ValidatableDateField.this, value);
        }
    }

    @Override
    public Date getValue()
    {
        return super.getValue();
    }

    @Override
    public void setValue(Date d)
    {
        setValue(d, false);
    }

    public void setValue(Date d, boolean b)
    {
        fireChangeEvents = b;
        super.setValue(d);
        fireValidationChangeEvent();
        fireChangeEvents = true;
    }

    @Override
    public String getFieldLabel()
    {
        return StringUtil.emptyStringIfNull(super.getFieldLabel());
    }

    public Option<String> getUsageHint()
    {
        return Option.option(getEmptyText());
    }

    public void setUsageHint(String usageHint)
    {
        setEmptyText(usageHint);
    }

    @Override
    public boolean validate()
    {
        // only use binder validator, not gxt
        return true;
    }

    public DateField getField()
    {
        return this;
    }
}
