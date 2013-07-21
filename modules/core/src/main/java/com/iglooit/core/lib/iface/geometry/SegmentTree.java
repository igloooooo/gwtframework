package com.iglooit.core.lib.iface.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 23/04/13 10:32 AM
 */
public class SegmentTree<V extends Comparable, T extends SegmentTree.Interval<V>>
{
    private SegmentNode<V> root;

    public SegmentTree(List<T> items)
    {
        constructTree(items);
    }

    private void constructTree(List<T> items)
    {
        ArrayList<V> points = new ArrayList<V>(2 * items.size());
        for (T item : items)
        {
            points.add(item.getMin());
            points.add(item.getMax());
        }
        Collections.sort(points);

        root = buildTree(points, 0, points.size() - 1);
        populateTree(items, root);

    }

    private void populateTree(List<T> items, SegmentNode<V> root)
    {
        for (T item : items)
        {
            populateTree0(item, root);
        }
    }

    private void populateTree0(T item, SegmentNode<V> node)
    {
        if (root.interval.containedIn(item))
            root.payload.add(item);
        else
        {
            if (root.left != null && root.left.interval.intersects(item))
                populateTree0(item, root.left);
            if (root.right != null && root.right.interval.intersects(item))
                populateTree0(item, root.right);
        }
    }

    private SegmentNode<V> buildTree(ArrayList<V> points, int low, int high)
    {
        if (low > high)
            return null;

        V minValue = low <= 0 ? null : points.get(low - 1);
        V maxValue = points.get(high);
        SegmentNode<V> top = new SegmentNode<V>(minValue, false, maxValue, true);

        if (low == high)
        {
            // create a leaf tree
            V lMinValue = low <= 0 ? null : points.get(low - 1);
            V lMaxValue = points.get(low);
            SegmentNode<V> left = new SegmentNode<V>(lMinValue, false, lMaxValue, false);

            V rMinValue = points.get(low);
            V rMaxValue = points.get(low);
            SegmentNode<V> right = new SegmentNode<V>(rMinValue, true, rMaxValue, true);

            top.left = left;
            top.right = right;

            return top;
        }
        else
        {
            int mid = (low + high) >>> 1;
            top.left = buildTree(points, low, mid);
            top.right = buildTree(points, mid + 1, high);
            return top;
        }
    }

    public interface Interval<TValue extends Comparable>
    {
        TValue getMin();
        TValue getMax();
    }

    public static class SegmentInterval<TValue extends Comparable> implements Interval<TValue>
    {
        private TValue min;
        private TValue max;
        private boolean maxInclusive;
        private boolean minInclusive;

        public SegmentInterval(TValue min, boolean minInclusive, TValue max, boolean maxInclusive)
        {
            this.min = min;
            this.minInclusive = minInclusive;
            this.max = max;
            this.maxInclusive = maxInclusive;
        }

        @Override
        public TValue getMin()
        {
            return min;
        }

        @Override
        public TValue getMax()
        {
            return max;
        }

        public boolean intersects(Interval<TValue> interval)
        {
            return min == null ? true
                    : (minInclusive ? min.compareTo(interval.getMax()) <= 0
                                    : min.compareTo(interval.getMax()) < 0)
                && max == null ? true
                    : (maxInclusive ? max.compareTo(interval.getMin()) >= 0
                                    : max.compareTo(interval.getMin()) > 0);
        }

        public boolean containedIn(Interval<TValue> interval)
        {
            return getMin().compareTo(interval.getMin()) >= 0 && getMax().compareTo(interval.getMax()) <= 0;
        }
    }

    public static class SegmentNode<TValue extends Comparable>
    {
        private SegmentNode<TValue> left;
        private SegmentNode<TValue> right;
        private ArrayList<Interval<TValue>> payload;
        private SegmentInterval<TValue> interval;

        public SegmentNode(TValue min, boolean minInclusive, TValue max, boolean maxInclusive)
        {
            interval = new SegmentInterval<TValue>(min, minInclusive, max, maxInclusive);
            payload = new ArrayList<Interval<TValue>>();
        }

        public SegmentNode<TValue> getLeft()
        {
            return left;
        }

        public SegmentNode<TValue> getRight()
        {
            return right;
        }

        public void setLeft(SegmentNode<TValue> left)
        {
            this.left = left;
        }

        public void setRight(SegmentNode<TValue> right)
        {
            this.right = right;
        }
    }
}
