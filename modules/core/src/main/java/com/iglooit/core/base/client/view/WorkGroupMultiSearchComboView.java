package com.iglooit.core.base.client.view;

import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.widget.combobox.ValidatableMultiSelectSearchComboBox;
import com.clarity.core.base.client.widget.grid.SearchResultListDisplay;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class WorkGroupMultiSearchComboView implements Display
{

    private ValidatableMultiSelectSearchComboBox<WorkGroupModelData> workGroupMultiSearchCombo;
    private AsyncCallback<PagingLoadResult<WorkGroupModelData>> pagingLoadResultCallback;
    private IPresenter presenter;
    private List<String> defaultWorkGroupNames;

    public interface IPresenter
    {
        void onLoadPage(final SearchPaging searchPaging, final Option<SearchSorting> searchSortingOption);
    }

    public WorkGroupMultiSearchComboView()
    {
        workGroupMultiSearchCombo = new ValidatableMultiSelectSearchComboBox<WorkGroupModelData>();
        initComboBox();
    }

    public WorkGroupMultiSearchComboView(PrivilegeResolver resolver, PrivilegeConst privilege)
    {
        workGroupMultiSearchCombo =
            new ValidatableMultiSelectSearchComboBox.Secured<WorkGroupModelData>(resolver, privilege);

        initComboBox();
    }

    private void initComboBox()
    {
        workGroupMultiSearchCombo.getField().setDisplayField(WorkGroupModelData.WORK_GROUP_NAME);
        workGroupMultiSearchCombo.getField().setWidth(250);
        workGroupMultiSearchCombo.getField().setPageSize(10);

        // pageLoader
        workGroupMultiSearchCombo.getField().setStore();
        workGroupMultiSearchCombo.setPagingLoader(new CommandPagingLoader<WorkGroupModelData>()
        {
            public void load(final SearchPaging searchPaging, final Option<SearchSorting> searchSortingOption,
                             final AsyncCallback<PagingLoadResult<WorkGroupModelData>> callback)
            {
                pagingLoadResultCallback = callback;
                presenter.onLoadPage(searchPaging, searchSortingOption);
            }
        });

        // Listeners
        workGroupMultiSearchCombo.addValueChangeHandler(new ValueChangeHandler<List<WorkGroupModelData>>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<List<WorkGroupModelData>> event)
            {
                workGroupMultiSearchCombo.getField().fireEvent(Events.Change);
            }
        });
    }

    public void setPagingLoadResult(List<String> workGroupNames, SearchPaging searchPaging)
    {
        List<WorkGroupModelData> modelDataList = new ArrayList<WorkGroupModelData>();
        for (String workGroupName : workGroupNames)
        {
            WorkGroupModelData modelData = new WorkGroupModelData(workGroupName);
            modelDataList.add(modelData);

        }
        workGroupMultiSearchCombo.getField().setSearchPaging(searchPaging);
        pagingLoadResultCallback.onSuccess(SearchResultListDisplay.getPagingLoadResult(modelDataList, searchPaging));

        setSelectedWorkGroupNames(defaultWorkGroupNames);

    }

    public void setPresenter(IPresenter presenter)
    {
        this.presenter = presenter;
    }

    @Override
    public String getLabel()
    {
        return "";
    }

    @Override
    public Widget asWidget()
    {
        return workGroupMultiSearchCombo.getField();
    }

    public String getComboRawValue()
    {
        return workGroupMultiSearchCombo.getField().getRawValue();
    }

    public void setDefaultWorkGroupNames(List<String> workGroupNames)
    {
        defaultWorkGroupNames = workGroupNames;

        if (workGroupMultiSearchCombo.getField().getStore() == null
            || workGroupMultiSearchCombo.getField().getStore().getModels().size() == 0)
        {
            workGroupMultiSearchCombo.getField().manualLoad();
        }
        else
        {
            setSelectedWorkGroupNames(defaultWorkGroupNames);
        }
    }

    private void setSelectedWorkGroupNames(List<String> workGroupNames)
    {
        if (workGroupNames != null && workGroupNames.size() > 0)
        {
            List<WorkGroupModelData> modelDataList = new ArrayList<WorkGroupModelData>();
            for (String workGroupName : workGroupNames)
            {
                for (WorkGroupModelData storeData : workGroupMultiSearchCombo.getField().getStore().getModels())
                {
                    if (storeData.getValue().equals(workGroupName))
                    {
                        WorkGroupModelData modelData = new WorkGroupModelData(workGroupName);
                        modelDataList.add(modelData);
                    }
                }
            }
            if (modelDataList.size() > 0)
            {
                workGroupMultiSearchCombo.setValue(modelDataList);
            }
        }
    }

    public static class WorkGroupModelData extends SimpleComboValue<String>
    {
        public static final String WORK_GROUP_NAME = "workGroupName";

        public WorkGroupModelData()
        {
        }

        public WorkGroupModelData(String workGroupName)
        {
            setValue(workGroupName);
        }

        @Override
        public String getValue()
        {
            return get(WORK_GROUP_NAME);
        }

        @Override
        public void setValue(String workGroupName)
        {
            set(WORK_GROUP_NAME, workGroupName);
        }

    }
}
