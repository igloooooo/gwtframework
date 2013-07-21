package com.iglooit.core.base.client.view.mxgraph.overlay;

import com.clarity.core.base.client.view.mxgraph.CellOverlay;
import com.clarity.core.base.client.view.mxgraph.Graph;
import com.clarity.core.base.client.view.mxgraph.Style;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 09/02/2011
 * Time: 3:22:44 PM
 *
 * Note: Currently there's no way to retrieve the style back once an overlay is created
 */
public class StyledOverlay extends CellOverlay
{
    protected StyledOverlay()
    {
    }

    public String getType()
    {
        return getType(getBrowserObject());
    }

    private static native String getType(JavaScriptObject overlay) /*-{
        return overlay.overlayStyleType;
    }-*/;
    
    public void setType(String type)
    {
        setType(getBrowserObject(), type);
    }

    private static native void setType(JavaScriptObject overlay, String type) /*-{
        overlay.overlayStyleType = type;
    }-*/;

    public static final StyledOverlay create(Graph graph, Style style, String tooltip, double width, double height)
    {
        style.processStyleJS(graph);
        StyledOverlay overlay = new StyledOverlay();
        overlay.setBrowserObject(create(style.getStyleObject(), tooltip, width, height));
        
        return overlay;
    }

    private static native JavaScriptObject create(JavaScriptObject style, String label, double width,
                                                         double height) /*-{
        var overlay = new $wnd.mxCellOverlay();
        overlay.overlayStyle = style;
        overlay.label = label;
        overlay.tooltip = label;

        // hacking to make bounds work
        overlay.image = new Object();
        overlay.image.width = width;
        overlay.image.height = height;

        return overlay;
    }-*/;
}
