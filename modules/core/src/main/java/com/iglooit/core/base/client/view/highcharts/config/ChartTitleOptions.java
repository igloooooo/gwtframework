package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class ChartTitleOptions extends ChartConfig
{

    public static final String ALIGN = "align";
    public static final String FLOATING = "floating";
    public static final String MARGIN = "margin";
    public static final String TEXT = "text";
    public static final String STYLE = "style";
    public static final String VERTICALALIGN = "verticalAlign";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String OFFSET = "offset";

    public ChartTitleOptions()
    {
        this(true);
    }

    public ChartTitleOptions(boolean preConfig)
    {
        if (!preConfig)
            return;

        set(ALIGN, "center");
        set(FLOATING, Boolean.FALSE);
        set(MARGIN, 15);
        set(TEXT, "");
        set(VERTICALALIGN, "top");
        set(X, 0);
        set(Y, 25);
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {
        
    }

    public String getAlign()
    {
        return get(ALIGN);
    }
    public void setAlign(String data)
    {
        set(ALIGN, data);
    }
    public boolean getFloating()
    {
        return (Boolean)get(FLOATING);
    }
    public void setFloating(boolean data)
    {
        set(FLOATING, data);
    }
    public int getMargin()
    {
        return (Integer)get(MARGIN);
    }
    public void setMargin(int data)
    {
        set(MARGIN, data);
    }
    public String getTeXt()
    {
        return get(TEXT);
    }
    public void setText(String data)
    {
        set(TEXT, data);
    }
    public CSSObject getStyle()
    {
        return get(STYLE);
    }
    public void setStyle(CSSObject data)
    {
        set(STYLE, data);
    }
    public String getVerticalAlign()
    {
        return get(VERTICALALIGN);
    }
    public void setVerticalAlign(String data)
    {
        set(VERTICALALIGN, data);
    }
    public int getX()
    {
        return (Integer)get(X);
    }
    public void setX(int data)
    {
        set(X, data);
    }
    public int getY()
    {
        return (Integer)get(Y);
    }
    public void setY(int data)
    {
        set(Y, data);
    }

    public int getOffset()
    {
        return (Integer)get(OFFSET);
    }

    public void setOffset(int data)
    {
        set(OFFSET, data);
    }

}
