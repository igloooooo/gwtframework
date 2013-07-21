package com.iglooit.core.base.client.view.highcharts.data;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.clarity.core.base.client.view.highcharts.config.AnimationOptions;
import com.clarity.core.base.client.view.highcharts.config.PointOptions;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;

import java.util.ArrayList;
import java.util.List;

public class Series extends ChartConfig
{
    public static final String CHART = "chart";

    public static final String DATA = "data";

    public static final String NAME = "name";

    public static final String OPTIONS = "options";

    public static final String SELECTED = "selected";

    public static final String TYPE = "type";

    public static final String VISIBLE = "visible";

    public static final String XAXIS = "xAxis";

    public static final String YAXIS = "yAxis";

    private transient JavaScriptObject instance;


    @Override
    public void initialise(JavaScriptObject obj)
    {
        instance = obj;
        set(NAME, (String)getProperty(NAME, obj));
        set(SELECTED, ("true".equals(getProperty(NAME, obj))));
    }

    public String getName()
    {
        return get(NAME).toString();
    }

    public Boolean getVisible()
    {
        if (instance != null && getBool(VISIBLE, instance) != null)
            return getBool(VISIBLE, instance);
        else
            return get(VISIBLE);
    }

    public String getColor()
    {
        if (instance != null)
        {
            return getString("color", instance);
        }
        return "";
    }

    public List<Point> getPoints()
    {
        JsArray<JavaScriptObject> arr = getPoints(instance);
        List<Point> points = new ArrayList<Point>();
        for (int i = 0; i < arr.length(); ++i)
        {
            if (arr.get(i) == null)
                continue;
            Point p = new Point();
            p.initialise(arr.get(i));
            points.add(p);
        }
        return points;
    }

    private native JsArray<JavaScriptObject> getPoints(JavaScriptObject obj)/*-{
            return obj.data;
    }-*/;

    private native void addPoint(JavaScriptObject obj, JavaScriptObject point, boolean redraw,
                                 boolean shift, boolean animation)/*-{
        obj.addPoint(point, redraw, shift, animation);
    }-*/;

    private native void addPoint(JavaScriptObject obj, JavaScriptObject point, boolean redraw,
                                 boolean shift, JavaScriptObject animation)/*-{
        obj.addPoint(point, redraw, shift, animation);
    }-*/;

    private native void addPoint(JavaScriptObject obj, JavaScriptObject point, boolean redraw,
                                 boolean shift)/*-{
        obj.addPoint(point, redraw, shift);
    }-*/;

    public void addPoint(PointOptions point, boolean redraw, boolean shift, AnimationOptions animation)
    {
        addPoint(instance, Util.getJsObject(point), redraw, shift, Util.getJsObject(animation));
    }

    public void addPoint(PointOptions point, boolean redraw, boolean shift, boolean animation)
    {
        addPoint(instance, Util.getJsObject(point), redraw, shift, animation);
    }

    public void addPoint(PointOptions point, boolean redraw, boolean shift)
    {
        addPoint(instance, Util.getJsObject(point), redraw, shift);
    }

    public void addPoint(PointOptions point, boolean redraw)
    {
        addPoint(instance, Util.getJsObject(point), redraw, false);
    }

    public void addPoint(PointOptions point)
    {
        addPoint(instance, Util.getJsObject(point), true, false);
    }

    public final native void hide()/*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Series::instance.hide();
    }-*/;

    public final native void remove(boolean redraw)/*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Series::instance.remove(redraw);
    }-*/;

    public void remove()
    {
        remove(true);
    }

    public final native void show()/*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Series::instance.show();
    }-*/;

    public final native void select(boolean selected)/*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Series::instance.select(selected);
        this.@com.clarity.core.base.client.view.highcharts.data.Series::instance.visible = false;
    }-*/;

    public void setData(PointDataArray data)
    {
        setData(data, true);
    }

    public void setData(PointDataArray data, boolean redraw)
    {
        setData(data.getRepresentation(), redraw);
    }

    public void setData(JsArrayNumber data)
    {
        setData(data, false);
    }

    private native void setData(JavaScriptObject data, boolean redraw)/*-{

        this.@com.clarity.core.base.client.view.highcharts.data.Series::instance.setData(data, redraw);
    }-*/;

    public native void changeFillColor(String color)/*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Series::instance.area.attr('fill', color);
    }-*/;

    public native void changeFillOpacity(Double opacity)/*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Series::instance.area.attr('fill-opacity', opacity);
    }-*/;
}
