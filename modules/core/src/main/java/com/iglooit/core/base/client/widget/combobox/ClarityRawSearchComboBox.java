package com.iglooit.core.base.client.widget.combobox;

import com.clarity.core.base.client.model.SimpleModelValue;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 8/06/12
 * Time: 11:23 AM
 */
public class ClarityRawSearchComboBox extends ClaritySearchComboBox2<String, SimpleModelValue<String>>
{
    private String newValue;
    private String oldValue = "";

    private boolean fireChangeEvents = true;

    public boolean fireChanged()
    {
        if (!fireChangeEvents)
            return false;
        SearchComboBox<SimpleModelValue<String>> combo = getField();
        newValue = combo.getRawValue();
        boolean fire = false;
        if (oldValue == null || !oldValue.equals(newValue))
            fire = true;
        return fire;
    }

    public void fireValueChangeEvent()
    {
        if (fireChanged())
        {
            ValueChangeEvent.fire(this, newValue);
        }
        oldValue = newValue;
    }

    @Override
    public void setValue(String s, boolean b)
    {
        fireChangeEvents = b;
        setValue(s);
        fireValueChangeEvent();
        fireChangeEvents = true;
    }
}
