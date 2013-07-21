package com.iglooit.core.base.client.widget.htmlcontainer;

import java.util.List;

public class FsmFormFieldWrapper
{
    private String label;
    private List<String> storeValues;

    public FsmFormFieldWrapper(String label)
    {
        this.label = label;
    }

    public FsmFormFieldWrapper(String label, List<String> storeValues)
    {
        this(label);
        this.storeValues = storeValues;
    }

    public String getLabel()
    {
        return label;
    }

    public List<String> getStoreValues()
    {
        return storeValues;
    }
}
