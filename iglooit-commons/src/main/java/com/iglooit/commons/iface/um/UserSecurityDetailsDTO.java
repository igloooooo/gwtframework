package com.iglooit.commons.iface.um;

import java.io.Serializable;
import java.util.Set;

public class UserSecurityDetailsDTO implements Serializable
{
    private UserLoginStatus userLoginStatus;
    private String privilege;
    private Set<String> authorities;
    private UserRoleDTO userRole;
    private String errorMessage;

    public UserLoginStatus getUserLoginStatus()
    {
        return userLoginStatus;
    }

    public void setUserLoginStatus(UserLoginStatus userLoginStatus)
    {
        this.userLoginStatus = userLoginStatus;
    }

    public String getPrivilege()
    {
        return privilege;
    }

    public void setPrivilege(String privilege)
    {
        this.privilege = privilege;
    }

    public Set<String> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities)
    {
        this.authorities = authorities;
    }

    public UserRoleDTO getUserRole()
    {
        return userRole;
    }

    public void setUserRole(UserRoleDTO userRole)
    {
        this.userRole = userRole;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public UserSecurityDetailsDTO()
    {
    }

    public UserSecurityDetailsDTO(UserLoginStatus userLoginStatus, String privilege, Set<String> authorities,
                                  UserRoleDTO userRole)
    {
        this(userLoginStatus, privilege, authorities, userRole, null);
    }

    public UserSecurityDetailsDTO(UserLoginStatus userLoginStatus, String privilege, Set<String> authorities,
                                  UserRoleDTO userRole, String errorMessage)
    {
        this.userLoginStatus = userLoginStatus;
        this.privilege = privilege;
        this.authorities = authorities;
        this.userRole = userRole;
        this.errorMessage = errorMessage;
    }

    public boolean isGranted()
    {
        return userLoginStatus.isGranted();
    }

    public boolean isExpired()
    {
        return userLoginStatus.isExpired();
    }

    @Override
    public String toString()
    {
        return "UserSecurityDetailsDTO{" +
            "userLoginStatus=" + userLoginStatus +
            ", privilege='" + privilege + '\'' +
            ", authorities=" + authorities +
            ", userRole=" + userRole +
            ", errorMessage='" + errorMessage + '\'' +
            '}';
    }
}
