package com.iglooit.core.base.client.view.mxgraph;

import com.clarity.core.base.client.ClientUUIDFactory;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.UUID;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.HashMap;

public abstract class MXGraphObject
{
    private int x;
    private int y;
    private int width;
    private int height;
    private UUID cUUID;
    private Style style;

    public boolean isVisible()
    {
        if (browserObject == null)
            throw new AppX("Must add object to graph first");

        return isVisible(browserObject);
    }

    public void setVisible(boolean visible)
    {
        if (browserObject == null)
            throw new AppX("Must add object to graph first");


        setVisible(browserObject, visible);
    }

    private static native boolean isVisible(JavaScriptObject cell) /*-{
        return cell.isVisible();
    }-*/;

    private static native void setVisible(JavaScriptObject cell, boolean value) /*-{
        cell.setVisible(value);
    }-*/;

    private JavaScriptObject browserObject;
    private Graph memberGraph;

    private HashMap<String, Event> objectEvents = new HashMap<String, Event>();

    public void addEvent(String eventName, Event event)
    {
        objectEvents.put(eventName, event);
    }

    public void executeEvent(String eventName)
    {
        executeEvent(eventName, null);
    }

    public void executeEvent(String eventName, EventData eventData)
    {
        Event e = objectEvents.get(eventName);

        if (e == null)
        {
            //Not an exception, just a warn
            GWT.log("No events registered for object : " + this);
        }
        else
        {
            e.onEvent(this, eventData);
        }
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public String getUUID()
    {
        return cUUID.toString();
    }

    public MXGraphObject(int x, int y, int width, int height)
    {
        this();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    public MXGraphObject()
    {
        cUUID = ClientUUIDFactory.generateStatic();
    }

    public void setStyle(Style style)
    {
        this.style = style;
        if (this.getMemberGraph() != null)
        {
            this.getMemberGraph().checkAndRegisterStyle(style);
        }

        //If we're in the graph, use the style now...
        if (this.getBrowserObject() != null && this.getMemberGraph() != null)
        {
            setStyle(this.getBrowserObject(), style.getStyleName());
        }
    }

    private native void setStyle(JavaScriptObject cobject, String styleName) /*-{
        cobject.setStyle(styleName);
    }-*/;

    public Style getStyle()
    {
        return style;
    }

    public JavaScriptObject getBrowserObject()
    {
        return browserObject;
    }

    public void setBrowserObject(JavaScriptObject browserObject)
    {
        this.browserObject = browserObject;
    }

    protected Graph getMemberGraph()
    {
        return memberGraph;
    }

    protected void setMemberGraph(Graph memberGraph)
    {
        this.memberGraph = memberGraph;
    }


}
