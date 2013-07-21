package com.iglooit.core.base.client.widget.combobox;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.util.ClarityWidgetEvent;
import com.clarity.core.base.client.view.CommandPagingLoader;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClarityMultiSelectSearchComboBox
    implements ClarityField<List<String>, MultiSelectSearchComboBox>, IComboBox<String>
{
    private MultiSelectSearchComboBox combo;
    private HandlerManager innerHandlerManager = new HandlerManager(null);
    private Comparator<Object> comparator;

    private List<String> oldValue = new ArrayList<String>();
    private List<String> newValue = new ArrayList<String>();
    private List<ModelData> selectedValue = new ArrayList<ModelData>();
    private boolean onMoreLoad = false;


    public ClarityMultiSelectSearchComboBox()
    {
        combo = new MultiSelectSearchComboBox()
        {
            @Override
            public void collapse()
            {
                super.collapse();
                fireValueChangeEvent();
            }

            @Override
            protected void onMoreDotSelected()
            {
                callOnMoreSelect();
            }
        };
    }

    public void callOnMoreSelect()
    {
        combo.fireEvent(ClarityWidgetEvent.MORE_SELECTION_EVENT_TYPE);
        if (onMoreLoad) getField().manualLoad();
        /* this flag need to be manually set to true every time before we can trigger 'more' load */
        /* will set back to default=false each time after reload */
        onMoreLoad = false;
    }

    public void setOnMoreLoad(boolean onMoreLoad)
    {
        this.onMoreLoad = onMoreLoad;
    }

    public void resetSelectedValue(List preSelectedValue)
    {
        selectedValue.clear();
        if (preSelectedValue != null)
            selectedValue.addAll(preSelectedValue);
    }

    public void updateStoreValue(List newStoreList)
    {
        combo.getStore().removeAll();
        if (comparator != null)
            Collections.sort(newStoreList, comparator);
        combo.getStore().add(newStoreList);
    }

    public void selectNewValue(ModelData newSelect)
    {
        selectedValue.add(newSelect);
    }

    public void deselectValue(ModelData value)
    {
        selectedValue.remove(value);
    }

    /**
     * this will update store add/remove based on selection
     *
     * @param newSelectedValue
     * @return whether the store is empty afterwards
     */
    public boolean updateSelectedValue(List<? extends ModelData> newSelectedValue)
    {
        return updateSelectedValue(newSelectedValue, -1);
    }

    public boolean updateSelectedValue(List<? extends ModelData> newSelectedValue, int posToInsert)
    {
        List<ModelData> itemsNewSelects = new ArrayList<ModelData>();
        List<ModelData> itemsRemoveFromSelects = new ArrayList<ModelData>();

        for (ModelData md : newSelectedValue)
        {
            if (!selectedValue.contains(md))
                itemsNewSelects.add(md);
        }
        for (ModelData md : selectedValue)
        {
            if (!newSelectedValue.contains(md))
                itemsRemoveFromSelects.add(md);
        }

        /* calculate new store */
        List<ModelData> entityList = combo.getStore().getModels();
        // for each new item we select we would like to remove that from the store
        for (ModelData md : itemsNewSelects)
        {
            if (comparator == null)
                combo.getStore().remove(md);
            else
                entityList.remove(md);
        }
        // for each item we remove from select we would like to add that back from the store
        for (ModelData md : itemsRemoveFromSelects)
        {
            if (comparator == null)
            {
                if (posToInsert >= 0 && posToInsert < combo.getStore().getCount())
                    combo.getStore().insert(md, posToInsert);
                else
                    combo.getStore().add(md);
            }
            else
                entityList.add(md);
        }
        if (comparator != null)
        {
            Collections.sort(entityList, comparator);
            combo.getStore().removeAll();
            combo.getStore().add(entityList);
        }

        // update selected value
        selectedValue.clear();
        selectedValue.addAll(newSelectedValue);

        return combo.getStore().getCount() == 0;
    }

    /**
     * remove all selected items and put them back to store
     */
    public void removeAllSelectedItems()
    {
        updateSelectedValue(new ArrayList<ModelData>());
        combo.removeAllSelectedItems();
    }

    public void setComparator(Comparator<Object> comparator)
    {
        this.comparator = comparator;
    }

    public boolean fireChanged()
    {
        newValue = combo.getSelectedItems();
        boolean fire = false;
        if (!listEquals(oldValue, newValue))
            fire = true;
        return fire;
    }

    public void fireValueChangeEvent()
    {
        if (fireChanged())
        {
            oldValue = newValue;
            ValueChangeEvent.fire(this, newValue);
        }
    }

    @Override
    public MultiSelectSearchComboBox getField()
    {
        return combo;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
    }

    @Override
    public void valueExternallyChangedFrom(List<String> oldLocalValue)
    {
    }

    @Override
    public void setValue(List<String> values)
    {
        List<BaseModel> modelData = new ArrayList<BaseModel>();
        for (final String name : values)
        {
            modelData.add(new MultiSelectSearchComboBox.ComboModel(name));
        }
        combo.setSelectedItems(modelData);
    }

    public void setSelectedModels(List models)
    {
        combo.setSelectedItems(models);
    }

    public void setStaticSelectedModels(List models)
    {
        combo.setStaticSelectedItems(models);
    }

    @Override
    public void setValue(List<String> values, boolean fireEvents)
    {
        setValue(values);
        fireValueChangeEvent();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<String>> listValueChangeHandler)
    {
        final HandlerRegistration valueChangeReg =
            innerHandlerManager.addHandler(ValueChangeEvent.getType(), listValueChangeHandler);
        return valueChangeReg;
    }

    @Override
    public String getFieldLabel()
    {
        return combo.getFieldLabel();
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        combo.setFieldLabel(fieldLabel);
    }

    @Override
    public Option<String> getUsageHint()
    {
        return Option.option(combo.getEmptyText());
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        combo.setEmptyText(usageHint);
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        innerHandlerManager.fireEvent(gwtEvent);
    }

    @Override
    public void updateStore(List<String> strings)
    {

    }

    @Override
    public void maskCombo()
    {

    }

    @Override
    public void unmaskCombo()
    {

    }

    @Override
    public List<String> getSelectedItems()
    {
        return combo.getSelectedItems();
    }

    public boolean listEquals(List<String> oldList, List<String> newList)
    {
        if (oldList.size() != newList.size())
        {
            return false;
        }
        for (String newValue : newList)
        {
            if (!oldList.contains(newValue))
                return false;
        }
        return true;
    }

    @Override
    public List<String> getValue()
    {
        return combo.getSelectedItems();
    }

    public String getCSVValues()
    {
        List<String> values = getValue();
        if (values == null || values.isEmpty())
            return "";
        int sz = values.size();
        if (sz == 1)
            return values.get(0);
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < sz - 1; i++)
            buf.append(values.get(i)).append(",");
        buf.append(values.get(sz - 1));
        return buf.toString();
    }

    public List getSelectedMetaModelData()
    {
        return combo.getSelectedObjectItems();
    }

    public ListStore getListStore()
    {
        return combo.getStore();
    }

    public void addListener(EventType eventType, Listener<? extends BaseEvent> listener)
    {
        combo.addListener(eventType, listener);
    }

    public void setPagingLoader(CommandPagingLoader asyncPagingLoader)
    {
        combo.setPagingLoader(asyncPagingLoader);
    }

    public void removeItemsFromSelectedAndStore(List<? extends ModelData> removelist)
    {
        for (ModelData modelData : removelist)
        {
            combo.getStore().remove(modelData);
            combo.removeSelectedItemFromInputField(modelData);
        }
        selectedValue.removeAll(removelist);
        combo.getSelectedObjectItems().removeAll(removelist);
    }

    public void removeItemsFromStore(List<? extends ModelData> removelist)
    {
        for (ModelData modelData : removelist)
        {
            combo.getStore().remove(modelData);
        }
    }

    public void updateStoreModelData(List<? extends ModelData> models)
    {
        getListStore().removeAll();
        getListStore().add(models);
    }
}
