package com.iglooit.coreum.security.iface.access.domain;

public interface Subject
{
    boolean hasPrivilege(Privilege applicablePrivilege);
}
