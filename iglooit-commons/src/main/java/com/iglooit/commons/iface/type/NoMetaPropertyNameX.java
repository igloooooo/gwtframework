package com.iglooit.commons.iface.type;

public class NoMetaPropertyNameX extends AppX
{
    private String propertyName;

    public NoMetaPropertyNameX()
    {
    }

    public NoMetaPropertyNameX(String func, String propertyName)
    {
        super("No meta property name in meta." + func + ": " + propertyName);
        this.propertyName = propertyName;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    protected void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }
}
