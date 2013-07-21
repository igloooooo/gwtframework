package com.iglooit.core.base.client.view.highcharts.config;

import com.extjs.gxt.ui.client.data.BaseModel;

import java.util.Map;

public class RadialGradientColor extends GradientColor
{
    public static final String RADIALGRADIENT = "radialGradient";

    public RadialGradientColor(Double gradientCX, Double gradientCY,
                               Double gradientR,
                               Map<Double, String> stops)
    {
        super(stops);

        RadialGradient radialGradient = new RadialGradient(gradientCX, gradientCY, gradientR);
//        set(RADIALGRADIENT, Util.getJsObject(radialGradient));
        set(RADIALGRADIENT, radialGradient);
    }

    public static class RadialGradient extends BaseModel
    {
        public RadialGradient(Double gradientCX, Double gradientCY,
                              Double gradientR)
        {
            set("cx", gradientCX);
            set("cy", gradientCY);
            set("r", gradientR);
        }
    }
}
