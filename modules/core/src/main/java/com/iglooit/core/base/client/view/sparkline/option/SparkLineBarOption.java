package com.iglooit.core.base.client.view.sparkline.option;

import com.google.gwt.core.client.JavaScriptObject;

/*
* Bar Chart Options

Set the "type" option to "bar" to generate bar charts. Values can be omitted by using the "null" value
instead of a number.

barColor - Colour used for postive values
negBarColor - Colour used for negative values
zeroColor - Colour used for values equal to zero
nullColor - Colour used for values equal to null - By default null values are omitted entirely, but setting this
adds a thin marker for the entry - This can be useful if your chart is pretty sparse; perhaps try setting it to a
light grey or something equally unobtrusive
barWidth - Width of each bar, in pixels
barSpacing - Space between each bar, in pixels
zeroAxis - Centers the y-axis at zero if true (default is to automatically do the right thing)
colorMap - Map override colors to certain values - For example if you want all values of -2 to appear yellow,
use colorMap: { '-2': '#ff0' }. As of version 1.5 you may also pass an Array of values here instead of a mapping
to specifiy a color for each individual bar. For example if your chart has three values 1,3,1 you can set
colorMap=["red", "green", "blue"]
See also all of the common options above, that can also be used with bar charts
* */
public class SparkLineBarOption extends SparkLineOption
{
    public static final String BAR_COLOR = "barColor";
    public static final String NEG_BAR_COLOR = "negBarColor";
    public static final String ZERO_COLOR = "zeroColor";
    public static final String NULL_COLOR = "nullColor";
    public static final String BAR_WIDTH = "barWidth";
    public static final String BAR_SPACING = "barSpacing";
    public static final String ZERO_AXIS = "zeroAxis";
    public static final String COLOR_MAP = "colorMap";

    public SparkLineBarOption()
    {
        set(TYPE, "bar");
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }
}