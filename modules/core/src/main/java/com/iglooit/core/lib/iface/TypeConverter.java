package com.iglooit.core.lib.iface;

public interface TypeConverter<OldType, NewType>
{
    NewType convert(OldType oldType);
}
