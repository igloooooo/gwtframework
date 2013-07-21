package com.iglooit.core.base.client.event;

public abstract class RecoverableFailedEvent<DATA> extends BasicPayloadEvent<DATA>
{
    private String errorMessage;

    protected RecoverableFailedEvent(DATA data, String errorMessage)
    {
        super(data);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public DATA getData()
    {
        return getPayload();
    }
}
