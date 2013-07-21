package com.iglooit.core.base.client.widget.grid;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public abstract class ClarityRemovableGrid<MD extends ModelData> extends ClarityBasicGrid<MD>
{
    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private boolean showAddRemoveToolbar;

    // "Remove" Button Listener
    private SelectionListener<ButtonEvent> removeButtonListener;
    private ToolBar toolBar;
    private Button addButton;
    private ContentPanel toolBarPlus;

    private Button removeButton;
    public static final String ADDTEXT = BVC.add();
    public static final String REMOVETEXT = BVC.remove();

    public static final String NEW_ROW_KEY = "##NewRow##";

    public ClarityRemovableGrid(boolean checkBox, boolean checkBoxFirstColumn, boolean showAddRemoveToolbar)
    {
        super(checkBox, checkBoxFirstColumn);
        this.showAddRemoveToolbar = showAddRemoveToolbar;
        initToolbar();
        initListeners();
//        addRemoveButtonSelectionListener(removeButtonListener);
    }

    public void setMaskAdding()
    {
        maskGrid(BVC.gridAdding());
    }

    public void setMaskLoading()
    {
        maskGrid(BVC.loading());
    }

    public void setMaskDeleting()
    {
        maskGrid(BVC.gridDeleting());
    }

    public void setMaskUpdating()
    {
        maskGrid(BVC.gridUpdating());
    }

    public void addToolbarComponent(Component component)
    {
        toolBar.add(component);
    }

    private void initToolbar()
    {
        toolBar = new ToolBar();
        addButton = new Button(ADDTEXT);
        removeButton = new Button(REMOVETEXT);
        addButton.addStyleName(ClarityStyle.TOOLBAR_BUTTON);
        removeButton.addStyleName(ClarityStyle.TOOLBAR_BUTTON);

        toolBar.add(addButton);
        toolBar.add(removeButton);
        toolBar.setSpacing(5);

        if (showAddRemoveToolbar)
            setTopComponent(toolBar);
    }

    public void setToolbarVisible(boolean isVisible)
    {
        toolBar.setVisible(isVisible);
    }

    public void setAddButtonText(String s)
    {
        addButton.setText(s);
    }

    public void setRemoveButtonText(String s)
    {
        removeButton.setText(s);
    }

    private void initListeners()
    {
        initDeleteButtonListener();
        //addDefaultRemoveButtonListener();
        addListStoreListener();
    }

    private void addListStoreListener()
    {
        getStore().addStoreListener(new StoreListener<MD>()
        {
            @Override
            public void handleEvent(StoreEvent<MD> e)
            {
                super.handleEvent(e);
                if (getStore().getModels().size() == 0)
                {
                    disableRemoveButton();
                }
                else
                {
                    enableRemoveButton();
                }
            }
        });
    }

    private void enableRemoveButton()
    {
        if (removeButton != null)
        {
            removeButton.enable();
            getContentPanel().layout(true);
        }
    }

    private void disableRemoveButton()
    {
        if (removeButton != null)
        {
            removeButton.disable();
            getContentPanel().layout(true);
        }
    }

    public void removeAddButton()
    {
        toolBar.remove(addButton);
    }

    public void removeAddButtonLisnter()
    {
        addButton.removeAllListeners();
    }

    public void addAddButtonSelectionListener(SelectionListener<ButtonEvent> listener)
    {
        addButton.addSelectionListener(listener);
    }

    public void addRemoveButtonSelectionListener(SelectionListener<ButtonEvent> listener)
    {
        removeButton.addSelectionListener(listener);
    }

    public void addDefaultRemoveButtonListener()
    {
        removeButton.addSelectionListener(removeButtonListener);
    }

    public void removeDefaultRemoveButtonListener()
    {
        if (removeButtonListener != null)
            removeButton.removeSelectionListener(removeButtonListener);
    }

    private void initDeleteButtonListener()
    {
        removeButtonListener = new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                onRemoveRows();
            }
        };
    }

    protected void onRemoveRows()
    {
        deleteSelectedModels();
        getGrid().reconfigure(getGrid().getStore(), getGrid().getColumnModel());
        getGrid().fireEvent(Events.OnMouseOver);
    }

    protected int addModel()
    {
        MD metaModelData = getEmptyMetaModelData();
        metaModelData.set(NEW_ROW_KEY, Boolean.TRUE);
        addNewModel(metaModelData, 0);
        return 0;
    }

    protected void removeModel(int i)
    {
        MD model = getGrid().getStore().getAt(i);
        getGrid().getStore().remove(model);
    }


    public void enableAddButton()
    {
        if (addButton != null)
        {
            addButton.enable();
        }
    }

    public void enableButtons()
    {
        if (addButton != null)
        {
            addButton.enable();
        }
        if (removeButton != null)
        {
            removeButton.enable();
        }
    }

    public void disableButtons()
    {
        if (addButton != null)
        {
            addButton.disable();
        }
        if (removeButton != null)
        {
            removeButton.disable();
        }
    }

    public void hideToolBar()
    {
        toolBar.hide();
    }

    public void showToolBar()
    {
        toolBar.show();
    }

    public abstract MD getEmptyMetaModelData();
}

