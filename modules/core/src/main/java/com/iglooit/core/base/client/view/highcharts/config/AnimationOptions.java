package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class AnimationOptions extends ChartConfig
{
    public static final String DURATION = "duration";

    /**
     *  we are using jquery, and the EASING supported (by default) is linear or swing
     */
    public static final String EASING = "easing";


    public AnimationOptions()
    {
        set(DURATION, 500);
        set(EASING, "linear");
    }

    public long getDuration()
    {
        return (Long)get(DURATION);
    }
    public void setDuration(long duration)
    {
        set(DURATION, duration);
    }

    public String getEasing()
    {
        return get(EASING);
    }

    public void setEasing(String ease)
    {
        set(EASING, ease);
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }
}
