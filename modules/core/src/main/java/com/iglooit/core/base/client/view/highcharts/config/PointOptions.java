package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.Date;

public class PointOptions extends ChartConfig
{
    public static final String COLOR = "color";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String SLICED = "sliced";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String MARKER = "marker";

    public static final EventType CLICK = new EventType();
    public static final EventType MOUSEOVER = new EventType();
    public static final EventType MOUSEOUT = new EventType();
    public static final EventType REMOVE = new EventType();
    public static final EventType SELECT = new EventType();
    public static final EventType UNSELECT = new EventType();
    public static final EventType UPDATE = new EventType();

    public PointOptions()
    {
        set(SLICED, Boolean.FALSE);
        set("events", createEventsModel());
    }

    public void setMarker(MarkerOptions marker)
    {
        set(MARKER, marker);
    }

    public MarkerOptions getMarker()
    {
        return get(MARKER);
    }

    private JavaScriptObject createEventsModel()
    {
        BaseModel model = new BaseModel();

        model.set("click", createCallback(this, CLICK));
        model.set("mouseOver", createCallback(this, MOUSEOVER));
        model.set("mouseOut", createCallback(this, MOUSEOUT));
        model.set("remove", createCallback(this, REMOVE));
        model.set("select", createCallback(this, SELECT));
        model.set("unselect", createCallback(this, UNSELECT));
        model.set("update", createCallback(this, UPDATE));

        return Util.getJsObject(model);
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public String getColor()
    {
        return get(COLOR);
    }
    public void setColor(String data)
    {
        set(COLOR, data);
    }
    public String getId()
    {
        return get(ID);
    }
    public void setId(String data)
    {
        set(ID, data);
    }
    public String getName()
    {
        return get(NAME);
    }
    public void setName(String data)
    {
        set(NAME, data);
    }
    public boolean getSliced()
    {
        return (Boolean)get(SLICED);
    }
    public void setSliced(boolean data)
    {
        set(SLICED, data);
    }
    public Long getX()
    {
        return get(X);
    }
    public Date getXAsDate()
    {
        return new Date(getX().longValue());
    }

    public void setX(Long data)
    {
        set(X, data);
    }
    public Double getY()
    {
        return (Double)get(Y);
    }
    public void setY(Double data)
    {
        set(Y, data);
    }

}
