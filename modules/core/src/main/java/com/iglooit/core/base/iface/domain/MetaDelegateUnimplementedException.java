package com.iglooit.core.base.iface.domain;

import com.clarity.commons.iface.type.AppX;

public class MetaDelegateUnimplementedException extends AppX
{
    public MetaDelegateUnimplementedException()
    {
    }

    public MetaDelegateUnimplementedException(String message)
    {
        super(message);
    }

    public MetaDelegateUnimplementedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
