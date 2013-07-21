package com.iglooit.core.security.iface.access.domain;

public class PrivilegeRuleContext
{
    private Subject subject;
    private RestrictedResource resource;

    public PrivilegeRuleContext(Subject subject, RestrictedResource resource)
    {
        this.subject = subject;
        this.resource = resource;
    }

    public Subject getSubject()
    {
        return subject;
    }

    public RestrictedResource getResource()
    {
        return resource;
    }
}
