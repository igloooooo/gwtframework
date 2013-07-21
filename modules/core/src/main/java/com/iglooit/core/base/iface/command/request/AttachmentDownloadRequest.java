package com.iglooit.core.base.iface.command.request;

import com.clarity.core.base.iface.command.ReadOnlyRequest;
import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.response.Tuple3Response;

import javax.servlet.http.HttpServletRequest;

@ReadOnlyRequest
public class AttachmentDownloadRequest extends Request<Tuple3Response<String, byte[], String>>
{

    private HttpServletRequest request;

    public AttachmentDownloadRequest()
    {

    }

    public AttachmentDownloadRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }
}

