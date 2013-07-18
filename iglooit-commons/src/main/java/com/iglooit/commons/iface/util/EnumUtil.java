package com.iglooit.commons.iface.util;

import com.clarity.commons.iface.type.AppX;

public class EnumUtil
{
    public static <T extends Enum<T>> T convertStringToEnumName(String name, Class<T> clazz, String fieldName)
            throws AppX
    {
        if (name == null)
            return null;
        try
        {
            return Enum.valueOf(clazz, name);
        }
        catch (IllegalArgumentException argExp)
        {
            throw new AppX(fieldName + ": Invalid value " + name);
        }
    }
}
