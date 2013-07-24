package com.iglooit.core.base.iface.command.request;

import com.iglooit.core.base.iface.command.ReadOnlyRequest;
import com.iglooit.core.base.iface.command.Request;
import com.iglooit.core.base.iface.command.response.VoidResponse;

@ReadOnlyRequest
public class KeepAliveRequest extends Request<VoidResponse>
{
}
