package com.iglooit.core.base.client.view.mxgraph.util;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 01/02/2011
 * Time: 3:07:07 PM
 */
public class Path extends JavaScriptObject
{
    protected Path()
    {
    }

    public final native void moveTo(double x, double y) /*-{
        this.moveTo(x, y);
     }-*/;

    public final native void lineTo(double x, double y) /*-{
        this.lineTo(x, y);
     }-*/;

    public final native void curveTo(double x1, double y1, double x2, double y2, double x, double y) /*-{
        this.curveTo(x1, y1, x2, y2, x, y);
     }-*/;

    public final native void quadTo(double x1, double y1, double x, double y) /*-{
        this.quadTo(x1, y1, x, y);
     }-*/;

    public final native void end() /*-{
        this.end();
     }-*/;

    public final native void close() /*-{
        this.close();
     }-*/;
}
