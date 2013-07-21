package com.iglooit.core.base.iface.command.response;

import com.clarity.core.base.iface.command.Response;
import com.clarity.commons.iface.type.Option;

public class OptionResponse<T> extends Response
{
    private Option<T> opt;

    public OptionResponse(Option<T> opt)
    {
        this.opt = opt;
    }

    public OptionResponse()
    {
    }

    public Option<T> getOpt()
    {
        return opt;
    }

    public void setOpt(Option<T> opt)
    {
        this.opt = opt;
    }
}
