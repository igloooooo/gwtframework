package com.iglooit.core.base.iface.validation;

import com.clarity.core.account.iface.domain.UserRole;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class PasswordValidator extends Validator
{
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    // part of me dies writing this :(
    private static final Collection<Character> VALID_PASSWORD_CHARS = getValidChars();

    private static Collection<Character> getValidChars()
    {
        final String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890_" +
            "<>!#$%^*()_-+={[}]|:; ";
        TreeSet<Character> set = new TreeSet<Character>();
        for (char c : validChars.toCharArray())
            set.add(c);
        return Collections.unmodifiableSet(set);
    }

    public PasswordValidator(String tag)
    {
        super(tag);
    }

    public List<ValidationResult> validate(Meta instance)
    {
        List<ValidationResult> results = new ArrayList<ValidationResult>();
        results.addAll(validateChars(instance));
        results.addAll(validateSameAsUsername(instance));
        return results;
    }

    private List<ValidationResult> validateChars(Meta instance)
    {
        if (instance instanceof UserRole)
        {
            char[] password = StringUtil.emptyStringIfNull(((UserRole)instance).getPassword()).toCharArray();
            for (char c : password)
                if (!VALID_PASSWORD_CHARS.contains(c))
                    return Arrays.asList(new ValidationResult(getTags(), VC.passwordInvalidChars()));
        }
        return Collections.emptyList();
    }

    private List<ValidationResult> validateSameAsUsername(Meta instance)
    {
        if (instance instanceof UserRole)
        {
            UserRole userRole = (UserRole)instance;
            if (userRole.getUsername() != null
                && userRole.getPassword() != null
                && userRole.getUsername().equalsIgnoreCase(userRole.getPassword()))
                return Arrays.asList(new ValidationResult(getTags(), VC.passwordSameAsUsername()));
        }
        return Collections.emptyList();
    }
}
