package com.iglooit.core.base.client.widget.grid;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public abstract class ClarityFilterLocalPagingGrid<T extends ModelData> extends ClarityLocalPagingGrid<T>
{
    private StoreFilterField<T> searchField;
    private ToolBar toolBar;

    protected ClarityFilterLocalPagingGrid(int pagingSize)
    {
        super(false, false, 20);
        createSearchToolBar();
    }

    private void createSearchToolBar()
    {
        // create search field and search button
        toolBar = new ToolBar();

        toolBar.setAutoWidth(true);
        toolBar.setWidth("100%");
        searchField = createSearchField();
        searchField.setTriggerStyle("x-form-clear-trigger");
        searchField.setName("searchfilter");
        searchField.setAutoWidth(true);
        searchField.bind(getGrid().getStore());
        toolBar.add(searchField);
        setTopComponent(toolBar);
    }

    public void setSearchFieldEmptyText(String message)
    {
        searchField.setEmptyText(message);
    }

    @Override
    public String getLabel()
    {
        return null;
    }

    /**
     * @return Creates and return the filter to use - subclasses should implement this to filter on
     *         specific fields.
     */
    protected StoreFilterField<T> createSearchField()
    {
        return new StoreFilterField<T>()
        {
            @Override
            protected boolean doSelect(Store<T> store, T parent,
                                       T record, String property, String filter)
            {
                return record.toString().toLowerCase().contains(filter.toLowerCase());
            }
        };
    }

    public ToolBar getToolBar()
    {
        return toolBar;
    }
}
