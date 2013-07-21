package com.iglooit.core.lib.iface.geometry;

public class AggregatingPointTree
{
}
//public class AggregatingPointTree extends KDPointTree<AggregatingPointTree.AggregatingNode>
//{
//    public AggregatingPointTree()
//    {
////        super(Collections.<AggregatingNode>emptyList());
//    }
////
////    public void add(GPoint item, double threshold)
////    {
////        if (getRoot().isNone())
////        {
////            setRoot(Option.some(new Node(new AggregatingNode(item), 0, Option.<Node>none())));
////        }
//////        else
//////        {
//////            Node parent = getRoot().value();
//////            for (Node node = getRoot().value(); node != null; )
//////            {
//////                parent = node;
//////                double diff = diffOnAxis(item, node.getPayload(), node.getAxis());
//////                if (node.getPayload().intersects(item, threshold))
//////                {
//////                    // aggregate the point
//////                }
////////                else if (diff < 0 && node.value().getLeft().isSome())
////////                {
////////                }
//////            }
//////        }
////    }
////
////    public class AggregatingNode implements KDComparable
////    {
////        private int points = 1;
////        private final GPoint point;
////
////        public AggregatingNode(GPoint point)
////        {
////            this.point = point;
////        }
////
////        public void incrementPoints()
////        {
////            points++;
////        }
////
////        public int getPoints()
////        {
////            return points;
////        }
////
////        public void setPoints(int points)
////        {
////            this.points = points;
////        }
////
////        public double getCoordinate(int axis)
////        {
////            return point.getCoordinate(axis);
////        }
////
////        public boolean isMininumBoundAxis(int axis)
////        {
////            return point.isMininumBoundAxis(axis);
////        }
////
////        public boolean isMaximumBoundAxis(int axis)
////        {
////            return point.isMaximumBoundAxis(axis);
////        }
////
////        public double getMinimumOnAxis(int axis, double threshold)
////        {
////            return point.getMinimumOnAxis(axis, threshold);
////        }
////
////        public double getMaximumOnAxis(int axis, double threshold)
////        {
////            return point.getMaximumOnAxis(axis, threshold);
////        }
////
////        public boolean intersects(GPoint point, double threshold)
////        {
////            return this.point.intersects(point, threshold);
////        }
////
////        public boolean intersects(GRectangle rectangle)
////        {
////            return point.intersects(rectangle);
////        }
////    }
////
////    private double diffOnAxis(KDComparable i1, KDComparable i2, int axis)
////    {
////        return i1.getCoordinate(axis) - i2.getCoordinate(axis);
////    }
//}
