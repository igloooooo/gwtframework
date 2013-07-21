package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.commons.iface.domain.I18NFactoryProvider;

public class LengthLimitedTextField extends ValidatableTextField
{
    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    @Override()
    public boolean validate()
    {
        if (getValue() != null && getValue().length() > getMaxLength())
        {
            forceInvalid(BVC.maxLengthExceed(getMaxLength()));
            return false;
        }
        return super.validate();
    }
}
