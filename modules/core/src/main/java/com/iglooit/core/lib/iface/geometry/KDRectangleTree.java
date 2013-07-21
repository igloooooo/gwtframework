package com.iglooit.core.lib.iface.geometry;

import java.util.List;

public class KDRectangleTree<T extends KDRectangleTree.KDRectangleComparable> extends KDTree<T>
{
    public KDRectangleTree(List<T> items)
    {
        super(items, 4);
    }

    public interface KDRectangleComparable extends KDComparable
    {
        GRectangle getRectangle();
    }
}
