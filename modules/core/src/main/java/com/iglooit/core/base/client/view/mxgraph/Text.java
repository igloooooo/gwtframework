package com.iglooit.core.base.client.view.mxgraph;

import com.google.gwt.core.client.JavaScriptObject;

public class Text extends Shape
{
    public Text(JavaScriptObject jsObject)
    {
        super(jsObject);
    }

    public MXRectangle getBounds()
    {
        return new MXRectangle(getBounds(getBrowserObject()));
    }

    private native JavaScriptObject getBounds(JavaScriptObject jsObject) /*-{
        return jsObject.boundingBox;
    }-*/;
}
