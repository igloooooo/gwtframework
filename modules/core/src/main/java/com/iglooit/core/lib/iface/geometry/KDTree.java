package com.iglooit.core.lib.iface.geometry;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.type.Tuple3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class KDTree<T extends KDTree.KDComparable> implements Serializable
{
    private final int axes;
    private Option<Node> root = Option.none();

    public KDTree(List<T> items, int axes)
    {
        this.axes = axes;
        this.root = construct(items, 0);
    }

    public int size()
    {
        if (root.isNone())
            return 0;
        return size(root);
    }

    protected Option<Node> getRoot()
    {
        return root;
    }

    protected void setRoot(Option<Node> root)
    {
        this.root = root;
    }

    public int getAxes()
    {
        return axes;
    }

    private int size(Option<Node> node)
    {
        if (node.isNone())
            return 0;
        return 1 + size(node.value().getLeft()) + size(node.value().getRight());
    }

    protected void sortOnAxis(List<T> items, final int axis)
    {
        Collections.sort(items, new Comparator<T>() {
            public int compare(T o1, T o2) {
                return comp(o1, o2, axis);
            }
        });
    }

    protected static int comp(KDComparable i1, KDComparable i2, int axis)
    {
        double d1 = i1.getCoordinate(axis);
        double d2 = i2.getCoordinate(axis);
        return Double.compare(d1, d2);
    }

    public static double distance(KDComparable i1, KDComparable i2, int axes)
    {
        double d = 0;
        for (int i = 0; i < axes; i++)
        {
            double x = i1.getCoordinate(i) - i2.getCoordinate(i);
            d += x * x;
        }
        return Math.sqrt(d);
    }

    protected List<T> sublist(List<T> list, int lower, int upper)
    {
        if (upper < lower) return Collections.emptyList();
        return list.subList(lower, upper);
    }

    private Option<Node> construct(List<T> itemsIn, int axis)
    {
        if (itemsIn == null || itemsIn.size() == 0)
            return Option.none();

        List<T> items = new ArrayList<T>(itemsIn);
        Tuple3<List<T>, T, List<T>> ps = pivotAndSplit(items, axis);
        List<T> leftItems = ps.getFirst();
        T item = ps.getSecond();
        List<T> rightItems = ps.getThird();

        Node node = new Node(item, axis, Option.<Node>none(), axes);
        Option<Node> leftChild = construct(leftItems, node.nextAxis());
        Option<Node> rightChild = construct(rightItems, node.nextAxis());
        node.setLeft(leftChild);
        node.setRight(rightChild);
        if (leftChild.isSome()) leftChild.value().setParent(Option.some(node));
        if (rightChild.isSome()) rightChild.value().setParent(Option.some(node));
        return Option.some(node);
    }

    protected Tuple3<List<T>, T, List<T>> pivotAndSplit(List<T> items, int axis)
    {
        sortOnAxis(items, axis);
        int itemIndex = items.size() / 2;
        T item = items.get(itemIndex);

        List<T> leftItems = sublist(items, 0, itemIndex);
        List<T> rightItems = sublist(items, itemIndex + 1, items.size());
        return new Tuple3<List<T>, T, List<T>>(leftItems, item, rightItems);
    }

    public List<T> getNodesInside(final GRectangle rectangle)
    {
        if (getRoot().isNone()) return Collections.emptyList();
        List<T> results = new ArrayList<T>();
        getNodesIn(rectangle, 0, getRoot().value(), results);
        return results;
    }

    public List<T> getNodesNear(final GPoint point, final double threshold)
    {
        if (getRoot().isNone()) return Collections.emptyList();
        List<T> results = new ArrayList<T>();
        getNodesIn(point, threshold, getRoot().value(), results);
        return results;
    }

    // todo ms: ugly non-oo.... but fix isn't obvious since this isn't quite polymorphic
    private boolean intersects(KDComparable node, KDComparable geom, double threshold)
    {
        if (geom instanceof GPoint)
            return node.intersects((GPoint)geom, threshold);
        else if (geom instanceof GRectangle)
            return node.intersects((GRectangle)geom);
        else
            throw new AppX("Bad geom");
    }


    private void getNodesIn(KDSearchable geom, double threshold, Node<T> node, List<T> results)
    {
        if (intersects(node.getPayload(), geom, threshold))
            results.add(node.getPayload());

        if (node.getLeft().isSome() && traverseLeft(geom, threshold, node.getPayload(), node.getAxis()))
            getNodesIn(geom, threshold, node.getLeft().value(), results);
        if (node.getRight().isSome() && traverseRight(geom, threshold, node.getPayload(), node.getAxis()))
            getNodesIn(geom, threshold, node.getRight().value(), results);

    }

    private boolean traverseLeft(KDSearchable target, double threshold, T payload, int axis)
    {
        if (payload.isMininumBoundAxis(axis))
            return true;
        double pVal = payload.getCoordinate(axis);
        if (payload.isMaximumBoundAxis(axis))
        {
            // every element in the left subtree has a smaller maximum bound.
            double minTarget = target.getMinimumOnAxis(axis, threshold);
            return minTarget < pVal;
        }
        else
        {
            // just regular point space. go left if we need to look at smaller items.
            double tVal = target.getCoordinate(axis);
            return tVal <= pVal + threshold;
        }
    }

    private boolean traverseRight(KDSearchable target, double threshold, T payload, int axis)
    {
        if (payload.isMaximumBoundAxis(axis))
            return true;
        double pVal = payload.getCoordinate(axis);
        if (payload.isMininumBoundAxis(axis))
        {
            double maxTarget = target.getMaximumOnAxis(axis, threshold);
            return maxTarget > pVal;
        }
        else
        {
            double tVal = target.getCoordinate(axis);
            return tVal >= pVal - threshold;
        }
    }

    public static class Node<T extends KDComparable> implements Serializable
    {
        private int axis;
        private final int axes;
        private Option<Node<T>> left;
        private Option<Node<T>> right;
        private Option<Node<T>> parent;
        private T payload;

        public Node(T payload, int axis, Option<Node<T>> parent, int axes)
        {
            this.axis = axis;
            this.payload = payload;
            this.parent = parent;
            this.axes = axes;
        }

        public int getAxis()
        {
            return axis;
        }

        public void setAxis(int axis)
        {
            this.axis = axis;
        }

        public Option<Node<T>> getLeft()
        {
            return left;
        }

        public void setLeft(Option<Node<T>> left)
        {
            this.left = left;
        }

        public Option<Node<T>> getRight()
        {
            return right;
        }

        public void setRight(Option<Node<T>> right)
        {
            this.right = right;
        }

        public T getPayload()
        {
            return payload;
        }

        public void setPayload(T payload)
        {
            this.payload = payload;
        }

        public int nextAxis()
        {
            return (axis + 1) % axes;
        }

        public void setChild(T item)
        {
            Node<T> newNode = new Node(item, nextAxis(), Option.some(this), axes);
            int comp = comp(payload, item, axis);
            if (comp < 0)
                setLeft(Option.some(newNode));
            else
                setRight(Option.some(newNode));
        }

        public Option<Node<T>> getParent()
        {
            return parent;
        }

        public void setParent(Option<Node<T>> parent)
        {
            this.parent = parent;
        }
    }

    public interface KDSearchable extends KDComparable
    {
        double getMinimumOnAxis(int axis, double threshold);

        double getMaximumOnAxis(int axis, double threshold);
    }

    public interface KDComparable extends Serializable
    {
        double getCoordinate(int axis);

        boolean isMininumBoundAxis(int axis);

        boolean isMaximumBoundAxis(int axis);

        boolean intersects(GPoint point, double threshold);

        boolean intersects(GRectangle rectangle);
    }

}
