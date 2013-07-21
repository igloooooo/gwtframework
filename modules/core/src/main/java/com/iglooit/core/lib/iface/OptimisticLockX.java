package com.iglooit.core.lib.iface;

import com.clarity.commons.iface.type.AppX;

public class OptimisticLockX extends AppX
{
    public OptimisticLockX()
    {
    }

    public OptimisticLockX(String message)
    {
        super(message);
    }

    public OptimisticLockX(String message, Throwable cause)
    {
        super(message, cause);
    }
}
