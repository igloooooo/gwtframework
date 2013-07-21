package com.iglooit.core.base.client.view.highcharts.charts;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import java.util.List;

public class ButtonOptions extends ChartConfig
{
    public static final String ALIGN = "align";
    public static final String ENABLED = "enabled";
    public static final String HEIGHT = "height";
    public static final String SYMBOL_FILL = "symbolFill";
    public static final String SYMBOL_SIZE = "symbolSize";
    public static final String SYMBOL_STROKE = "symbolStroke";
    public static final String SYMBOL_STROKE_WIDTH = "symbolStrokeWidth";
    public static final String SYMBOL_X = "symbolX";
    public static final String SYMBOL_Y = "symbolY";
    //highchart 3.0
    //public static final String TEXT = "text";
    //highchart 3.0
    //public static final String theme = "theme";
    public static final String VERTICAL_ALIGN = "verticalAlign";
    public static final String WIDTH = "width";
    public static final String Y = "y";

    public static final String SYMBOL = "symbol";
    public static final String X = "x";
    public static final String ID = "_id";
    public static final String TITLEKEY = "_titleKey";
    public static final String ON_CLICK = "onclick";
    public static final String MENU_ITEMS = "menuItems";

    public ButtonOptions()
    {
        this(true);
    }

    public ButtonOptions(boolean preConfigure)
    {
        if (!preConfigure)
            return;

        set(ALIGN, "right");
        set(ENABLED, Boolean.TRUE);
        set(HEIGHT, 20);
        set(SYMBOL_FILL, "#E0E0E0");
        set(SYMBOL_SIZE, 14);
        set(SYMBOL_STROKE, "#666");
        set(SYMBOL_STROKE_WIDTH, 1);
        set(SYMBOL_X, 12.5);
        set(SYMBOL_Y, 10.5);
        set(VERTICAL_ALIGN, "top");
        set(WIDTH, 24);
        set(Y, 0);
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public void setSymbol(String data)
    {
        set(SYMBOL, data);
    }

    public String getSymbol()
    {
        return get(SYMBOL);
    }

    public void setTitlekey(String data)
    {
        set(TITLEKEY, data);
    }

    public String getTitlekey()
    {
        return get(TITLEKEY);
    }

    public void setId(String data)
    {
        set(ID, data);
    }

    public String getId()
    {
        return get(ID);
    }

    public void setX(int data)
    {
        set(X, data);
    }

    public void setY(int data)
    {
        set(Y, data);
    }

    public void setOnClick(JavaScriptObject onClickCallBack)
    {
        set(ON_CLICK, onClickCallBack);
    }

    public void setSymbolStrokeWidth(int width)
    {
        set(SYMBOL_STROKE_WIDTH, width);
    }

    public void setSymbolFill(String color)
    {
        set(SYMBOL_FILL, color);
    }

    public void setSymbolStroke(String color)
    {
        set(SYMBOL_STROKE, color);
    }

    public void setMenuItems(List<MenuItemOptions> menuItems)
    {
        JsArray arr = JavaScriptObject.createArray().cast();
        for (MenuItemOptions option : menuItems)
        {
            arr.push(Util.getJsObject(option, 10));
        }
        set(MENU_ITEMS, arr);
    }
}
