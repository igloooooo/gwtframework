package com.iglooit.core.base.iface.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface EnumBooleanType extends IsSerializable
{
    String getDisplayString();

    Boolean getBoolean();
}
