package com.iglooit.core.base.client.view.highcharts.charts;

import com.clarity.commons.iface.type.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class PieChartOptions extends CommonSeriesOptions
{
    public static final String ALLOWPOINTSELECT = "allowPointSelect";
    public static final String SIZE = "size";
    public static final String CENTER = "center";

    public PieChartOptions()
    {
        set(ALLOWPOINTSELECT, Boolean.FALSE);
        set(TYPE, "pie");
    }

    public void setSize(String percentage)
    {
        set(SIZE, percentage);
    }

    public void setSize(int size)
    {
        set(SIZE, size);
    }

    public void setCenter(Tuple2<String, String> center)
    {
        List<Object> array = new ArrayList<Object>();
        array.add(center.getFirst());
        array.add(center.getSecond());
        set(CENTER, array);
    }
}
