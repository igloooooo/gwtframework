package com.iglooit.core.command.server.handlers;

import com.iglooit.core.base.iface.command.request.AttachmentDownloadRequest;
import com.iglooit.core.base.iface.command.response.Tuple3Response;
import com.iglooit.core.oss.server.AttachmentHome;
import com.iglooit.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.iglooit.core.security.iface.access.domain.PrivilegeConst;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AttachmentDownloadRequestHandler extends RequestHandler<AttachmentDownloadRequest,
    Tuple3Response<String, byte[], String>>
{
    private static final PrivilegeConst[] PRIVILEGES = {SystemPrivilege.LOGIN};

    @Resource
    private AttachmentHome attachmentHome;

    @Override
    public PrivilegeConst[] getRequiredPrivileges()
    {
        return PRIVILEGES;
    }

    @Override
    public Tuple3Response<String, byte[], String> handleRequest(AttachmentDownloadRequest request)
    {
        return new Tuple3Response<String, byte[], String>(attachmentHome.downloadAttachment(request.getRequest()));
    }
}
