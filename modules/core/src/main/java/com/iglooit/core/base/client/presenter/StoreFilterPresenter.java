package com.iglooit.core.base.client.presenter;

import com.clarity.core.base.client.AbstractCommands;
import com.clarity.core.base.client.event.MasterViewItemNullSelectionEvent;
import com.clarity.core.base.client.event.MasterViewItemSelectionEvent;
import com.clarity.core.base.client.event.NewMasterViewItemEvent;
import com.clarity.core.base.client.mvp.DefaultPresenter;
import com.clarity.core.base.client.view.StoreFilterView;
import com.clarity.core.lib.client.MetaModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.event.shared.HandlerManager;

import java.util.ArrayList;
import java.util.List;

public class StoreFilterPresenter extends
    DefaultPresenter<StoreFilterView>
{
    private AbstractCommands commands;
    private HandlerManager eventBus;

    public StoreFilterPresenter(StoreFilterView display)
    {
        super(display);
    }

    public StoreFilterPresenter(StoreFilterView display, HandlerManager eventBus, AbstractCommands commands)
    {
        super(display);
        this.eventBus = eventBus;
        this.commands = commands;
    }

    public void init()
    {
        getDisplay().addNewItemButtonClickListener(newItemButtonClickListener());
        setMasterViewItemsSelectionListener(masterViewItemsSelectionListener());
    }

    protected SelectionListener<ButtonEvent> newItemButtonClickListener()
    {
        return new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent ce)
            {
                disableAddButton();
                eventBus.fireEvent(new NewMasterViewItemEvent());
            }
        };
    }

    public void disableAddButton()
    {
        getDisplay().disableAddButton();
    }

    public void setMasterViewItemsSelectionListener(SelectionChangedListener listener)
    {
        getDisplay().setMasterItemsSelectionListener(listener);
    }

    private SelectionChangedListener masterViewItemsSelectionListener()
    {
        return new SelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent se)
            {
                MetaModelData mmd = ((MetaModelData)se
                    .getSelectedItem());
                if (mmd == null)
                {
                    eventBus.fireEvent(new MasterViewItemNullSelectionEvent());
                    getDisplay().cancelEdit();
                    return;
                }
                eventBus.fireEvent(new MasterViewItemSelectionEvent(mmd));
            }

        };
    }

    public void cancelEdit()
    {
        getDisplay().cancelEdit();
        getDisplay().enableAddButton();
    }

    public void removeSelectedItem(ModelData unsavedModelData)
    {
        getDisplay().removeSelectedItem(unsavedModelData);
    }

    public void refresh()
    {
        getDisplay().getMasterItems().refresh();
        getDisplay().layout(true);
    }

    public void enableAddButton()
    {
        getDisplay().enableAddButton();
    }

    public void addRefreshButtonClickListener(SelectionListener<ButtonEvent> listener)
    {
        getDisplay().addRefreshButtonClickListener(listener);
    }

    public void deselectItem()
    {
        getDisplay().deselectItem();
    }

    public void selectItem(MetaModelData metaModelData)
    {
        List<MetaModelData> metaModelDataList = new ArrayList<MetaModelData>();
        metaModelDataList.add(metaModelData);
        getDisplay().setSelectionAndSuppressEvent(metaModelDataList);
    }

    public void maskItemView()
    {
        getDisplay().maskItemView();
    }

    public void unmaskItemView()
    {
        getDisplay().unmaskItemView();
    }

    public void setAddButtonEnabled(boolean enabled)
    {
        if (enabled)
            enableAddButton();
        else
            disableAddButton();
    }
}
