package com.iglooit.commons.iface.type;

/**
 * Thrown when a security violation has occured
 */
public class SecurityX extends AppX
{
    public SecurityX()
    {
    }

    public SecurityX(String message)
    {
        super(message);
    }

    public SecurityX(String message, Throwable cause)
    {
        super(message, cause);
    }


}
