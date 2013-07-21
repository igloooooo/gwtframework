package com.iglooit.core.base.client.mvp;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public class NullClarityField implements ClarityField<Void, TextField>
{
    public TextField getField()
    {
        return null;
    }

    public void handleValidationResults(List<ValidationResult> validationResultList)
    {

    }

    public void valueExternallyChangedFrom(Void oldLocalValue)
    {

    }

    public String getFieldLabel()
    {
        return null;
    }

    public void setFieldLabel(String fieldLabel)
    {

    }

    public Option<String> getUsageHint()
    {
        return null;
    }

    public void setUsageHint(String usageHint)
    {

    }

    public Void getValue()
    {
        return null;
    }

    public void setValue(Void aVoid)
    {

    }

    public void setValue(Void aVoid, boolean b)
    {

    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> voidValueChangeHandler)
    {
        return null;
    }

    public void fireEvent(GwtEvent<?> gwtEvent)
    {

    }
}
