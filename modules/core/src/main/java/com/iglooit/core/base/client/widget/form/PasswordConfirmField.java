package com.iglooit.core.base.client.widget.form;

public class PasswordConfirmField extends PasswordField
{
    private final PasswordField passwordField;

    public PasswordConfirmField(PasswordField passwordField)
    {
        this.passwordField = passwordField;
    }

    public PasswordField getPasswordField()
    {
        return passwordField;
    }
}
