package com.iglooit.core.base.client.view.sparkline.option;

import com.google.gwt.core.client.JavaScriptObject;

/*
* Pie Chart Options

Set the "type" option to "pie" to generate pie charts.

These little pie charts tend only to be useful with 2 or 3 values at most

sliceColors - An array of colors to use for pie slices
offset - Angle in degrees to offset the first slice - Try -90 or +90
See also all of the common options above, that can also be used with pie charts
* */
public class SparkLinePieOption extends SparkLineOption
{
    public static final String SLICE_COLORS = "sliceColors";
    public static final String OFFSET = "offset";

    public SparkLinePieOption()
    {
        set(TYPE, "pie");
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }
}