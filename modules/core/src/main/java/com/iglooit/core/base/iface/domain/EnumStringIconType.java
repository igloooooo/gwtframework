package com.iglooit.core.base.iface.domain;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

public interface EnumStringIconType extends EnumStringType
{
    AbstractImagePrototype getIcon();
    AbstractImagePrototype getDefaultIcon();
    String getHeaderString();

}
