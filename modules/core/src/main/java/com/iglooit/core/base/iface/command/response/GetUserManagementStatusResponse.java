package com.iglooit.core.base.iface.command.response;

import com.clarity.core.base.iface.command.Response;

public class GetUserManagementStatusResponse extends Response
{
    private boolean passwordChangesAllowed;

    public GetUserManagementStatusResponse()
    {
    }

    public GetUserManagementStatusResponse(boolean passwordChangesAllowed)
    {
        this.passwordChangesAllowed = passwordChangesAllowed;
    }

    public boolean isPasswordChangesAllowed()
    {
        return passwordChangesAllowed;
    }
}
