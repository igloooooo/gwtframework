package com.iglooit.core.base.client.view.mxgraph;

import com.google.gwt.core.client.JavaScriptObject;

public class Shape implements BrowserObjectMappable
{
    private final JavaScriptObject jsObject;

    public Shape(JavaScriptObject jsObject)
    {
        this.jsObject = jsObject;
    }

    public JavaScriptObject getBrowserObject()
    {
        return jsObject;
    }
}
