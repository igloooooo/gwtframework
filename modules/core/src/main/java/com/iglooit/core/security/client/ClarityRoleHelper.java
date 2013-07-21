package com.iglooit.core.security.client;

import com.clarity.core.account.iface.domain.UserOrgSecurityRole;
import com.clarity.core.account.iface.domain.UserRole;
import com.clarity.core.command.client.DefaultRequestCallback;

import java.util.ArrayList;
import java.util.List;

public class ClarityRoleHelper<T extends Enum<T>>
{
    private Class<T> clarityRole;

    private T mostPreviledgedRole;
    private T leastPreviledgedRole;

    public ClarityRoleHelper(Class<T> clarityRole)
    {
        this.clarityRole = clarityRole;
        mostPreviledgedRole = getValues()[0];
        leastPreviledgedRole = getValues()[getValues().length - 1];
    }

    /**
     * Alternative to valueOf to find a value without throwing an exception.
     * @param roleName the string to look for a match for.
     */
    public T findRoleByName(String roleName)
    {
        for (T enumValue : getValues())
        {
            if (enumValue.toString().equals(roleName))
                return enumValue;
        }
        return null;
    }

    public T findUsersMostPrivilegedRole(UserRole loginUser)
    {
        T userRole = leastPreviledgedRole;
        for (UserOrgSecurityRole role : loginUser.getUserOrgSecurityRoles())
        {
            String roleName = role.getOrgSecurityRole().getSecurityRoleName();
            T nextUserRole = findRoleByName(roleName);
            if (nextUserRole != null)
            {
                userRole = getMorePrivilegedRole(nextUserRole, userRole);
                if (userRole.equals(mostPreviledgedRole)) //should be near same efficiency as an ordinal == 0;
                    return userRole;
            }
        }
        return userRole;
    }

    /**
     * get all roles of current login user
     * @return
     */
    public List<T> getLoginUserRoles()
    {
        List<T> userRoles = new ArrayList<T>();
        for (UserOrgSecurityRole role : DefaultRequestCallback.getCachedUserRole().getUserOrgSecurityRoles())
        {
            String roleName = role.getOrgSecurityRole().getSecurityRoleName();
            T nextUserRole = findRoleByName(roleName);
            userRoles.add(nextUserRole);
        }
        return userRoles;
    }

    /**
     * returns the role which has the most privileges
     *
     * @param role1 a non-null role
     * @param role2 a non-null role
     * @return the passed in role with more privs
     */
    public T getMorePrivilegedRole(T role1, T role2)
    {
        return role1.ordinal() <= role2.ordinal() ? role1 : role2;
    }

    public T findCurrentUsersMostPrivilegedRole()
    {
        return findUsersMostPrivilegedRole(DefaultRequestCallback.getCachedUserRole());
    }

    public boolean isCurrentUserMostPrivileged()
    {
        return findCurrentUsersMostPrivilegedRole() == mostPreviledgedRole;
    }

    public UserRole findCurrentUserRole()
    {
        return DefaultRequestCallback.getCachedUserRole();
    }

    public T[] getValues()
    {
        return clarityRole.getEnumConstants();
    }

    public boolean hasPrivilegedRole()
    {
        for (UserOrgSecurityRole role : DefaultRequestCallback.getCachedUserRole().getUserOrgSecurityRoles())
        {
            String roleName = role.getOrgSecurityRole().getSecurityRoleName();
            T nextUserRole = findRoleByName(roleName);
            if (nextUserRole != null)
            {
                return true;
            }
        }
        return false;
    }

}
