package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.I18NFactoryProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class XORFieldRequiredValidator extends XORDelegatingValidator
{
    private static final ValidationConstants VALIDATION_CONSTANTS = I18NFactoryProvider.get(ValidationConstants.class);
    private Map<String, String> propertyNameFieldLabelMap;

    /**
     * @param propertyNameFieldLabelMap Map of String<ValidatorTag, ValidatorDisplayName>
     */
    public XORFieldRequiredValidator(Map<String, String> propertyNameFieldLabelMap)
    {
        super(propertyNameFieldLabelMap.keySet(), getRequiredFieldValidators(propertyNameFieldLabelMap.keySet()));
        this.propertyNameFieldLabelMap = propertyNameFieldLabelMap;
    }

    private static Collection<Validator> getRequiredFieldValidators(Set<String> set)
    {
        Collection<Validator> validators = new ArrayList<Validator>();
        for (String validatorTag : set)
        {
            validators.add(new RequiredField(validatorTag));
        }
        return validators;
    }

    @Override
    public String getErrorString(Meta instance)
    {
        String errorMessage = VALIDATION_CONSTANTS.onlyOneFieldMustPass();

        if (propertyNameFieldLabelMap != null)
        {
            //make a custom error msg.
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : propertyNameFieldLabelMap.entrySet())
            {
                builder.append(" ");
                builder.append(entry.getValue());
            }
            errorMessage += builder.toString();

        }
        return errorMessage;

    }
}
