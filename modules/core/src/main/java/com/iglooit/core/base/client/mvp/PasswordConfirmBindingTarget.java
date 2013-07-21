package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.form.PasswordConfirmField;
import com.clarity.core.base.client.widget.form.ValidatableTextField;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.core.base.iface.domain.ValidationHandler;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.core.base.iface.validation.RequiredField;
import com.clarity.core.base.iface.validation.ValidationConstants;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordConfirmBindingTarget implements ValidatableMeta
{
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);
    private String value;
    private final String fieldLabel;
    private final String propertyName;
    private final transient ValidatableTextField passwordField;
    private final boolean isRequiredField;
    private transient ValidationHandler validationHandler;
    private transient HandlerRegistration passwordFieldHandlerReg;
    private transient HandlerRegistration passwordConfirmChangePropogatorHandlerRequest;
    private transient PasswordConfirmValidator validator;
    private transient RequiredField requiredFieldValidator;

    protected PasswordConfirmBindingTarget(String fieldLabel,
                                           String propertyName,
                                           ValidatableTextField passwordField,
                                           boolean isRequiredField)
    {
        this.fieldLabel = fieldLabel;
        this.propertyName = propertyName;
        this.passwordField = passwordField;
        this.isRequiredField = isRequiredField;
        this.validator = new PasswordConfirmValidator(propertyName);
        this.requiredFieldValidator = new RequiredField(propertyName);
    }

    public static ValidatableTextFieldBaseBinder<String> getBinder(final PasswordConfirmField passwordConfirmField,
                                                                   final ValidatableTextField passwordField,
                                                                   final String passwordPropertyname,
                                                                   final ValidatableMeta passwordTarget)
    {
        boolean isRequiredField = false;
        for (Validator validator : passwordTarget.getValidators(Option.some(passwordPropertyname)))
            if (validator instanceof RequiredField)
                isRequiredField = true;

        final PasswordConfirmBindingTarget target =
            new PasswordConfirmBindingTarget(
                passwordConfirmField.getFieldLabel(),
                passwordPropertyname,
                passwordField,
                isRequiredField);

        target.passwordFieldHandlerReg =
            passwordField.addValueChangeHandler(new ValueChangeHandler<String>()
            {
                public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent)
                {
                    if (StringUtil.emptyStringIfNull(passwordConfirmField.getValue()).length() > 0)
                        target.validateAndNotify(Option.option(passwordPropertyname));
                }
            });

        target.passwordConfirmChangePropogatorHandlerRequest =
            passwordConfirmField.addValueChangeHandler(new ValueChangeHandler<String>()
            {
                public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent)
                {
                    passwordTarget.validateAndNotify(Option.some(passwordPropertyname));
                }
            });

        ValidatableTextFieldBaseBinder<String> binder =
            new ValidatableTextFieldBaseBinder<String>(passwordConfirmField, target, passwordPropertyname);
        passwordConfirmField.setAllowBlank(true);
        return binder;
    }

    public String getDefaultFieldLabel(String propertyName)
    {
        return fieldLabel;
    }

    public String getDefaultFieldUsageHint(String propertyName)
    {
        return "";
    }

    public final class PasswordConfirmValidator extends Validator
    {
        private PasswordConfirmValidator(String... tags)
        {
            super(tags);
        }

        public List<ValidationResult> validate(Meta instance)
        {
            String passwordFieldVal = StringUtil.emptyStringIfNull(passwordField.getValue());
            if (!passwordFieldVal.equals(StringUtil.emptyStringIfNull(value)))
                return Arrays.asList(new ValidationResult(Arrays.asList(propertyName), VC.passwordsDontMatch()));
            return new ArrayList<ValidationResult>();
        }
    }

    public HandlerRegistration registerValidationHandler(ValidationHandler validationHandler, String propertyName)
    {
        this.validationHandler = validationHandler;
        return new HandlerRegistration()
        {
            public void removeHandler()
            {
                clearValidationHandlers();
            }
        };
    }

    public void clearValidationHandlers()
    {
        validationHandler = null;
        if (passwordFieldHandlerReg != null)
            passwordFieldHandlerReg.removeHandler();
        passwordFieldHandlerReg = null;
        if (passwordConfirmChangePropogatorHandlerRequest != null)
            passwordConfirmChangePropogatorHandlerRequest.removeHandler();
        passwordConfirmChangePropogatorHandlerRequest = null;
    }

    public <X> X get(String propertyName)
    {
        if (this.propertyName.equals(propertyName))
            return (X)value;
        return null;
    }

    public String getPropertyTypeName(String propertyName)
    {
        if (this.propertyName.equals(propertyName))
            return String.class.getName();
        return null;
    }

    public Map<String, Object> getProperties()
    {

        HashMap<String, Object> ret = new HashMap<String, Object>();
        for (String propertyName : getPropertyNames())
            ret.put(propertyName, get(propertyName));
        return ret;
    }

    public List<String> getPropertyNames()
    {
        return Arrays.asList(propertyName);
    }

    public void set(String propertyName, Object value)
    {
        if (this.propertyName.equals(propertyName))
            this.value = (String)value;
    }

    public List<Validator> getValidators(Option<String> propertyNameOption)
    {
        List<Validator> validators = new ArrayList<Validator>();
        if (propertyNameMatch(propertyNameOption))
        {
            validators.add(validator);
            if (isRequiredField)
                validators.add(requiredFieldValidator);
        }
        return validators;
    }

    private boolean propertyNameMatch(Option<String> prOption)
    {
        return prOption.isNone() || prOption.value().equals(propertyName);
    }

    public List<ValidationResult> validate(Option<String> metaFieldName)
    {
        return validate(metaFieldName, Arrays.asList((Validator)validator));
    }

    public List<ValidationResult> validate(Option<String> metaFieldName, List<Validator> validators)
    {
        List<ValidationResult> validationResults = new ArrayList<ValidationResult>();
        if (propertyNameMatch(metaFieldName))
            for (Validator v : validators)
                validationResults.addAll(v.validate(this));
        return validationResults;
    }

    public void validateAndNotify(Option<String> metaFieldName)
    {
        if (validationHandler != null)
            validationHandler.handleValidationResults(validate(metaFieldName));
    }
}
