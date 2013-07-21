package com.iglooit.core.base.iface.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public interface EnumStringType extends IsSerializable, Serializable
{
    String getDisplayString();

    String getWebserviceString();
}
