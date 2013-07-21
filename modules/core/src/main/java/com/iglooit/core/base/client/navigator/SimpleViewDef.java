package com.iglooit.core.base.client.navigator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleViewDef
{
    public static final String HELP_NAME = "hn";
    private List<SimpleViewElement> viewElementList = new ArrayList<SimpleViewElement>();
    private LinkedHashMap<String, Object> attributes = new LinkedHashMap<String, Object>();

    public SimpleViewDef()
    {

    }

    public SimpleViewDef(String helpName)
    {
        attributes.put(HELP_NAME, helpName);
    }

    public void addSimpleViewElement(SimpleViewElement viewElement)
    {
        viewElementList.add(viewElement);
    }

    public void addAttribute(String key, String value)
    {
        attributes.put(key, value);
    }

    public String getHelpName()
    {
        return (String)attributes.get(HELP_NAME);
    }

    public List<SimpleViewElement> getViewElementList()
    {
        return viewElementList;
    }

    public Map<String, Object> getAttributes()
    {
        return attributes;
    }
}
