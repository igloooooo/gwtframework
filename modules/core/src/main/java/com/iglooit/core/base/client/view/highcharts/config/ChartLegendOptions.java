package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.clarity.core.base.client.view.highcharts.data.Series;
import com.clarity.core.base.client.view.highcharts.handlers.LabelFormatHandler;
import com.google.gwt.core.client.JavaScriptObject;

public class ChartLegendOptions extends ChartConfig
{
    public static final String ALIGN = "align";
    public static final String BACKGROUNDCOLOR = "backgroundColor";
    public static final String BORDERCOLOR = "borderColor";
    public static final String BORDERRADIUS = "borderRadius";
    public static final String BORDERWIDTH = "borderWidth";
    public static final String ENABLED = "enabled";
    public static final String FLOATING = "floating";
    public static final String ITEMHIDDENSTYLE = "itemHiddenStyle";
    public static final String ITEMHOVERSTYLE = "itemHoverStyle";
    public static final String ITEMSTYLE = "itemStyle";
    public static final String ITEMWIDTH = "itemWidth";
    public static final String LAYOUT = "layout";
    public static final String LABELFORMATTER = "labelFormatter";
    public static final String LINEHEIGHT = "lineHeight";
    public static final String MARGIN = "margin";
    public static final String REVERSED = "reversed";
    public static final String SHADOW = "shadow";
    public static final String STYLE = "style";
    public static final String SYMBOLPADDING = "symbolPadding";
    public static final String SYMBOLWIDTH = "symbolWidth";
    public static final String VERTICALALIGN = "verticalAlign";
    public static final String WIDTH = "width";
    public static final String X = "x";
    public static final String Y = "y";


    public ChartLegendOptions(boolean useDefault)
    {
        if (!useDefault)
            return;
        set(LABELFORMATTER, createFormatterFunction(this));
        set(ALIGN, "center");
        set(BORDERCOLOR, "#909090");
        set(BORDERRADIUS, 5);
        set(BORDERWIDTH, 1);
        set(ENABLED, Boolean.TRUE);
        set(FLOATING, Boolean.FALSE);
        set(LAYOUT, "horizontal");
        set(LINEHEIGHT, 16);
        set(MARGIN, 15);
        set(REVERSED, Boolean.FALSE);
        set(SHADOW, Boolean.FALSE);
        set(SYMBOLPADDING, 5);
        set(SYMBOLWIDTH, 30);
        set(VERTICALALIGN, "bottom");
        set(X, 15);
        set(Y, 0);
    }

    public ChartLegendOptions()
    {
        this(true);
    }


    @Override
    public void initialise(JavaScriptObject obj)
    {

    }


    public String getAlign()
    {
        return get(ALIGN);
    }
    public void setAlign(String data)
    {
        set(ALIGN, data);
    }
    public String getBackgroundColor()
    {
        return get(BACKGROUNDCOLOR);
    }
    public void setBackgroundColor(String data)
    {
        set(BACKGROUNDCOLOR, data);
    }
    public int getBorderColor()
    {
        return (Integer)get(BORDERCOLOR);
    }
    public void setBorderColor(int data)
    {
        set(BORDERCOLOR, data);
    }
    public int getBorderRadius()
    {
        return (Integer)get(BORDERRADIUS);
    }
    public void setBorderRadius(int data)
    {
        set(BORDERRADIUS, data);
    }
    public int getBorderWidth()
    {
        return (Integer)get(BORDERWIDTH);
    }
    public void setBorderWidth(int data)
    {
        set(BORDERWIDTH, data);
    }
    public boolean getEnabled()
    {
        return (Boolean)get(ENABLED);
    }
    public void setEnabled(boolean data)
    {
        set(ENABLED, data);
    }
    public boolean getFloating()
    {
        return (Boolean)get(FLOATING);
    }
    public void setFloating(boolean data)
    {
        set(FLOATING, data);
    }
    public CSSObject getItemHiddenStYle()
    {
        return (CSSObject)get(ITEMHIDDENSTYLE);
    }
    public void setItemHiddenStyle(CSSObject data)
    {
        set(ITEMHIDDENSTYLE, data);
    }
    public CSSObject getItemHoverStYle()
    {
        return (CSSObject)get(ITEMHOVERSTYLE);
    }
    public void setItemHoverStyle(CSSObject data)
    {
        set(ITEMHOVERSTYLE, data);
    }
    public CSSObject getItemStYle()
    {
        return (CSSObject)get(ITEMSTYLE);
    }
    public void setItemStyle(CSSObject data)
    {
        set(ITEMSTYLE, data);
    }
    public int getItemWidth()
    {
        return (Integer)get(ITEMWIDTH);
    }
    public void setItemWidth(int data)
    {
        set(ITEMWIDTH, data);
    }
    public String getLaYout()
    {
        return get(LAYOUT);
    }
    public void setLayout(String data)
    {
        set(LAYOUT, data);
    }
    //WARNING: NOT SUPPORTED LABELFORMATTER

    public int getLineHeight()
    {
        return (Integer)get(LINEHEIGHT);
    }
    public void setLineHeight(int data)
    {
        set(LINEHEIGHT, data);
    }
    public int getMargin()
    {
        return (Integer)get(MARGIN);
    }
    public void setMargin(int data)
    {
        set(MARGIN, data);
    }
    public boolean getReversed()
    {
        return (Boolean)get(REVERSED);
    }
    public void setReversed(boolean data)
    {
        set(REVERSED, data);
    }
    public boolean getShadow()
    {
        return (Boolean)get(SHADOW);
    }
    public void setShadow(boolean data)
    {
        set(SHADOW, data);
    }
    public CSSObject getStYle()
    {
        return (CSSObject)get(STYLE);
    }
    public void setStyle(CSSObject data)
    {
        set(STYLE, data);
    }
    public int getSYmbolPadding()
    {
        return (Integer)get(SYMBOLPADDING);
    }
    public void setSymbolPadding(int data)
    {
        set(SYMBOLPADDING, data);
    }
    public int getSYmbolWidth()
    {
        return (Integer)get(SYMBOLWIDTH);
    }
    public void setSymbolWidth(int data)
    {
        set(SYMBOLWIDTH, data);
    }
    public String getVerticalAlign()
    {
        return get(VERTICALALIGN);
    }
    public void setVerticalAlign(String data)
    {
        set(VERTICALALIGN, data);
    }
    public int getWidth()
    {
        return (Integer)get(WIDTH);
    }
    public void setWidth(int data)
    {
        set(WIDTH, data);
    }
    public int getX()
    {
        return (Integer)get(X);
    }
    public void setX(int data)
    {
        set(X, data);
    }
    public int getY()
    {
        return (Integer)get(Y);
    }
    public void setY(int data)
    {
        set(Y, data);
    }

    private transient LabelFormatHandler handler;

    public void setLabelFormatter(LabelFormatHandler handler)
    {
        this.handler = handler;
    }

    private String format(JavaScriptObject seriesData)
    {
        //convert to model data from javascript object
        Series data = new Series();
        data.initialise(seriesData);
        

        if (handler == null)
            return data.get("name");

        return handler.format(data);

    }

    private native JavaScriptObject createFormatterFunction(ChartLegendOptions option)
    /*-{
        return function()
        {
            return option.@com.clarity.core.base.client.view.highcharts.config.ChartLegendOptions::format(Lcom/google/gwt/core/client/JavaScriptObject;)(this);
        };
    }-*/;
}
