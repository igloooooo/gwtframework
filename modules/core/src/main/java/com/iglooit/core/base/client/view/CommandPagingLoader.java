package com.iglooit.core.base.client.view;

import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.type.Option;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommandPagingLoader<MD>
{
    void load(SearchPaging searchPaging, Option<SearchSorting> searchSortingOption,
              AsyncCallback<PagingLoadResult<MD>> callback);
}
