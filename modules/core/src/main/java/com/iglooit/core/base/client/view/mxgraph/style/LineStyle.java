package com.iglooit.core.base.client.view.mxgraph.style;

import com.clarity.core.base.client.view.mxgraph.Graph;
import com.clarity.core.base.client.view.mxgraph.Style;
import com.google.gwt.core.client.JavaScriptObject;

public class LineStyle extends Style
{
    @Override
    protected native JavaScriptObject addStyle(JavaScriptObject graph) /*-{
         return $wnd.mxUtils.clone(graph.getStylesheet().getDefaultEdgeStyle());
    }-*/;

    public LineStyle(String name)
    {
        super(name);
    }

    public LineStyle(String name, Graph graph)
    {
        super(name);
    }

    public static LineStyle fromStyle(String name, LineStyle style)
    {
        LineStyle clonedStyle = new LineStyle(name);

        for (String key : style.getStyleHash().keySet())
        {
            clonedStyle.getStyleHash().put(key, style.getStyleHash().get(key));
        }

        return clonedStyle;
    }
}
