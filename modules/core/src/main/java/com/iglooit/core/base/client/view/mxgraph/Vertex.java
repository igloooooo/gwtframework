package com.iglooit.core.base.client.view.mxgraph;

import com.clarity.core.base.client.view.ClarityStyle;

public class Vertex extends Cell
{

    public Vertex(int x, int y, int width, int height, String name, String caption)
    {
        super(x, y, width, height, name);

        setCaption(caption);
    }

    protected String getLabelStyleName()
    {
        return ClarityStyle.CIRCUIT_VERTEX_LABEL;
    }

    @Override
    public String toString()
    {
        return "[Vertex: name: " + getName() + " cap: " + getCaption() + "]";
    }
}
