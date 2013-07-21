package com.iglooit.core.base.iface.command;

import java.util.List;

public class StringListResponse extends Response
{
    private List<String> values;

    public StringListResponse()
    {

    }

    public StringListResponse(List<String> values)
    {
        this.values = values;
    }

    public List<String> getValues()
    {
        return values;
    }

    public void setValues(List<String> values)
    {
        this.values = values;
    }
}
