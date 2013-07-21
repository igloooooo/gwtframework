package com.iglooit.core.base.client.view;

import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.widget.combobox.SearchComboBox;
import com.clarity.core.lib.client.MetaModelData;
import com.clarity.core.oss.iface.domain.Employees;
import com.clarity.core.oss.iface.domain.EmployeesMeta;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.google.gwt.user.client.ui.Widget;

public class EmployeeSearchComboView implements Display
{

    private SearchComboBox<MetaModelData<Employees>> employeeCombo;

    public EmployeeSearchComboView()
    {
        employeeCombo = new SearchComboBox<MetaModelData<Employees>>();
        employeeCombo.setWidth(200);
        employeeCombo.setPageSize(25);
        employeeCombo.setDisplayField(EmployeesMeta.NAME_PROPERTYNAME);
        employeeCombo.setUseQueryCache(false);
    }

    public void setPagingLoader(CommandPagingLoader<MetaModelData<Employees>> loader)
    {
        employeeCombo.setPagingLoader(loader);
        employeeCombo.setStore();
    }

    public SearchComboBox<MetaModelData<Employees>> getComboBox()
    {
        return employeeCombo;
    }

    public void clear()
    {
        employeeCombo.clear();
    }

    public String getRawValue()
    {
        return employeeCombo.getRawValue();
    }

    public MetaModelData getValue()
    {
        return employeeCombo.getValue();
    }

    public void setSelectionListener(SelectionChangedListener<MetaModelData<Employees>> selectionChangedListener)
    {
        employeeCombo.addSelectionChangedListener(selectionChangedListener);
    }

    public void setSearchPaging(SearchPaging paging)
    {
        employeeCombo.setSearchPaging(paging);
    }

    @Override
    public Widget asWidget()
    {
        return employeeCombo;
    }

    @Override
    public String getLabel()
    {
        return "Employee Role";
    }
}
