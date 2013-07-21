package com.iglooit.core.base.client.view.highcharts.gauge;

import com.clarity.core.base.client.view.highcharts.config.LinearGradientColor;
import com.extjs.gxt.ui.client.data.BaseModel;

public class HGaugesRangeOption extends BaseModel
{
    public static final String FROM_VALUE = "from";
    public static final String TO_VALUE = "to";
    public static final String COLOR = "color";
    public static final String COLOR_INNER = "colorInner";
    public static final String TEXT_COLOR = "textColor";
    public static final String BORDER_COLOR = "borderColor";

    public HGaugesRangeOption(Double from , Double to,
                              String singleColor)
    {
        set(FROM_VALUE, from);
        set(TO_VALUE, to);
        set(COLOR, singleColor);
        set(COLOR_INNER, singleColor);
        set(TEXT_COLOR, singleColor);
        set(BORDER_COLOR, singleColor);
    }

    public HGaugesRangeOption(Double from , Double to, String textColor,
                              LinearGradientColor linearGradientColor)
    {
        set(FROM_VALUE, from);
        set(TO_VALUE, to);
        set(COLOR, linearGradientColor);
        set(COLOR_INNER, linearGradientColor);
        set(TEXT_COLOR, textColor);
        set(BORDER_COLOR, textColor);
    }

    public void setTextColor(String color)
    {
        set(TEXT_COLOR, color);
    }

    public void setBorderColor(String color)
    {
        set(BORDER_COLOR, color);
    }

    public void setInnerColor(String color)
    {
        set(COLOR_INNER, color);
    }

    public void setInnerColor(LinearGradientColor color)
    {
        set(COLOR_INNER, color);
    }

    public void setOuter(String color)
    {
        set(COLOR, color);
    }
}
