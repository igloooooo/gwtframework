package com.iglooit.core.lib.iface.geometry;

import java.util.List;

public class KDPointTree<T extends KDTree.KDComparable> extends KDTree<T>
{
    public KDPointTree(List<T> items)
    {
        super(items, 2);
    }

    private double diffOnAxis(KDComparable i1, KDComparable i2, int axis)
    {
        return i1.getCoordinate(axis) - i2.getCoordinate(axis);
    }

    protected void getNodesInside(GRectangle rectangle, Node<T> node, List<T> results)
    {
        if (node.getPayload().intersects(rectangle))
            results.add(node.getPayload());

        double diff = diffOnAxis(rectangle, node.getPayload(), node.getAxis());
        if (diff < 0 && node.getLeft().isSome())
            getNodesInside(rectangle, node.getLeft().value(), results);
        if (diff > 0 && node.getRight().isSome())
            getNodesInside(rectangle, node.getRight().value(), results);
    }

    protected void getNodesNear(GPoint point, double threshold, Node<T> node, List<T> results)
    {
        if (node.getPayload().intersects(point, threshold))
            results.add(node.getPayload());

        double diff = diffOnAxis(point, node.getPayload(), node.getAxis());
        if (diff < 0 && node.getLeft().isSome())
            getNodesNear(point, threshold, node.getLeft().value(), results);
        if (diff > 0 && node.getRight().isSome())
            getNodesNear(point, threshold, node.getRight().value(), results);
    }
}
