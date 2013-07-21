package com.iglooit.core.base.client.view.sparkline.option;

import com.google.gwt.core.client.JavaScriptObject;

/*
* Box Plot Options

raw - If set to false (default) then the values supplied are used to caculate the box data points for you. If true
then you must pre-calculate the points (see below)
showOutliers - If true (default) then outliers (values > 1.5x the IQR) are marked with circles and the whiskers are
placed at Q1 and Q3 instead of the least and greatest value
outlierIQR - Set the inter-quartile range multipler used to calculate values that qualify as an outlier -
Defaults to 1.5x
boxLineColor - Line colour used to outline the box
boxFillColor - Fill colour used for the box
whiskerColor - Colour used to draw the whiskers
outlierLineColor - Colour used to draw the outlier circles
outlierFillColor - Colour used to fill the outlier circles
spotRadius - Radius to draw the outlier circles
medianColor - Colour used to draw the median line
target - If set to a value, then a small crosshair is drawn at that point to represent a target value
targetColor - Colour used to draw the target crosshair, if set
minValue - If minvalue and maxvalue are set then the scale of the plot is fixed. By default minValue and maxValue
are deduced from the values supplied
maxValue - See minValue
See also all of the common options above, that can also be used with box plot charts
Set the "type" option to "box" to generate box plots.

As noted in the options above, by default "raw" is set to false. This means that you can just pass an arbitrarily
long list of values to the sparkline function and the corresponding box plot will be calculated from those values.
This is probably the behaviour you want most of the time.

If, on the other hand, you have thousands of values to deal with you may want to pre-compute the points needed for
the box plot. In that case, set raw=true and pass in the computed values. If showing outliers, supplied values of:
low_outlier, low_whisker, q1, median, q3, high_whisker, high_outlier
Omit the outliers and set showOutliers to false to omit outlier display.
* */
public class SparkLineBoxOption extends SparkLineOption
{
    public static final String RAW = "raw";
    public static final String SHOW_OUTLIERS = "showOutliers";
    public static final String OUTLIER_IQR = "outlierIQR";
    public static final String BOX_LINE_COLOR = "boxLineColor";
    public static final String BOX_FILL_COLOR = "boxFillColor";
    public static final String WHISKER_COLOR = "whiskerColor";
    public static final String OUTLIER_LINE_COLOR = "outlierLineColor";
    public static final String OUTLIER_FILL_COLOR = "outlierFillColor";
    public static final String SPOT_RADIUS = "spotRadius";
    public static final String MEDIAN_COLOR = "medianColor";
    public static final String TARGET = "target";
    public static final String TARGET_COLOR = "targetColor";
    public static final String MIN_VALUE = "minValue";
    public static final String MAX_VALUE = "maxValue";

    public SparkLineBoxOption()
    {
        set(TYPE, "box");
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }
}