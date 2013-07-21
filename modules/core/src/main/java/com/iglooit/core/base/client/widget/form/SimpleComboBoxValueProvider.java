package com.iglooit.core.base.client.widget.form;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 27/06/12
 * Time: 3:14 PM
 */
public class SimpleComboBoxValueProvider<T> implements ValidatableAdapterField.ValueProvider<T>
{
    private SimpleComboBox<T> combo;

    public SimpleComboBoxValueProvider(SimpleComboBox<T> combo)
    {
        this.combo = combo;
    }

    @Override
    public T get()
    {
        return combo.getSimpleValue();
    }

    @Override
    public void set(T value)
    {
        combo.setSimpleValue(value);
    }

    @Override
    public void addHandleOnChange(Listener<BaseEvent> listener)
    {
        combo.addListener(Events.Change, listener);
        combo.addListener(Events.Select, listener);
        combo.addListener(Events.Blur, listener);
    }

    @Override
    public void removeHandleOnChange(Listener<BaseEvent> listener)
    {
        combo.removeListener(Events.Blur, listener);
        combo.removeListener(Events.Select, listener);
        combo.removeListener(Events.Change, listener);
    }
}
