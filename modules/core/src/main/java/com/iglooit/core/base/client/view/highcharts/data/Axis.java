package com.iglooit.core.base.client.view.highcharts.data;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.clarity.core.base.client.view.highcharts.config.ChartTitleOptions;
import com.clarity.core.base.client.view.highcharts.config.PlotBandOptions;
import com.clarity.core.base.client.view.highcharts.config.PlotLineOptions;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;

public class Axis extends ChartConfig
{
    private transient JavaScriptObject instance;

    @Override
    public void initialise(JavaScriptObject obj)
    {
        instance = obj;
    }

    public native void removePlotLine(String id) /*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Axis::instance.removePlotLine(id);
    }-*/;

    public void addPlotLine(PlotLineOptions plotLineOptions)
    {
        JavaScriptObject plotLineJs = Util.getJsObject(plotLineOptions, 10);
        addPlotLine(plotLineJs);
    }

    private native void addPlotLine(JavaScriptObject plotLine) /*-{
            this.@com.clarity.core.base.client.view.highcharts.data.Axis::instance.addPlotLine(plotLine);
    }-*/;

    public native void removePlotBand(String id) /*-{
            this.@com.clarity.core.base.client.view.highcharts.data.Axis::instance.removePlotBand(id);
    }-*/;

    public void addPlotBand(PlotBandOptions plotBandOptions)
    {
        JavaScriptObject plotLineJs = Util.getJsObject(plotBandOptions, 10);
        addPlotBand(plotLineJs);
    }

    private native void addPlotBand(JavaScriptObject plotBand) /*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Axis::instance.addPlotBand(plotBand);
    }-*/;

    public native void setExtremes(double min, double max) /*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Axis::instance.setExtremes(min, max);
    }-*/;

    public native double getExtremesMax() /*-{
        return this.@com.clarity.core.base.client.view.highcharts.data.Axis::instance.getExtremes().max;
    }-*/;

    public native double getExtremesMin() /*-{
        return this.@com.clarity.core.base.client.view.highcharts.data.Axis::instance.getExtremes().min;
    }-*/;

    public void setTitle(ChartTitleOptions titleOptions)
    {
        JavaScriptObject titleJs = Util.getJsObject(titleOptions, 10);
        setTitle(titleJs);
    }

    private native void setTitle(JavaScriptObject titleJs) /*-{
        this.@com.clarity.core.base.client.view.highcharts.data.Axis::instance.setTitle(titleJs, false);
    }-*/;
}
