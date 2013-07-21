package com.iglooit.core.base.client.view.sparkline.option;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.core.client.JavaScriptObject;

/*
* Common Options

type - line (default), bar, tristate, discrete, bullet, pie or box
width - Width of the chart - Defaults to 'auto' - May be any valid css width - 1.5em, 20px, etc (using a number
without a unit specifier won't do what you want) - This option does nothing for bar and tristate chars (see barWidth)
height - Height of the chart - Defaults to 'auto' (line height of the containing tag)
lineColor - Used by line and discrete charts
fillColor - Set to false to disable fill.
chartRangeMin - Specify the minimum value to use for the range of the chart - Defaults to the minimum value supplied
chartRangeMax - Specify the maximum value to use for the range of the chart - Defaults to the maximum value supplied
composite - If true then don't erase any existing chart attached to the tag, but draw another chart over the top -
Note that width and height are ignored if an existing chart is detected.
enableTagOptions - If true then options can be specified as attributes on each tag to be transformed into a sparkline,
as well as passed to the sparkline() function. See also tagOptionPrefix
tagOptionPrefix - String that each option passed as an attribute on a tag must begin with. Defaults to 'spark'
tagValuesAttribute - The name of the tag attribute to fetch values from, if present - Defaults to 'values'
* */
public abstract class SparkLineOption extends BaseModel
{
    public static final String TYPE = "type";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String FILL_COLOR = "fillColor";
    public static final String CHART_RANGE_MIN = "chartRangeMin";
    public static final String CHART_RANGE_MAX = "chartRangeMax";
    public static final String COMPOSITE = "composite";
    public static final String ENABLE_TAG_OPTIONS = "enableTagOptions";
    public static final String TAG_OPTION_PREFIX = "tagOptionPrefix";
    public static final String TAG_VALUES_ATTRIBUTES = "tagValuesAttribute";
    
    public abstract void initialise(JavaScriptObject obj);

    protected final native Object getProperty(String prop, JavaScriptObject obj)/*-{
        return obj[prop];
    }-*/;

    protected final native boolean containsProperty(String prop, JavaScriptObject obj)/*-{
        return obj[prop] != null && obj[prop] != undefined
    }-*/;

    protected final native JavaScriptObject getJsProperty(String prop, JavaScriptObject obj)/*-{
        if (obj == undefined)
            return null;

        if (obj[prop] == undefined)
            return null;

        return obj[prop];
    }-*/;
}
