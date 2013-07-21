package com.iglooit.core.base.client.view.highcharts.config;

import com.extjs.gxt.ui.client.data.BaseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GradientColor extends BaseModel
{
    public static final String STOPS = "stops";

    public GradientColor(String startColor, String endColor)
    {
        List<List<String>> stopsList = new ArrayList<List<String>>();
        List<String> stop1 = new ArrayList<String>();
        stop1.add("0");
        stop1.add(startColor);
        stopsList.add(stop1);
        List<String> stop2 = new ArrayList<String>();
        stop2.add("1");
        stop2.add(endColor);
        stopsList.add(stop2);
        set(STOPS, stopsList);
    }

    public GradientColor(Map<Double, String> stops)
    {
        List<List<String>> stopsList = new ArrayList<List<String>>();
        for (Map.Entry<Double, String> entity : stops.entrySet())
        {
            List<String> stop = new ArrayList<String>();
            stop.add(entity.getKey().toString());
            stop.add(entity.getValue());
            stopsList.add(stop);
        }
        set(STOPS, stopsList);
    }
}
