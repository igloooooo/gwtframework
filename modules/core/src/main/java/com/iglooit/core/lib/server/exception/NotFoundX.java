package com.iglooit.core.lib.server.exception;

import com.clarity.commons.iface.type.AppX;

/**
 * For use when a query has missing data (when it was expected)
 */
public class NotFoundX extends AppX
{
    public NotFoundX(String message)
    {
        super(message);
    }

    public NotFoundX(String message, Throwable cause)
    {
        super(message, cause);
    }

}
