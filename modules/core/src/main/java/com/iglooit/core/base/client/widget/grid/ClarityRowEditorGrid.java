package com.iglooit.core.base.client.widget.grid;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.NonSerOpt;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.composition.display.FormMode;
import com.clarity.core.base.client.composition.display.FormWidgetContainer;
import com.clarity.core.base.client.composition.presenter.BinderContainer;
import com.clarity.core.base.client.composition.presenter.FormBinderContainer;
import com.clarity.core.base.client.mvp.Binder;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.MutableBinder;
import com.clarity.core.base.client.mvp.NullClarityField;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.combobox.ValidatableComboBox;
import com.clarity.core.base.iface.domain.DomainEntity;
import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.core.lib.client.MetaModelData;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.RowEditorEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.LayoutData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTip;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ClarityRowEditorGrid<MD extends MetaModelData> extends ClarityRemovableGrid<MD>
{
    /**
     * If delegateActionsToContainer is true, my container must provide an implementation of EventsCallback, and
     * when I call onAddItems, onChangeItems, or onRemoveItems, the container is responsible for masking me,
     * validating and performing the relevant transaction, refreshing the grid if successful, and unmasking me.
     * <p/>
     * Otherwise, I will not call the EventsCallback methods, I will do what validation I can when an item is added,
     * changed or removed. The container will be informed of the changes. However, the container can get the mode of my
     * store whenever it chooses.
     */
    public interface EventsCallback<MD>
    {
        void onAddItems(List<MD> items);

        void onCancelAddItems(List<MD> items);

        void onChangeItems(List<MD> items);

        void onCancelChangeItems(List<MD> items);

        void onRemoveItems(List<MD> items);

        void onBeforeAdd();

        void onBeforeChange(MD item);
    }

    public interface StateChangeCallback
    {
        void onEmpty();
        void onEdit();
    }

    private static final BaseViewConstants BASEVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private EventsCallback<MD> eventsCallback;
    private StateChangeCallback stateChangeCallback;

    private Map<ColumnConfig, ClarityField> widgetMap;
    private SelectionListener<ButtonEvent> saveButtonListener;
    private SelectionListener<ButtonEvent> cancelButtonListener;
    private ClarityRowEditor rowEditor;

    private boolean allowDuplicate;
    private boolean delegateActionsToContainer;
    private NewRowAwareStoreSorter storeSorter;
    private List<String> insertModeReadOnlyFields;

    //Action Column Configuration
    public static final String ACTION_ID = "ACTION";
    public static final String IS_EDITABLE = "##editable##";

    public enum Mode
    {
        ADD, EDIT, DISPLAY;
    }

    private Mode mode = Mode.DISPLAY;
    private Integer latestRowToEditIndex = null;

    public abstract LinkedHashMap<ColumnConfig, ClarityField> getColumnWidgetMap();

    private TooltipManager tooltipManager;

    private MD savedModel;

    // We use the saveInProgress flag to prevent double-submit esp. when the Enter key is pressed over the Save button.
    private boolean saveInProgress = false;

    /**
     * @param checkBox
     * @param checkBoxFirstColumn
     * @param allowDuplicate
     * @param showAddRemoveToolbar
     * @param delegateActionsToContainer If true, will call EventsCallback as events occur. See EventsCallback
     *                                   for more information.
     */
    protected ClarityRowEditorGrid(boolean checkBox, boolean checkBoxFirstColumn,
                                   boolean allowDuplicate, boolean showAddRemoveToolbar,
                                   boolean delegateActionsToContainer)
    {
        super(checkBox, checkBoxFirstColumn, showAddRemoveToolbar);
        this.allowDuplicate = allowDuplicate;
        this.delegateActionsToContainer = delegateActionsToContainer;

        mode = Mode.DISPLAY;

        setRowEditor(new ClarityRowEditor());
        tooltipManager = new TooltipManager();

        getContentPanel().addStyleName(ClarityStyle.GRID_TYPE_ROW_EDITOR);

        getGrid().addPlugin(getRowEditor());
        addListeners();
    }

    public void setEventsCallback(EventsCallback<MD> eventsCallback)
    {
        this.eventsCallback = eventsCallback;
    }

    public void setStateChangeCallback(StateChangeCallback stateChangeCallback)
    {
        this.stateChangeCallback = stateChangeCallback;
    }

    @Override
    public void updateStore(List<MD> itemList)
    {
        super.updateStore(itemList);
        for (int i = 0; i < getStoreList().size(); i++)
        {
            if (itemList.get(i).equals(savedModel))
            {
                getGridView().getRow(i).addClassName(ClarityStyle.GRID_SAVED_ROW);
            }
        }
        saveInProgress = false;
        disableButtons();
        enableButtonsByCheckingStore();
    }

    private void addListeners()
    {
        addAddButtonListener();
        addSelectionChangedListener();
        createSaveButtonListener();
        createCancelButtonListener();
        addRemoveButtonListener();
        addBeforeSortListener();
    }

    private void addAddButtonListener()
    {
        SelectionListener<ButtonEvent> addButtonListener = new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                onAddRow();
            }
        };
        addAddButtonSelectionListener(addButtonListener);
    }

    private void addSelectionChangedListener()
    {
        SelectionChangedListener<MD> selectionChangedListener = new SelectionChangedListener<MD>()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent<MD> mdSelectionChangedEvent)
            {
                onSelectionChanged();
            }
        };
        getGrid().getSelectionModel().addSelectionChangedListener(selectionChangedListener);
    }

    private void createSaveButtonListener()
    {
        saveButtonListener = new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                onSaveRow();
            }
        };
    }

    private void createCancelButtonListener()
    {
        cancelButtonListener = new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                onCancelEditRow();
                if (getStore().getModels().size() == 0 && stateChangeCallback != null)
                {
                    stateChangeCallback.onEmpty();
                }
            }
        };
    }

    private void addRemoveButtonListener()
    {
        SelectionListener<ButtonEvent> removeButtonListener = new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                onRemoveRows();
                if (getStore().getModels().size() == 0 && stateChangeCallback != null)
                {
                    stateChangeCallback.onEmpty();
                }
            }
        };
        addRemoveButtonSelectionListener(removeButtonListener);
    }

    private void addBeforeSortListener()
    {
        Listener<StoreEvent> beforeSortListener = new Listener<StoreEvent>()
        {
            @Override
            public void handleEvent(StoreEvent event)
            {
                onBeforeSortGrid();
            }
        };
        getStore().addListener(Store.BeforeSort, beforeSortListener);
    }

    public void onAddRow()
    {
        if (getRowEditor().isEditing())
            getRowEditor().stopEditing(false);

        addModel();
//        getGrid().reconfigure(getGrid().getStore(), getGrid().getColumnModel());
        enableButtonsByCheckingStore();

        //TODO - call presenterCallback.beforeAddMode(...)
        if (delegateActionsToContainer)
            eventsCallback.onBeforeAdd();

        mode = Mode.ADD;
        latestRowToEditIndex = 0;
        getRowEditor().startEditing(0, true);
        getRowEditor().validationManager.clearAllInvalid();
    }

    private void onSelectionChanged()
    {
        savedModel = null;
        mode = Mode.EDIT;
    }

    protected void onEditRow(int rowIndex)
    {
        int rowToEditIndex = rowIndex;
        if (getRowEditor() == null)
            throw new AppX("Row editor is null");

        // If already editing a row, stop editing it.

        if (getRowEditor().isEditing())
        {
            getRowEditor().stopEditing(false);
            if (mode == Mode.ADD)
            {
                removeModel(0);
                mode = Mode.EDIT;
                getGrid().reconfigure(getGrid().getStore(), getGrid().getColumnModel());
                rowToEditIndex--;
            }
        }

        if (delegateActionsToContainer)
            eventsCallback.onBeforeChange(getValueAt(rowToEditIndex));
        //TODO - call presenterCallback.beforeEditMode(...)

        mode = Mode.EDIT;
        latestRowToEditIndex = rowToEditIndex;
        getRowEditor().startEditing(rowToEditIndex, false);
    }

    private void onSaveRow()
    {
        if (!saveInProgress)
        {
            saveInProgress = true;

            if (getRowEditor().isCurrentRowValid())
            {
                setIsDuplicateMessageVisible(false);

                if (delegateActionsToContainer)
                {
                    List<MD> items = Arrays.asList(getCurrentEditingValue());

                    if (mode == Mode.ADD)
                    {
                        eventsCallback.onAddItems(items);
                    }
                    else
                    {
                        eventsCallback.onChangeItems(items);
                    }
                }
                else
                {
                    getRowEditor().stopEditing(true);
                    mode = Mode.DISPLAY;
                    getGrid().getStore().getAt(0).set(NEW_ROW_KEY, (Object)Boolean.FALSE);
                    getGrid().reconfigure(getGrid().getStore(), getGrid().getColumnModel());
                    enableButtonsByCheckingStore();
                }
            }
            else
            {
                //target method will only show duplicate message if the row is a duplicate
                setIsDuplicateMessageVisible(true);
                saveInProgress = false;
            }
        }
    }

    public void cancelEditRow()
    {
        onCancelEditRow();
    }

    private void onCancelEditRow()
    {
        setIsDuplicateMessageVisible(false);
        getRowEditor().stopEditing(false);

        if (mode == Mode.ADD)
        {
            removeModel(0);
            getGrid().reconfigure(getGrid().getStore(), getGrid().getColumnModel());
        }

        if (delegateActionsToContainer)
        {
            // the reason I did this is because the way it was
            // will generate a list of null values, which is not
            // awesome. by CH
            List<MD> items;
            if (getCurrentEditingValue() != null)
                items = Arrays.asList(getCurrentEditingValue());
            else
                items = Collections.<MD>emptyList();

            if (mode == Mode.ADD)
            {
                eventsCallback.onCancelAddItems(items);
            }
            else
            {
                eventsCallback.onCancelChangeItems(items);
            }
        }

        enableButtonsByCheckingStore();
        mode = Mode.DISPLAY;
    }

    protected void onRemoveRows()
    {
        if (delegateActionsToContainer)
        {
            List<MD> itemsToRemove = getSelectedItems();
            if (itemsToRemove.size() > 0)
            {
                eventsCallback.onRemoveItems(itemsToRemove);
            }
        }
        else
        {
            super.onRemoveRows();
        }

        mode = Mode.DISPLAY;
    }

    private void onBeforeSortGrid()
    {
        if (mode == Mode.ADD || mode == Mode.EDIT)
        {
            onCancelEditRow();
        }

        // Now that a Sort.BeforeSort has been fired, lazy-load our StoreSorter. Don't refactor this to happen
        // earlier because it seems to stop the Store's initial sort and/or ignore our StoreSorter.

        if (storeSorter == null)
        {
            storeSorter = new NewRowAwareStoreSorter();
            getGrid().getStore().setStoreSorter(storeSorter);
        }
    }

    private class NewRowAwareStoreSorter extends StoreSorter<MD>
    {
        @Override
        public int compare(Store<MD> store, MD m1, MD m2, String property)
        {
            // Ensure if there is a new row then it will display as the first row,
            // regardless of which column is being sorted or the sort direction

            Style.SortDir sortDir = getGrid().getStore().getSortDir();

            if (m1.get(NEW_ROW_KEY) != null && m1.get(NEW_ROW_KEY).equals(Boolean.TRUE)
                && m2.get(NEW_ROW_KEY) != null && m2.get(NEW_ROW_KEY).equals(Boolean.TRUE))
            {
                throw new IllegalStateException("Can't possibly have more than one new row.");
            }
            else if (m1.get(NEW_ROW_KEY) != null && m1.get(NEW_ROW_KEY).equals(Boolean.TRUE))
            {
                return sortDir == Style.SortDir.ASC ? -1 : 1;
            }
            else if (m2.get(NEW_ROW_KEY) != null && m2.get(NEW_ROW_KEY).equals(Boolean.TRUE))
            {
                return sortDir == Style.SortDir.ASC ? 1 : -1;
            }
            else
            {
                if (property != null)
                {
                    Object v1 = m1.get(property);
                    Object v2 = m2.get(property);
                    return comparator.compare(v1, v2);
                }
                else
                {
                    return comparator.compare(m1, m2);
                }
            }
        }
    }

    /**
     * The container should call this method when Add, Change or Delete succeeds if it doesn't want to
     * refresh the grid. It makes sure the successful row is no longer editable and it changes the mode to DISPLAY.
     * It unmasks the grid.
     */
    public void doUpdateSucceeded()
    {
        if (!delegateActionsToContainer)
        {
            throw new IllegalStateException();
        }

        if (getRowEditor().isEditing())
        {
            getRowEditor().stopEditing(true);
        }
        unMaskGrid();
        mode = Mode.DISPLAY;
    }

    /**
     * The container should call this method when Add, Change or Delete fails. If Add or Change,
     * it makes the failed row editable again so that the user can alter the values or cancel. It unmasks the grid.
     */
    public void doUpdateFailed()
    {
        if (!delegateActionsToContainer)
        {
            throw new IllegalStateException();
        }

        if (mode == Mode.ADD || mode == Mode.EDIT)
        {
            if (!getRowEditor().isEditing())
            {
                getRowEditor().startEditing(latestRowToEditIndex, true);
            }
        }
        unMaskGrid();
    }

    public void unMaskGrid()
    {
        super.unMaskGrid();
        saveInProgress = false;
    }

    private void enableButtonsByCheckingStore()
    {
        if (gridStoreIsEmpty())
            enableAddButton();
        else
            enableButtons();
    }

    private boolean gridStoreIsEmpty()
    {
        return getGrid() == null || getGrid().getStore().getCount() == 0;
    }

    protected Map<ColumnConfig, ClarityField> getWidgetMap()
    {
        if (widgetMap == null)
            widgetMap = getColumnWidgetMap();
        return widgetMap;
    }

    @Override
    public List<ColumnConfig> getColumnConfig()
    {
        List<ColumnConfig> columnConfigList = new ArrayList<ColumnConfig>();
        for (Map.Entry<ColumnConfig, ClarityField> entry : getWidgetMap().entrySet())
        {
            columnConfigList.add(getColumnWithEditor(entry.getKey(), entry.getValue()));
        }
        columnConfigList.add(createActionColumnConfig());
        return columnConfigList;
    }

    private ColumnConfig getColumnWithEditor(ColumnConfig columnConfig, ClarityField field)
    {
        if (field != null)
            columnConfig.setEditor(createCellEditor(field));
        return columnConfig;
    }

    private ColumnConfig createActionColumnConfig()
    {
        ColumnConfig columnConfig = new ColumnConfig(ACTION_ID, BASEVC.actions(), COLUMN_WIDTH);

        columnConfig.setMenuDisabled(true);
        columnConfig.setSortable(false);
        columnConfig.setRenderer(getActionRenderer());
        columnConfig.setEditor(new CellEditor(new LabelField()));
        return columnConfig;
    }

    protected GridCellRenderer getActionRenderer()
    {
        return new GridCellRenderer<MD>()
        {
            @Override
            public Object render(MD model, String property, ColumnData config,
                                 final int rowIndex, final int colIndex, ListStore<MD> store, final Grid<MD> grid)
            {
                Boolean isEditable = (Boolean)model.get(IS_EDITABLE);
                if (isEditable != null && !isEditable)
                    return new Label();
                Button editButton = new Button(getEditButtonText());
                editButton.addSelectionListener(new SelectionListener<ButtonEvent>()
                                {
                                    public void componentSelected(ButtonEvent buttonEvent)
                                    {
                                        onEditRow(rowIndex);
                                    }
                                });
                                editButton.addStyleName(ClarityStyle.TOOLBAR_BUTTON);
                                return editButton;
            }
        };
    }

    public static void setGridModelEditable(MetaModelData model, Boolean isEditable)
    {
        model.set(IS_EDITABLE, isEditable);
    }

    protected String getEditButtonText()
    {
        return BASEVC.edit();
    }

    private <H> CellEditor createCellEditor(ClarityField<H, ? extends Field> field)
    {
        if (field instanceof NullClarityField)
            return null;
        return CellEditorFactory.createCellEditor(field);
    }

    public void setInvalidRowStyle(int num)
    {
        getGridView().setRowInvalid(num);
    }

    public void removeInvalidRowStyle(int num)
    {
        getGridView().removeRowInvalid(num);
    }

    public boolean isGridValid()
    {
        return getRowEditor().validationManager.isCurrentRowValid();
    }

    public boolean isDuplicate()
    {
        return getRowEditor().validationManager.isDuplicate();
    }

    public void setIsDuplicateMessageVisible(boolean visible)
    {
        if (visible)
            getRowEditor().validationManager.showDuplicateMessage();
        else
            getRowEditor().validationManager.clearDuplicateRowsInvalidInGrid();
    }

    public boolean hasSelectedModels()
    {
        return (getSelectedItems() != null && getSelectedItems().size() > 0);
    }

    public <X> X getCurrentOriginalValue(String meta)
    {
        BinderContainer container = (BinderContainer)getRowEditor().binderManager.binderContainerOpt.value();
        Binder binder = container.findBinder(meta);
        if (binder instanceof MutableBinder)
        {
            MutableBinder<X> mutableBinder = (MutableBinder<X>)binder;
            return mutableBinder.getOriginalMetaValue();
        }

        return null;
    }

    public MD getCurrentEditingValue()
    {
        return getGrid().getStore().getAt(getRowEditor().getRowIndex());
    }

    public MD getValueAt(int index)
    {
        return getGrid().getStore().getAt(index);
    }

    public boolean isInsertMode()
    {
        return mode == Mode.ADD;
    }

    public void setEditorEnable(String columnName, boolean enable)
    {

        List<ColumnConfig> columnList = getColumnConfigList();
        int i = 0;

        for (ColumnConfig columnConfig : columnList)
        {
            if (StringUtil.isNotEmpty(columnConfig.getHeader())
                && columnConfig.getHeader().equals(columnName))
            {
                getGrid().getColumnModel().getEditor(i).getField().setEnabled(enable);
                break;
            }
            i++;
        }
    }

    public void setActionRender(Object o)
    {
        for (ColumnConfig columnConfig : getColumnConfigList())
        {
            if (ACTION_ID.equals(columnConfig.getId()))
            {
                columnConfig.setRenderer(null);
                return;
            }
        }
    }

    public boolean hasModifications()
    {
        return getRowEditor().isRendered() && getRowEditor().hasModifications();
    }

    // In order to change the field to read only
    // When it is editing

    public void setEditModeReadOnlyFields(List<String> insertModeReadOnlyFields)
    {
        this.insertModeReadOnlyFields = insertModeReadOnlyFields;
    }

    private ClarityField getCorrespondingField(String attribute)
    {
        for (ColumnConfig columnConfig : getColumnConfigList())
        {
            if (columnConfig.getId().equals(attribute))
            {
                ClarityField f = widgetMap.get(columnConfig);
                if (f instanceof ValidatableComboBox<?>)
                {
                    ((ValidatableComboBox)f).loadUnbound();
                }
                return widgetMap.get(columnConfig);
            }
        }
        return null;
    }

    public ClarityRowEditor getRowEditor()
    {
        return rowEditor;
    }

    public void setRowEditor(ClarityRowEditor rowEditor)
    {
        this.rowEditor = rowEditor;
    }

    public void setRowIndex(int rowIndex)
    {
        rowEditor.setRowIndex(rowIndex);
    }

    public int getCurrentEditingRowIndex()
    {
        return getRowEditor().getRowIndex();
    }

    public void setCurrentEditingRowSaveButtonText(String text)
    {
        getRowEditor().setSaveButtonText(text);
    }


    private final class ClarityRowEditor<M extends MetaModelData> extends RowEditor
    {
        private Button saveButton;
        private Button cancelButton;
        private ValidationManager validationManager;
        private BinderManager binderManager;
        private FormWidgetContainer widgetContainer;
        private boolean editing;
        private int buttonPad = 3;

        public ClarityRowEditor()
        {
            super();
            // use row layout instead of HBox to fix IE issue
            setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
            validationManager = new ValidationManager();
            binderManager = new BinderManager();
            addStyleName(ClarityStyle.GRID_ROW_EDITOR_INLINE);
            setMonitorValid(false);
            setMinButtonWidth(40);
        }

        public void setSaveButtonText(String text)
        {
            if (saveButton != null)
                saveButton.setText(text);
        }

        public Grid<MD> getGrid()
        {
            return grid;
        }

        public int getRowIndex()
        {
            return rowIndex;
        }

        public void setRowIndex(int rowIndex)
        {
            this.rowIndex = rowIndex;
        }

        @Override
        protected void onGridKey(GridEvent e)
        {
        }

        @Override
        protected void doAttachChildren()
        {
            super.doAttachChildren();
            ComponentHelper.doDetach(btns);
        }

        @Override
        protected void showTooltip(String msg)
        {
            tooltipManager.showTooltip(msg);
        }

        @Override
        protected void hideTooltip()
        {
            tooltipManager.hideTooltip();
        }

        @Override
        public void stopEditing(boolean saveChanges)
        {
            editing = false;
            if (saveChanges)
            {
                if (validationManager.isCurrentRowValid())
                {
                    validationManager.clearAllInvalid();
                    super.stopEditing(true);
                    changeListViewToFixBug();
                    binderManager.unbindContainer();
                }
                else
                {
                    validationManager.showInvalidMessage();
                }
            }
            else
            {
                if (isRendered())
                {
                    validationManager.clearAllInvalid();
                }
                binderManager.undoModifications();
                binderManager.unbindContainer();
                super.stopEditing(false);
            }
        }

        // In order to fix the conflicts between View and Store
        // Because we are changing store all the time

        private void changeListViewToFixBug()
        {
            Map<String, Object> data = new FastMap<Object>();
            ColumnModel cm = grid.getColumnModel();
            Record record = getRecord(getGrid().getStore().getAt(rowIndex));
            for (int i = 0, len = cm.getColumnCount(); i < len; i++)
            {
                if (!cm.isHidden(i))
                {
                    Field<?> f = (Field<?>)getItem(i);
                    if (f instanceof LabelField)
                    {
                        continue;
                    }
                    String dindex = cm.getDataIndex(i);
                    Object value = cm.getEditor(i).postProcessValue(f.getValue());
                    data.put(dindex, value);
                }
            }
            RowEditorEvent ree = new RowEditorEvent(this, rowIndex);
            ree.setRecord(record);
            ree.setChanges(data);

            if (fireEvent(Events.ValidateEdit, ree))
            {
                record.beginEdit();
                for (Map.Entry<String, Object> entry : data.entrySet())
                {
                    record.set(entry.getKey(), data.get(entry.getKey()));
                }

                record.endEdit();
                ree.setRecord(record);
                fireEvent(Events.AfterEdit, ree);
            }
        }

        @Override
        public void startEditing(int rowIndex, boolean doFocus)
        {
            setHeight(grid.getView().getRow(0).getClientHeight());
            unMaskGrid();
            super.startEditing(rowIndex, false);
            validationManager.clearAllInvalid();
            if (!btns.isVisible())
                btns.show();
            disableButtons();
            doOnRender();
            replaceActionField();
            replaceReadOnlyFieldInEditMode();
            focusFirstColumn();
            layout();
            binderManager.createBinder();
            binderManager.undoModificationForAttributes(insertModeReadOnlyFields);
            editing = true;
        }

        protected void doOnRender()
        {

        }

        private void focusFirstColumn()
        {
            ColumnModel cm = this.grid.getColumnModel();
            for (int i = 0, len = cm.getColumnCount(); i < len; i++)
            {
                Field<?> f = (Field<?>)getItem(i);
                if (f instanceof LabelField)
                {
                    continue;
                }
                ColumnConfig c = cm.getColumn(i);
                if (!c.isHidden() && c.getEditor() != null)
                {
                    c.getEditor().getField().focus();
                    break;
                }
            }
        }

        @Override
        protected void onShow()
        {
            setHeight(grid.getView().getRow(0).getClientHeight());
            super.onShow();
        }

        @Override
        protected void onRender(Element target, int index)
        {
            super.onRender(target, index);
            btns.hide();
            initBtns();
        }

        @Override
        protected void onRowClick(GridEvent e)
        {
            // may not do anything
        }

        private void initBtns()
        {
            btns.removeStyleName("x-btns");
            saveButton = (Button)btns.getItem(0);
            cancelButton = (Button)btns.getItem(1);

            //we use our own listener for stop editing to ensure consistency with ClarityRowEditor (not gxt)
            saveButton.removeAllListeners();
            saveButton.addSelectionListener(saveButtonListener);

            saveButton.addStyleName(ClarityStyle.BUTTON_PRIMARY);
            saveButton.setAutoWidth(true);
            saveButton.setText(getSaveButtonText());

            //we use our own listener for stop editing to ensure consistency with ClarityRowEditor (not gxt)
            cancelButton.removeAllListeners();
            cancelButton.addSelectionListener(cancelButtonListener);

            cancelButton.addStyleName(ClarityStyle.TOOLBAR_BUTTON);
            cancelButton.setAutoWidth(true);
            cancelButton.setText(BASEVC.cancelButton());
            //cancelButton.setWidth(50);

            btns.setAutoWidth(true);
            btns.layout(true);
        }

        private void replaceActionField()
        {
            ColumnModel cm = grid.getColumnModel();
            for (int actionPos = 0, len = cm.getColumnCount(); actionPos < len; actionPos++)
            {
                if (cm.getColumn(actionPos).getId().equals(ACTION_ID))
                {
                    remove(getItem(actionPos));
                    AdapterField adapterField = new AdapterField(btns);
                    adapterField.setWidth(cm.getColumnWidth(actionPos));
                    HBoxLayoutData ld = new HBoxLayoutData();
                    ld.setMargins(new Margins(0, 0, 0, 0));
                    insert(adapterField, actionPos, ld);
                    break;
                }
            }

            layout(true);
        }

        @Override
        public boolean insert(Widget widget, int index, LayoutData layoutData)
        {
            // use rowlayout instead of HBOX (roweditor default layout) to fix IE issue
            // This margin only works in IE
            // For FFX Chrome, use Margins(0, 1, 2, 1)
            // Even can show widget properly, layout still looks not good
            RowData layoutData2 = new RowData(-1, 1);
            if (GXT.isIE || GXT.isIE6 || GXT.isIE7 || GXT.isIE8)
                layoutData2.setMargins(new Margins(0, 1, 3, 0));
            else
                layoutData2.setMargins(new Margins(0, 1, 2, 1));
            return super.insert(widget, index, layoutData2);
        }

        private void replaceReadOnlyFieldInEditMode()
        {
            if (insertModeReadOnlyFields == null || insertModeReadOnlyFields.size() == 0)
                return;
            ColumnModel cm = grid.getColumnModel();
            for (String id : insertModeReadOnlyFields)
            {
                for (int widgetPos = 0, len = cm.getColumnCount(); widgetPos < len; widgetPos++)
                {
                    if (cm.getColumn(widgetPos).getId().equals(id))
                    {
                        remove(getItem(widgetPos));
                        ColumnConfig c = cm.getColumn(widgetPos);
                        CellEditor ce;
                        if (mode == Mode.ADD)
                        {
                            ce = createCellEditor(getCorrespondingField(id));
                        }
                        else
                        {
                            Object attribute = getCurrentEditingAttribute(id);
                            if (attribute != null && attribute instanceof Number && c.getNumberFormat() != null)
                            {
                                Number n = (Number)attribute;
                                attribute = c.getNumberFormat().format(n.doubleValue());
                            }
                            else if (attribute != null && attribute instanceof Date && c.getDateTimeFormat() != null)
                            {
                                DateTimeFormat dtf = c.getDateTimeFormat();
                                attribute = dtf.format((Date)attribute);
                            }

                            String attrString = attribute == null ? "" : attribute.toString();
                            LabelField labelField = new LabelField(attrString);
                            ce = new CellEditor(labelField);
                        }
                        c.setEditor(ce);
                        Field<?> f = ce.getField();
                        if (f instanceof TriggerField<?>)
                        {
                            ((TriggerField<? extends Object>)f).setMonitorTab(true);

                        }
                        f.setWidth(cm.getColumnWidth(widgetPos));
                        HBoxLayoutData layoutData = getHBoxLayoutData(widgetPos, len);
                        clearParent(f);
                        insert(f, widgetPos, layoutData);
                    }
                }
            }
            layout();
        }

        private HBoxLayoutData getHBoxLayoutData(int widgetPos, int len)
        {
            HBoxLayoutData ld = new HBoxLayoutData();
            if (widgetPos == 0)
            {
                ld.setMargins(new Margins(0, 1, 2, 1));
            }
            else if (widgetPos == len - 1)
            {
                ld.setMargins(new Margins(0, 0, 2, 1));
            }
            else
            {
                ld.setMargins(new Margins(0, 1, 2, 2));
            }
            return ld;
        }

        private native void clearParent(Widget parent) /*-{
            parent.@com.google.gwt.user.client.ui.Widget::parent=null;
        }-*/;

        private Object getCurrentEditingAttribute(String attributeName)
        {
            Meta meta = getCurrentMeta();
            if (meta instanceof ValidatableMeta)
            {
                ValidatableMeta validatableMeta = (ValidatableMeta)meta;
                return validatableMeta.get(attributeName);
            }
            return null;
        }

        public Meta getCurrentMeta()
        {
            return getGrid().getStore().getAt(rowIndex).getMeta();
        }

        @Override
        public void onComponentEvent(ComponentEvent ce)
        {
            if (ce.getEventTypeInt() == KeyNav.getKeyEvent().getEventCode())
            {
                if (ce.getKeyCode() == KeyCodes.KEY_ENTER)
                {
                    ButtonEvent be = new ButtonEvent(saveButton);
                    saveButton.focus();
                    saveButton.fireEvent(Events.Select, be);
                }
            }
        }

        protected boolean isCurrentRowValid()
        {
            return !isDuplicationError() && validationManager.isCurrentRowValid();
        }

        @Override
        protected void verifyLayout(boolean force)
        {
            super.verifyLayout(force);
            if (isRendered() && (isVisible() || force))
            {
                Element row = (Element)grid.getView().getRow(rowIndex);
                row.addClassName(ClarityStyle.EDITABLE_ROW_HEIGHT);
                setSize(El.fly(row).getWidth(false), row.getClientHeight());
            }

        }

        public boolean isDuplicationError()
        {
            return !allowDuplicate && getDuplicateLine() != -1;
        }

        private int getDuplicateLine()
        {
            M currentM = (M)grid.getStore().getAt(rowIndex);
            for (int i = 0; i < grid.getStore().getCount(); i++)
            {
                if (rowIndex == i)
                    continue;
                if (grid.getStore().getAt(i).equals(currentM))
                    return i;
            }
            return -1;
        }

        @Override
        protected void positionButtons()
        {
            // may not do anything
        }

        private void setInvalidRowStyle(int num)
        {
            ClarityRowEditorGrid.this.setInvalidRowStyle(num);
        }

        public void removeInvalidRowStyle(int num)
        {
            ClarityRowEditorGrid.this.removeInvalidRowStyle(num);
        }

        private void cleanDuplicateInvalids()
        {
            for (int i = 0; i < grid.getStore().getCount(); i++)
            {
                removeInvalidRowStyle(i);
            }
        }

        public boolean hasModifications()
        {
            return binderManager.hasModifications();
        }

        public final class ValidationManager
        {
            public ValidationManager()
            {
            }

            public boolean isCurrentRowValid()
            {
                return !isDuplicate() && isIndividualValueValid();
            }

            public boolean isDuplicate()
            {
                return !allowDuplicate && getDuplicateLine() != -1;
            }

            public boolean isIndividualValueValid()
            {
                return binderManager.isValuesValid();
            }

            private int getDuplicateLine()
            {
                M currentM = (M)grid.getStore().getAt(rowIndex);
                for (int i = 0; i < grid.getStore().getCount(); i++)
                {
                    if (rowIndex == i)
                        continue;
                    if (grid.getStore().getAt(i).equals(currentM))
                        return i + 1;
                }
                return -1;
            }

            private String getDuplicateMessage()
            {
                return BASEVC.rowEditorGridHasDuplicationMessage(Integer.toString(getDuplicateLine()));
            }

            public void showDuplicateInvalid(int duplicateNum, String message)
            {
                hideTooltip();
                cleanDuplicateInvalids();
                setDuplicateRowStyleInGrid(duplicateNum - 1);
                setDuplicateRowStyleInEditor();
                showDuplicateTooltip(message);
            }

            public void clearAllInvalid()
            {
                clearFieldsInvalid();
                clearDuplicateRowsInvalidInGrid();
                clearDuplicateRowInvalidInEditor();
                hideTooltip();
            }

            private void clearDuplicateRowInvalidInEditor()
            {
                getRowEditor().getElement().removeClassName(ClarityStyle.GRID_ERROR);
            }

            private void setDuplicateRowStyleInGrid(int num)
            {
                ClarityRowEditorGrid.this.setInvalidRowStyle(num);
            }

            private void setDuplicateRowStyleInEditor()
            {
                getRowEditor().getElement().addClassName(ClarityStyle.GRID_ERROR);
            }

            private void showDuplicateTooltip(String message)
            {
                getRowEditor().showTooltip(message);
            }

            private void hideTooltip()
            {
                getRowEditor().hideTooltip();
                getRowEditor().layout(true);
            }

            private void clearDuplicateRowsInvalidInGrid()
            {

                final Grid<MD> grid = getGrid();
                if (grid == null || grid.getStore() == null) return;
                for (int i = 0; i < grid.getStore().getCount(); i++)
                {
                    ClarityRowEditorGrid.this.getGridView().removeRowInvalid(i);
                }
            }

            private void clearFieldsInvalid()
            {
                ColumnModel cm = getGrid().getColumnModel();
                for (int i = 1, len = cm.getColumnCount(); i < len; i++)
                {
                    Field<?> f = (Field<?>)getRowEditor().getItem(i);
                    if (f != null)
                        f.clearInvalid();
                }
            }

            public void showDuplicateMessage()
            {
                if (isDuplicate())
                {
                    showDuplicateInvalid(getDuplicateLine(), getDuplicateMessage());
                }
            }

            public void showInvalidMessage()
            {
                if (!isIndividualValueValid())
                {
                    Map<String, List<ValidationResult>> validationResults = binderManager.getValidationResults();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<ul class='error-list'>");
                    for (Map.Entry<String, List<ValidationResult>> entry : validationResults.entrySet())
                    {
                        for (ValidationResult validationResult : entry.getValue())
                        {
                            if (validationResult.isDisplayInErrorSummary())
                            {
                                sb.append("<li>");
                                sb.append(validationResult.getFieldLabel()).append(": ").append(
                                    validationResult.getReason());
                                sb.append("</li>");
                            }
                        }
                    }
                    sb.append("</ul>");
                    tooltipManager.showTooltip(sb.toString());
                }
                else if (isDuplicationError())
                {
                    showDuplicateMessage();
                }
            }
        }

        private final class BinderManager
        {
            private NonSerOpt<BinderContainer> binderContainerOpt = NonSerOpt.none();

            public Map<String, List<ValidationResult>> getValidationResults()
            {
                if (binderContainerOpt.isSome())
                {
                    return binderContainerOpt.value().getValidationResults();
                }
                return Collections.emptyMap();
            }

            private void undoModifications()
            {
                if (binderContainerOpt.isSome())
                    binderContainerOpt.value().undoModifications();
            }

            private void unbindContainer()
            {
                if (binderContainerOpt.isSome())
                    binderContainerOpt.value().unbind();
                binderContainerOpt = NonSerOpt.none();
            }

            private void undoModificationForAttributes(List<String> attributes)
            {
                if (binderContainerOpt.isNone())
                    return;
                BinderContainer binderContainer = binderContainerOpt.value();
                binderContainer.undoModificationsForAttributes(attributes);
            }

            private void createBinder()
            {
                Meta meta = getCurrentMeta();
                if (meta instanceof ValidatableMeta)
                {
                    ValidatableMeta vm = (ValidatableMeta)meta;
                    BinderContainer bc = new FormBinderContainer(getWidgetContainer(), vm);
                    bc.bind(FormMode.UPDATE);
                    if (vm instanceof DomainEntity)
                    {
                        ((DomainEntity)vm).notifyValidationHandlers(
                            Collections.<ValidationResult>emptyList(), Option.<String>none());
                    }
                    this.binderContainerOpt = NonSerOpt.some(bc);
                }
                removeComboListItemIfDuplicateNoAllowed(getGrid().getStore().getAt(rowIndex));
            }

            private void removeComboListItemIfDuplicateNoAllowed(MD model)
            {
                if (needToRemoveDuplicateComboValue(model))
                {
                    String duplicateAttribute = model.getDuplicateAttribute();
                    ValidatableComboBox combo = getCorrespondingCombo(duplicateAttribute);
                    if (combo != null)
                        combo.removeListFromStore(getDuplicateList(model.get(duplicateAttribute), duplicateAttribute));
                }
            }

            private List<Object> getDuplicateList(Object exceptObject,
                                                  String duplicateAttribute)
            {
                List<Object> duplicateValueList = new ArrayList<Object>();
                for (MD md : getStoreList())
                {
                    if (md.get(duplicateAttribute) != null
                        && !md.get(duplicateAttribute).equals(exceptObject))
                    {
                        duplicateValueList.add(md.get(duplicateAttribute));
                    }
                }
                return duplicateValueList;
            }

            private ValidatableComboBox getCorrespondingCombo(String duplicateAttribute)
            {
                ClarityField field = getCorrespondingField(duplicateAttribute);
                if (field instanceof ValidatableComboBox)
                {
                    return (ValidatableComboBox)field;
                }
                return null;
            }

            private boolean needToRemoveDuplicateComboValue(MD model)
            {
                return !allowDuplicate && StringUtil.isNotEmpty(model.getDuplicateAttribute());
            }

            private synchronized FormWidgetContainer getWidgetContainer()
            {
                if (widgetContainer == null)
                {
                    widgetContainer = new FormWidgetContainer()
                    {
                        protected void doSetup()
                        {
                            for (Map.Entry<ColumnConfig, ClarityField> entry : widgetMap.entrySet())
                            {
                                ClarityField field = entry.getValue();
                                ColumnConfig config = entry.getKey();
                                String propertyName = config.getId();
                                addField(propertyName, FormMode.UPDATE, field);
                            }
                        }

                        public void selectFirstField()
                        {

                        }
                    };
                }
                return widgetContainer;
            }

            public boolean isValuesValid()
            {
                return binderContainerOpt.isNone() || binderContainerOpt.value().forceReValidate();
            }

            public boolean hasModifications()
            {
                return binderContainerOpt.isSome() && binderContainerOpt.value().hasModifications();
            }
        }

        public boolean isEditing()
        {
            return editing;
        }

    }

    protected String getSaveButtonText()
    {
        return BASEVC.save();
    }

    private final class TooltipManager
    {
        private ToolTip tooltip;
        private static final String CLARITY_ERROR_TOOLTIP_STYLE = "fsm-x-tip";
        private Timer timer;

        private TooltipManager()
        {
            timer = new Timer()
            {
                @Override
                public void run()
                {
                    hideTooltip();
                }
            };
        }

        protected void showTooltip(String msg)
        {
            if (tooltip == null)
            {
                ToolTipConfig config = new ToolTipConfig();
                config.setAutoHide(false);
                config.setMouseOffset(new int[]{25, 0});
                config.setTitle(GXT.MESSAGES.rowEditor_tipTitleText());
                config.setAnchor("left");
                tooltip = new ToolTip(getRowEditor(), config);
                tooltip.addStyleName(CLARITY_ERROR_TOOLTIP_STYLE);
                tooltip.setMaxWidth(600);
            }
            ToolTipConfig config = tooltip.getToolTipConfig();
            config.setText(msg);
            tooltip.update(config);
            tooltip.enable();
            tooltip.show();
            timer.schedule(2000);
        }

        protected void hideTooltip()
        {
            if (tooltip != null)
            {
                tooltip.hide();
                tooltip.disable();
            }
        }
    }

    public void reconfigure()
    {
        getGrid().reconfigure(getGrid().getStore(), getGrid().getColumnModel());
    }

    protected void updateSaveInProgress(boolean inProgress)
    {
        saveInProgress = inProgress;
    }

    public Mode getMode()
    {
        return mode;
    }
}
