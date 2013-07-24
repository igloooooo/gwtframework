package com.iglooit.core.base.iface.domain;

import com.iglooit.commons.iface.type.AppX;

public class FsmAppX extends AppX
{
    private String serverMessage;

    public FsmAppX()
    {
    }

    public FsmAppX(String serverMessage, String webMessage)
    {
        super(webMessage);
        this.serverMessage = serverMessage;
    }

    public String getServerMessage()
    {
        return serverMessage;
    }
}
