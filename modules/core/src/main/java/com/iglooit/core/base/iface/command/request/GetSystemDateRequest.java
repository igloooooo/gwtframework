package com.iglooit.core.base.iface.command.request;

import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.response.GetSystemDateResponse;

import java.util.Date;

public class GetSystemDateRequest extends Request<GetSystemDateResponse>
{
    private Date localDate;

    public GetSystemDateRequest()
    {
    }

    public GetSystemDateRequest(Date localDate)
    {
        this.localDate = localDate;
    }

    public Date getLocalDate()
    {
        return localDate;
    }

    public void setLocalDate(Date localDate)
    {
        this.localDate = localDate;
    }
}
