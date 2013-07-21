package com.iglooit.core.base.client.widget.grid;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TwinTriggerField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.KeyCodes;

import java.util.List;

public abstract class FilteredSearchResultListDisplay<T extends ModelData> extends SearchResultListDisplay<T>
{
    private TwinTriggerField searchField;
    private ToolBar searchBar;
    private Label titleLabel;
    private String title;

    public FilteredSearchResultListDisplay(int pagingPageSize, boolean bottomPaging, List<ColumnConfig> columnConfig)
    {
        this(pagingPageSize, bottomPaging, columnConfig, null, null);
    }

    public FilteredSearchResultListDisplay(int pagingPageSize, boolean bottomPaging, List<ColumnConfig> columnConfig,
                                           String prompt)
    {
        this(pagingPageSize, bottomPaging, columnConfig, null, prompt);
    }
    
    public FilteredSearchResultListDisplay(int pagingPageSize, boolean bottomPaging, List<ColumnConfig> columnConfig,
                                           String title, String prompt)
    {
        super(pagingPageSize, null, bottomPaging, columnConfig);
        this.title = title;

        createSearchToolBar(prompt);
        getGrid().setAutoHeight(false);

        addKeyboardListener();
        addSearchButtonListener();
    }

    public void setTitle(String title)
    {
        this.title = title;
        if (title != null)
        {
            titleLabel.setVisible(true);
            titleLabel.setText(title);
            asWidget().layout();
        }
    }
    
    public void createSearchToolBar()
    {
        createSearchToolBar(null);
    }

    public void createSearchToolBar(String prompt)
    {
        titleLabel = new Label(title);
        titleLabel.setVisible(false);
        LayoutContainer layoutContainer = new LayoutContainer();
        layoutContainer.setLayout(new RowLayout());
        
        // create search field and search button
        searchBar = new ToolBar();
        searchBar.setAutoWidth(true);
        searchBar.setWidth("100%");
        searchBar.setSpacing(5);



        searchField = new TwinTriggerField()
        {
            @Override
            protected void onTriggerClick(ComponentEvent ce)
            {
                super.onTriggerClick(ce);
                setValue(null);
                focus();
            }
        };

        if (prompt != null)
        {
            searchBar.add(new Label(prompt));
        }

        searchField.setTriggerStyle("x-form-clear-trigger");
        searchField.setTwinTriggerStyle("x-form-search-trigger");
        searchField.setName("searchfilter");
        searchField.setWidth(230);
        searchBar.add(searchField);        
        
        layoutContainer.add(titleLabel, new RowData(1, -1));
        layoutContainer.add(searchBar, new RowData(1, -1));
        
        asWidget().setTopComponent(layoutContainer);
    }

    public void setSearchFieldEmptyText(String emptyText)
    {
        searchField.setEmptyText(emptyText);
    }

    public String getSearchFieldValue()
    {
        return searchField.getRawValue();
    }

    /**
     * Keyboard listener of search text field
     */
    private void addKeyboardListener()
    {
        addSearchFieldListener(new KeyListener()
        {
            public void componentKeyDown(ComponentEvent event)
            {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER)
                {
                    newSearch();
                }
            }
        });
    }

    /**
     * Button listener of search button
     */
    private void addSearchButtonListener()
    {
        addSearchTriggerListener(new Listener<ComponentEvent>()
        {
            public void handleEvent(ComponentEvent componentEvent)
            {
                newSearch();
            }
        });
    }

    public void addSearchFieldListener(KeyListener keyListener)
    {
        searchField.addKeyListener(keyListener);
    }

    public void addSearchTriggerListener(Listener<ComponentEvent> listener)
    {
        searchField.addListener(Events.TwinTriggerClick, listener);
    }

    public ToolBar getSearchBar()
    {
        return searchBar;
    }
}
