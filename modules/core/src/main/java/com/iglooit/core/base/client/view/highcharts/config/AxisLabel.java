package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class AxisLabel extends ChartConfig
{
    public static final String ALIGN = "align";
    public static final String ENABLED = "enabled";
    public static final String STEP = "step";
    public static final String DISTANCE = "distance";
    public static final String STYLE = "style";
    public static final String ROTATION = "rotation";

    public static final String X = "x";
    public static final String Y = "y";

    public AxisLabel()
    {
        this(true);
    }

    public AxisLabel(boolean setDefault)
    {
        if (!setDefault)
            return;

        set(ALIGN, "right");
        set(ENABLED, Boolean.TRUE);
        set(X, -8);
        set(Y, 3);
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {
    }

    public void setStep(int step)
    {
        set(STEP, step);
    }

    public void setDistance(int distance)
    {
        set(DISTANCE, distance);
    }

    public void setStyle(CSSObject cssObject)
    {
        set(STYLE, cssObject);
//        set(STYLE, Util.getJsObject(cssObject, 10));
    }

    /* rotation can be string sometimes, check highchart api where you use this object*/
    public void setRotation(String rotation)
    {
        set(ROTATION, rotation);
    }

    /* rotation can be Double sometimes, check highchart api where you use this object*/
    public void setDoubleRotation(Double rotation)
    {
        set(ROTATION, rotation);
    }

    /**
     * parameter data accepts "left", "right" and "center"
     * @param data
     */
    public void setAlign(String data)
    {
        set(ALIGN, data);
    }

    public void setX(int data)
    {
        set(X, data);
    }

    public void setY(int data)
    {
        set(Y, data);
    }

    public void setEnabled(boolean enabled)
    {
        set(ENABLED, enabled);
    }

    public String getAlign()
    {
        return get(ALIGN);
    }

    public int getX()
    {
        return (Integer)get(X);
    }

    public int getY()
    {
        return (Integer)get(Y);
    }

    public boolean getEnabled()
    {
        return (Boolean)get(ENABLED);
    }
}
