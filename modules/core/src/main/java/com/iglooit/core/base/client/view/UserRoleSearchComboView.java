package com.iglooit.core.base.client.view;

import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.core.account.iface.domain.IndividualMeta;
import com.clarity.core.account.iface.domain.UserRole;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.widget.combobox.SearchComboBox;
import com.clarity.core.lib.client.MetaModelData;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.google.gwt.user.client.ui.Widget;

public class UserRoleSearchComboView implements Display
{

    private SearchComboBox<MetaModelData<UserRole>> userRoleCombo;

    public UserRoleSearchComboView()
    {
        userRoleCombo = new SearchComboBox<MetaModelData<UserRole>>();
        userRoleCombo.setWidth(200);
        userRoleCombo.setPageSize(25);
        userRoleCombo.setDisplayField(IndividualMeta.NAME_PROPERTYNAME);
        userRoleCombo.setUseQueryCache(false);
    }

    public void setPagingLoader(CommandPagingLoader<MetaModelData<UserRole>> loader)
    {
        userRoleCombo.setPagingLoader(loader);
        userRoleCombo.setStore();
    }

    public SearchComboBox<MetaModelData<UserRole>> getComboBox()
    {
        return userRoleCombo;
    }


    public void clear()
    {
        userRoleCombo.clear();
    }

    public String getRawValue()
    {
        return userRoleCombo.getRawValue();
    }

    public MetaModelData getValue()
    {
        return userRoleCombo.getValue();
    }

    public void setSelectionListener(SelectionChangedListener<MetaModelData<UserRole>> selectionChangedListener)
    {
        userRoleCombo.addSelectionChangedListener(selectionChangedListener);
    }

    public void setSearchPaging(SearchPaging paging)
    {
        userRoleCombo.setSearchPaging(paging);
    }

    @Override
    public Widget asWidget()
    {
        return userRoleCombo;
    }

    @Override
    public String getLabel()
    {
        return "User Role";
    }
}
