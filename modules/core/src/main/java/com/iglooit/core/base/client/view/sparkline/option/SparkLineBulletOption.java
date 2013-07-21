package com.iglooit.core.base.client.view.sparkline.option;

import com.google.gwt.core.client.JavaScriptObject;

/*
* Bullet Graph Options

Set the "type" option to "bullet" to generate bullet graphs.

See the wikipedia page for more information on Bullet graphs.
Supplied values must be in this order: target, performance, range1, range2, range3, ...

targetColor - The colour of the vertical target marker
targetWidth - The width of the target marker in pixels
performanceColor - The color of the performance measure horizontal bar
rangeColors - Colors to use for each qualitative range background color - This must be a javascript array.
eg ['red','green', '#22f']
See also all of the common options above, that can also be used with bullet charts
* */
public class SparkLineBulletOption extends SparkLineOption
{
    public static final String TARGET_COLOR = "targetColor";
    public static final String TARGET_WIDTH = "targetWidth";
    public static final String PERFORMANCE_COLOR = "performanceColor";
    public static final String RANGE_COLOR = "rangeColors";

    public SparkLineBulletOption()
    {
        set(TYPE, "bullet");
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }
}