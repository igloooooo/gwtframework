package com.iglooit.core.base.iface.domain;

import java.util.Map;

public class InstanceDerivedMeta extends MapDerivedMeta
{
    private String instanceClassName;

    public InstanceDerivedMeta()
    {
    }

    public InstanceDerivedMeta(String instanceClassName,
                               Map<String, Object> values,
                               Map<String, String> classes)
    {
        super(values, classes);
        this.instanceClassName = instanceClassName;
    }

    public String getInstanceClassName()
    {
        return instanceClassName;
    }

    public void setInstanceClassName(String instanceClassName)
    {
        this.instanceClassName = instanceClassName;
    }
}
