package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.extjs.gxt.ui.client.event.ComponentEvent;

import java.util.List;

public class ValidatableIntegerField extends ValidatableTextFieldBase<Integer>
{
    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);
    private boolean isConvertError = false;

    @Override
    protected void onBlur(ComponentEvent be)
    {
        super.onBlur(be);
        fireValueChangeEvent();
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        super.handleValidationResults(validationResultList);
        if (validationResultList.size() == 0 && isConvertError)
            forceInvalid(BVC.invalidInteger());
    }

    protected Integer convertType(String typedValue)
    {
        try
        {
            Integer i = Integer.valueOf(typedValue);
            if (isConvertError)
            {
                clearInvalid();
                isConvertError = false;
            }
            return i;
        }
        catch (NumberFormatException e)
        {
            isConvertError = true;
            forceInvalid(BVC.invalidInteger());
        }
        if (typedValue == null || typedValue.trim().length() == 0)
            return null;
        return 0;
    }
}
