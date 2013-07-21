package com.iglooit.core.security.iface.domain;

import com.clarity.commons.iface.type.AppX;

public class ClientRequestX extends AppX
{
    public ClientRequestX()
    {
    }

    public ClientRequestX(String message)
    {
        super(message);
    }

    public ClientRequestX(String message, Throwable cause)
    {
        super(message, cause);
    }

}
