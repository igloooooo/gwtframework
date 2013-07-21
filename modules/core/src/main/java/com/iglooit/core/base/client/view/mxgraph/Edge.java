package com.iglooit.core.base.client.view.mxgraph;

import com.clarity.core.base.client.view.ClarityStyle;
import com.google.gwt.core.client.JavaScriptObject;

public class Edge extends Cell implements Captionable, Removable
{
    private Linkable start;
    private Linkable end;

    public Linkable getStart()
    {
        return start;
    }

    /**
     * Can only be called after the edge has been added to the graph
     */
    public void setGuide(boolean value)
    {
        setGuide(getBrowserObject(), value);
    }

    private static native void setGuide(JavaScriptObject edge, boolean value) /*-{
        edge.isGuide = value;
    }-*/;

    /**
     * Can only be called after the edge has been added to the graph
     * @return
     */
    public boolean isGuide()
    {
        return isGuide(getBrowserObject());
    }

    private static native boolean isGuide(JavaScriptObject edge) /*-{
        if (edge.isGuide) // could be undefined
            return true;
        else
            return false;
    }-*/;

    public void setStart(Vertex start)
    {
        this.start = start;
    }

    public Linkable getEnd()
    {
        return end;
    }

    public void setEnd(Linkable end)
    {
        this.end = end;
    }

    public Edge(Linkable start, Linkable end, String caption)
    {
        super();

        this.start = start;
        this.end = end;
        setCaption(caption);
    }

    public Edge(Linkable start, Linkable end)
    {
        super();

        this.start = start;
        this.end = end;
    }

    protected String getLabelStyleName()
    {
        return ClarityStyle.CIRCUIT_EDGE_LABEL;
    }

    @Override
    public String toString()
    {
        return "[Edge: name: " + getName() + " cap: " + getCaption() + "]";
    }
}
