package com.iglooit.core.base.client.view.mxgraph.layout;

import com.clarity.core.base.client.view.mxgraph.Cell;
import com.clarity.core.base.client.view.mxgraph.Graph;

import java.util.ArrayList;

public class MultiStageLayout extends Layout
{
    private ArrayList<Layout> stages = new ArrayList<Layout>();

    public MultiStageLayout(Graph graph)
    {
        super(graph);
    }

    @Override
    public void setStartCell(Cell c)
    {
        for (Layout layout : stages)
        {
            layout.setStartCell(c);
        }
    }

    public void setStartCell(int index, Cell c)
    {
        stages.get(index).setStartCell(c);
    }

    public int addLayout(Layout layout)
    {
        stages.add(layout);
        return stages.size() - 1;
    }

    public void removeLayout(int i)
    {
        stages.remove(i);
    }

    @Override
    public void execute()
    {
        for (Layout layout : stages)
        {
            layout.execute();
        }
    }
}
