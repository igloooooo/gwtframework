package com.iglooit.commons.iface.um;

import java.io.Serializable;
import java.util.List;

public class UserRoleDTO implements Serializable
{
    private String id;
    private String username;
    private List<String> securityRoles;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public List<String> getSecurityRoles()
    {
        return securityRoles;
    }

    public void setSecurityRoles(List<String> securityRoles)
    {
        this.securityRoles = securityRoles;
    }

    public UserRoleDTO()
    {
    }

    public UserRoleDTO(String id, String username)
    {
        this.id = id;
        this.username = username;
    }

    public UserRoleDTO(String id, String username, List<String> securityRoles)
    {
        this.id = id;
        this.username = username;
        this.securityRoles = securityRoles;
    }

    @Override
    public String toString()
    {
        return "UserRoleDTO{" +
            "id='" + id + '\'' +
            ", username='" + username + '\'' +
            '}';
    }
}
