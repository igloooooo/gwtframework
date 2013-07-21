package com.iglooit.core.base.client.view.mxgraph.layout;

import com.clarity.core.base.client.view.mxgraph.Cell;
import com.clarity.core.base.client.view.mxgraph.Graph;
import com.google.gwt.core.client.JavaScriptObject;

public abstract class Layout
{
    private Graph graph;

    protected Cell getStartCell()
    {
        return startCell;
    }

    public void setStartCell(Cell startCell)
    {
        this.startCell = startCell;
    }

    private Cell startCell;

    public Layout(Graph graph)
    {
        this.graph = graph;
    }

    public abstract void execute();

    public Graph getGraph()
    {
        return graph;
    }

    protected JavaScriptObject getLayoutObject()
    {
        return layoutObject;
    }

    protected void setLayoutObject(JavaScriptObject layoutObject)
    {
        this.layoutObject = layoutObject;
    }

    private JavaScriptObject layoutObject;
}
