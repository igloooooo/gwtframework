package com.iglooit.core.base.client.view.highcharts.data;

import com.clarity.commons.iface.type.Tuple2;
import com.clarity.core.base.client.view.highcharts.config.PointOptions;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PointDataArray extends BaseModelData
{
    public static final String NAME = "name";
    public static final String COLOR = "color";
    public static final String VISIBLE = "visible";

    @Deprecated
    private List<Double> rawData = new ArrayList<Double>();

    @Deprecated
    private List<Tuple2> dataPointsBasic = new ArrayList<Tuple2>();

    @Deprecated
    private List<Tuple2<Date, Double>> dataPointsDate = new ArrayList<Tuple2<Date, Double>>();

    private List<PointOptions> dataPointObjects = new ArrayList<PointOptions>();

    private void clearLists()
    {
        rawData.clear();
        dataPointObjects.clear();
        dataPointsBasic.clear();
        dataPointsDate.clear();
    }

    @Deprecated
    public List<Double> getRawData()
    {
        return rawData;
    }

    @Deprecated
    public void setRawData(List<Double> rawDataList)
    {
        clearLists();
        this.rawData.addAll(rawDataList);
    }

    @Deprecated
    public List<Tuple2> getDataPointsBasic()
    {
        return dataPointsBasic;
    }

    @Deprecated
    public void setDataPointsBasic(List<Tuple2> dataPointsBasic)
    {
        clearLists();
        this.dataPointsBasic.addAll(dataPointsBasic);
    }

    @Deprecated
    public List<Tuple2<Date, Double>> getDataPointsDate()
    {
        return dataPointsDate;
    }

    @Deprecated
    public void setDataPointsDate(List<Tuple2<Date, Double>> dataPointsDate)
    {
        clearLists();
        this.dataPointsDate.addAll(dataPointsDate);
    }

    public List<PointOptions> getDataPointObjects()
    {
        return dataPointObjects;
    }

    public void setDataPointObjects(List<PointOptions> dataPointObjects)
    {
        clearLists();
        if (dataPointObjects != null)
            this.dataPointObjects.addAll(dataPointObjects);
    }

    public void addDataPointObject(PointOptions dataPoint)
    {
        this.dataPointObjects.add(dataPoint);
    }

    public void addDataPointObjects(List<PointOptions> dataPoints)
    {
        this.dataPointObjects.addAll(dataPoints);
    }

    /**
     * Convert tuple2 objects which are simple types into a jsarray
     *
     * @param items
     * @return
     */
    private JavaScriptObject getRepresentation(List<Tuple2> items)
    {
        JsArray array = JavaScriptObject.createArray().cast();

        for (Tuple2 point : items)
        {
            JsArray pointArray = JavaScriptObject.createArray().cast();
            pointArray.push(create(point.getFirst()));
            pointArray.push(create(point.getSecond()));
            array.push(pointArray);
        }

        return array;
    }

    public JavaScriptObject getRepresentation()
    {
        if (!dataPointObjects.isEmpty())
        {
            JsArray array = JavaScriptObject.createArray().cast();

            for (PointOptions option : dataPointObjects)
            {
                array.push(Util.getJsObject(option, 10));
            }
            return array;
        }

        if (!dataPointsDate.isEmpty())
        {
            JsArray array = JavaScriptObject.createArray().cast();

            for (Tuple2<Date, Double> point : dataPointsDate)
            {
                if (point.getSecond() == null)
                {
                    array.push(create(point.getFirst().getTime()));
                }
                else
                {
                    array.push(create(point.getFirst().getTime(), point.getSecond()));
                }
            }

            return array;
        }

        if (!dataPointsBasic.isEmpty())
        {
            return getRepresentation(dataPointsBasic);
        }

        JsArrayNumber arr = JsArrayNumber.createArray().cast();

        for (double data : rawData)
        {
            arr.push(data);
        }

        return arr;
    }

    /**
     * This is just A CONVENIENCE method, use it carefully
     * if data points don't share the same colour.
     *
     * @return
     */
    public String getDataPointsColour()
    {
        if (dataPointObjects == null || dataPointObjects.size() == 0)
            return "#eee";
        else
            return dataPointObjects.get(0).getColor();
    }

    protected native JavaScriptObject createDouble(double value)/*-{
        return value;
    }-*/;

    protected native JavaScriptObject create(Object obj)/*-{
        return obj;
    }-*/;

    protected native JavaScriptObject create(double ms)/*-{
        return [ms, null];
    }-*/;


    protected native JavaScriptObject create(double ms, double value)/*-{
        return [ms, value];
    }-*/;

    public String getName()
    {
        return get(NAME);
    }

    public void setName(String name)
    {
        set(NAME, name);
    }

    public String getColor()
    {
        return get(COLOR);
    }

    public void setColor(String color)
    {
        set(COLOR, color);
    }

    public boolean isVisible()
    {
        return get(VISIBLE);
    }

    public void setVisible(boolean visible)
    {
        set(VISIBLE, visible);
    }
}
