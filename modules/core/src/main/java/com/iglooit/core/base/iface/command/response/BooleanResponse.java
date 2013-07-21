package com.iglooit.core.base.iface.command.response;

import com.clarity.core.base.iface.command.Response;

public class BooleanResponse extends Response
{
    private Boolean match;

    public BooleanResponse()
    {

    }

    public BooleanResponse(Boolean match)
    {
        this.match = match;
    }

    public Boolean isMatch()
    {
        return match;
    }

    public void setMatch(Boolean match)
    {
        this.match = match;
    }
}
