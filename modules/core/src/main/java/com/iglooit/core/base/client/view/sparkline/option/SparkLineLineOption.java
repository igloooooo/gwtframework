package com.iglooit.core.base.client.view.sparkline.option;

import com.google.gwt.core.client.JavaScriptObject;

/*
* Line charts are the default chart type, but to specify the type explicitly set an option called "type" to "line".

defaultPixelsPerValue - Defaults to 3 pixels of width for each value in the chart
spotColor - Set to false or an empty string to hide the final value marker
minSpotColor - Set to false or an empty string to hide the minimum value marker
maxSpotColor - Set to false or an empty string to hide the maximum value marker
spotRadius - In pixels (default: 1.5)
lineWidth - In pixels (default: 1)
normalRangeMin, normalRangeMax Specify threshold values between which to draw a bar to denote the "normal" or expected
range of values. For example the green bar here  might denote a normal operating temperature range.
drawNormalOnTop - By default the normal range is drawn behind the fill area of the chart. Setting this option to true
causes it to be drawn over the top of the fill area.
xvalues - See below
chartRangeClip - If true then the y values supplied to plot will be clipped to fall between chartRangeMin and
chartRangeMax - By default chartRangeMin/Max just ensure that the chart spans at least that range of values, but does
not constrain it
chartRangeMinX - Specifies the minimum value to use for the X value of the chart
chartRangeMaxX - Specifies the maximum value to use for the X value of the chart
See also all of the common options above, that can also be used with line charts
By default the values supplied to line charts are presumed to be y values mapping on to sequential (integer) x values.
If you need to specify both the x and y values for your chart, you have a few options:

Inline: x and y values are separated by a colon: x:y,x:y,x:y - eg. <span class="linechart">1:3,2.7:4,4.8:3</span>
Array of arrays: An array of [x,y] arrays: $('#linechart').sparkline([ [1,3], [2.7,4], [4.8,3] ]);
Separate arrays: Pass x values separately: $('#linechart').sparkline([3,4,3], { xvalues: [1,2.7,4.8]});
You can also specify a value of "null" to omit values from the chart completely. eg:
<span class="linechart">1,2,3,null,3,4,2</span> becomes:
* */
public class SparkLineLineOption extends SparkLineOption
{
    public static final String LINE_COLOR = "lineColor";
    public static final String DEFAULT_PIXELS_PER_VALUE = "defaultPixelsPerValue";
    public static final String SPOT_COLOR = "spotColor";
    public static final String MIN_SPOT_COLOR = "minSpotColor";
    public static final String MAX_SPOT_COLOR = "maxSpotColor";
    public static final String SPOT_RADIUS = "spotRadius";
    public static final String LINE_WIDTH = "lineWidth";
    public static final String NORMAL_RANGE_MIN = "normalRangeMin";
    public static final String NORMAL_RANGE_MAX = "normalRangeMax";
    public static final String DRAW_NORMAL_ON_TOP = "drawNormalOnTop";
    public static final String X_VALUES = "xvalues";
    public static final String CHART_RANGE_CLIP = "chartRangeClip";
    public static final String CHART_RANGE_MIN_X = "chartRangeMinX";
    public static final String CHART_RANGE_MAX_X = "chartRangeMaxX";

    /*
    * TYPE DEFAULT SET TO "LINE"
    * */
    public SparkLineLineOption()
    {
        set(LINE_COLOR, "black");
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }
}
