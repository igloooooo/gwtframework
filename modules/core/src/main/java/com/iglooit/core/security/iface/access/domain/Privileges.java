package com.iglooit.core.security.iface.access.domain;

import com.clarity.commons.iface.type.AppX;
import com.clarity.core.security.iface.access.domain.FunctionPrivileges.*;

/**
 * Constants file for the system privileges - these should map against the
 * 'name' column in the Privilege table;
 */

// todo ms: split privileges across relevant services
public class Privileges
{
    //todo jm: add relational data level privileges, eg: Employee of group X can edit only entity Defs of org M
    //todo jm: add user defined data level privileges, eg: Employee of group X can edit only M's entity Def Z, not A

    @Deprecated
    public static final String WORKFLOW_VIEW = getPrivilegeString(WorkflowPrivilege.VIEW);
    @Deprecated
    public static final String WORKFLOW_START = getPrivilegeString(WorkflowPrivilege.START);
    @Deprecated
    public static final String WORKFLOW_PAUSE = getPrivilegeString(WorkflowPrivilege.PAUSE);
    @Deprecated
    public static final String WORKFLOW_CREATE = getPrivilegeString(WorkflowPrivilege.CREATE);
    @Deprecated
    public static final String WORKFLOW_UPDATE = getPrivilegeString(WorkflowPrivilege.UPDATE);
    @Deprecated
    public static final String WORKFLOW_CANCEL = getPrivilegeString(WorkflowPrivilege.CANCEL);

    @Deprecated
    public static final String SYSTEM_LOGIN = getPrivilegeString(SystemPrivilege.LOGIN);
    @Deprecated
    public static final String PARTY_CREATE = getPrivilegeString(PartyPrivilege.CREATE);
    @Deprecated
    public static final String SYSTEM_ADMIN = getPrivilegeString(SystemPrivilege.ADMIN);
    @Deprecated
    public static final String EMPLOYEE_ADMIN = getPrivilegeString(EmployeePrivilege.ADMIN);
    @Deprecated
    public static final String PROFILE_ADMIN = getPrivilegeString(ProfilePrivilege.ADMIN);
    @Deprecated
    public static final String PARTY_READ = getPrivilegeString(PartyPrivilege.READ);
    @Deprecated
    public static final String PARTY_UPDATE = getPrivilegeString(PartyPrivilege.UPDATE);
    @Deprecated
    public static final String PARTY_DELETE = getPrivilegeString(PartyPrivilege.DELETE);

    @Deprecated
    public static final String CATALOG_READ = getPrivilegeString(CatalogPrivilege.READ);

    public static String getPrivilegeString(PrivilegeConst p)
    {
        return p.getPrivilegeString();
    }

    @Deprecated
    public static String[] getStringsForPrivileges(PrivilegeConst[] privilegeEnums)
    {
        //convenience method till we migrate to PrivilegeEnum everywhere.
        String[] stringPrivs = new String[privilegeEnums.length];
        for (int i = 0; i < privilegeEnums.length; i++)
        {
            stringPrivs[i] = privilegeEnums[i].getPrivilegeString();
        }
        return stringPrivs;
    }

    public static PrivilegeConst[] getPrivilegesForStrings(String[] strPrivs)
    {
        PrivilegeConst[] privs = new PrivilegeConst[strPrivs.length];
        for (int i = 0; i < privs.length; i++)
        {
            privs[i] = getPrivForString(strPrivs[i]);
        }
        return privs;
    }

    /**
     * This method is provided only for backwards compatibility, uses Magic,
     * and should be removed at earliest convenience.
     * @param s the priviledge string
     * @return the priv found
     */
    public static PrivilegeConst getPrivForString(String s)
    {
        if (s != null && s.length() > 0 && s.contains("."))
        {
            PrivilegeCategories category = Enum.valueOf(PrivilegeCategories.class,
                    s.substring(0, s.indexOf(".")).toUpperCase());
            if (category != null)
            {
                return category.getClassForCategory(s.substring(s.indexOf(".") + 1).toUpperCase());
            }
        }
        throw new AppX("No Priv found for " + s);
    }

    private enum PrivilegeCategories
    {
        CATALOG (CatalogPrivilege.class),
        EMPLOYEE (EmployeePrivilege.class),
        ENTITYDEF (EntityPrivilege.class),
        ENTITY (EntityPrivilege.class),
        PARTY (PartyPrivilege.class),
        PROFILE (ProfilePrivilege.class),
        SYSTEM (SystemPrivilege.class),
        WORKFLOW (WorkflowPrivilege.class);

        //todo jm: force ? to be Priv as well so I can remove the cast.
        private final Class<? extends Enum> privEnum;

        <T extends Enum & PrivilegeConst> PrivilegeCategories(Class<T> privEnum)
        {
            this.privEnum = privEnum;
        }

        PrivilegeConst getClassForCategory(String name)
        {
            PrivilegeConst p = (PrivilegeConst) Enum.valueOf(privEnum, name);
            return p;
        }
    }


}
