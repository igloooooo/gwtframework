package com.iglooit.core.security.iface.domain;

import com.clarity.commons.iface.type.AppX;

public class PasswordNoLongerValidX extends AppX
{
    public PasswordNoLongerValidX()
    {
    }

    public PasswordNoLongerValidX(String message)
    {
        this(message, null);
    }

    public PasswordNoLongerValidX(String message, Throwable e)
    {
        super(message, e);
    }
}
