package com.iglooit.core.lib.iface;

public class UniqueConstraintViolationX extends ConstraintViolationX
{
    public UniqueConstraintViolationX()
    {
    }

    public UniqueConstraintViolationX(String message)
    {
        super(message);
    }

    public UniqueConstraintViolationX(String message, Throwable cause)
    {
        super(message, cause);
    }
}