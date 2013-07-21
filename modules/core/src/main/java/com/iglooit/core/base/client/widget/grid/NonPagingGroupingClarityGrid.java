package com.iglooit.core.base.client.widget.grid;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.lib.client.AsyncLoadingList;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public abstract class NonPagingGroupingClarityGrid<MD extends ModelData> extends ClarityGrid<MD>
{
    private Grid<MD> grid;
    private GroupingView groupingView;
    private GroupingStore<MD> groupStore;
    private ContentPanel contentPanel;
    private ColumnModel cm;
    private GroupingRowEditor re;

    public NonPagingGroupingClarityGrid()
    {
        initGrid();
    }


    protected void initGrid()
    {
        initGroupStore();
        //column config = grid
        cm = new ColumnModel(getColumnConfig());
        grid = new Grid<MD>(groupStore, cm);
        re = new GroupingRowEditor<MD>();
        getGrid().addPlugin(re);

        //grid view
        groupingView = initGroupingView();
        grid.setView(groupingView);
//move to initGroupingView so subclass can override if want
//        grid.getView().setAutoFill(true);
//        grid.getView().setForceFit(true);

        contentPanel = new ContentPanel();
        contentPanel.setHeaderVisible(false);
        contentPanel.addStyleName(ClarityStyle.GRID_TYPE_NON_PAGING);
        contentPanel.setLayout(new FillLayout());
        contentPanel.add(grid);
    }

    protected GroupingView initGroupingView()
    {
        GroupingView groupingView = new GroupingView();

        groupingView.setShowGroupedColumn(false);
        groupingView.setForceFit(true);
        groupingView.setAutoFill(true);
        groupingView.setGroupRenderer(getGridGroupRenderer());
        return groupingView;
    }

    protected void initGroupStore()
    {
        //store
        groupStore = new GroupingStore<MD>();
        groupStore.groupBy(getGroupByField());
    }

    public GridGroupRenderer getGridGroupRenderer()
    {
        return new GridGroupRenderer()
        {
            public String render(GroupColumnData data)
            {
                String f = getCm().getColumnById(data.field).getHeader();
                String l = data.models.size() == 1 ? "Item" : "Items";
                return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
            }
        };
    }

    public ColumnModel getCm()
    {
        return cm;
    }

    public void collapseAllGroups()
    {
        groupingView.collapseAllGroups();
    }

    public void expandAllGroups()
    {
        groupingView.expandAllGroups();
    }

    public GroupingView getGroupingView()
    {
        return groupingView;
    }

    public MD getCurrentEditingValue()
    {
        return getGrid().getStore().getAt(re.getRowIndex());
    }

    public void setCurrentRowIndex(int index)
    {
        re.setRowIndex(index);
    }

    /**
     *    return null to disable grouping at the beginning
     *    return "" will result in grouping by space
     */
    protected abstract String getGroupByField();

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

    public void addStore(List<MD> modelList)
    {
        groupStore.removeAll();
        groupStore.add(modelList);
    }

    public Grid<MD> getGrid()
    {
        return grid;
    }

    public ContentPanel asWidget()
    {
        return contentPanel;
    }

    public GroupingStore<MD> getGroupStore()
    {
        return groupStore;
    }

    public void setGroupStore(GroupingStore<MD> groupStore)
    {
        this.groupStore = groupStore;
    }

    public void setTopComponent(Component component)
    {
        contentPanel.setTopComponent(component);
    }

    public ContentPanel getContentPanel()
    {
        return contentPanel;
    }

    public void refreshGrid()
    {
        getGrid().getView().refresh(false);
    }

    private final class GroupingRowEditor<M extends ModelData> extends RowEditor
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
}
