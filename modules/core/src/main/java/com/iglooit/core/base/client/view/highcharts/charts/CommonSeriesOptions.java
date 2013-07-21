package com.iglooit.core.base.client.view.highcharts.charts;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Common Series Options for all charts except Gauge
 */
public abstract class CommonSeriesOptions extends SeriesOptions
{
    //public static final String ALLOWPOINTSELECT = "allowPointSelect";
//    public static final String ANIMATION = "animation";
//    public static final String COLOR = "color";
//    public static final String CURSOR = "cursor";
    public static final String DASHSTYLE = "dashStyle";
//    public static final String ENABLEMOUSETRACKING = "enableMouseTracking";
//    public static final String ID = "id";
    public static final String LINEWIDTH = "lineWidth";
    public static final String POINTSTART = "pointStart";
    public static final String POINTINTERVAL = "pointInterval";
//    public static final String SELECTED = "selected";
    public static final String SHADOW = "shadow";
//    public static final String SHOWCHECKBOX = "showCheckbox";
//    public static final String SHOWINLEGEND = "showInLegend";
    public static final String STACKING = "stacking";
    public static final String CONNECTNULLS = "connectNulls";
//    public static final String STICKYTRACKING = "stickyTracking";
//    public static final String VISIBLE = "visible";
//    public static final String ZINDEX = "zIndex";
//    public static final String NAME = "name";
//    public static final String POINT = "point";
//    public static final String EVENT = "events";


//    public static final EventType CLICKED = new EventType();
//    public static final EventType CHECKBOXCLICKED = new EventType();
//    public static final EventType HIDE = new EventType();
//    public static final EventType LEGENDITEMCLICKED = new EventType();
//    public static final EventType MOUSEOVER = new EventType();
//    public static final EventType MOUSEOUT = new EventType();
//    public static final EventType SHOW = new EventType();

//    private PointOptions pointOption = null;
//
//    private PointDataArray dataArray = new PointDataArray();

    public CommonSeriesOptions()
    {

//        set(ANIMATION, Boolean.TRUE);
//        set(ENABLEMOUSETRACKING, Boolean.TRUE);
        set(LINEWIDTH, 2);
        set(POINTSTART, 0);
        set(POINTINTERVAL, 1);
//        set(SELECTED, Boolean.FALSE);
        set(SHADOW, Boolean.TRUE);
//        set(SHOWCHECKBOX, Boolean.FALSE);
//        set(SHOWINLEGEND, Boolean.TRUE);
//        set(STICKYTRACKING, Boolean.TRUE);
//        set(VISIBLE, Boolean.TRUE);
        //set(POINT, new PointOptions());
//        set(EVENT, createEventConfig());
    }

//    private JavaScriptObject createEventConfig()
//    {
//        BaseModel model = new BaseModel();
//        model.set("click", createCallback(this, CLICKED));
//        model.set("checkboxClick", createCallback(this, CHECKBOXCLICKED));
//        model.set("hide", createCallback(this, HIDE));
//        model.set("legendItemClick", createCallback(this, LEGENDITEMCLICKED));
//        model.set("mouseOver", createCallback(this, MOUSEOVER));
//        model.set("mouseOut", createCallback(this, MOUSEOUT));
//        model.set("show", createCallback(this, SHOW));
//
//        return Util.getJsObject(model);
//    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

//    public PointDataArray getDataArray()
//    {
//        return dataArray;
//    }
//
//    public void setDataArray(PointDataArray dataArray)
//    {
//        this.dataArray = dataArray;
//        set("data", dataArray.getRepresentation());
//    }



//    public void setPoint(PointOptions point)
//    {
//        set(POINT, Util.getJsObject(point));
//        pointOption = point;
//    }
//
//    public PointOptions getPoint()
//    {
//        return pointOption;
//    }

//    public String getName()
//    {
//        return get(NAME);
//    }
//
//    public void setName(String name)
//    {
//        set(NAME, name);
//    }
//
//    public boolean getAnimation()
//    {
//        return (Boolean)get(ANIMATION);
//    }
//    public void setAnimation(boolean data)
//    {
//        set(ANIMATION, data);
//    }
//    public String getColor()
//    {
//        return get(COLOR);
//    }
//    public void setColor(String data)
//    {
//        set(COLOR, data);
//    }
//    public int getCursor()
//    {
//        return (Integer)get(CURSOR);
//    }
//    public void setCursor(int data)
//    {
//        set(CURSOR, data);
//    }
    public String getDashStyle()
    {
        return get(DASHSTYLE);
    }
    public void setDashStyle(String data)
    {
        set(DASHSTYLE, data);
    }
//    public boolean getEnableMouseTracking()
//    {
//        return (Boolean)get(ENABLEMOUSETRACKING);
//    }
//    public void setEnableMouseTracking(boolean data)
//    {
//        set(ENABLEMOUSETRACKING, data);
//    }
//    public String getId()
//    {
//        return get(ID);
//    }
//    public void setId(String data)
//    {
//        set(ID, data);
//    }
    public int getLineWIDth()
    {
        return (Integer)get(LINEWIDTH);
    }
    public void setLineWidth(int data)
    {
        set(LINEWIDTH, data);
    }
    public int getPointStart()
    {
        return (Integer)get(POINTSTART);
    }
    public void setPointStart(int data)
    {
        set(POINTSTART, data);
    }
    public Long getPointInterval()
    {
        return (Long)get(POINTINTERVAL);
    }
    public void setPointInterval(Long data)
    {
        set(POINTINTERVAL, data);
    }
//    public boolean getSelected()
//    {
//        return (Boolean)get(SELECTED);
//    }
//    public void setSelected(boolean data)
//    {
//        set(SELECTED, data);
//    }
    public boolean getShadow()
    {
        return (Boolean)get(SHADOW);
    }
    public void setShadow(boolean data)
    {
        set(SHADOW, data);
    }
//    public boolean getShowCheckbox()
//    {
//        return (Boolean)get(SHOWCHECKBOX);
//    }
//    public void setShowCheckbox(boolean data)
//    {
//        set(SHOWCHECKBOX, data);
//    }
//    public boolean getShowInLegend()
//    {
//        return (Boolean)get(SHOWINLEGEND);
//    }
//    public void setShowInLegend(boolean data)
//    {
//        set(SHOWINLEGEND, data);
//    }
    public String getStacking()
    {
        return get(STACKING);
    }
    public void setStacking(String data)
    {
        set(STACKING, data);
    }
//    public boolean getStickyTracking()
//    {
//        return (Boolean)get(STICKYTRACKING);
//    }
//    public void setStickyTracking(boolean data)
//    {
//        set(STICKYTRACKING, data);
//    }
//    public boolean getVisible()
//    {
//        return (Boolean)get(VISIBLE);
//    }
//    public void setVisible(boolean data)
//    {
//        set(VISIBLE, data);
//    }
//    public int getZIndex()
//    {
//        return (Integer)get(ZINDEX);
//    }
//    public void setZIndex(int data)
//    {
//        set(ZINDEX, data);
//    }

    public boolean getconnectNulls()
    {
        return (Boolean)get(CONNECTNULLS);
    }

    public void setConnectNulls(boolean data)
    {
        set(CONNECTNULLS, data);
    }

}
