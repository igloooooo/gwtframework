package com.iglooit.core.base.client.view.sparkline.option;

import com.google.gwt.core.client.JavaScriptObject;

/*
* Tristate Chart Options

Set the "type" option to "tristate" to generate tristate charts.

posBarColor - Colour for positive (win) values
negBarColor - Colour for negative (lose) values
zeroBarColor - Colour for zero (draw) values
barWidth - Width of each bar, in pixels
barSpacing - Space between each bar, in pixels
zeroAxis - Centers the y-axis at zero if true (default is to automatically do the right thing)
colorMap - Map override colors to certain values - For example if you want all values of -2 to appear yellow,
use colorMap: { '-2': '#ff0' }. As of version 1.5 you may also pass an Array of values here instead of a mapping to
specifiy a color for each individual bar. For example if your chart has three values 1,3,1 you can set
colorMap=["red", "green", "blue"]
See also all of the common options above, that can also be used with tristate charts
* */
public class SparkLineTristateOption extends SparkLineOption
{
    public static final String POS_BAR_COLOR = "posBarColor";
    public static final String NEG_BAR_COLOR = "negBarColor";
    public static final String ZERO_BAR_COLOR = "zeroBarColor";
    public static final String BAR_WIDTH = "barWidth";
    public static final String BAR_SPACING = "barSpacing";
    public static final String ZERO_AXIS = "zeroAxis";
    public static final String COLOR_MAP = "colorMap";

    public SparkLineTristateOption()
    {
        set(TYPE, "tristate");
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }
}