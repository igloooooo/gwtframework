package com.iglooit.core.base.client.view.mxgraph;

import com.google.gwt.core.client.JavaScriptObject;

public class MXEvent extends JavaScriptObject
{
    protected MXEvent()
    {
    }

    public final native String getName()/*-{ return this.getName(); }-*/;

    public final native boolean isConsumed() /*-{ return this.isConsumed(); }-*/;

    public final native void consume() /*-{ this.consume(); }-*/;

}
