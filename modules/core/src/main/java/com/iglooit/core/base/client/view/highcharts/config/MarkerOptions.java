package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class MarkerOptions extends ChartConfig
{
    public static final String ENABLED = "enabled";
    public static final String FILLCOLOR = "fillColor";
    public static final String LINECOLOR = "lineColor";
    public static final String LINEWIDTH = "lineWidth";
    public static final String RADIUS = "radius";
    public static final String SYMBOL = "symbol";
    public static final String DATA = "data";
    public static final String BREACH_ID = "breachId";
    public static final String BREACH_LEVEL = "breachLevel";
    public static final String BREACH_SEVERITY = "breachSeverity";

    public static enum MarkerSymbol
    {
        CIRCLE("circle"), SQUARE("square"), DIAMOND("diamond"), TRIANGLE("triangle"), TRIANGLE_DOWN("triangle-down");
        private String name;

        private MarkerSymbol(String name)
        {
            this.name = name;

        }

        @Override
        public String toString()
        {
            return this.name;
        }
    }


    public MarkerOptions()
    {
        set(ENABLED, Boolean.TRUE);
        set(LINEWIDTH, 0);
        set(RADIUS, 0);
    }
    public MarkerOptions(JavaScriptObject obj)
    {
        initialise(obj);
    }



    @Override
    public void initialise(JavaScriptObject obj)
    {
        setEnabled(getBool(ENABLED, obj));
        if (getProperty(FILLCOLOR, obj) != null)
            setFillColor(getProperty(FILLCOLOR, obj).toString());
        if (getProperty(LINECOLOR, obj) != null)
            setLineColor(getProperty(LINECOLOR, obj).toString());
        setLineWidth(getInteger(LINEWIDTH, obj));
        setRadius(getInteger(RADIUS, obj));
        if (getProperty(SYMBOL, obj) != null)
            setSymbol(getProperty(SYMBOL, obj).toString());
        if (getProperty(DATA, obj) != null)
            setData(getProperty(DATA, obj).toString());
        if (getProperty(BREACH_ID, obj) != null)
            setData(getProperty(BREACH_ID, obj).toString());
        if (containsProperty(BREACH_LEVEL, obj))
        {
            setBreachLevel(getInteger(BREACH_LEVEL, obj));
        }


    }
    public void setData(String data)
    {
        set(DATA, data);
    }
    public void setBreachId(String id)
    {
        set(BREACH_ID, id);
    }

    public String getBreachId()
    {
        return get(BREACH_ID);
    }
    public int getBreachLevel()
    {
        return this.<Integer>get(BREACH_LEVEL);
    }
    public void setBreachLevel(int level)
    {
        this.set(BREACH_LEVEL, level);
    }
    public String getBreachSeverity()
    {
        return this.get(BREACH_SEVERITY);
    }

    public void setBreachSeverity(String severity)
    {
        this.set(BREACH_SEVERITY, severity);
    }
    public String getData()
    {
        return get(DATA);
    }
    public void setSymbol(String symbol)
    {
        set(SYMBOL, symbol);
    }

    public void setSymbol(MarkerSymbol symbol)
    {
        set(SYMBOL, symbol.toString());
    }

    public String getSymbol()
    {
        return get(SYMBOL);
    }

    public void setRadius(int radius)
    {
        set(RADIUS, radius);
    }
    public int getRadius()
    {
        return this.<Integer>get(RADIUS);
    }

    public void setLineWidth(int width)
    {
        set(LINEWIDTH, width);
    }
    public int getLineWidth()
    {
        return this.<Integer>get(LINEWIDTH);
    }

    public void setLineColor(String color)
    {
        set(LINECOLOR, color);
    }
    public String getLineColor()
    {
        return get(LINECOLOR);
    }

    public void setFillColor(String color)
    {
        set(FILLCOLOR, color);
    }
    public String getFillColor()
    {
        return get(FILLCOLOR);
    }

    public void setEnabled(boolean enabled)
    {
        set(ENABLED, enabled);
    }

    public boolean getEnabled()
    {
        return this.<Boolean>get(ENABLED);
    }
}
