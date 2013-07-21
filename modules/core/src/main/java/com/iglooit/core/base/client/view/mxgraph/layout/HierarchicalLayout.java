package com.iglooit.core.base.client.view.mxgraph.layout;

import com.clarity.core.base.client.view.mxgraph.Graph;
import com.google.gwt.core.client.JavaScriptObject;

public class HierarchicalLayout extends Layout
{
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    public HierarchicalLayout(Graph graph, int orientation)
    {
        super(graph);

        setLayoutObject(createLayout(getGraph().getGraphObject(), orientation));
    }

    private native JavaScriptObject createLayout(JavaScriptObject gObject, int orientation) /*-{
        var mxOrientation = $wnd.mxConstants.DIRECTION_NORTH;
        if (orientation == 1)
            mxOrientation = $wnd.mxConstants.DIRECTION_EAST;
        else if (orientation == 2)
            mxOrientation = $wnd.mxConstants.DIRECTION_SOUTH;
        else if (orientation == 3)
            mxOrientation = $wnd.mxConstants.DIRECTION_WEST;

        var layout = new $wnd.mxHierarchicalLayout(gObject, mxOrientation);
        layout.intraCellSpacing = 110;
        layout.interRankCellSpacing = 40;
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

    private native void setEdgeStyleEnabled(JavaScriptObject layout, boolean enabled)
        /*-{
            layout.disableEdgeStyle = !enabled;
        }-*/;

    //to enable routing to the center of vertices, set this to true
    public void setEdgeStyleEnabled(boolean enabled)
    {
        setEdgeStyleEnabled(getLayoutObject(), enabled);
    }

    public void execute()
    {
        if (getStartCell() != null)
            runLayouter(getStartCell().getBrowserObject(), getGraph().getGraphObject(), getLayoutObject());
        else
            runLayouter(null, getGraph().getGraphObject(), getLayoutObject());
    }
}

