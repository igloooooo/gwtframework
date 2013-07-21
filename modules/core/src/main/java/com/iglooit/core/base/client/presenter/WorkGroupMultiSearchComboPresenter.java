package com.iglooit.core.base.client.presenter;

import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.account.iface.command.request.WorkGroupPagingListReadRequest;
import com.clarity.core.account.iface.command.response.WorkGroupPagingListReadResponse;
import com.clarity.core.base.client.controller.GAsyncCallback;
import com.clarity.core.base.client.mvp.DefaultPresenter;
import com.clarity.core.base.client.view.WorkGroupMultiSearchComboView;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.google.gwt.event.shared.HandlerManager;

import java.util.List;

public class WorkGroupMultiSearchComboPresenter extends DefaultPresenter<WorkGroupMultiSearchComboView> implements
    WorkGroupMultiSearchComboView.IPresenter
{
    private CommandServiceClientImpl commandService;
    private List<String> defaultWorkGroupNames;
    private boolean isFirstLoad = true;

    public WorkGroupMultiSearchComboPresenter(CommandServiceClientImpl commandService, HandlerManager sharedEventBus)
    {
        this(new WorkGroupMultiSearchComboView(), commandService, sharedEventBus);
    }

    public WorkGroupMultiSearchComboPresenter(WorkGroupMultiSearchComboView display,
                                              CommandServiceClientImpl commandService, HandlerManager sharedEventBus)
    {
        super(display, sharedEventBus);
        this.commandService = commandService;

        bind();
    }

    @Override
    public void bind()
    {
        getDisplay().setPresenter(this);
    }


    @Override
    public void onLoadPage(SearchPaging searchPaging, Option<SearchSorting> searchSortingOption)
    {
        commandService.run(new WorkGroupPagingListReadRequest(searchPaging,
            searchSortingOption, getDisplay().getComboRawValue()),
            new GAsyncCallback<WorkGroupPagingListReadResponse>()
            {
                @Override
                public void onSuccess(WorkGroupPagingListReadResponse response)
                {
                    getDisplay().setPagingLoadResult(response.getList(), response.getSearchPaging());
                }
            }
        );
    }
}
