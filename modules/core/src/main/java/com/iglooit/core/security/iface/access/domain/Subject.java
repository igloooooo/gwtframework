package com.iglooit.core.security.iface.access.domain;

public interface Subject
{
    boolean hasPrivilege(Privilege applicablePrivilege);

}
