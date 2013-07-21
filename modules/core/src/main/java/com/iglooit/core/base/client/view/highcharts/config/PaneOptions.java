package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import java.util.List;

public class PaneOptions extends ChartConfig
{
    public static final String STARTANGLE = "startAngle";
    public static final String ENDANGLE = "endAngle";
    public static final String BACKGROUND = "background";
    public static final String CENTER = "center";

    @Override
    public void initialise(JavaScriptObject obj)
    {
    }

    public PaneOptions()
    {
        //set default value
    }

    public PaneOptions setStartAngle(Double angle)
    {
        set(STARTANGLE, angle);
        return this;
    }

    public Float getStartAngle()
    {
        return get(STARTANGLE);
    }

    public PaneOptions setEndAngle(Double angle)
    {
        set(ENDANGLE, angle);
        return this;
    }

    public Float getEndAngle()
    {
        return get(ENDANGLE);
    }

    public PaneOptions setBackground(List<BackgroundOptions> backgrounds)
    {
        //set(BACKGROUND, convertToJsArray(backgrounds));
        set(BACKGROUND, backgrounds);
        return this;
    }

    private JsArray convertToJsArray(List<BackgroundOptions> dataList)
    {
        JsArray jsArray = JavaScriptObject.createArray().cast();
        for (BackgroundOptions data : dataList)
        {
            jsArray.push(Util.getJsObject(data));
        }
        return jsArray;
    }
}
