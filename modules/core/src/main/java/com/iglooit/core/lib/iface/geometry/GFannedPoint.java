package com.iglooit.core.lib.iface.geometry;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 3/11/11
 * Time: 6:25 PM
 */
public class GFannedPoint extends GPoint
{
    private double fanOffset; // in radians
    private double radius;
    private double wedgeSize; // in radians


    public GFannedPoint(GPoint point)
    {
        super(point.getX(), point.getY(), point.getUnits(), point.getProjection());
    }

    public double getFanOffset()
    {
        return fanOffset;
    }

    public void setFanOffset(double fanOffset)
    {
        this.fanOffset = fanOffset;
    }

    public double getRadius()
    {
        return radius;
    }

    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    public double getWedgeSize()
    {
        return wedgeSize;
    }

    public void setWedgeSize(double wedgeSizes)
    {
        this.wedgeSize = wedgeSizes;
    }
}
