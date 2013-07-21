package com.iglooit.core.lib.iface;

import com.clarity.commons.iface.type.AppX;

public class ConstraintViolationX extends AppX
{
    public ConstraintViolationX()
    {
    }

    public ConstraintViolationX(String message)
    {
        super(message);
    }

    public ConstraintViolationX(String message, Throwable cause)
    {
        super(message, cause);
    }
}
