package com.iglooit.core.base.client.widget.grid;

import com.clarity.commons.iface.type.NonSerOpt;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.menu.ClarityMenu;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.menu.CheckMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.Event;

import java.util.List;

public abstract class ClarityGrid<MD extends ModelData> implements Display
{
    public static final String CLARITY_ROW_ENABLE_STATE = "clarity_row_enable_state";

    public abstract Grid<MD> getGrid();

    public abstract List<ColumnConfig> getColumnConfig();

    public NonSerOpt<MD> getSelectedModelData()
    {
        MD md = (MD)getGrid().getSelectionModel().getSelectedItem();
        return NonSerOpt.option(md);
    }

    public void setStore(ListStore<MD> store)
    {
    }

    public ListStore<MD> getStore()
    {
        return getGrid().getStore();
    }

    public final class OpenStoreGridView extends GridView
    {
        // Purpose:
        //    In order to record current selected row and set it as unselected when searching begin
        // Function:
        //
        private com.google.gwt.dom.client.Element selectedRow;

        public void refreshCurrentRow(int i)
        {
            super.refreshRow(i);
        }

        @Override
        protected void onRowSelect(int rowIndex)
        {
            super.onRowSelect(rowIndex);
            selectedRow = getRow(rowIndex);
        }

        public void selectRow(int i)
        {
            addRowStyle(getRow(i), "x-grid3-row-selected");
        }


        public void deselectRow()
        {
            removeRowStyle(selectedRow, "x-grid3-row-selected");
        }

        public void deselectAllRows()
        {
            for (int i = 0; i < getRows().getLength(); i++)
            {
                removeRowStyle(getRow(i), "x-grid3-row-selected");
            }
        }

        @Override
        protected void onDataChanged(StoreEvent<ModelData> se)
        {
            super.onDataChanged(se);
            if (grid.isLoadMask())
                grid.el().unmask();
        }

        public void setRowInvalid(int num)
        {
            getRow(num).addClassName(ClarityStyle.GRID_ERROR);
        }

        public void removeRowInvalid(int num)
        {
            getRow(num).removeClassName(ClarityStyle.GRID_ERROR);
        }

        public void updateRowStyles()
        {
            for (int i = 0; i < ds.getCount(); i++)
            {
                Boolean isEnable = ds.getAt(i).get(CLARITY_ROW_ENABLE_STATE);
                if (isEnable != null && !isEnable)
                {
                    addRowStyle(getRow(i), "x-item-disabled");
                }
                else
                {
                    removeRowStyle(getRow(i), "x-item-disabled");
                }
            }
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        protected void handleComponentEvent(GridEvent ge)
        {
            if ((ge.getEventTypeInt() == Event.ONMOUSEMOVE || ge.getEventTypeInt() == Event.ONMOUSEOVER)
                    && ds.getAt(ge.getRowIndex()) != null)
            {
                Boolean isEnable = ds.getAt(ge.getRowIndex()).get(CLARITY_ROW_ENABLE_STATE);
                if (Boolean.FALSE.equals(isEnable))
                    return;
            }
            super.handleComponentEvent(ge);
        }

        //Remove me if upgrade to gxt 2.2.5 or above
        protected Menu createContextMenu(final int colIndex)
        {
            final ClarityMenu menu = new ClarityMenu();

            if (cm.isSortable(colIndex))
            {
                MenuItem item = new MenuItem();
                item.setText(GXT.MESSAGES.gridView_sortAscText());
                item.setIcon(getImages().getSortAsc());
                item.addSelectionListener(new SelectionListener<MenuEvent>()
                {
                    public void componentSelected(MenuEvent ce)
                    {
                        doSort(colIndex, Style.SortDir.ASC);
                    }

                });
                menu.add(item);

                item = new MenuItem();
                item.setText(GXT.MESSAGES.gridView_sortDescText());
                item.setIcon(getImages().getSortDesc());
                item.addSelectionListener(new SelectionListener<MenuEvent>()
                {
                    public void componentSelected(MenuEvent ce)
                    {
                        doSort(colIndex, Style.SortDir.DESC);
                    }
                });
                menu.add(item);
            }

            MenuItem columns = new MenuItem();
            columns.setText(GXT.MESSAGES.gridView_columnsText());
            columns.setIcon(getImages().getColumns());
            columns.setData("gxt-columns", "true");

            final Menu columnMenu = new Menu();

            int cols = cm.getColumnCount();
            for (int i = 0; i < cols; i++)
            {
                if (shouldNotCount(i, false))
                {
                    continue;
                }
                final int fcol = i;
                final CheckMenuItem check = new CheckMenuItem();
                check.setHideOnClick(false);
                check.setText(cm.getColumnHeader(i));
                check.setChecked(!cm.isHidden(i));
                check.addSelectionListener(new SelectionListener<MenuEvent>()
                {
                    public void componentSelected(MenuEvent ce)
                    {
                        cm.setHidden(fcol, !cm.isHidden(fcol));
                        restrictMenu(columnMenu);
                    }
                });
                columnMenu.add(check);
            }

            restrictMenu(columnMenu);

            columns.setSubMenu(columnMenu);
            menu.add(columns);
            return menu;
        }

        private boolean shouldNotCount(int columnIndex, boolean includeHidden)
        {
            return cm.getColumnHeader(columnIndex) == null || cm.getColumnHeader(columnIndex).equals("")
                    || (includeHidden && cm.isHidden(columnIndex)) || cm.isFixed(columnIndex);
        }


        private void restrictMenu(Menu columns)
        {
            int count = 0;
            for (int i = 0, len = cm.getColumnCount(); i < len; i++)
            {
                if (!shouldNotCount(i, true))
                {
                    count++;
                }
            }

            if (count == 1)
            {
                for (Component item : columns.getItems())
                {
                    CheckMenuItem ci = (CheckMenuItem)item;
                    if (ci.isChecked())
                    {
                        ci.disable();
                    }
                }
            }
            else
            {
                for (Component item : columns.getItems())
                {
                    item.enable();
                }
            }
        }
    }
}
