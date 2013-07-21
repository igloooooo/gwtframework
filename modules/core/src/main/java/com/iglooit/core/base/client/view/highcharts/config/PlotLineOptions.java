package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class PlotLineOptions extends ChartConfig
{
    private static final String COLOR = "color";
    private static final String DASHSTYLE = "dashstyle";
    private static final String EVENTS = "events";
    private static final String ID = "id";
    private static final String LABEL = "label";
    private static final String VALUE = "value";
    private static final String WIDTH = "width";
    private static final String ZINDEX = "zIndex";

    public PlotLineOptions()
    {
        this(null, null, null, null);
    }

    public PlotLineOptions(String color, String id, Double value, Double width)
    {
        setColor(color);
        set(DASHSTYLE, "Solid");
        setId(id);
        setValue(value);
        setWidth(width);
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {
    }

    public void setColor(String color)
    {
        set(COLOR, color);
    }

    public void setId(String id)
    {
        set(ID, id);
    }

    public void setValue(Double value)
    {
        set(VALUE, value);
    }

    public void setWidth(Double width)
    {
        set(WIDTH, width);
    }
}

