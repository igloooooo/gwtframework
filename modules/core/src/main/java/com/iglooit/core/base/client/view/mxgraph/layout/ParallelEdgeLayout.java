package com.iglooit.core.base.client.view.mxgraph.layout;

import com.clarity.core.base.client.view.mxgraph.Graph;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 01/04/2011
 * Time: 12:03:32 PM
 */
public class ParallelEdgeLayout extends Layout
{
    public ParallelEdgeLayout(Graph graph)
    {
        super(graph);
        setLayoutObject(createLayout(getGraph().getGraphObject()));
    }

    private native JavaScriptObject createLayout(JavaScriptObject gObject) /*-{
        return new $wnd.mxParallelEdgeLayout(gObject);    
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

    public double getSpacing()
    {
        return getSpacing(getLayoutObject());
    }

    public void setSpacing(double spacing)
    {
        setSpacing(getLayoutObject(), spacing);
    }

    private static native void setSpacing(JavaScriptObject layoutObject, double spacing)  /*-{
        layout.spacing = spacing;
    }-*/;

    private static native double getSpacing(JavaScriptObject layout) /*-{
        return layout.spacing;
    }-*/;

}
