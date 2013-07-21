package com.iglooit.core.base.client.model;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class GTreeModel extends BaseTreeModel
{
    public static final String NAME = "name";
    private static final String USER_OBJECT = "user_object";

    public GTreeModel(String name, Object userObject)
    {
        super();
        set(NAME, name);
        set(USER_OBJECT, userObject);
    }

    public void setName(String name)
    {
        set(NAME, name);
    }

    public void setUserObject(String userObject)
    {
        set(USER_OBJECT, userObject);
    }

    public String getName()
    {
        return get(NAME);
    }

    public Object getUserObject()
    {
        return get(USER_OBJECT);
    }

}
