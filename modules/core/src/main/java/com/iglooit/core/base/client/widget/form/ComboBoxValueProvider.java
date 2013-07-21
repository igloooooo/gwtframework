package com.iglooit.core.base.client.widget.form;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 11/04/13 12:37 PM
 */
public abstract class ComboBoxValueProvider<TValue>
    implements ValidatableAdapterField.ValueProvider<TValue>
{
    private final ComboBox combo;
    private final String key;

    private boolean useRawValue;

    protected ComboBoxValueProvider(ComboBox combo, String key, boolean useRawValue)
    {
        this.combo = combo;
        this.key = key;
        this.useRawValue = useRawValue;
    }

    public ComboBoxValueProvider(ComboBox combo, String key)
    {
        this(combo, key, false);
    }

    public boolean isUseRawValue()
    {
        return useRawValue;
    }

    /**
     * Can be set to true only if the target binding property value is a string.
     * @param useRawValue
     */
    public void setUseRawValue(boolean useRawValue)
    {
        this.useRawValue = useRawValue;
    }

    @Override
    public TValue get()
    {
        if (useRawValue)
            return (TValue)combo.getRawValue();
        else
            return combo.getValue().get(key);
    }

    public abstract ModelData createModelData(TValue value);

    @Override
    public void set(TValue value)
    {
        combo.setValue(createModelData(value));
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
