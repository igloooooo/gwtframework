package com.iglooit.core.base.client.view.sparkline.option;

import com.google.gwt.core.client.JavaScriptObject;

/*
* Discrete Chart Options

Set the "type" option to "discrete" to generate discrete charts.

lineHeight - Height of each line in pixels - Defaults to 30% of the graph height
thresholdValue - Values less than this value will be drawn using thresholdColor instead of lineColor
thresholdColor
See also all of the common options above, that can also be used with discrete charts
* */
public class SparkLineDiscreteOption extends SparkLineOption
{
    public static final String LINE_HEIGHT = "lineHeight";
    public static final String THRESHOLD_VALUE = "thresholdValue";
    public static final String THRESHOLD_COLOR = "thresholdColor";

    public SparkLineDiscreteOption()
    {
        set(TYPE, "discrete");
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }
}