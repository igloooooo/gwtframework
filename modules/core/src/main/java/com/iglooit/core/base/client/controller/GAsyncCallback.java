package com.iglooit.core.base.client.controller;

import com.clarity.core.base.client.widget.dialog.GErrorDialog;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;

public abstract class GAsyncCallback<T> implements AsyncCallback<T>
{
    public void onFailure(Throwable e)
    {
        /*StatusCode 0 is really not an error & could be caused when we navigate
          away from browser before async call has returned, we can ignore this*/
        if (!(e instanceof StatusCodeException && ((StatusCodeException)e).getStatusCode() == 0))
        {
            String msg = "";
            String stackTrace = "";
            if (e != null)
            {
                msg = e.getMessage();
                stackTrace = e.toString();
            }

            GErrorDialog errorDialog = new GErrorDialog("Error", "Error getting to server - " + msg, stackTrace);
            errorDialog.show();
        }
    }
}
                                                          
