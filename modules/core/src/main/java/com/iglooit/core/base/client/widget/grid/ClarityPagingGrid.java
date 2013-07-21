package com.iglooit.core.base.client.widget.grid;

import com.clarity.commons.iface.domain.OrderBy;
import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.view.ClarityBasePagingLoader;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.CommandPagingLoader;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public abstract class ClarityPagingGrid<MD extends ModelData> extends ClarityBasicGrid<MD>
{
    private PagingToolBar toolBar;

    private CommandPagingLoader<MD> commandPagingLoader;
    private PagingLoader<PagingLoadResult<MD>> gridLoader;

    private int pagingSize;

    protected ClarityPagingGrid(boolean checkBox, boolean checkBoxFirstColumn, int pagingSize)
    {
        super(checkBox, checkBoxFirstColumn);
        this.pagingSize = pagingSize;

        setupLoader();
        setupStoreAndReconfigGrid();
        setupPagingToolBar();
    }

    protected ClarityPagingGrid(boolean checkBox, boolean checkBoxFirstColumn,
                                boolean supportHeaderFilter, int pagingSize)
    {
        super(checkBox, checkBoxFirstColumn);
        this.pagingSize = pagingSize;

        setupLoader(supportHeaderFilter);
        setupStoreAndReconfigGrid();
        setupPagingToolBar();
    }

    private void setupLoader(boolean supportFilter)
    {
        RpcProxy<PagingLoadResult<MD>> rpcProxy = new RpcProxy<PagingLoadResult<MD>>()
        {
            public void load(Object o, AsyncCallback<PagingLoadResult<MD>> listAsyncCallback)
            {
                if (commandPagingLoader == null)
                    throw new AppX("Paging loader not set, implementation error");
                if (!(o instanceof PagingLoadConfig))
                    throw new AppX("Object isn't PagingLoadConfig");
                SearchPaging searchPaging = getSearchPaging((PagingLoadConfig)o);
                Option<SearchSorting> searchSortingOption = getSearchSorting((PagingLoadConfig)o);
                commandPagingLoader.load(searchPaging, searchSortingOption, listAsyncCallback);
            }
        };

        if (!supportFilter)
        {
            gridLoader = new BasePagingLoader<PagingLoadResult<MD>>(rpcProxy);
        }
        else
        {
            gridLoader = new ClarityBasePagingLoader<PagingLoadResult<MD>>(rpcProxy);
        }
        gridLoader.setRemoteSort(true);
        gridLoader.setOffset(0);
        gridLoader.setLimit(pagingSize);
    }

    private void setupLoader()
    {
        setupLoader(false);
    }

    private void setupPagingToolBar()
    {
        toolBar = new PagingToolBar(pagingSize);
        toolBar.bind(gridLoader);
        toolBar.addStyleName(ClarityStyle.TOOLBAR_BASIC);
        setBottomComponent(toolBar);
    }

    private void setupStoreAndReconfigGrid()
    {
        ListStore<MD> store = new ListStore<MD>(gridLoader);
        setStore(store);
        reconfigGrid();
    }

    public SearchPaging getSearchPaging(PagingLoadConfig searchData)
    {
        return new SearchPaging(pagingSize, searchData.getOffset());
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

    public void setPagingLoader(CommandPagingLoader<MD> pagingLoader)
    {
        this.commandPagingLoader = pagingLoader;
    }

    public void reLoad()
    {
        this.gridLoader.load(0, pagingSize);
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

    public void setLoadConfig(PagingLoadConfig config)
    {
        if (gridLoader instanceof BasePagingLoader)
        {
            ((BasePagingLoader)gridLoader).useLoadConfig(config);
        }
    }

    public PagingLoader getLoader()
    {
        return gridLoader;
    }
}
