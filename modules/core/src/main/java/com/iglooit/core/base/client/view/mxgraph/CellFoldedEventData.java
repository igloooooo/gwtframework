package com.iglooit.core.base.client.view.mxgraph;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 14/02/2011
 * Time: 5:13:00 PM
 */
public class CellFoldedEventData implements EventData
{
    private boolean collapse;

    public CellFoldedEventData(boolean collapse)
    {
        this.collapse = collapse;
    }

    public boolean isCollapse()
    {

        return collapse;
    }

    public void setCollapse(boolean collapse)
    {
        this.collapse = collapse;
    }

    public CellFoldedEventData()
    {
    }
}
