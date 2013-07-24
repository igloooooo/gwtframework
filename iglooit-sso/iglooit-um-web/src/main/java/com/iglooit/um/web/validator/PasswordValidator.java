package com.iglooit.um.web.validator;

import com.iglooit.um.web.model.PasswordChangeData;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

public class PasswordValidator implements Validator
{
    @Resource
    private String clientDefaultAccount;

    private String orgName;

    private String finalUsername;

    public void setClientDefaultAccount(String clientDefaultAccount)
    {
        this.clientDefaultAccount = clientDefaultAccount;
    }

    public String getOrgName()
    {
        return orgName;
    }

    public String getFinalUsername()
    {
        return finalUsername;
    }

    @Override
    public boolean supports(Class clazz)
    {
        //just validate the PasswordChangeData instances
        return PasswordChangeData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        ValidationUtils
            .rejectIfEmptyOrWhitespace(errors, "username", "required.username", "Field username is required.");
        ValidationUtils
            .rejectIfEmptyOrWhitespace(errors, "password", "required.password", "Field password is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "required.newPassword",
            "Field new password is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "required.confirmPassword",
            "Field confirm password is required.");

        orgName = clientDefaultAccount;
        PasswordChangeData cust = (PasswordChangeData)target;
        finalUsername = cust.getUsername();
        if (orgName == null || orgName.length() < 1)
        {
            String[] organizationIndividual = finalUsername.split("/");
            if (organizationIndividual.length != 2)
                errors.rejectValue("username", "invalid.username", "The username is invalid.");
            orgName = organizationIndividual[0];
            finalUsername = organizationIndividual[1];
        }
        if (!(cust.getNewPassword().equals(cust.getConfirmPassword())))
            errors.rejectValue("newPassword", "notmatch.password", "Passwords don't match.");
    }
}
