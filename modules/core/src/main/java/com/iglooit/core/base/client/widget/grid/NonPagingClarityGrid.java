package com.iglooit.core.base.client.widget.grid;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.lib.client.AsyncLoadingList;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public abstract class NonPagingClarityGrid<MD extends ModelData> extends ClarityGrid<MD>
{
    private Grid<MD> grid;
    private ListStore<MD> listStore;
    private ContentPanel contentPanel;
    private SingleRowEditor re;

    public NonPagingClarityGrid()
    {
        listStore = new ListStore<MD>();
        grid = new Grid<MD>(listStore, new ColumnModel(getColumnConfig()));
        re = new SingleRowEditor<MD>();
        getGrid().addPlugin(re);

        contentPanel = new ContentPanel();
        contentPanel.setHeaderVisible(false);
        contentPanel.addStyleName(ClarityStyle.GRID_TYPE_NON_PAGING);
        contentPanel.setLayout(new FillLayout());
        contentPanel.add(grid);

        //In order to get the store
        grid.setView(getGridView());
        grid.getView().setAutoFill(true);
        grid.getView().setForceFit(true);
    }

    public GridView getGridView()
    {
        return new OpenStoreGridView();
    }

    public HandlerRegistration setLoader(AsyncLoadingList<MD> loader)
    {
        return loader.addListHandler(new AsyncLoadingList.ListUpdatedHandler<MD>()
        {
            public void handle(List<MD> updatedList)
            {
                addStore(updatedList);
            }
        });
    }

    public MD getCurrentEditingValue()
    {
        return getGrid().getStore().getAt(re.getRowIndex());
    }

    public void setCurrentRowIndex(int index)
    {
        re.setRowIndex(index);
    }

    public void addStore(List<MD> modelList)
    {
        listStore.removeAll();
        listStore.add(modelList);
    }

    public Grid<MD> getGrid()
    {
        return grid;
    }

    public ContentPanel asWidget()
    {
        return contentPanel;
    }

    public ListStore<MD> getListStore()
    {
        return listStore;
    }

    public void setTopComponent(Component component)
    {
        contentPanel.setTopComponent(component);
    }

    public ContentPanel getContentPanel()
    {
        return contentPanel;
    }

    private final class SingleRowEditor<M extends ModelData> extends RowEditor
    {
        public void startEditing(int rowIndex, boolean doFocus)
        {
//                super.startEditing(rowIndex, doFocus);
        }

        public int getRowIndex()
        {
            return rowIndex;
        }

        public void setRowIndex(int index)
        {
            rowIndex = index;
        }
    }

    public void layout()
    {
        contentPanel.layout();
    }

    public void layout(boolean force)
    {
        contentPanel.layout(force);
    }
}
