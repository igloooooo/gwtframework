package com.iglooit.core.base.client.view.highcharts;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Base configuration class for all objects
 * <p/>
 * todo: vp - figure out how to get a result from a fired event
 * (some functions expect a return result, but we can't get that
 * result directly ... perhaps I need to create a generic interface
 * that people inherit and the call is made to that interface instead ...)
 */
public abstract class ChartConfig extends BaseModel
{
    // shared observable object for event handling
    private transient BaseObservable observable = new BaseObservable();

    protected final native Object getProperty(String prop, JavaScriptObject obj)/*-{
        return obj[prop];
    }-*/;

    protected final native boolean containsProperty(String prop, JavaScriptObject obj)/*-{
        return obj[prop] != null && obj[prop] != undefined
    }-*/;

    protected final native Boolean getBool(String prop, JavaScriptObject obj)/*-{

        if (obj[prop] != undefined)
            return this.@com.clarity.core.base.client.view.highcharts.ChartConfig::createWrapper(Z)(obj[prop]);
        return this.@com.clarity.core.base.client.view.highcharts.ChartConfig::createWrapper(Z)(true);
    }-*/;

    protected final native String getString(String prop, JavaScriptObject obj)/*-{

        if (obj[prop] != undefined)
            return this.@com.clarity.core.base.client.view.highcharts.ChartConfig::createWrapper(Ljava/lang/Object;)(obj[prop]);
        return this.@com.clarity.core.base.client.view.highcharts.ChartConfig::createWrapper(Ljava/lang/Object;)('');
    }-*/;

    protected final native JavaScriptObject getJsProperty(String prop, JavaScriptObject obj)/*-{
        if (obj == undefined)
            return null;

        if (obj[prop] == undefined)
            return null;

        return obj[prop];
    }-*/;

    protected final native Double getDouble(String prop, JavaScriptObject obj) /*-{
        return this.@com.clarity.core.base.client.view.highcharts.ChartConfig::createWrapper(D)(Number(obj[prop]));
    }-*/;

    protected final native Integer getInteger(String prop, JavaScriptObject obj) /*-{
        return this.@com.clarity.core.base.client.view.highcharts.ChartConfig::createWrapper(I)(Number(obj[prop]));
    }-*/;

    public Integer createWrapper(int d)
    {
        return Integer.valueOf(d);
    }

    public Double createWrapper(double d)
    {
        return Double.valueOf(d);
    }

    public Boolean createWrapper(boolean d)
    {
        return Boolean.valueOf(d);
    }

    public String createWrapper(Object d)
    {
        return d.toString();
    }

    public abstract void initialise(JavaScriptObject obj);

    protected void fireEvent(EventType type)
    {
        observable.fireEvent(type);
    }

    public void removeListener(EventType type, Listener listener)
    {
        observable.removeListener(type, listener);
    }

    public void addListener(EventType type, Listener listener)
    {
        observable.addListener(type, listener);
    }

    public void removeAllListeners()
    {
        observable.removeAllListeners();
    }

    protected native JavaScriptObject createCallback(ChartConfig instance, EventType type)/*-{
        return function(){
            instance.@com.clarity.core.base.client.view.highcharts.ChartConfig::fireEvent(Lcom/extjs/gxt/ui/client/event/EventType;)(type);
        }
    }-*/;
}
