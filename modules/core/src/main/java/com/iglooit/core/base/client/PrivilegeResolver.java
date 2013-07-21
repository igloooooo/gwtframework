package com.iglooit.core.base.client;

import com.clarity.core.base.client.widget.SecuredWidgetDelegate;
import com.clarity.core.security.iface.access.domain.Privilege;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PrivilegeResolver
{
    private final HashMap<String, Privilege> privilegeMap;
    private final Set<PrivilegeConst> localPrivilegeConstraints;

    public PrivilegeResolver()
    {
        privilegeMap = new HashMap<String, Privilege>();
        localPrivilegeConstraints = new HashSet<PrivilegeConst>();
    }

    public boolean hasPrivilege(PrivilegeConst privilegeConst)
    {
        return privilegeMap.containsKey(privilegeConst.getPrivilegeString())
            && !localPrivilegeConstraints.contains(privilegeConst);
    }

    public void setLocalPrivilegeConstraints(Collection<PrivilegeConst> privileges)
    {
        clearLocalPrivilegeConstraints();
        localPrivilegeConstraints.addAll(privileges);
    }

    public void clearLocalPrivilegeConstraints()
    {
        localPrivilegeConstraints.clear();
    }

    public void addLocalPrivilegeConstraint(PrivilegeConst privilege)
    {
        localPrivilegeConstraints.add(privilege);
    }

    public void removeLocalPrivilegeConstraint(PrivilegeConst privilege)
    {
        localPrivilegeConstraints.remove(privilege);
    }

    /**
     * Adds the given collection privileges - here we are assuming that the
     * Privilege.getName().equals(PrivilegeConst.getPrivilegeString()) for the privilege concerned.
     *
     * @param privileges
     */
    public void addPrivileges(Collection<Privilege> privileges)
    {
        for (Privilege privilege : privileges)
        {
            privilegeMap.put(privilege.getName(), privilege);
        }
    }
    
    public void setPrivilegesMap(HashMap<String, Privilege> privilegesMap)
    {
        this.privilegeMap.putAll(privilegesMap);
    }

    public SecuredWidgetDelegate createDelegate(PrivilegeConst privilege)
    {
        return new SecuredWidgetDelegate(this, privilege);
    }
}
