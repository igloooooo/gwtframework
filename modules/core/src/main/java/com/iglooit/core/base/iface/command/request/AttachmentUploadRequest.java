package com.iglooit.core.base.iface.command.request;

import com.clarity.core.base.iface.command.ReadWriteRequest;
import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.response.Tuple2Response;
import com.clarity.core.oss.iface.AttachmentCodes;

import javax.servlet.http.HttpServletRequest;

@ReadWriteRequest
public class AttachmentUploadRequest extends Request<Tuple2Response<AttachmentCodes.RESULT, Long>>
{
    private HttpServletRequest request;

    public AttachmentUploadRequest()
    {

    }

    public AttachmentUploadRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }
}
