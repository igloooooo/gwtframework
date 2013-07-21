package com.iglooit.core.base.client.view.highcharts.charts;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class MenuItemOptions extends ChartConfig
{
    public static final String TEXT = "text";
    public static final String ON_CLICK = "onclick";
    public static final String SEPARATOR = "separator";

    public MenuItemOptions()
    {
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public void setText(String menuItemText)
    {
        set(TEXT, menuItemText);
    }

    public void setOnClick(JavaScriptObject onClickCallBack)
    {
        set(ON_CLICK, onClickCallBack);
    }

    public void setSeparator(Boolean isSeparator)
    {
        set(SEPARATOR, isSeparator);
    }
}
