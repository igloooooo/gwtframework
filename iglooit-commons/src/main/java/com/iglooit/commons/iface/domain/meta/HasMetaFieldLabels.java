package com.iglooit.commons.iface.domain.meta;

public interface HasMetaFieldLabels
{
    String getDefaultFieldLabel(String propertyName);

    String getDefaultFieldUsageHint(String propertyName);
}
