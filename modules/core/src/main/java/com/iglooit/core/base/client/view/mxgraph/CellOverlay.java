package com.iglooit.core.base.client.view.mxgraph;

import com.google.gwt.core.client.JavaScriptObject;

public class CellOverlay extends MXGraphObject
{
    protected CellOverlay()
    {
    }

    public final native String getVerticalAlign() /*-{
        return this.verticalAlign;
    }-*/;

    public final void setVerticalAlign(String vAlign)
    {
        setVerticalAlign(getBrowserObject(), vAlign);
    }

    private native void setVerticalAlign(JavaScriptObject overlay, String vAlign) /*-{
        overlay.verticalAlign = vAlign;
    }-*/;


    public final native String getAlign() /*-{
        return this.align;
    }-*/;

    public void setAlign(String align)
    {
        setAlign(getBrowserObject(), align);
    }

    private native void setAlign(JavaScriptObject object, String align) /*-{
        object.align = align;
    }-*/;

    public final native double getOverlap() /*-{
        return this.defaultOverlap;
    }-*/;

    public final native void setDefaultOverlap(double overlap) /*-{
        this.defaultOverlap = overlap;
    }-*/;

    public final native String getTooltip() /*-{
        return this.tooltip;
    }-*/;

    public final native void setTooltip(String tooltip) /*-{
        this.tooltip = tooltip;
    }-*/;


}
