package com.iglooit.core.lib.iface.geometry;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class GLine implements Serializable, IsSerializable, KDRectangleTree.KDRectangleComparable
{
    private GPoint start;
    private GPoint end;
    private GRectangle rectangle;

    public GLine()
    {
    }

    public GLine(GPoint start, GPoint end)
    {
        this.start = start;
        this.end = end;
    }

    public GPoint getStart()
    {
        return start;
    }

    public void setStart(GPoint start)
    {
        this.start = start;
        this.rectangle = null;
    }

    public GPoint getEnd()
    {
        return end;
    }

    public void setEnd(GPoint end)
    {
        this.end = end;
        this.rectangle = null;
    }

    public double getMinX()
    {
        return Math.min(start.getX(), end.getX());
    }

    public double getMaxX()
    {
        return Math.max(start.getX(), end.getX());
    }

    public double getMinY()
    {
        return Math.min(start.getY(), end.getY());
    }

    public double getMaxY()
    {
        return Math.max(start.getY(), end.getY());
    }

    public double getCoordinate(int axis)
    {
        return getRectangle().getCoordinate(axis);
    }

    public boolean isMininumBoundAxis(int axis)
    {
        return getRectangle().isMininumBoundAxis(axis);
    }

    public boolean isMaximumBoundAxis(int axis)
    {
        return getRectangle().isMaximumBoundAxis(axis);
    }

    public double getMinimumOnAxis(int axis, double threshold)
    {
        return getRectangle().getMinimumOnAxis(axis, threshold);
    }

    public double getMaximumOnAxis(int axis, double threshold)
    {
        return getRectangle().getMaximumOnAxis(axis, threshold);
    }

    private double tX(double intercept)
    {
        double dx = end.getX() - start.getX();
        return dx == 0 ? Double.NaN : (intercept - start.getX()) / dx;
    }

    private double tY(double intercept)
    {
        double dy = end.getY() - start.getY();
        return dy == 0 ? Double.NaN : (intercept - start.getY()) / dy;
    }

    private static boolean isBetween(double val, double lower, double upper)
    {
        return val >= lower && val <= upper;
    }

    private static boolean is0to1(double val)
    {
        return isBetween(val, 0, 1);
    }

    public boolean intersects(GRectangle rectangle)
    {
        if (rectangle.intersects(getStart(), 0) || rectangle.intersects(getEnd(), 0))
            return true;

        double txl = tX(rectangle.getMinX());
        if (is0to1(txl))
        {
            double t = start.getY() + txl * (end.getY() - start.getY());
            if (isBetween(t, rectangle.getMinY(), rectangle.getMaxY()))
                return true;
        }

        double tyl = tY(rectangle.getMinY());
        if (is0to1(tyl))
        {
            double t = start.getX() + tyl * (end.getX() - start.getX());
            if (isBetween(t, rectangle.getMinX(), rectangle.getMaxX()))
                return true;
        }

        double txr = tX(rectangle.getMaxX());
        if (is0to1(txl))
        {
            double t = start.getY() + txr * (end.getY() - start.getY());
            if (isBetween(t, rectangle.getMinY(), rectangle.getMaxY()))
                return true;
        }

        double tyr = tY(rectangle.getMaxY());
        if (is0to1(tyl))
        {
            double t = start.getX() + tyr * (end.getX() - start.getX());
            if (isBetween(t, rectangle.getMinX(), rectangle.getMaxX()))
                return true;
        }

        return false;
    }

    public boolean intersects(GPoint point, double threshold)
    {
        // this is not a true ray circle intersection test. it's a square hack.
        return intersects(new GRectangle(
            point.getX() - threshold,
            point.getY() - threshold,
            point.getX() + threshold,
            point.getY() + threshold));
    }

    public GRectangle getRectangle()
    {
        if (rectangle == null)
            rectangle = new GRectangle(getMinX(), getMinY(), getMaxX(), getMaxY());
        return rectangle;
    }
}
