package com.iglooit.core.base.client.view.mxgraph.style;

import com.clarity.core.base.client.view.mxgraph.Graph;
import com.clarity.core.base.client.view.mxgraph.Style;
import com.google.gwt.core.client.JavaScriptObject;

public class VertexStyle extends Style
{


    @Override
    protected native JavaScriptObject addStyle(JavaScriptObject graph) /*-{
         return $wnd.mxUtils.clone(graph.getStylesheet().getDefaultVertexStyle());
    }-*/;

    protected native JavaScriptObject cloneStyle(JavaScriptObject graph, JavaScriptObject oldStyle) /*-{
         return $wnd.mxUtils.clone(oldStyle);
    }-*/;

    @Deprecated
    public VertexStyle(String name, Graph graph)
    {
        super(name);
    }

    public VertexStyle(String styleName)
    {
        super(styleName);
    }

    public static VertexStyle fromStyle(String name, VertexStyle style)
    {
        VertexStyle clonedStyle = new VertexStyle(name);

        for (String key : style.getStyleHash().keySet())
        {
            clonedStyle.getStyleHash().put(key, style.getStyleHash().get(key));
        }

        return clonedStyle;
    }
}
