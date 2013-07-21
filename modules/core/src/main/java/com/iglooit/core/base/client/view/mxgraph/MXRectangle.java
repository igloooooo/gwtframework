package com.iglooit.core.base.client.view.mxgraph;

import com.google.gwt.core.client.JavaScriptObject;

public class MXRectangle
{
    private final JavaScriptObject repr;

    private transient double minX;
    private transient double maxX;
    private transient double minY;
    private transient double maxY;

    public MXRectangle(JavaScriptObject repr)
    {
        this.repr = repr;
    }

    private void refreshTransients()
    {
        minX = getMinX();
        maxX = getMaxX();
        minY = getMinY();
        maxY = getMaxY();
    }

    public double getCenterX()
    {
        return getCenterX(repr);
    }

    public double getCenterY()
    {
        return getCenterY(repr);
    }

    public double getWidth()
    {
        return getWidth(repr);
    }

    public double getHeight()
    {
        return getHeight(repr);
    }

    private native double getCenterX(JavaScriptObject repr) /*-{
        return repr.getCenterX();
     }-*/;

    private native double getCenterY(JavaScriptObject repr) /*-{
        return repr.getCenterY();
     }-*/;

    private native double getWidth(JavaScriptObject repr) /*-{
        return repr.width;
     }-*/;

    private native double getHeight(JavaScriptObject repr) /*-{
        return repr.height;
     }-*/;

    public double getMinX()
    {
        return getCenterX() - getWidth() / 2;
    }

    public double getMaxX()
    {
        return getCenterX() + getWidth() / 2;
    }

    public double getMinY()
    {
        return getCenterY() - getHeight() / 2;
    }

    public double getMaxY()
    {
        return getCenterY() + getHeight() / 2;
    }

    public boolean intersects(MXRectangle other)
    {
        return intersects(other, 0, 0);
    }

    public boolean intersects(MXRectangle other, double xBuffer, int yBuffer)
    {
        return !(other.getMinX() > getMaxX() + xBuffer
            || other.getMaxX() < getMinX() - xBuffer
            || other.getMinY() > getMaxY() + yBuffer
            || other.getMaxY() < getMinY() - yBuffer);
    }

    @Override
    public String toString()
    {
        refreshTransients();
        return "[mxRect x: " + minX + " - " + maxX + ", y: " + minY + " - " + maxY + "]";
    }

}
