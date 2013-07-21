package com.iglooit.core.base.client.composition.display;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.commons.iface.type.Tuple2;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class WidgetContainer
{
    private HashMap<Tuple2<String, FormMode>, ClarityField> widgetCache =
        new HashMap<Tuple2<String, FormMode>, ClarityField>();

    public abstract void selectFirstField();

    public Collection<String> getDeclaredWidgetPropertyNames()
    {
        Set<String> propertyNames = new HashSet<String>();
        for (Tuple2<String, FormMode> tuple2 : widgetCache.keySet())
            propertyNames.add(tuple2.getFirst());
        return propertyNames;
    }

    public final ClarityField getField(String propertyName, FormMode formMode)
    {
        Tuple2<String, FormMode> key = key(propertyName, formMode);
        if (!widgetCache.containsKey(key))
            widgetCache.put(key, doGetField(propertyName, formMode));
        return widgetCache.get(key);
    }

    private Tuple2<String, FormMode> key(String propertyName, FormMode formMode)
    {
        return new Tuple2<String, FormMode>(propertyName, formMode);
    }

    protected abstract ClarityField doGetField(String propertyName, FormMode formMode);

    protected void addField(String propertyName, FormMode formMode, ClarityField field)
    {
        widgetCache.put(key(propertyName, formMode), field);
    }
    
    public void clearWidgets()
    {
        widgetCache.clear();
    }
}
