package com.iglooit.core.base.client.mvp;

import com.google.gwt.user.client.ui.HasValue;

public interface NamedDomainEntityDisplay extends Display
{
    HasValue<String> getName();

    HasValue<String> getDescription();
}
