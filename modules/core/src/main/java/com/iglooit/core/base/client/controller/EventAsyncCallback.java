package com.iglooit.core.base.client.controller;

import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.event.StatusErrorEvent;
import com.clarity.core.base.client.event.StatusSuccessEvent;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.StatusCodeException;

/**
 * This callback should only in charge of status message flow
 * -- a mandatory status event bus is required for status flow in status hierarchy channel
 * -- a default success message or/and a default fail message is optional where the successful message
 *    will be shown if no specific onSuccess method override the default one and fail message
 *    will be shown if you would like a specific message instead of message within exception from server
 *
 * @param <T>
 */
public class EventAsyncCallback<T> extends GAsyncCallback<T>
{
    private HandlerManager statusEventBus;
    private String gSuccessMessage;
    private String gFailMessage;

    /**
     * Default constructor with mandatory statusEventBus and no default messages
     *
     * @param statusEventBus
     */
    public EventAsyncCallback(HandlerManager statusEventBus)
    {
        this(statusEventBus, "", "");
    }

    /**
     * Constructor with only fail message defined for common pattern on how we use this
     *
     * @param statusEventBus
     * @param gFailMessage
     */
    public EventAsyncCallback(HandlerManager statusEventBus, String gFailMessage)
    {
        this(statusEventBus, "", gFailMessage);
    }

    /**
     * Constructor with both success/fail message defined
     *
     * @param statusEventBus
     * @param failMessage
     * @param successMessage
     */
    public EventAsyncCallback(HandlerManager statusEventBus, String successMessage, String failMessage)
    {
        super();
        this.statusEventBus = statusEventBus;
        this.gSuccessMessage = successMessage;
        this.gFailMessage = failMessage;
    }

    /**
     * Default onFailure handler
     * -- This will throw a status error event by default
     * -- override me to handle server error in customized way
     *
     * @param e
     */
    public void onFailure(Throwable e)
    {
        /*StatusCode 0 is really not an error & could be caused when we navigate
          away from browser before async call has returned, we can ignore this*/
        if (!(e instanceof StatusCodeException && ((StatusCodeException)e).getStatusCode() == 0))
        {
            GWT.log("Request failed.", e);
            statusEventBus.fireEvent(new StatusErrorEvent(StringUtil.isBlank(gFailMessage)
                ? e.getMessage() : gFailMessage, MessageBoxHtml.MessageType.ERROR));
        }
    }

    /**
     * Default onSuccess handler
     * -- this will show default message if there are any defined
     *
     * @param t
     */
    @Override
    public void onSuccess(T t)
    {
        GWT.log("Request succeeded.");
        statusEventBus.fireEvent(new StatusSuccessEvent(StringUtil.isBlank(gSuccessMessage)
            ? "Request Performed" : gSuccessMessage, MessageBoxHtml.MessageType.SUCCESS));
    }

    public HandlerManager getStatusEventBus()
    {
        return statusEventBus;
    }
}
