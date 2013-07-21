package com.iglooit.core.lib.iface.geometry;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class GPoint implements IsSerializable, Serializable, KDTree.KDSearchable
{
    private Double x;
    private Double y;
    private Units units;
    private Projection projection;

    public GPoint()
    {
    }

    public GPoint(Double x, Double y)
    {
        this(x, y, Units.NONE, Projection.NONE);
    }

    public GPoint(Double x, Double y, Units units, Projection projection)
    {
        this.x = x == null ? Double.NaN : x;
        this.y = y == null ? Double.NaN : y;
        this.units = units;
        this.projection = projection;
    }

    public Double getX()
    {
        return x;
    }

    public Double getY()
    {
        return y;
    }

    public Units getUnits()
    {
        return units;
    }

    public Projection getProjection()
    {
        return projection;
    }

    public void setX(Double x)
    {
        this.x = x;
    }

    public void setY(Double y)
    {
        this.y = y;
    }

    public void setUnits(Units units)
    {
        this.units = units;
    }

    public void setProjection(Projection projection)
    {
        this.projection = projection;
    }

    public boolean isValid()
    {
        return (!Double.valueOf(x).isInfinite()
            && !Double.valueOf(x).isNaN()
            && !Double.valueOf(y).isInfinite()
            && !Double.valueOf(y).isNaN());
    }

    @Override
    public String toString()
    {
        return "[" + x + ", " + y + " " + units + " " + projection + "]";
    }

    @Override
    public int hashCode()
    {
        return Double.valueOf(x).hashCode() ^ Double.valueOf(y).hashCode() ^ units.hashCode() ^ projection.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof GPoint)
        {
            GPoint other = (GPoint)obj;
            return getX().doubleValue() == other.getX().doubleValue()
                && getY().doubleValue() == other.getY().doubleValue()
                && getUnits().equals(other.getUnits())
                && getProjection().equals(other.getProjection());
        }
        return false;
    }

    public double getCoordinate(int axis)
    {
        return axis % 2 == 0 ? getX() : getY();
    }

    public boolean isMininumBoundAxis(int axis)
    {
        return false;
    }

    public boolean isMaximumBoundAxis(int axis)
    {
        return false;
    }

    public double getMinimumOnAxis(int axis, double threshold)
    {
        return getCoordinate(axis) - threshold;
    }

    public double getMaximumOnAxis(int axis, double threshold)
    {
        return getCoordinate(axis) + threshold;
    }

    public boolean intersects(GPoint point, double threshold)
    {
        double dx = point.getX() - getX();
        double dy = point.getY() - getY();
        if (dy < 0)
            return false;
        return Math.sqrt(dx * dx + dy * dy) <= threshold;
    }

    public boolean intersects(GRectangle rectangle)
    {
        return rectangle.intersects(this, 0);
    }
}
