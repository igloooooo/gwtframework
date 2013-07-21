package com.iglooit.core.base.client.widget.form;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.extjs.gxt.ui.client.event.ComponentEvent;

import java.util.List;

public class ValidatableLongField extends ValidatableTextFieldBase<Long>
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

    protected Long convertType(String typedValue)
    {
        try
        {
            Long i = Long.valueOf(typedValue);
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
        return 0L;
    }
}
