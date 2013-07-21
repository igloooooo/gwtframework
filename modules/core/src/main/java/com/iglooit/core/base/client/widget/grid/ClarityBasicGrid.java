package com.iglooit.core.base.client.widget.grid;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.lib.client.AsyncLoadingList;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ClarityBasicGrid<MD extends ModelData> extends ClarityGrid<MD>
{
    protected static final BaseViewConstants BVC = GWT.create(BaseViewConstants.class);

    private Grid<MD> grid;
    private ListStore<MD> store;
    private List<ColumnConfig> columnConfigList;
    private ContentPanel contentPanel;
    private OpenStoreGridView gridView;
    private CheckBoxSelectionModel<MD> sm;
    private String emptyStoreMessage = BVC.noResultFoundText();

    private boolean checkBox;
    private boolean checkBoxFirstColumn;
    private ICallback callback;

    public static final int COLUMN_WIDTH = 200;

    public interface ICallback
    {
        // it's now just for notifying the container that one of the rows has been clicked
        // feel free to pass params around
        void onRowClick();
    }

    public void setCallback(ICallback callback)
    {
        this.callback = callback;
    }

    public ICallback getCallback()
    {
        return callback;
    }

    public ClarityBasicGrid(boolean checkBox, boolean checkBoxFirstColumn)
    {
        this.checkBox = checkBox;
        this.checkBoxFirstColumn = checkBoxFirstColumn;
        this.store = new ListStore<MD>();
        this.columnConfigList = new ArrayList<ColumnConfig>();
        this.gridView = new OpenStoreGridView();
        gridView.setForceFit(true);
        gridView.setAutoFill(true);
        reconfigGrid(checkBox, checkBoxFirstColumn, new ArrayList<MD>(), getColumnConfig());
        buildDefaultPanel();
    }

    public ListStore<MD> getStore()
    {
        return store;
    }

    public void setStore(ListStore<MD> store)
    {
        this.store = store;
    }

    private void buildDefaultPanel()
    {
        contentPanel = new ContentPanel()
        {
            @Override
            public boolean layout(boolean force)
            {
                boolean layout = super.layout(force);
                if (grid instanceof AutoAdjustMaskGrid)
                {
                    ((AutoAdjustMaskGrid)grid).adjustMask();
                }
                return layout;
            }
        };
        contentPanel.setHeaderVisible(false);
        contentPanel.addStyleName(ClarityStyle.GRID_TYPE_BASIC);
        contentPanel.setLayout(new FitLayout());
        contentPanel.add(grid);
        //contentPanel.setScrollMode(Style.Scroll.AUTOY);
//        contentPanel.add(grid);
    }

    public void setBottomComponent(Component bottomComponent)
    {
        contentPanel.setBottomComponent(bottomComponent);
    }

    public HandlerRegistration setLoader(AsyncLoadingList<MD> loader)
    {
        return loader.addListHandler(new AsyncLoadingList.ListUpdatedHandler<MD>()
        {
            public void handle(List<MD> updatedList)
            {
                updateStore(updatedList);
            }
        });
    }

    public void reconfigGrid(boolean checkBox, boolean checkBoxFirstColumn,
                             List<MD> itemList, List<ColumnConfig> configs)
    {
        this.checkBox = checkBox;
        this.checkBoxFirstColumn = checkBoxFirstColumn;
        updateStore(itemList);
        updateConfigs(configs);
        reconfigGrid();
    }

    public void reconfigGrid()
    {
        if (grid == null)
            initGrid();
        else
            grid.reconfigure(store, new ColumnModel(columnConfigList));
        updateCheckBoxSelection();
    }

    private void initGrid()
    {
        grid = createGrid();
        grid.setView(gridView);
        grid.setLoadMask(false);
    }

    //needed so that this component can be secured
    protected Grid<MD> createGrid()
    {
        return new AutoAdjustMaskGrid<MD>(getStore(), new ColumnModel(getColumnConfigList()));
    }


    private void updateCheckBoxSelection()
    {
        if (checkBox)
        {
            if (sm == null)
                throw new AppX("sm should have existed");
            else
            {
                if (!grid.getPlugins().contains(sm))
                {
                    grid.setSelectionModel(sm);
                    grid.addPlugin(sm);
                }
            }
        }
    }

    public void maskGrid(String message)
    {
        if (grid != null)
        {
            if (!isGridMasked())
            {
                grid.unmask();
            }
            grid.mask(message);
        }

//        if (!isGridMasked())
//        {
//            grid.mask(message);
//        }
    }

    public void maskGrid()
    {
        if (grid != null)
        {
            if (!isGridMasked())
            {
                grid.unmask();
            }
            grid.mask("", ClarityStyle.MASK_FIELD);
        }
    }

    public void maskLoading()
    {
        maskGrid(BVC.loading());
    }

    public void maskGrid(String s, String maskField)
    {
        if (grid != null)
        {
            if (!isGridMasked())
            {
                grid.unmask();
            }
//            grid.setLoadMask(false);
            grid.mask(s, maskField);
            getContentPanel().layout(true);
        }
    }

    public boolean isGridMasked()
    {
        if (grid == null)
            return false;
        else
            return grid.isMasked();
    }

    public void unMaskGrid()
    {
        if (grid != null)
        {
            grid.unmask();
//            grid.setLoadMask(true);
            getContentPanel().layout(true);
        }
    }


    private void updateConfigs(List<ColumnConfig> configs)
    {
        columnConfigList = new ArrayList<ColumnConfig>();
        if (checkBox && checkBoxFirstColumn)
            columnConfigList.add(createSMColumn());
        for (ColumnConfig config : configs)
        {
            columnConfigList.add(config);
        }
        if (checkBox && !checkBoxFirstColumn)
            columnConfigList.add(createSMColumn());
    }

    public void removeModelFromStore(MD model)
    {
        store.remove(model);
        updateMask();
    }

    public void addModelToStore(MD model)
    {
        store.add(model);
        updateMask();
    }

    public void updateStore(List<MD> itemList)
    {
        store.removeAll();
        if (itemList != null)
        {
            store.add(itemList);
            updateMask();
        }
    }

    public void clearStore()
    {
        store.removeAll();
    }

    private void updateMask()
    {
        if (store.getCount() == 0)
            remaskGridWithEmptyMessage(emptyStoreMessage);
        else
        {
            if (grid != null)
                grid.unmask();
        }
    }

    private boolean storeIsEmpty()
    {
        return store.getCount() == 0;
    }

    public void remaskGridWithEmptyMessage(String emptyStoreMessage)
    {
        if (grid == null)
            return;
        if (StringUtil.isBlank(emptyStoreMessage))
            return;
        if (getStoreList() == null || getStoreList().size() == 0)
        {
            if (grid.isMasked())
                grid.unmask();
            grid.mask(emptyStoreMessage, ClarityStyle.MASK_NO_SPINNER);
        }
    }

    public void updateGridStoreWithEmptyMessage(List<MD> modelList, String emptyStoreMessage)
    {
        updateStore(modelList);
        remaskGridWithEmptyMessage(emptyStoreMessage);
    }

    public List<MD> getStoreList()
    {
        List<MD> list = new ArrayList<MD>();
        for (int i = 0; i < store.getCount(); i++)
        {
            list.add(store.getAt(i));
        }
        return list;
    }

    private ColumnConfig createSMColumn()
    {
        if (sm != null)
            return sm.getColumn();

        sm = createCheckBoxSelectionModel();
        sm.setSelectionMode(Style.SelectionMode.MULTI);
        sm.setFiresEvents(false);
        return sm.getColumn();
    }

    protected CheckBoxSelectionModel createCheckBoxSelectionModel()
    {
        return new CheckBoxSelectionModel<MD>()
        {
            protected void handleMouseDown(GridEvent<MD> e)
            {
                if (!(selectionMode == Style.SelectionMode.MULTI))
                    super.handleMouseDown(e);
            }

            protected void handleMouseClick(GridEvent<MD> e)
            {
                if (selectionMode == Style.SelectionMode.MULTI)
                {
                    GridView view = grid.getView();
                    MD sel = listStore.getAt(e.getRowIndex());
                    if (isSelected(sel))
                    {
                        doDeselect(Arrays.asList(sel), false);
                    }
                    else
                    {
                        doSelect(Arrays.asList(sel), true, false);
                        view.focusCell(e.getRowIndex(), e.getColIndex(), true);
                    }
                }
                else
                    super.handleMouseClick(e);
                getCallback().onRowClick();
            }
        };
    }

    public CheckBoxSelectionModel<MD> getSm()
    {
        return sm;
    }

    public HandlerRegistration addGridSelectionChangedListener(final SelectionChangedListener<MD> listener)
    {
        if (sm != null)
            sm.setFiresEvents(true);
        grid.getSelectionModel().addSelectionChangedListener(listener);
        return new HandlerRegistration()
        {
            public void removeHandler()
            {
                grid.getSelectionModel().removeSelectionListener(listener);
            }
        };
    }

    @Override
    public Grid<MD> getGrid()
    {
        return grid;
    }

    public void setTopComponent(Component component)
    {
        contentPanel.setTopComponent(component);
    }

    public void deleteSelectedModels()
    {
        List<MD> selectedList = grid.getSelectionModel().getSelectedItems();
        for (MD md : selectedList)
        {
            store.remove(md);
        }
    }

    public List<MD> getSelectedItems()
    {
        return grid.getSelectionModel().getSelectedItems();
    }


    public void addNewModel(MD md, int i)
    {
        store.insert(md, i);
        if (isGridMasked()) unMaskGrid();
    }

    public ContentPanel getContentPanel()
    {
        return contentPanel;
    }

    @Override
    public Widget asWidget()
    {
        return contentPanel;
    }

    public OpenStoreGridView getGridView()
    {
        return gridView;
    }


    public void suppressSelectionListeners()
    {
        grid.getSelectionModel().setFiresEvents(false);
    }

    public void unSuppresssSelectionListeners()
    {
        grid.getSelectionModel().setFiresEvents(true);
    }

    public void deselectAll()
    {
        grid.getSelectionModel().deselectAll();
    }

    public void selectFirstItem()
    {
        if (store.getCount() > 0)
            grid.getSelectionModel().select(store.getAt(0), true);
    }

    public void updateModel(MD model)
    {
        store.update(model);
    }

    protected List<ColumnConfig> getColumnConfigList()
    {
        return columnConfigList;
    }


    public void selectModel(MD oldModel)
    {
        grid.getSelectionModel().select(oldModel, true);
    }

    public MD findModel(String key, Object value)
    {
        return grid.getStore().findModel(key, value);
    }

    public void selectValueIfPropertyTheSame(String propertyName, String property)
    {
        if (propertyName == null || property == null)
            throw new AppX("property Name or value should not be null");
        for (int index = 0; index < store.getCount(); index++)
        {
            if (property.equals(store.getAt(index).get(propertyName)))
            {
                grid.getSelectionModel().select(store.getAt(index), true);
                grid.getView().focusRow(index);
                return;
            }
        }
    }

    public void addListener(EventType viewReady, Listener<? extends BaseEvent> listener)
    {
        grid.addListener(viewReady, listener);
    }

    public static class AutoAdjustMaskGrid<M extends ModelData> extends Grid<M>
    {
        public AutoAdjustMaskGrid(ListStore<M> mListStore, ColumnModel cm)
        {
            super(mListStore, cm);
        }

        public AutoAdjustMaskGrid()
        {
        }

        public void adjustMask()
        {
            if (rendered && mask)
                mask(maskMessage, maskMessageStyleName);

        }
    }

    public void setEmptyStoreMessage(String emptyStoreMessage)
    {
        this.emptyStoreMessage = emptyStoreMessage;
    }

    public boolean isCheckBox()
    {
        return checkBox;
    }
}
