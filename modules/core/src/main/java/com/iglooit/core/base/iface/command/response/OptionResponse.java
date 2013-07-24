package com.iglooit.core.base.iface.command.response;

import com.iglooit.commons.iface.type.Option;
import com.iglooit.core.base.iface.command.Response;

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
