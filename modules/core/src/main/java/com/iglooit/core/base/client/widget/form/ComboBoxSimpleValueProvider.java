package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.model.SimpleModelValue;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class ComboBoxSimpleValueProvider<T> implements ValidatableAdapterField.ValueProvider<T>
{
    private ComboBox<SimpleModelValue<T>> combo;

    public ComboBoxSimpleValueProvider(ComboBox<SimpleModelValue<T>> combo)
    {
        this.combo = combo;
    }

    @Override
    public T get()
    {
        return (T)(combo.getValue().get(SimpleModelValue.VALUE));
    }

    @Override
    public void set(T value)
    {
        SimpleModelValue<T> model = new SimpleModelValue<T>(value);
        combo.setValue(model);
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
