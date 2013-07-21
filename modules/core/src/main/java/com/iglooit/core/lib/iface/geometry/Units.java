package com.iglooit.core.lib.iface.geometry;

public enum Units
{
    NONE(""),
    METRES("m"),
    FEET("ft"),
    DEGREES("deg");

    private String unitsString;

    Units()
    {
    }

    Units(String unitsString)
    {
        this.unitsString = unitsString;
    }

    @Override
    public String toString()
    {
        return unitsString;
    }
}
