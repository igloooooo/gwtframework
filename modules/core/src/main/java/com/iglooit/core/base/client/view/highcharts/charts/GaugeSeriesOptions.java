package com.iglooit.core.base.client.view.highcharts.charts;

import com.clarity.core.base.client.view.highcharts.config.DialOptions;
import com.clarity.core.base.client.view.highcharts.config.PivotOptions;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.List;

public class GaugeSeriesOptions extends SeriesOptions
{
    public static final String NAME = "name";
    public static final String DATA = "data";

    public static final String DIAL = "dial";
    public static final String PIVOT = "pivot";

    //    private DialOptions dialOptions = new DialOptions();

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public GaugeSeriesOptions()
    {
        set("type", "gauge");
    }

    public void setName(String name)
    {
        set(NAME, name);
    }

    public GaugeSeriesOptions setData(List<Double> data)
    {
        set(DATA, data);
        return this;
    }

    public void setPivot(PivotOptions pivot)
    {
        //set(PIVOT, Util.getJsObject(pivot, 10));
        set(PIVOT, pivot);
    }

    public DialOptions getDialOptions()
    {
        return get(DIAL);
    }

    public void setDialOptions(DialOptions dialOptions)
    {
        //this.dialOptions = dialOptions;
//        set(DIAL, Util.getJsObject(dialOptions));
        set(DIAL, dialOptions);
    }
}
