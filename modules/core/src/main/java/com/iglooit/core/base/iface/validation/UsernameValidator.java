package com.iglooit.core.base.iface.validation;

import com.clarity.core.account.iface.domain.UserRole;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UsernameValidator extends Validator
{
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    public UsernameValidator(String... tags)
    {
        super(tags);
    }

    public List<ValidationResult> validate(Meta instance)
    {
        if (instance instanceof UserRole)
        {
            char[] username = StringUtil.emptyStringIfNull(((UserRole)instance).getUsername()).toCharArray();
            if (username.length > 0 && !Character.isLetter(username[0]))
                return Arrays.asList(new ValidationResult(getTags(), VC.usernameFirstMustBeLetter()));
            for (char c : username)
                if (!(Character.isLetterOrDigit(c) || c == '_'))
                    return Arrays.asList(new ValidationResult(getTags(), VC.invalidUsername()));
        }
        return Collections.emptyList();
    }
}
