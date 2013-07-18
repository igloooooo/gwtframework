package com.iglooit.commons.iface.domain.meta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface Meta extends Serializable
{
    String ILLEGAL_CALL_TO_PROPERTY_SET = "It is illegal to manually set the lockVersion";
    String FIELD_META_SPLIT = ":";
    String FIELD_META_SPLIT_REGEX = "\\:";


    <X> X get(String propertyName);

    String getPropertyTypeName(String propertyName);

    Map<String, Object> getProperties();

    List<String> getPropertyNames();

    void set(String propertyName, Object value);
}
