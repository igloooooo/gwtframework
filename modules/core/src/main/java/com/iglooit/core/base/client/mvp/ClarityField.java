package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.iface.domain.HasValidatingValue;
import com.google.gwt.user.client.ui.Widget;

public interface ClarityField<T, FT extends Widget> extends HasField<FT>, HasValidatingValue<T>
{
}
