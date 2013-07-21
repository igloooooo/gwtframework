package com.iglooit.core.base.client.view.mxgraph;

import com.clarity.commons.iface.type.AppX;
import com.google.gwt.core.client.JavaScriptObject;

public class CellState implements BrowserObjectMappable
{
    private final JavaScriptObject jsObject;
    private final Cell cell;

    public CellState(Cell cell)
    {
        this.cell = cell;
        if (!cell.hasCellState())
            throw new AppX("Cell has no cell state");
        this.jsObject = cell.getStateObject();
    }

    public Text getText()
    {
        return new Text(getText(jsObject));
    }

    private native JavaScriptObject getText(JavaScriptObject cellState) /*-{
        return cellState.text;
    }-*/;

    public MXRectangle getBounds()
    {
        return new MXRectangle(getBounds(getBrowserObject()));
    }

    /**
     *
     * @param cellStateObject
     * @return bounds of the cellStateObject or bounds of a new rectangle if the cellstate object is not rendered
     */
    private native JavaScriptObject getBounds(JavaScriptObject cellStateObject) /*-{
        //Handle the situation where a shape is being positioned on a graph
        //prior to it being rendered, therefore it has no shape.
        //It's ok to return a new rectangle because it will be overridden at render time.
        if (cellStateObject.shape != null)
        {
            return cellStateObject.shape.bounds;
        }
        else
        {
            return new $wnd.mxRectangle().bounds;
        }
    }-*/;
    

    public JavaScriptObject getBrowserObject()
    {
        return jsObject;
    }
}
