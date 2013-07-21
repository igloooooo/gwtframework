package com.iglooit.core.base.client.widget.grid;

import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;

/**
 * Created by IntelliJ IDEA.
 * User: chuang
 * Date: 8/05/13
 * Time: 3:23 PM
 */

public class ClarityPagingToolBar extends PagingToolBar
{

    public static final int MIN_TOOL_BAR_SIZE = 190;
    /**
     * Creates a new paging tool bar with the given page size.
     *
     * @param pageSize the page size
     */
    public ClarityPagingToolBar(int pageSize)
    {
        super(pageSize);
    }


    /**
     * This is used only for those grids that did not get data from load function
     * but from some other places.
     * refresh doesn't really make sense
     * whoever wants to use this please talk to Rhandy.
     */
    public void setRefreshButtonVisible(boolean visible)
    {
        refresh.setVisible(visible);
    }
}
