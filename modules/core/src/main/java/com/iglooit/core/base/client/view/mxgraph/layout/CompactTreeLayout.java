package com.iglooit.core.base.client.view.mxgraph.layout;

import com.clarity.core.base.client.view.mxgraph.Graph;
import com.google.gwt.core.client.JavaScriptObject;

public class CompactTreeLayout extends Layout
{
    public CompactTreeLayout(Graph graph, boolean isHorizontal)
    {
        super(graph);

        setLayoutObject(createLayout(getGraph().getGraphObject(), isHorizontal));
    }

    private native JavaScriptObject createLayout(JavaScriptObject graph, boolean isHorizontal) /*-{
        var layout = new $wnd.mxCompactTreeLayout(graph, isHorizontal);
        layout.resizeParent = true;
        layout.edgeRouting = false;
        layout.nodeDistance = 60;
        layout.moveTree = false;
        return layout;
    }-*/;

    private native void runLayouter(JavaScriptObject root, JavaScriptObject graph, JavaScriptObject layout) /*-{

        var parent = null;
        if (root != null)
            parent = root;
        else
            parent = graph.getDefaultParent();        
        layout.execute(parent);
    }-*/;


    private native void setNodeDistance(JavaScriptObject layout, int pixels)  /*-{
        layout.nodeDistance = pixels;
    }-*/;

    public void setNodeDistance(int nodeDistance)
    {
        setNodeDistance(getLayoutObject(), nodeDistance);
    }

    private native void setLevelDistance(JavaScriptObject layout, int pixels)  /*-{
		layout.levelDistance = pixels;
    }-*/;

    public void setLevelDistance(int levelDistance)
    {
        setLevelDistance(getLayoutObject(), levelDistance);
    }


    public void execute()
    {
        if (getStartCell() != null)
            runLayouter(getStartCell().getBrowserObject(), getGraph().getGraphObject(), getLayoutObject());
        else
            runLayouter(null, getGraph().getGraphObject(), getLayoutObject());
    }
}
