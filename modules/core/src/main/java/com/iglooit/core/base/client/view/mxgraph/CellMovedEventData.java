package com.iglooit.core.base.client.view.mxgraph;

public class CellMovedEventData implements EventData
{
    private int x;
    private int y;

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public CellMovedEventData(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public CellMovedEventData()
    {
    }
}
