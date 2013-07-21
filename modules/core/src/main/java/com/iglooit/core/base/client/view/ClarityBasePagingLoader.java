package com.iglooit.core.base.client.view;

import com.extjs.gxt.ui.client.data.BaseFilterPagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public class ClarityBasePagingLoader<D extends PagingLoadResult<?>> extends BasePagingLoader
{

    private BaseFilterPagingLoadConfig loaderConfig;

    public ClarityBasePagingLoader(DataProxy proxy)
    {
        super(proxy);
    }

    @Override
    protected Object newLoadConfig()
    {
        loaderConfig = new BaseFilterPagingLoadConfig();
        return loaderConfig;
    }

    public BaseFilterPagingLoadConfig getLoaderConfig()
    {
        return loaderConfig;
    }
}
