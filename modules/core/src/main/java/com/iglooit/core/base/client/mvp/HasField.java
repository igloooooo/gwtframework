package com.iglooit.core.base.client.mvp;

import com.google.gwt.user.client.ui.Widget;

public interface HasField<F extends Widget>
{
    F getField();
}
