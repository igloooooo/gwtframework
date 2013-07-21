package com.iglooit.core.base.client.view.mxgraph.layout;

import com.clarity.core.base.client.view.mxgraph.Graph;
import com.google.gwt.core.client.JavaScriptObject;

public class OrganicLayout extends Layout
{
    private boolean useInputOrigin = false;
    private boolean deterministic = false;

    public boolean isDeterministic()
    {
        return deterministic;
    }

    public boolean isUseInputOrigin()
    {
        return useInputOrigin;
    }

    public void setUseInputOrigin(boolean useInputOrigin)
    {
        this.useInputOrigin = useInputOrigin;
    }

    public OrganicLayout(Graph graph)
    {
        super(graph);
        setLayoutObject(createLayout(getGraph().getGraphObject()));
    }

    public OrganicLayout(Graph graph, boolean deterministic)
    {
        super(graph);
        this.deterministic = deterministic;
        setLayoutObject(createLayout(getGraph().getGraphObject(), deterministic));
    }

    public OrganicLayout(Graph graph, double forceConstant)
    {
        this(graph, forceConstant, false);
    }

    public OrganicLayout(Graph graph, double forceConstant, boolean deterministic)
    {
        super(graph);
        this.deterministic = deterministic;
        setLayoutObject(createLayout(getGraph().getGraphObject(), deterministic));
        setForceConstant(forceConstant);
    }

    public void setForceConstant(double value)
    {
        setForceConstant(getLayoutObject(), value);
    }

    private static native void setForceConstant(JavaScriptObject layout, double value) /*-{
        layout.forceConstant = value;
    }-*/;

    private JavaScriptObject createLayout(JavaScriptObject gObject)
    {
        return createLayout(gObject, false);
    }

    private native JavaScriptObject createLayout(JavaScriptObject gObject, boolean deterministic) /*-{
        var layout = new $wnd.mxFastOrganicLayout(gObject);
        layout.deterministic = deterministic;

        return layout;
    }-*/;

    private native void runLayouter(JavaScriptObject root, JavaScriptObject graph, JavaScriptObject layout,
                                    boolean useInputOrigin) /*-{

        var parent = null;
        if (root != null)
            parent = root;
        else
            parent = graph.getDefaultParent();

        layout.useBoundingBox = false;
        layout.useInputOrigin = useInputOrigin;

        layout.execute(parent);
    }-*/;

    public void execute()
    {
        if (getStartCell() != null)
            runLayouter(getStartCell().getBrowserObject(), getGraph().getGraphObject(), getLayoutObject(),
                    isUseInputOrigin());
        else
            runLayouter(null, getGraph().getGraphObject(), getLayoutObject(), isUseInputOrigin());
    }
}
