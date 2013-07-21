package com.iglooit.core.lib.client;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.widget.form.TextField;

import java.util.List;

public class HasValueTextFieldAdapter<X> extends HasValidatingValueTextFieldAdapter<X>
{
    public HasValueTextFieldAdapter(TextField<X> textField)
    {
        super(textField);
    }

    public void handleValidationResults(List<ValidationResult> validationResultList)
    {

    }

    public void valueExternallyChangedFrom(X oldLocalValue)
    {

    }

    public String getFieldLabel()
    {
        return StringUtil.emptyStringIfNull(getTextField().getFieldLabel());
    }

    public void setFieldLabel(String fieldLabel)
    {
        getTextField().setFieldLabel(fieldLabel);
    }

    @Override
    public Option<String> getUsageHint()
    {
        return Option.option(getTextField().getEmptyText());
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        getTextField().setEmptyText(usageHint);
    }
}
