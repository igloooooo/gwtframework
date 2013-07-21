package com.iglooit.core.base.client.model;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;

/*
* model data wrapper for combo box use
*
* inner model data need to have attribute DISPLAY_VALUE = "value"
* */
public class ModelDataComboValue<T extends ModelData> extends BaseModelData
{
    /* T need to have 2 attributes
    * 1. DISPLAY_VALUE = "value"
    * 2. IS_SELECTABLE = "isSelectable"
    * */
    public static final String MODEL_DATA = "modelData";
    public static final String DISPLAY_VALUE = "value";
    public static final String IS_SELECTABLE = "isSelectable";

    public ModelDataComboValue()
    {
    }

    public ModelDataComboValue(T value)
    {
        set(MODEL_DATA, value);
        set(DISPLAY_VALUE, value.<String>get(DISPLAY_VALUE));
        set(IS_SELECTABLE, value.<Boolean>get(IS_SELECTABLE));
    }

    @SuppressWarnings("unchecked")
    public T getValue()
    {
        return (T)get(MODEL_DATA);
    }
    
    public String toString()
    {
        return get(MODEL_DATA).toString();
    }
}