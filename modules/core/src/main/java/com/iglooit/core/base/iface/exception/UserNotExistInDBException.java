package com.iglooit.core.base.iface.exception;

import com.clarity.commons.iface.type.AppX;

public class UserNotExistInDBException extends AppX
{
    public UserNotExistInDBException()
    {
    }

    public UserNotExistInDBException(String msg)
    {
        super(msg);
    }

    public UserNotExistInDBException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
