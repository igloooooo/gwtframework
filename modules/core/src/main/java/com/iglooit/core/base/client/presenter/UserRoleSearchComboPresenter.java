package com.iglooit.core.base.client.presenter;

import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.account.iface.command.request.SearchUseRoleRequest;
import com.clarity.core.account.iface.command.response.SearchUserRoleResponse;
import com.clarity.core.account.iface.domain.Individual;
import com.clarity.core.account.iface.domain.IndividualMeta;
import com.clarity.core.account.iface.domain.UserRole;
import com.clarity.core.account.iface.domain.UserRoleMeta;
import com.clarity.core.base.client.controller.GAsyncCallback;
import com.clarity.core.base.client.event.WorkGroupComboChangeEvent;
import com.clarity.core.base.client.view.CommandPagingLoader;
import com.clarity.core.base.client.view.UserRoleSearchComboView;
import com.clarity.core.base.client.widget.combobox.SearchComboBox;
import com.clarity.core.base.client.widget.grid.SearchResultListDisplay;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.clarity.core.lib.client.MetaModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

public class UserRoleSearchComboPresenter extends CustomComponentPresenter<UserRoleSearchComboView>
{
    private CommandServiceClientImpl commandService;
    private String workGroupFilter;

    public UserRoleSearchComboPresenter(CommandServiceClientImpl commandService,
                                        HandlerManager sharedEventBus)
    {
        super(new UserRoleSearchComboView(), sharedEventBus);
        this.commandService = commandService;
        initPagingLoader();
        addHandlerRegistration(getSharedEventBus().addHandler(WorkGroupComboChangeEvent.getType(),
            new WorkGroupComboChangeEvent.Handler()
            {
                @Override
                public void handle(WorkGroupComboChangeEvent event)
                {
                    workGroupFilter = event.getWorkGroupName();
                    getField().clear();
                }
            }));
    }

    private void initPagingLoader()
    {
        getDisplay().setPagingLoader(new CommandPagingLoader<MetaModelData<UserRole>>()
        {
            @Override
            public void load(SearchPaging searchPaging, Option<SearchSorting> searchSortingOption,
                             final AsyncCallback<PagingLoadResult<MetaModelData<UserRole>>> callback)
            {
                String employeeFilter = getDisplay().getRawValue() == null ? "%" : getDisplay().getRawValue();
                commandService.run(new SearchUseRoleRequest(searchPaging, searchSortingOption,
                    employeeFilter, workGroupFilter), new GAsyncCallback<SearchUserRoleResponse>()
                {
                    public void onSuccess(SearchUserRoleResponse response)
                    {
                        List<MetaModelData<UserRole>> mds = new ArrayList<MetaModelData<UserRole>>();
                        for (UserRole userRole : response.getList())
                        {
                            mds.add(createMetaModelData(userRole));
                        }
                        getDisplay().setSearchPaging(response.getSearchPaging());
                        callback.onSuccess(SearchResultListDisplay.getPagingLoadResult(
                            mds, response.getSearchPaging()));
                    }
                });
            }
        });
    }

    private MetaModelData<UserRole> createMetaModelData(UserRole userRole)
    {
        MetaModelData<UserRole> md = new MetaModelData<UserRole>(userRole);
        md.set(IndividualMeta.NAME_PROPERTYNAME, ((Individual)md.get(UserRoleMeta.PARTY_PROPERTYNAME)).getName());
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
        return getSelectedUserRole() == null ? null : getSelectedUserRole().getUsername();
    }

    public SearchComboBox<MetaModelData<UserRole>> getComboBox()
    {
        return getDisplay().getComboBox();
    }

    public UserRole getSelectedUserRole()
    {
        if (getDisplay().getValue() != null)
        {
            return (UserRole)getDisplay().getValue().getMeta();
        }
        return null;
    }

    public void setSelectionChangedListener(SelectionChangedListener<MetaModelData<UserRole>> selectionChangedListener)
    {
        getDisplay().setSelectionListener(selectionChangedListener);
    }

    public void setPagingLoader(CommandPagingLoader<MetaModelData<UserRole>> loader)
    {
        getDisplay().setPagingLoader(loader);
    }

    @Override
    public void setValue(String value)
    {
        commandService.run(new SearchUseRoleRequest(value), new GAsyncCallback<SearchUserRoleResponse>()
        {
            public void onSuccess(SearchUserRoleResponse response)
            {
                if (response.getList().size() > 0)
                {
                    UserRole userRole = response.getList().get(0);
                    getComboBox().setValue(createMetaModelData(userRole));
                }
            }
        });

    }

    @Override
    public void bind()
    {

    }

}
