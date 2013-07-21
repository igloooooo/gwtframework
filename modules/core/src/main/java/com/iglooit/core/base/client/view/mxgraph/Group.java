package com.iglooit.core.base.client.view.mxgraph;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.commons.iface.type.AppX;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.ArrayList;
import java.util.Collection;

public class Group extends Cell
{
    public Group(int x, int y, int width, int height)
    {
        super(x, y, width, height);
    }

    public Group(int x, int y, int width, int height, String name)
    {
        super(x, y, width, height, name);
    }
    
    public Group()
    {
        super();
    }

    public Group(String caption)
    {
        super();

        //this.vertices = vertices;
        setCaption(caption);
    }

    public Collection<Cell> getVertices()
    {
        return vertices;
    }

    public void setVertices(Collection<Cell> vertices)
    {
        this.vertices = vertices;
    }

    public boolean containsChildren()
    {
        return containsChildren(getBrowserObject());
    }

    private static native boolean containsChildren(JavaScriptObject cell) /*-{
        return cell.getChildCount() > 0;
    }-*/;

    private Collection<Cell> vertices = new ArrayList<Cell>();

    public JavaScriptObject[] getCellArray()
    {
        JavaScriptObject[] jsVertices = new JavaScriptObject[vertices.size()];

        int i = 0;
        for (Cell v : vertices)
        {
            jsVertices[i] = v.getBrowserObject();
            i++;
        }

        return jsVertices;
    }

    public boolean isCollapsed()
    {
        if (getMemberGraph() == null)
        {
            throw new AppX("Group must already be in a graph before collapse state is queried");
        }

        return isCollapsed(getBrowserObject());
    }

    private native boolean isCollapsed(JavaScriptObject cell) /*-{
        return cell.isCollapsed();
    }-*/;

    public void setCollapsed(boolean value)
    {
        if (getMemberGraph() == null)
        {
            throw new AppX("Group must already be in a graph before collapse state is queried");
        }

        setCollapsed(getBrowserObject(), value);

    }

    private native void setCollapsed(JavaScriptObject cell, boolean value) /*-{
        cell.setCollapsed(value);
    }-*/;


    //Allow a group to have cells added to it
    public void addCell(Cell c)
    {
        //If we're not in a graph, exception
        GWT.log("Group.addCell - begin");
        if (getMemberGraph() == null)
        {
            throw new AppX("Group must already be in a graph before vertices are added");
        }

        c.setParentCell(this);

        //If the vertex is not already in a graph, add it now
        // graph.addCell handles addition of child cell
        if (c.getMemberGraph() == null)
        {
            this.getMemberGraph().addCell(c);
        }
        else
            addVertex(getMemberGraph().getGraphObject(), getBrowserObject(), c.getBrowserObject());

        vertices.add(c);
    }

    private native JavaScriptObject addVertex(JavaScriptObject graphCanvas, JavaScriptObject groupObject,
                                              JavaScriptObject vertexObject) /*-{
       //Add a vertex to a group

        try
        {
            var parent = graphCanvas.getDefaultParent();

            var cellArray = new Array();
            cellArray[0] = vertexObject;
            cellArray[1] = graphCanvas.insertVertex(parent, 'dummy1', '', 2, 2, 2, 2);

            //TODO: Add border handling code
            var lastObject = groupObject;
            groupObject = graphCanvas.groupCells(groupObject, 1, cellArray);

            if (groupObject != lastObject)
                alert("Ooops! New object returned!");

            //Remove the dummy cell

            var removeArray = new Array();
            removeArray[0] = cellArray[1];
            graphCanvas.removeCells(removeArray, true);

        }
        finally
        {
        }

        return groupObject;
    }-*/;

    public void resizeToBound()
    {
        resizeToBound(getMemberGraph().getGraphObject(), getBrowserObject(), 10);
        syncGeometry();
    }

    private static native void resizeToBound(JavaScriptObject graphCanvas, JavaScriptObject groupObject,
                                             double border) /*-{
        graphCanvas.updateGroupBounds([groupObject], border, true);
    }-*/;

    public void resizeToBound(double border)
    {
        resizeToBound(getMemberGraph().getGraphObject(), getBrowserObject(), border);
        syncGeometry();
    }

    protected String getLabelStyleName()
    {
        return ClarityStyle.CIRCUIT_GROUP_LABEL;
    }

    @Override
    public String toString()
    {
        return "[Group: name: " + getName() + " cap: " + getCaption() + "]";
    }
}
