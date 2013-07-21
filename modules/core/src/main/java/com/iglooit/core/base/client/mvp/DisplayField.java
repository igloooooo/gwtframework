package com.iglooit.core.base.client.mvp;

import com.google.gwt.user.client.ui.HasValue;

public interface DisplayField extends HasValue<String>
{
    String getValue();

    void setValue(String s);
}
