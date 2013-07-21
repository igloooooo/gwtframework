package com.iglooit.core.base.client.widget.grid;

import com.clarity.core.base.client.view.ClarityStyle;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;

import java.util.ArrayList;
import java.util.List;

public abstract class ClarityLocalPagingGrid<MD extends ModelData> extends ClarityBasicGrid<MD>
{
    private PagingModelMemoryProxy proxy;
    private PagingLoader<PagingLoadResult<MD>> loader;
    private PagingToolBar toolBar;

    private boolean autoHidePagingBar;

    private int pagingSize;

    protected ClarityLocalPagingGrid(boolean checkBox, boolean checkBoxFirstColumn, int pagingSize)
    {
        super(checkBox, checkBoxFirstColumn);
        this.pagingSize = pagingSize;

        setupLoader();
        setupStoreAndReconfigGrid();
        setupPagingToolBar();
    }

    private void setupPagingToolBar()
    {
        toolBar = new PagingToolBar(pagingSize);
        toolBar.bind(loader);
        toolBar.addStyleName(ClarityStyle.TOOLBAR_BASIC);
        setBottomComponent(toolBar);
    }

    public PagingLoader<PagingLoadResult<MD>> getLoader()
    {
        return loader;
    }

    private void setupStoreAndReconfigGrid()
    {
        ListStore<MD> store = new ListStore<MD>(loader);
        setStore(store);
        reconfigGrid();
    }

    private void setupLoader()
    {
        proxy = new PagingModelMemoryProxy(new ArrayList());
        loader = new BasePagingLoader<PagingLoadResult<MD>>(proxy);
        loader.setRemoteSort(false);
        loader.addListener(Loader.Load, new Listener<LoadEvent>()
            {
                @Override
                public void handleEvent(LoadEvent le)
                {
                    if (!autoHidePagingBar)
                        return;

                    PagingLoadResult<MD> result = le.getData();
                    boolean allResultsFound = result.getTotalLength() <= result.getData().size();
                    toolBar.setVisible(!allResultsFound);
                }
            });
    }

    @Override
    public void updateStore(List<MD> itemList)
    {
        if (proxy != null)
            proxy.setData(itemList);
        if (loader != null)
            loader.load();
    }


    public void clearState()
    {
        loader.setOffset(0);
        toolBar.enable();
    }

    public boolean isAutoHidePagingBar()
    {
        return autoHidePagingBar;
    }

    public void setAutoHidePagingBar(boolean autoHidePagingBar)
    {
        this.autoHidePagingBar = autoHidePagingBar;
    }
}
