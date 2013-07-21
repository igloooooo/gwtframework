package com.iglooit.core.base.client.widget.grid;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public abstract class MultiFieldSearchResultListDisplay<T extends ModelData> extends SearchResultListDisplay<T>
{
    private ContentPanel functionBarGrid;

    public MultiFieldSearchResultListDisplay(int pagingPageSize, boolean bottomPaging)
    {
        super(pagingPageSize, bottomPaging);
        functionBarGrid = new ContentPanel(new RowLayout());
        functionBarGrid.setHeaderVisible(false);
//        functionBarGrid.setAutoWidth(false);
//        asWidget().setTopComponent(functionBarGrid);
    }

    public LayoutContainer getFunctionBarGrid()
    {
        return functionBarGrid;
    }
}
