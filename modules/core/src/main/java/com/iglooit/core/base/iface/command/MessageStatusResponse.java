package com.iglooit.core.base.iface.command;

public class MessageStatusResponse extends Response
{
    private boolean success;
    private String message;

    public MessageStatusResponse()
    {
    }

    public MessageStatusResponse(boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public String getMessage()
    {
        return message;
    }
}
