package com.iglooit.core.base.iface.domain;

import com.clarity.commons.iface.type.Option;
import com.google.gwt.user.client.ui.HasValue;

public interface HasLabelledValue<T> extends HasValue<T>
{
    String getFieldLabel();

    void setFieldLabel(String fieldLabel);

    Option<String> getUsageHint();

    void setUsageHint(String usageHint);
}
