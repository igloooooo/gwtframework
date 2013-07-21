package com.iglooit.core.security.iface.access.domain;

/**
 * Represents a enumeration of privileges.
 * Each child of this must be listed via PrivilegeCategories
 * @see Privileges.PrivilegeCategories
 */
public interface PrivilegeConst
{
    String getPrivilegeString();
}
