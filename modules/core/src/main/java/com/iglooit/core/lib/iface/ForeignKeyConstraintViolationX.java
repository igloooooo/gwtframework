package com.iglooit.core.lib.iface;

public class ForeignKeyConstraintViolationX extends ConstraintViolationX
{
    public ForeignKeyConstraintViolationX()
    {
    }

    public ForeignKeyConstraintViolationX(String message)
    {
        super(message);
    }

    public ForeignKeyConstraintViolationX(String message, Throwable cause)
    {
        super(message, cause);
    }
}
