package com.iglooit.core.lib.iface;

public interface TypeConverterTwoWay<OldType, NewType>
{
    NewType convertToNew(OldType oldType);

    OldType convertToOld(NewType newType);
}

