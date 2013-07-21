package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.List;

/**
 * Options for the Chart dialog
 * See link http://www.highcharts.com/ref/#chart for more info
 */
public class ChartOptions extends ChartConfig
{
    public static final String ALIGNTICKS = "alignTicks";
    public static final String ANIMATION = "animation";
    public static final String BACKGROUNDCOLOR = "backgroundColor";
    public static final String BORDERCOLOR = "borderColor";
    public static final String BORDERRADIUS = "borderRadius";
    public static final String BORDERWIDTH = "borderWidth";
    public static final String CLASSNAME = "className";
    public static final String DEFAULTSERIESTYPE = "defaultSeriesType";
    public static final String HEIGHT = "height";
    public static final String IGNOREHIDDENSERIES = "ignoreHiddenSeries";
    public static final String INVERTED = "inverted";
    public static final String MARGINTOP  = "marginTop";
    public static final String MARGINRIGHT  = "marginRight";
    public static final String MARGINBOTTOM  = "marginBottom";
    public static final String MARGINLEFT  = "marginLeft";
    public static final String PLOTBACKGROUNDCOLOR  = "plotBackgroundColor";
    public static final String PLOTBORDERCOLOR  = "plotBorderColor";
    public static final String PLOTBORDERWIDTH  = "plotBorderWidth";
    public static final String PLOTSHADOW  = "plotShadow";
    public static final String REFLOW  = "reflow";
    public static final String RENDERTO  = "renderTo";
    public static final String SHADOW  = "shadow";
    public static final String SHOWAXES  = "showAxes";
    public static final String SPACINGTOP  = "spacingTop";
    public static final String SPACINGRIGHT  = "spacingRight";
    public static final String SPACINGBOTTOM  = "spacingBottom";
    public static final String SPACINGLEFT  = "spacingLeft";
    public static final String STYLE  = "style";
    public static final String TYPE  = "type";
    public static final String WIDTH  = "width";
    public static final String ZOOMTYPE  = "zoomType";
    public static final String MARGIN  = "margin";


    public static final EventType SELECTION = new EventType();
    public static final EventType LOAD = new EventType();
    public static final EventType CLICK = new EventType();
    public static final EventType REDRAW = new EventType();
    public static final EventType ADDSERIES = new EventType();

    public ChartOptions()
    {
        this(true);
    }

    public ChartOptions(boolean preConfigure)
    {
        if (!preConfigure)
            return;
        // default values according to http://www.highcharts.com/ref/#chart
        set(ALIGNTICKS, Boolean.TRUE);
        set(ANIMATION, Boolean.TRUE);
        set(BACKGROUNDCOLOR, "#FFFFFF");
        set(BORDERCOLOR, "#4572A7");
        set(BORDERRADIUS, 5);
        set(BORDERWIDTH, 0);
        set(CLASSNAME, "");
        set(DEFAULTSERIESTYPE, "");
        set(HEIGHT, null);
        set(IGNOREHIDDENSERIES, Boolean.TRUE);
        set(INVERTED, Boolean.FALSE);

        set(MARGINTOP, 50);

        set(MARGINRIGHT, 50);
        set(MARGINBOTTOM, 70);
        set(MARGINLEFT, 50);
        
        set(PLOTBORDERCOLOR, "#C0C0C0");
        set(PLOTBORDERWIDTH, 0);
        set(PLOTSHADOW, Boolean.FALSE);
        set(REFLOW, Boolean.TRUE);
        
        set(SHADOW, Boolean.FALSE);
        set(SHOWAXES, Boolean.FALSE);
        set(SPACINGTOP, 10);
        set(SPACINGRIGHT, 10);
        set(SPACINGBOTTOM, 15);
        set(SPACINGLEFT, 10);
        //set(STYLE, new CSSObject());
        set(TYPE, "line");
        
        set(ZOOMTYPE, "");

        set("events", createEventsModel());
    }

    private JavaScriptObject createEventsModel()
    {
        BaseModel model = new BaseModel();
        model.set("addSeries", createCallback(this, ADDSERIES));
        model.set("click", createCallback(this, CLICK));
        model.set("load", createCallback(this, LOAD));
        model.set("redraw", createCallback(this, REDRAW));
        model.set("selection", createCallback(this, SELECTION));

        return Util.getJsObject(model);
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public boolean getAlignTicks()
    {
        return (Boolean)get(ALIGNTICKS);
    }
    public void setAlignTicks(boolean data)
    {
        set(ALIGNTICKS, data);
    }
    // ANIMATION can be true/false OR an AnimationOption
    public Object getAnimation()
    {
        return get(ANIMATION);
    }
    public void setAnimation(Object data)
    {
        set(ANIMATION, data);
    }
    public String getBackgroundColor()
    {
        return get(BACKGROUNDCOLOR);
    }
    public void setBackgroundColor(String data)
    {
        set(BACKGROUNDCOLOR, data);
    }
    public String getBorderColor()
    {
        return get(BORDERCOLOR);
    }
    public void setBorderColor(String data)
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
    public String getClassName()
    {
        return get(CLASSNAME);
    }
    public void setClassName(String data)
    {
        set(CLASSNAME, data);
    }
    public String getDefaultSeriesType()
    {
        return get(DEFAULTSERIESTYPE);
    }
    public void setDefaultSeriesType(String data)
    {
        set(DEFAULTSERIESTYPE, data);
    }
    public int getHeight()
    {
        return (Integer)get(HEIGHT);
    }
    public void setHeight(int data)
    {
        set(HEIGHT, data);
    }
    public boolean getIgnoreHiddenSeries()
    {
        return (Boolean)get(IGNOREHIDDENSERIES);
    }
    public void setIgnoreHiddenSeries(boolean data)
    {
        set(IGNOREHIDDENSERIES, data);
    }
    public boolean getInverted()
    {
        return (Boolean)get(INVERTED);
    }
    public void setInverted(boolean data)
    {
        set(INVERTED, data);
    }
    public int getMarginTop()
    {
        return (Integer)get(MARGINTOP);
    }
    public void setMarginTop(int data)
    {
        set(MARGINTOP, data);
    }
    public int getMarginRight()
    {
        return (Integer)get(MARGINRIGHT);
    }
    public void setMarginRight(int data)
    {
        set(MARGINRIGHT, data);
    }
    public int getMarginBottom()
    {
        return (Integer)get(MARGINBOTTOM);
    }
    public void setMarginBottom(int data)
    {
        set(MARGINBOTTOM, data);
    }
    public int getMarginLeft()
    {
        return (Integer)get(MARGINLEFT);
    }
    public void setMarginLeft(int data)
    {
        set(MARGINLEFT, data);
    }
    public String getPlotBackgroundColor()
    {
        return get(PLOTBACKGROUNDCOLOR);
    }
    public void setPlotBackgroundColor(String data)
    {
        set(PLOTBACKGROUNDCOLOR, data);
    }
    public String getPlotBorderColor()
    {
        return get(PLOTBORDERCOLOR);
    }
    public void setPlotBorderColor(String data)
    {
        set(PLOTBORDERCOLOR, data);
    }
    public int getPlotBorderWidth()
    {
        return (Integer)get(PLOTBORDERWIDTH);
    }
    public void setPlotBorderWidth(int data)
    {
        set(PLOTBORDERWIDTH, data);
    }
    public boolean getPlotShadow()
    {
        return (Boolean)get(PLOTSHADOW);
    }
    public void setPlotShadow(boolean data)
    {
        set(PLOTSHADOW, data);
    }
    public boolean getReflow()
    {
        return (Boolean)get(REFLOW);
    }
    public void setReflow(boolean data)
    {
        set(REFLOW, data);
    }
    public String getRenderTo()
    {
        return get(RENDERTO);
    }
    public void setRenderTo(String data)
    {
        set(RENDERTO, data);
    }
    public boolean getShadow()
    {
        return (Boolean)get(SHADOW);
    }
    public void setShadow(boolean data)
    {
        set(SHADOW, data);
    }
    public boolean getShowAxes()
    {
        return (Boolean)get(SHOWAXES);
    }
    public void setShowAxes(boolean data)
    {
        set(SHOWAXES, data);
    }
    public int getSpacingTop()
    {
        return (Integer)get(SPACINGTOP);
    }
    public void setSpacingTop(int data)
    {
        set(SPACINGTOP, data);
    }
    public int getSpacingRight()
    {
        return (Integer)get(SPACINGRIGHT);
    }
    public void setSpacingRight(int data)
    {
        set(SPACINGRIGHT, data);
    }
    public int getSpacingBottom()
    {
        return (Integer)get(SPACINGBOTTOM);
    }
    public void setSpacingBottom(int data)
    {
        set(SPACINGBOTTOM, data);
    }
    public int getSpacingLeft()
    {
        return (Integer)get(SPACINGLEFT);
    }
    public void setSpacingLeft(int data)
    {
        set(SPACINGLEFT, data);
    }
    public CSSObject getStyle()
    {
        return get(STYLE);
    }
    public void setStyle(CSSObject data)
    {
        set(STYLE, data);
    }
    public String getType()
    {
        return get(TYPE);
    }
    public void setType(String data)
    {
        set(TYPE, data);
    }
    public int getWidth()
    {
        return (Integer)get(WIDTH);
    }
    public void setWidth(int data)
    {
        set(WIDTH, data);
    }
    public String getZoomType()
    {
        return get(ZOOMTYPE);
    }
    public void setZoomType(String data)
    {
        set(ZOOMTYPE, data);
    }

    public void setMargin(List<Double> margins)
    {
        set(MARGIN, margins);
    }
}

