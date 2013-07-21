package com.iglooit.core.base.client.view.mxgraph;

public interface ConnectionCallback<E extends Edge> extends GraphCallback
{
    void cellsConnected(String sourceName, String destName, E edge);
}
