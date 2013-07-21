package com.iglooit.core.base.client.widget.grid;

import com.clarity.commons.iface.domain.OrderBy;
import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.NonSerOpt;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.view.ClarityBasePagingLoader;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.CommandPagingLoader;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseFilterPagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;
import java.util.Map;

public abstract class SearchResultListDisplay<MD extends ModelData> extends ClarityGrid<MD>
{
    private Grid<MD> grid;
    private OpenStoreGridView listView;
    private EditorGrid<MD> filterGrid;
    private ContentPanel contentPanel;
    private MockedRowEditor re;

    private PagingToolBar toolBar;

    private int pagingPageSize;

    private boolean bottomPaging;

    private CommandPagingLoader<MD> pagingLoader;
    private PagingLoader<PagingLoadResult<MD>> gridLoader;

    private NonSerOpt<MD> filterModelData;
    private boolean isManualLoad;

    private String initialSortField = null;
    private OrderBy initialSortDirection = null;

    public SearchResultListDisplay(int pagingPageSize)
    {
        this(pagingPageSize, null);
    }

    public SearchResultListDisplay(int pagingPageSize, boolean bottomPaging)
    {
        this(pagingPageSize, null, bottomPaging, null);
    }

    public SearchResultListDisplay(int pagingPageSize, boolean bottomPaging,
                                   String initialSortField, OrderBy initialSortDirection)
    {
        this(pagingPageSize, null, bottomPaging, initialSortField, initialSortDirection, null, false);
    }

    public SearchResultListDisplay(int pagingPageSize, MD filterModelData)
    {
        this(pagingPageSize, filterModelData, false, null);
    }

    public SearchResultListDisplay(int pagingPageSize, MD filterModelData, boolean bottomPaging, List<ColumnConfig>
        columnConfig)
    {
        this(pagingPageSize, filterModelData, bottomPaging, null, null, columnConfig, false);
    }

    public SearchResultListDisplay(int pagingPageSize, MD filterModelData, boolean bottomPaging,
                                   String initialSortField, OrderBy initialSortDirection,
                                   List<ColumnConfig> columnConfig, boolean supportHeaderFilter)
    {
        this.filterModelData = NonSerOpt.option(filterModelData);
        this.pagingPageSize = pagingPageSize;
        this.bottomPaging = bottomPaging;
        this.initialSortField = initialSortField;
        this.initialSortDirection = initialSortDirection;
        setColumnConfig(columnConfig);
        init(supportHeaderFilter);
    }
    
    public void setColumnConfig(List<ColumnConfig> columnConfig)
    {
        
    }

    public SearchPaging getSearchPaging(PagingLoadConfig searchData)
    {
        return new SearchPaging(pagingPageSize, searchData.getOffset());
    }

    public Option<SearchSorting> getSearchSorting(PagingLoadConfig searchData)
    {
        SearchSorting setSort = null;
        OrderBy searchDirection = searchData.getSortDir().equals(Style.SortDir.DESC)
            ? OrderBy.DESCENDING : OrderBy.ASCENDING;
        if (searchData.getSortField() != null && searchData.getSortField().length() > 0)
            setSort = new SearchSorting(searchData.getSortField(), searchDirection);
        else
        {
            String sortField = null;
            for (int column = 0; column < getGrid().getColumnModel().getColumnCount(); column++)
            {
                String columnId = getGrid().getColumnModel().getColumnId(column);
                if (columnId.contains("clarity"))
                {
                    sortField = columnId;
                    break;
                }
            }
            if (sortField != null)
                setSort = new SearchSorting(sortField, searchDirection);
        }
        return Option.option(setSort);
    }

    public void clearOffset()
    {
        gridLoader.setOffset(0);
    }

    public boolean isManualLoad()
    {
        return isManualLoad;
    }

    public void setManualLoad(boolean manualLoad)
    {
        isManualLoad = manualLoad;
    }

    public void init(boolean supportFilter)
    {
        RpcProxy<PagingLoadResult<MD>> rpcProxy = new RpcProxy<PagingLoadResult<MD>>()
        {
            public void load(Object o, AsyncCallback<PagingLoadResult<MD>> listAsyncCallback)
            {
                if (pagingLoader == null)
                    throw new AppX("Paging loader not set, implementation error");
                if (!(o instanceof PagingLoadConfig))
                    throw new AppX("Object isn't PagingLoadConfig");
                SearchPaging searchPaging = getSearchPaging((PagingLoadConfig)o);
                Option<SearchSorting> searchSortingOption = getSearchSorting((PagingLoadConfig)o);
                pagingLoader.load(searchPaging, searchSortingOption, listAsyncCallback);
            }
        };

        if (!supportFilter)
            gridLoader = new BasePagingLoader<PagingLoadResult<MD>>(rpcProxy);
        else
            gridLoader = new ClarityBasePagingLoader<PagingLoadResult<MD>>(rpcProxy);

        ListStore<MD> store = new ListStore<MD>(gridLoader);
        ListStore<MD> filterStore = new ListStore<MD>();

        filterGrid = new EditorGrid<MD>(filterStore, new ColumnModel(getColumnConfig()));
        if (filterModelData.isSome())
            filterGrid.getStore().add(filterModelData.value());

        grid = new Grid<MD>(store, new ColumnModel(getColumnConfig()));

        gridLoader.setRemoteSort(true);
        gridLoader.setOffset(0);
        gridLoader.setLimit(pagingPageSize);

        if (initialSortField != null)
        {
            gridLoader.setSortField(initialSortField);
            gridLoader.setSortDir(
                initialSortDirection == OrderBy.ASCENDING ? Style.SortDir.ASC : Style.SortDir.DESC);
        }

        toolBar = new PagingToolBar(pagingPageSize)
        {
            private Boolean savedEnableState;

            public void bind(PagingLoader<?> loader)
            {
                if (this.loader != null)
                {
                    this.loader.removeLoadListener(loadListener);
                }
                this.loader = loader;
                if (loader != null)
                {
                    loader.setLimit(pageSize);
                    if (loadListener == null)
                    {
                        loadListener = new LoadListener()
                        {
                            public void loaderBeforeLoad(final LoadEvent le)
                            {
                                savedEnableState = savedEnableState == null ? isEnabled() : savedEnableState;
                                setEnabled(false);
                                refresh.setIcon(IconHelper.createStyle("x-tbar-loading"));
                                DeferredCommand.addCommand(new Command()
                                {
                                    public void execute()
                                    {
                                        if (le.isCancelled())
                                        {
                                            refresh.setIcon(getImages().getRefresh());
                                            setEnabled(savedEnableState);
                                        }
                                    }
                                });
                            }

                            public void loaderLoad(LoadEvent le)
                            {
                                refresh.setIcon(getImages().getRefresh());
                                setEnabled(savedEnableState);
                                onLoad(le);
                            }

                            public void loaderLoadException(LoadEvent le)
                            {
                                refresh.setIcon(getImages().getRefresh());
                                setEnabled(savedEnableState);
                            }
                        };
                    }
                    loader.addLoadListener(loadListener);
                }
            }

        };
        toolBar.addStyleName(ClarityStyle.TOOLBAR_BASIC);
        toolBar.bind(gridLoader);
        toolBar.setEnabled(true);

        // todo ms: is statefulness required? this must come from some sort of factory,
        // otherwise state stuffs different grids, especially orderBy.
        //grid.setStateId("searchResults");
        //grid.setStateful(true);
        grid.addListener(Events.Attach, new Listener<GridEvent<MD>>()
        {
            public void handleEvent(GridEvent<MD> be)
            {
                if (!isManualLoad)
                    loadGrid();
            }
        });
        grid.setLoadMask(true);
        grid.setBorders(true);
        re = new MockedRowEditor<MD>();
        getGrid().addPlugin(re);

        contentPanel = new ContentPanel();
        contentPanel.setHeaderVisible(false);
        contentPanel.setLayout(new FitLayout());
        contentPanel.setFrame(true);

        if (filterModelData.isSome())
        {
            filterGrid.getView().setForceFit(true);
            filterGrid.getView().setAutoFill(true);
            contentPanel.add(filterGrid);
            filterGrid.setHeight(50);
            grid.setHideHeaders(true);
        }

        contentPanel.add(grid);

        if (bottomPaging)
            contentPanel.setBottomComponent(toolBar);
        else
            contentPanel.setTopComponent(toolBar);

        //In order to get the store
        listView = new OpenStoreGridView();
        listView.setForceFit(true);
        listView.setAutoFill(true);
        grid.setView(listView);
    }

    public void refreshCurrentRow(int i)
    {
        listView.refreshCurrentRow(i);
    }

    public Grid<MD> getGrid()
    {
        return grid;
    }


    public void loadGrid()
    {
        PagingLoadConfig config = new BasePagingLoadConfig();
        if (gridLoader instanceof ClarityBasePagingLoader)
            config = new BaseFilterPagingLoadConfig();
        config.setOffset(0);
        config.setLimit(pagingPageSize);
        Map<String, Object> state = grid.getState();
        if (state.containsKey("offset"))
        {
            int offset = (Integer)state.get("offset");
            int limit = (Integer)state.get("limit");
            config.setOffset(offset);
            config.setLimit(limit);
        }
        if (state.containsKey("sortField"))
        {
            config.setSortField((String)state.get("sortField"));
            config.setSortDir(Style.SortDir.valueOf((String)state.get("sortDir")));
        }
        gridLoader.load(config);
    }

    public HandlerRegistration addSelectionListener(final SelectionChangedListener<MD> listener)
    {
        getGrid().getSelectionModel().addSelectionChangedListener(listener);
        return new HandlerRegistration()
        {
            public void removeHandler()
            {
                getGrid().getSelectionModel().removeSelectionListener(listener);
            }
        };
    }

    public PagingToolBar getPagingToolBar()
    {
        return toolBar;
    }

    public ContentPanel asWidget()
    {
        return contentPanel;
    }

    public ContentPanel getContentPanel()
    {
        return contentPanel;
    }

    public void setPagingPageSize(int pagingPageSize)
    {
        this.pagingPageSize = pagingPageSize;
        gridLoader.setLimit(pagingPageSize);
        toolBar.setPageSize(pagingPageSize);
    }

    public static <M> PagingLoadResult<M> getPagingLoadResult(final List<M> data, final SearchPaging searchPaging)
    {
        return new PagingLoadResult<M>()
        {
            public int getOffset()
            {
                return searchPaging.getFirstHitIndex();
            }

            public int getTotalLength()
            {
                return (int)searchPaging.getTotalHits();
            }

            public void setOffset(int i)
            {
                throw new AppX("Cannot set offset for paging result");
            }

            public void setTotalLength(int i)
            {
                throw new AppX("Cannot set total length for paging result");
            }

            public List<M> getData()
            {
                return data;
            }
        };
    }


    public void setPagingLoader(CommandPagingLoader<MD> pagingLoader)
    {
        this.pagingLoader = pagingLoader;
    }


    public void refreshView()
    {
        listView.refresh(true);
    }

    public void refresh()
    {
        gridLoader.load();
    }

    public ListStore<MD> getStore()
    {
        return grid.getStore();
    }

    //
    public void deselectRow()
    {
        listView.deselectRow();
    }

    public void selectRow(int i)
    {
        listView.selectRow(i);
    }

    public void clearSelection()
    {
        getGrid().getSelectionModel().deselectAll();
    }

    public void newSearch()
    {
        clearSelection();
        clearOffset();
        toolBar.enable();
        refresh();
    }


    public void unMaskGrid()
    {
        getGrid().unmask();
    }

    public void setRemoteSorting(boolean sorting)
    {
        gridLoader.setRemoteSort(sorting);
    }

    public void selectModel(MD model)
    {
        getGrid().getSelectionModel().select(model, true);
    }

    public void selectValueIfPropertyTheSame(String propertyName, String property)
    {
        if (propertyName == null || property == null)
            throw new AppX("property Name or value should not be null");
        for (int index = 0; index < getStore().getCount(); index++)
        {
            if (property.equals(getStore().getAt(index).get(propertyName)))
            {
                grid.getSelectionModel().select(getStore().getAt(index), true);
                grid.getView().focusRow(index);
                return;
            }
        }
    }

    private final class MockedRowEditor<M extends ModelData> extends RowEditor
    {
        public void startEditing(int rowIndex, boolean doFocus)
        {
//                super.startEditing(rowIndex, doFocus);
        }

        public int getRowIndex()
        {
            return rowIndex;
        }

        public void setRowIndex(int index)
        {
            rowIndex = index;
        }
    }

    public void setCurrentRowIndex(int index)
    {
        re.setRowIndex(index);
    }

    public MD getCurrentEditingValue()
    {
        return getGrid().getStore().getAt(re.getRowIndex());
    }

    /**
     * Applies styling that's useful when grid is nested in other components
     */
    public void useInContainerStyle()
    {
        getContentPanel().setFrame(false);
        getContentPanel().setBorders(false);
        getContentPanel().setBodyBorder(true);
        getGrid().setBorders(false);
    }

    public PagingLoader<PagingLoadResult<MD>> getGridLoader()
    {
        return gridLoader;
    }
}
