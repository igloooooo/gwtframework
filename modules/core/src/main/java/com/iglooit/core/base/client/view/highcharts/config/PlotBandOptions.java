package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class PlotBandOptions extends ChartConfig
{
    public static final String COLOR = "color";
    public static final String FROM = "from";
    public static final String TO = "to";
    private static final String ID = "id";
    private static final String Z_INDEX = "zIndex";
    public static final String INNERRADIUS = "innerRadius";
    public static final String BORDERWIDTH = "borderWidth";

    public PlotBandOptions()
    {
        setColor(null);
        setFrom(null);
        setTo(null);
        set(ID, null);
        set(Z_INDEX, null);
    }

    public PlotBandOptions(String color, Double from, Double to)
    {
        setColor(color);
        setFrom(from);
        setTo(to);
        set(ID, null);
        set(Z_INDEX, null);
    }

    public PlotBandOptions(LinearGradientColor color, Double from, Double to)
    {
        setGradientColor(color);
        setFrom(from);
        setTo(to);
        set(ID, null);
        set(Z_INDEX, null);
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public PlotBandOptions setBorderwidth(Double width)
    {
        set(BORDERWIDTH, width);
        return this;
    }

    public PlotBandOptions setInnerradius(String percentage)
    {
        set(INNERRADIUS, percentage);
        return this;
    }

    public String getColor()
    {
        return get(COLOR);
    }

    public PlotBandOptions setColor(String data)
    {
        set(COLOR, data);
        return this;
    }

    public String getGradientColor()
    {
        return get(COLOR);
    }

    public PlotBandOptions setGradientColor(LinearGradientColor data)
    {
//        set(COLOR, Util.getJsObject(data, 10));
        set(COLOR, data);
        return this;
    }

    public Double getFrom()
    {
        return get(FROM);
    }

    public PlotBandOptions setFrom(Double data)
    {
        set(FROM, data);
        return this;
    }

    public Double getTo()
    {
        return get(TO);
    }

    public PlotBandOptions setTo(Double data)
    {
        set(TO, data);
        return this;
    }

    public String getId()
    {
        return get(ID);
    }

    public PlotBandOptions setId(String data)
    {
        set(ID, data);
        return this;
    }
}
