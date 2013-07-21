package com.iglooit.core.base.client.view.mxgraph.layout;

import com.clarity.core.base.client.view.mxgraph.Graph;
import com.google.gwt.core.client.JavaScriptObject;

public class StackLayout extends Layout
{
    private int spacing = 20;

    public void setSpacing(int spacing)
    {
        this.spacing = spacing;
    }

    public StackLayout(Graph graph)
    {
        super(graph);
        setLayoutObject(createLayout(getGraph().getGraphObject()));
    }

    private native JavaScriptObject createLayout(JavaScriptObject gObject) /*-{
        return new $wnd.mxStackLayout(gObject);
    }-*/;

    private native void runLayouter(JavaScriptObject layout, JavaScriptObject graph, JavaScriptObject root,
                                    int spacing) /*-{

        var parent = null;
        if (root != null)
            parent = root;
        else
            parent = graph.getDefaultParent();

        layout.spacing = spacing;
        layout.execute(parent);
    }-*/;

    public void execute()
    {
        if (getStartCell() != null)
            runLayouter(getLayoutObject(), getGraph().getGraphObject(), getStartCell().getBrowserObject(), spacing);
        else
            runLayouter(getLayoutObject(), getGraph().getGraphObject(), null, spacing);
    }
}
