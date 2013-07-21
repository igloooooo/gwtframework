package com.iglooit.core.security.iface.access.domain;

public interface SecurityZone
{
    boolean matchesZone(SecurityZone subjectSecurityZone);

    boolean matchesParentZone(SecurityZone resourceSecurityZone);
}
