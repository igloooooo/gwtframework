package com.iglooit.core.base.client.view.mxgraph;

import com.clarity.core.base.client.view.mxgraph.overlay.StyledOverlay;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Cell extends CaptionableMXGraphObject implements Linkable, Removable, Nameable
{
    private String name;
    private CellState cellState;
    private Cell parentCell;
    private List<CellOverlay> cellOverlays = new ArrayList<CellOverlay>();


    public Cell getParentCell()
    {
        return parentCell;
    }

    protected void setParentCell(Cell parentCell)
    {
        this.parentCell = parentCell;
    }

    protected Cell(int x, int y, int width, int height, String name)
    {
        super(x, y, width, height);
        this.name = name;
    }

    public Cell(int x, int y, int width, int height)
    {
        super(x, y, width, height);
    }

    protected Cell()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private static native double deepGetX(JavaScriptObject cell) /*-{
        return cell.geometry.x;
    }-*/;

    private static native double deepGetY(JavaScriptObject cell) /*-{
        return cell.geometry.y;
    }-*/;

    private static native double deepGetWidth(JavaScriptObject cell) /*-{
        return cell.geometry.width;
    }-*/;

    private static native double deepGetHeight(JavaScriptObject cell) /*-{
        return cell.geometry.height;
    }-*/;

    private native void setConnectable(JavaScriptObject cellObject, boolean state)
    /*-{
        cellObject.setConnectable(state);
    }-*/;

    public void setConnectable(boolean state)
    {
        setConnectable(getBrowserObject(), state);
    }

    public void syncGeometry()
    {
        setX((int)deepGetX(getBrowserObject()));
        setY((int)deepGetY(getBrowserObject()));
        setWidth((int)deepGetWidth(getBrowserObject()));
        setHeight((int)deepGetHeight(getBrowserObject()));
    }

    private native void pushGeometry(JavaScriptObject graphCanvas, JavaScriptObject cellObject, int x, int y,
                                     int width, int height) /*-{
        try
        {
                cellObject.setGeometry(new $wnd.mxGeometry(x,  y, width, height));
        }
        finally
        {
        }

    }-*/;

    public void pushGeometry()
    {
        pushGeometry(getMemberGraph().getGraphObject(), getBrowserObject(), getX(), getY(), getWidth(), getHeight());
    }

    public void addOverlay(CellOverlay overlay)
    {
        cellOverlays.add(overlay);
        if (getMemberGraph() != null)
        {
            overlay.setBrowserObject(
                    addOverlay(getMemberGraph().getGraphObject(), getBrowserObject(), overlay.getBrowserObject()));
        }
    }

    public StyledOverlay getOverlayByStyleType(String type)
    {
        for (CellOverlay overlay : cellOverlays)
        {
            if (overlay instanceof StyledOverlay)
            {
                if (((StyledOverlay)overlay).getType().equalsIgnoreCase(type))
                    return (StyledOverlay)overlay;
            }
        }
        return null;
    }

    public void setLocked(boolean locked)
    {
        setLocked(getBrowserObject(), locked);
    }

    private static native void setLocked(JavaScriptObject cell, boolean locked) /*-{
        cell.locked = locked;
    }-*/;

    public boolean getLocked()
    {
        return getLocked(getBrowserObject());
    }

    private static native boolean getLocked(JavaScriptObject cell) /*-{
        return cell.locked;
    }-*/;

    public void setOffset(int x, int y)
    {
        setOffset(getBrowserObject(), x, y);
    }

    public void setGeometryRelative(boolean relative)
    {
        setGeometryRelative(getBrowserObject(), relative);
    }

    private static native void setGeometryRelative(JavaScriptObject cell, boolean relative) /*-{
        cell.geometry.relative = relative;
    }-*/;

    private static native void setOffset(JavaScriptObject cell, int x, int y) /*-{
        cell.geometry.offset = new $wnd.mxPoint(x, y);

    }-*/;


    public void processOverlays()
    {
        removeOverlays(getMemberGraph().getGraphObject(), getBrowserObject());
        for (CellOverlay overlay : cellOverlays)
        {
            //overlay.setGraph(getMemberGraph());
            overlay.setBrowserObject(
                    addOverlay(getMemberGraph().getGraphObject(), getBrowserObject(), overlay.getBrowserObject()));
        }
    }

    private native JavaScriptObject addOverlay(JavaScriptObject graphObject, JavaScriptObject cell,
                                   JavaScriptObject overlayObject) /*-{
        return graphObject.addCellOverlay(cell, overlayObject );
    }-*/;

    private native void removeOverlays(JavaScriptObject graphObject, JavaScriptObject cell) /*-{
        graphObject.removeCellOverlays(cell);
    }-*/;

    public void removeOverlays()
    {
        removeOverlays(getMemberGraph().getGraphObject(), getBrowserObject());
    }

    private native void removeOverlay(JavaScriptObject graph, JavaScriptObject cell, JavaScriptObject overlay) /*-{
         graph.removeCellOverlay(cell, overlay);
    }-*/;

    public void removeOverlay(CellOverlay overlay)
    {
        if (this.getBrowserObject() != null)
            removeOverlay(this.getMemberGraph().getGraphObject(), this.getBrowserObject(), overlay.getBrowserObject());
    }

    public void removeCellOverLay(CellOverlay overlay)
    {
        if (overlay == null)
            return;

        cellOverlays.remove(overlay);
        removeCellOverlay(getMemberGraph().getGraphObject(), getBrowserObject(), overlay);
    }

    private static native void removeCellOverlay(JavaScriptObject graphObject, JavaScriptObject cell,
                                                 CellOverlay overlay) /*-{
        graphObject.removeCellOverlay(cell, overlay);
    }-*/;

    public CellState getState()
    {
        if (cellState == null)
            cellState = new CellState(this);
        return cellState;
    }

    public MXRectangle getCaptionBounds()
    {
        return getState().getText().getBounds();
    }

    public MXRectangle getBounds()
    {
        return getState().getBounds();
    }

    protected void setLabel(Object label)
    {
        setLabel(getMemberGraph().getGraphObject(), getBrowserObject(), label.toString());
    }

    private native void setLabel(JavaScriptObject graphObject, JavaScriptObject cellObject, String label) /*-{
        graphObject.cellLabelChanged(cellObject, label, false);
    }-*/;

    public boolean hasCellState()
    {
        return cellState != null || hasCellState(getMemberGraph().getGraphObject(), getBrowserObject());
    }

    private static native boolean hasCellState(JavaScriptObject graphObject, JavaScriptObject cellObject) /*-{
        return graphObject.getView().getState(cellObject, true) != null;
    }-*/;

    protected JavaScriptObject getStateObject()
    {
        return getStateObject(getMemberGraph().getGraphObject(), getBrowserObject());
    }

    private native JavaScriptObject getStateObject(JavaScriptObject graphObject, JavaScriptObject cellObject) /*-{
        return graphObject.getView().getState(cellObject, true);
    }-*/;
}
