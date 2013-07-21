package com.iglooit.core.base.client.widget.grid;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.model.SimpleModelValue;
import com.clarity.core.base.client.widget.SecuredWidget;
import com.clarity.core.base.client.widget.SecuredWidgetDelegate;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public abstract class ClarityFilterSimpleGrid<T> extends ClaritySimpleGrid<T>
{
    private StoreFilterField<SimpleModelValue<T>> searchField;
    private ToolBar toolBar;

    protected ClarityFilterSimpleGrid(boolean checkBox, boolean checkBoxFirstColumn)
    {
        super(checkBox, checkBoxFirstColumn);
        createSearchToolBar();
    }

    private void createSearchToolBar()
    {
        // create search field and search button
        toolBar = new ToolBar();

        toolBar.setAutoWidth(true);
        toolBar.setWidth("100%");
        searchField = createSearchField();
        searchField.setTriggerStyle("x-form-clear-trigger");
        searchField.setName("searchfilter");
        searchField.setAutoWidth(true);
        searchField.bind(getGrid().getStore());
        toolBar.add(searchField);
        setTopComponent(toolBar);
    }

    public void setSearchFieldEmptyText(String message)
    {
        searchField.setEmptyText(message);
    }

    public void setSearchFieldWidth(int width)
    {
        searchField.setWidth(width);
    }

    @Override
    public String getLabel()
    {
        return null;
    }

    /**
     * @return Creates and return the filter to use - subclasses should implement this to filter on
     *         specific fields.
     */
    protected StoreFilterField<SimpleModelValue<T>> createSearchField()
    {
        return new StoreFilterField<SimpleModelValue<T>>()
        {
            @Override
            protected boolean doSelect(Store<SimpleModelValue<T>> store, SimpleModelValue<T> parent,
                                       SimpleModelValue<T> record, String property, String filter)
            {
                final T value = record.getValue();
                return value.toString().toLowerCase().contains(filter.toLowerCase());
            }
        };
    }

    public ToolBar getToolBar()
    {
        return toolBar;
    }

    public abstract static class Secured<T> extends ClarityFilterSimpleGrid<T>
    {
        public Secured(PrivilegeResolver resolver, PrivilegeConst privilege, boolean checkBox, boolean checkBoxFirstCol)
        {
            super(checkBox, checkBoxFirstCol);
            ((SecuredGrid)getGrid()).setDelegate(new SecuredWidgetDelegate(resolver, privilege));
        }

        @Override
        protected Grid<SimpleModelValue<T>> createGrid()
        {
            return new SecuredGrid<T>(getStore(), new ColumnModel(getColumnConfigList()));
        }
    }

    private static class SecuredGrid<T> extends AutoAdjustMaskGrid<SimpleModelValue<T>> implements SecuredWidget
    {
        private SecuredWidgetDelegate delegate;

        public SecuredGrid(ListStore<SimpleModelValue<T>> listStore, ColumnModel cm)
        {
            super(listStore, cm);
        }

        @Override
        public void setEnabled(boolean enabled)
        {
            delegate.setEnabled(enabled);
        }

        @Override
        public boolean isRendered()
        {
            //simple way to get around the problem of the helper not being available during construction
            return delegate == null ? false : delegate.isRendered();
        }

        @Override
        public void superSetEnabled(boolean enabled)
        {
            super.setEnabled(enabled);
        }

        @Override
        public boolean superIsRendered()
        {
            return super.isRendered();
        }

        public void setDelegate(SecuredWidgetDelegate delegate)
        {
            this.delegate = delegate;
            delegate.setSecuredWidget(this);
        }
    }
}
