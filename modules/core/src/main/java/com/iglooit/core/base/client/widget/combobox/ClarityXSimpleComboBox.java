package com.iglooit.core.base.client.widget.combobox;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.mvp.ClarityField;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClarityXSimpleComboBox implements ClarityField<List<String>, XSimpleFilterAllComboBox>, IComboBox<String>
{
    private XSimpleFilterAllComboBox combo;
    private HandlerManager innerHandlerManager = new HandlerManager(null);
    // If allReplacedString is set,
    // when selectall == true, this string would be set as the value
    private String allReplacedString;
    private boolean collapseChanged;

    private List<String> oldValue = new ArrayList<String>();
    private List<String> newValue = new ArrayList<String>();

    public ClarityXSimpleComboBox()
    {
        combo = new XSimpleFilterAllComboBox()
        {
            @Override
            public void collapse()
            {
                super.collapse();
                fireValueChangeEvent();
            }

            @Override
            public void onBrowserEvent(Event event)
            {
                int eventType = DOM.eventGetType(event);
                if (disabled && (eventType == Event.ONMOUSEOVER || eventType == Event.ONMOUSEOUT))
                {
                    enable();
                    super.onBrowserEvent(event);
                    disable();
                }
                else
                {
                    super.onBrowserEvent(event);
                }
            }
        };
        allReplacedString = null;

    }


    public void setSelectAll(boolean selectAll)
    {
        combo.setSelectAll(selectAll);
        fireValueChangeEvent();
        combo.formatText();
    }


    public void unCheckAll()
    {
        combo.clearSelections();
        setSelectAll(false);
        combo.deCheckAll();
    }

    public ClarityXSimpleComboBox(String allReplacedString)
    {
        this();
        this.allReplacedString = allReplacedString;
    }

    public void notifyMeta(List<String> newValue)
    {
        ValueChangeEvent.fire(this, newValue);
    }

    public boolean fireChanged()
    {
        if (combo.isSelectAll() && allReplacedString != null)
            newValue = Arrays.asList(allReplacedString);
        else
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

    public void setAllReplacedString(String allReplacedString)
    {
        this.allReplacedString = allReplacedString;
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
    public XSimpleFilterAllComboBox getField()
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
    public List<String> getValue()
    {
        if (combo.isSelectAll() && allReplacedString != null)
            return Arrays.asList(allReplacedString);
        else
            return combo.getSelectedItems();
    }

    @Override
    public void setValue(List<String> strings)
    {
        // not changing value on run time, do not use me!
        combo.setSelectionItems(strings);
    }

    @Override
    public void setValue(List<String> strings, boolean b)
    {
        setSelectAll(strings.size() == combo.getStore().getCount());
        combo.setSelectionItems(strings);
        fireValueChangeEvent();
        combo.formatText();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<String>> listValueChangeHandler)
    {
        final HandlerRegistration valueChangeReg =
            innerHandlerManager.addHandler(ValueChangeEvent.getType(), listValueChangeHandler);
        return valueChangeReg;
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        innerHandlerManager.fireEvent(gwtEvent);
    }

    @Override
    public void updateStore(List<String> strings)
    {
        combo.updateStore(strings);
        setSelectAll(true);
    }

    @Override
    public void maskCombo()
    {
        combo.maskCombo();
    }

    @Override
    public void unmaskCombo()
    {
        combo.unmaskCombo();
    }

    @Override
    public List<String> getSelectedItems()
    {
        return combo.getSelectedItems();
    }

    public void addListener(EventType eventType, Listener<ComponentEvent> listener)
    {
        combo.addListener(eventType, listener);
    }


    public List<String> getStoreList()
    {
        if (combo.getStore() == null)
            return Collections.emptyList();
        List<String> storeList = new ArrayList<String>();
        for (int i = 0; i < combo.getStore().getCount(); i++)
        {
            storeList.add(combo.getStore().getAt(i).getValue());
        }
        return storeList;
    }

    public boolean isAllSelected()
    {
        return combo.isSelectAll();
    }
}
