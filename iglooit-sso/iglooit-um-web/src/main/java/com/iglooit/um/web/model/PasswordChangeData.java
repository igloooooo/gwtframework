package com.iglooit.um.web.model;

import com.iglooit.commons.iface.um.UserPasswordUpdateStatus;

public class PasswordChangeData
{
    private String username;
    private String password;
    private String newPassword;
    private String confirmPassword;
    private UserPasswordUpdateStatus result;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNewPassword()
    {
        return newPassword;
    }

    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword()
    {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword)
    {
        this.confirmPassword = confirmPassword;
    }

    public UserPasswordUpdateStatus getResult()
    {
        return result;
    }

    public void setResult(UserPasswordUpdateStatus result)
    {
        this.result = result;
    }
}
