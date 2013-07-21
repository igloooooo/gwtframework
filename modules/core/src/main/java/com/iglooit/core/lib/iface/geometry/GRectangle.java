package com.iglooit.core.lib.iface.geometry;

import com.clarity.commons.iface.type.Tuple2;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class GRectangle implements Serializable, IsSerializable, KDTree.KDSearchable
{
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    public GRectangle()
    {
    }

    public GRectangle(double minX, double minY, double maxX, double maxY)
    {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public GRectangle(GPoint bottomLeft, GPoint topRight)
    {
        this(bottomLeft.getX(), bottomLeft.getY(), topRight.getX(), topRight.getY());
    }

    public GRectangle(Tuple2<GPoint, GPoint> currentViewportBounds)
    {
        this(currentViewportBounds.getFirst(), currentViewportBounds.getSecond());
    }

    public double getCoordinate(int axis)
    {
        if (axis == 0)
            return minX;
        else if (axis == 1)
            return minY;
        else if (axis == 2)
            return maxX;
        else
            return maxY;
    }

    public boolean isMininumBoundAxis(int axis)
    {
        return axis <= 1;
    }

    public boolean isMaximumBoundAxis(int axis)
    {
        return !isMininumBoundAxis(axis);
    }

    private boolean isXaxis(int axis)
    {
        return axis % 2 == 0;
    }

    public double getMinimumOnAxis(int axis, double threshold)
    {
        return isXaxis(axis) ? minX : minY;
    }

    public double getMaximumOnAxis(int axis, double threshold)
    {
        return isXaxis(axis) ? maxX : maxY;
    }

    public boolean intersects(GPoint point, double threshold)
    {
        return point.getX() >= minX
            && point.getX() <= maxX
            && point.getY() >= minY
            && point.getY() <= maxY;
    }

    public boolean intersects(GRectangle other)
    {
        return !(other.minX > maxX || other.maxX < minX || other.minY > maxY || other.maxY < minY);
    }

    public double getMinX()
    {
        return minX;
    }

    public double getMinY()
    {
        return minY;
    }

    public double getMaxX()
    {
        return maxX;
    }

    public double getMaxY()
    {
        return maxY;
    }

    public double getDX()
    {
        return maxX - minX;
    }

    public double getDY()
    {
        return maxY - minY;
    }

    public Tuple2<GPoint, GPoint> asTuple()
    {
        return new Tuple2<GPoint, GPoint>(
            new GPoint(getMinX(), getMinY(), Units.DEGREES, Projection.EPSG4326),
            new GPoint(getMaxX(), getMaxY(), Units.DEGREES, Projection.EPSG4326));
    }
}
