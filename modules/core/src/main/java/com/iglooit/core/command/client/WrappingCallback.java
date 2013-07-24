package com.iglooit.core.command.client;

import com.iglooit.commons.iface.type.AppX;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class WrappingCallback<ResponseType> implements AsyncCallback<ResponseType>
{
    private AsyncCallback<ResponseType> innerCallback;
    private Runnable command;

    public AsyncCallback<ResponseType> getInnerCallback()
    {
        if (command == null)
            throw new AppX("WrappingCallback not initialised properly: innerCallback is null");
        return innerCallback;
    }

    public void setInnerCallback(AsyncCallback<ResponseType> innerCallback)
    {
        this.innerCallback = innerCallback;
    }

    public Runnable getCommand()
    {
        if (command == null)
            throw new AppX("WrappingCallback not initialised properly: command is null");
        return command;
    }

    public void setCommand(Runnable command)
    {
        this.command = command;
    }

    protected WrappingCallback()
    {

    }

    protected WrappingCallback(AsyncCallback<ResponseType> innerCallback, Runnable command)
    {
        this.innerCallback = innerCallback;
        this.command = command;
    }
}

