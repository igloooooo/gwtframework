package com.iglooit.core.base.client.view.highcharts.data;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.clarity.core.base.client.view.highcharts.config.MarkerOptions;
import com.clarity.core.base.client.view.highcharts.config.PointOptions;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;

public class Point extends ChartConfig
{
    public static final String CATEGORY = "category";
    public static final String PERCENTAGE = "percentage";
    public static final String SELECTED = "selected";
    public static final String SERIES = "series";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String MARKER = "marker";

    private transient JavaScriptObject instance;

    @Override
    public void initialise(JavaScriptObject obj)
    {
        instance = obj;
        set(X, getXJs(instance));
        set(Y, getYJs(instance));
        JavaScriptObject markerObj = getJsProperty(MARKER, obj);
        if (markerObj == null)
            return;
        set(MARKER, new MarkerOptions(markerObj));
    }

    public final native Double getXJs(JavaScriptObject obj) /*-{
        return this.@com.clarity.core.base.client.view.highcharts.data.Point::createWrapper(D)(Number(obj.x));
    }-*/;

    public final native Double getYJs(JavaScriptObject obj) /*-{
        return this.@com.clarity.core.base.client.view.highcharts.data.Point::createWrapper(D)(Number(obj.y));
    }-*/;

    public Double createWrapper(double d)
    {
        return new Double(d);
    }

    public Double getX()
    {
        return Double.valueOf(get(X).toString());
    }

    public Double getY()
    {
        return Double.valueOf(get(Y).toString());
    }

    public final native void remove(boolean redraw)/*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Point::instance.remove(redraw);
    }-*/;

    public final native void select(boolean selected)/*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Point::instance.select(selected);
    }-*/;

    private native void update(JavaScriptObject pointOption, boolean redraw)/*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Point::instance.update(pointOption, redraw);        
    }-*/;

    public void remove()
    {
        remove(true);
    }

    public void update(PointOptions option, boolean redraw)
    {
        update(Util.getJsObject(option, 10), redraw);
    }

    public native void update(double value, boolean redraw)/*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Point::instance.update(value, redraw);
    }-*/;
}
