package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.commons.iface.type.NonSerOpt;

public interface Breadcrumb<T>
{
    NonSerOpt<T> getParent();

    String getText();
}