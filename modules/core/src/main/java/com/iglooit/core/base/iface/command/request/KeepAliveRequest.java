package com.iglooit.core.base.iface.command.request;

import com.clarity.core.base.iface.command.ReadOnlyRequest;
import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.response.VoidResponse;

@ReadOnlyRequest
public class KeepAliveRequest extends Request<VoidResponse>
{
}
