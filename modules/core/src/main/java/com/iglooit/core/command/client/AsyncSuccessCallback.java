package com.iglooit.core.command.client;

public interface AsyncSuccessCallback<Response>
{
    void onSuccess(Response response);
}
