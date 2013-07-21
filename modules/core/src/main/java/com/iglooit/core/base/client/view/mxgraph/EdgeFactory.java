package com.iglooit.core.base.client.view.mxgraph;
public interface EdgeFactory<E extends Edge> 
{
    E createEdge(Cell vStart, Cell vEnd);
}
