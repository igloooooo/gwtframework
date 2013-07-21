package com.iglooit.core.base.client.composition.display;

public enum FormMode
{
    READ("form-read"),
    UPDATE("form-updated"),
    CREATE("form-create");

    private final String value;

    private FormMode(String value)
    {
        this.value = value;
    }

    public String value()
    {
        return value;
    }
}
