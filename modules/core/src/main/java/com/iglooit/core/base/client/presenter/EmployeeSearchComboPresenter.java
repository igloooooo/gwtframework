package com.iglooit.core.base.client.presenter;

import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.account.iface.command.request.EmployeeSearchRequest;
import com.clarity.core.base.client.controller.GAsyncCallback;
import com.clarity.core.base.client.event.WorkGroupComboChangeEvent;
import com.clarity.core.base.client.view.CommandPagingLoader;
import com.clarity.core.base.client.view.EmployeeSearchComboView;
import com.clarity.core.base.client.widget.combobox.SearchComboBox;
import com.clarity.core.base.client.widget.grid.SearchResultListDisplay;
import com.clarity.core.base.iface.command.DomainEntityPagedListResponse;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.clarity.core.lib.client.MetaModelData;
import com.clarity.core.oss.iface.domain.Employees;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSearchComboPresenter extends CustomComponentPresenter<EmployeeSearchComboView>
{
    private CommandServiceClientImpl commandService;
    private String workGroupFilter;
    private Boolean isCurrentWorkGroup;
    private String excludedCurrentEmployeeName;

    public EmployeeSearchComboPresenter(CommandServiceClientImpl commandService, HandlerManager sharedEventBus)
    {
        super(new EmployeeSearchComboView(), sharedEventBus);
        this.commandService = commandService;
        initPagingLoader();
        addHandlerRegistration(getSharedEventBus().addHandler(WorkGroupComboChangeEvent.getType(),
            new WorkGroupComboChangeEvent.Handler()
            {
                @Override
                public void handle(WorkGroupComboChangeEvent event)
                {
                    workGroupFilter = event.getWorkGroupName();
                    isCurrentWorkGroup = event.isSameWorkGroup();
                    getField().clear();
                }
            }));
    }

    private void initPagingLoader()
    {
        getDisplay().setPagingLoader(new CommandPagingLoader<MetaModelData<Employees>>()
        {
            @Override
            public void load(SearchPaging searchPaging, Option<SearchSorting> searchSortingOption,
                             final AsyncCallback<PagingLoadResult<MetaModelData<Employees>>> callback)
            {
                String employeeFilter = getDisplay().getRawValue() == null ? "%" : getDisplay().getRawValue();
                commandService.run(new EmployeeSearchRequest(searchPaging, searchSortingOption, employeeFilter,
                    workGroupFilter), new GAsyncCallback<DomainEntityPagedListResponse<Employees>>()
                {
                    @Override
                    public void onSuccess(DomainEntityPagedListResponse<Employees> result)
                    {
                        List<MetaModelData<Employees>> mds = new ArrayList<MetaModelData<Employees>>();
                        for (Employees employee : result.getList())
                        {
                            // To exclude current employee in current workGroup
                            if (isCurrentWorkGroup
                                && excludedCurrentEmployeeName != null && !excludedCurrentEmployeeName.isEmpty())
                            {
                                if (employee.getName().equals(excludedCurrentEmployeeName))
                                    continue;
                            }
                            mds.add(createMetaModelData(employee));
                        }
                        getDisplay().setSearchPaging(result.getSearchPaging());
                        callback.onSuccess(SearchResultListDisplay.getPagingLoadResult(mds, result.getSearchPaging()));
                    }
                });
            }
        });
    }

    private MetaModelData<Employees> createMetaModelData(Employees employee)
    {
        MetaModelData<Employees> md = new MetaModelData<Employees>(employee);
        return md;
    }

    @Override
    public Field getField()
    {
        return getComboBox();
    }

    @Override
    protected void makeFieldRequired()
    {
        getComboBox().setAllowBlank(false);
    }

    @Override
    public String getValue()
    {
        return getSelectedEmployee() == null ? null : getSelectedEmployee().getName();
    }

    public SearchComboBox<MetaModelData<Employees>> getComboBox()
    {
        return getDisplay().getComboBox();
    }

    public Employees getSelectedEmployee()
    {
        if (getDisplay().getValue() != null)
        {
            return (Employees)getDisplay().getValue().getMeta();
        }
        return null;
    }

    public void setSelectionChangedListener(SelectionChangedListener<MetaModelData<Employees>>
                                                selectionChangedListener)
    {
        getDisplay().setSelectionListener(selectionChangedListener);
    }

    public void setPagingLoader(CommandPagingLoader<MetaModelData<Employees>> loader)
    {
        getDisplay().setPagingLoader(loader);
    }

    @Override
    public void setValue(String value)
    {
        commandService.run(new EmployeeSearchRequest(value),
            new GAsyncCallback<DomainEntityPagedListResponse<Employees>>()
            {
                @Override
                public void onSuccess(DomainEntityPagedListResponse<Employees> result)
                {
                    if (result.getList().size() > 0)
                    {
                        Employees employee = result.getList().get(0);
                        getComboBox().setValue(createMetaModelData(employee));
                    }
                }
            });
    }

    public void setExcludedCurrentEmployeeName(String currentEmployeeName)
    {
        this.excludedCurrentEmployeeName = currentEmployeeName;
    }

}
