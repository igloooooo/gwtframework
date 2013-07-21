package com.iglooit.core.base.iface.command.response;

import com.clarity.core.base.iface.command.Response;

public class GeneralUpdateResponse extends Response
{
    private Boolean success;
    private String remarks;

    public GeneralUpdateResponse()
    {
    }

    public GeneralUpdateResponse(Boolean success, String remarks)
    {
        this.success = success;
        this.remarks = remarks;
    }

    public Boolean getSuccess()
    {
        return success;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }
}
