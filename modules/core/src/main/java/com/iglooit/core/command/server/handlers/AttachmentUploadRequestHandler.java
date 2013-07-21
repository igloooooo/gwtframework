package com.iglooit.core.command.server.handlers;

import com.iglooit.core.base.iface.command.request.AttachmentUploadRequest;
import com.iglooit.core.base.iface.command.response.Tuple2Response;
import com.iglooit.core.oss.iface.AttachmentCodes;
import com.iglooit.core.oss.server.AttachmentHome;
import com.iglooit.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.iglooit.core.security.iface.access.domain.PrivilegeConst;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AttachmentUploadRequestHandler extends RequestHandler<AttachmentUploadRequest, Tuple2Response<
    AttachmentCodes.RESULT, Long>>
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
    public Tuple2Response<AttachmentCodes.RESULT, Long> handleRequest(AttachmentUploadRequest request)
    {
        return new Tuple2Response<AttachmentCodes.RESULT, Long>(
            attachmentHome.uploadAttachment(request.getRequest()));
    }
}
