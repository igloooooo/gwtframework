package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class DataLabelOptions extends ChartConfig
{
    public static final String ALIGN = "align";
    public static final String BACKGROUND_COLOR = "backgroundColor";
    public static final String BORDER_COLOR = "borderColor";
    public static final String BORDER_RADIUS = "borderRadius";
    public static final String BORDER_WIDTH = "borderWidth";
    public static final String COLOR = "color";
    public static final String CROP = "crop";
    public static final String ENABLED = "enabled";
    public static final String FORMATTER = "formatter";
    public static final String OVERFLOW = "overflow";
    public static final String PADDING = "padding";
    public static final String ROTATION = "rotation";
    public static final String SHADOW = "shadow";
    public static final String STAGGER_LINES = "staggerLines";
    public static final String STEP = "step";
    public static final String STYLE = "style";
    public static final String VERTICAL_ALIGN = "verticalAlign";
    public static final String X = "x";
    public static final String Y = "y";

    public DataLabelOptions()
    {

    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public void setEnabled(Boolean enabled)
    {
        set(ENABLED, enabled);
    }

    public void setColor(String color)
    {
        set(COLOR, color);
    }

    public void setAlign(String align)
    {
        set(ALIGN, align);
    }

    public void setFormatter(JavaScriptObject formatter)
    {
        set(FORMATTER, formatter);
    }

    public void setEmptyFormatter()
    {
        setFormatter(createEmptyFormatter());
    }

    public void setBorderWidth(double borderWidth)
    {
        set(BORDER_WIDTH, borderWidth);
    }

    private static native JavaScriptObject createEmptyFormatter()
        /*-{
            return function()
            {
               return '';
            }
        }-*/;
}
