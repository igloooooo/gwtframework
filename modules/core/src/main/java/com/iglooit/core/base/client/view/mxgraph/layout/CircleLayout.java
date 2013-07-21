package com.iglooit.core.base.client.view.mxgraph.layout;

import com.clarity.core.base.client.view.mxgraph.Graph;
import com.google.gwt.core.client.JavaScriptObject;

public class CircleLayout extends Layout
{
    public CircleLayout(Graph graph)
    {
        super(graph);

        setLayoutObject(createLayout(getGraph().getGraphObject()));
    }

    private native JavaScriptObject createLayout(JavaScriptObject gObject) /*-{
        return new $wnd.mxCircleLayout(gObject);    
    }-*/;

    private native void runLayouter(JavaScriptObject root, JavaScriptObject graph, JavaScriptObject layout) /*-{
        var parent = null;
        if (root != null)
            parent = root;
        else
            parent = graph.getDefaultParent();

        layout.execute(parent);
    }-*/;

    public void execute()
    {
        if (getStartCell() != null)
            runLayouter(getStartCell().getBrowserObject(), getGraph().getGraphObject(), getLayoutObject());
        else
            runLayouter(null, getGraph().getGraphObject(), getLayoutObject());
    }
}
