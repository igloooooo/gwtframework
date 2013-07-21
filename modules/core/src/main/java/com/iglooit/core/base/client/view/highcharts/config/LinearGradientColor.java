package com.iglooit.core.base.client.view.highcharts.config;

import com.extjs.gxt.ui.client.data.BaseModel;

public class LinearGradientColor extends GradientColor
{
    public static final String LINEAR_GRADIENT = "linearGradient";

    public LinearGradientColor(Double gradientX1, Double gradientY1,
                               Double gradientX2, Double gradientY2,
                               String startColor, String endColor)
    {
        super(startColor, endColor);

        LinearGradient linearGradient = new LinearGradient(gradientX1, gradientY1, gradientX2, gradientY2);
        set(LINEAR_GRADIENT, linearGradient);
    }

    public static class LinearGradient extends BaseModel
    {
        public LinearGradient(Double gradientX1, Double gradientY1,
                              Double gradientX2, Double gradientY2)
        {
            set("x1", gradientX1);
            set("y1", gradientY1);
            set("x2", gradientX2);
            set("y2", gradientY2);
        }
    }
}
