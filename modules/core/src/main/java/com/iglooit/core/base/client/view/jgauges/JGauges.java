package com.iglooit.core.base.client.view.jgauges;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Element;

public class JGauges extends LayoutContainer
{
    private JavaScriptObject gaugeObject;
    private String gaugeId;
    private JGaugesOption gaugeOption;

    /*
    * Gauge default values are
    * */
    public JGauges(String id)
    {
        gaugeId = id;
        gaugeOption = new JGaugesOption();
        add(new Html(generatePlaceHolderForGauge(id)));
    }

    public String generatePlaceHolderForGauge(String id)
    {
        return "<div id=\"" + id + "\" class=\"jgauge\"></div>";
    }

    public void setGaugeOption(JGaugesOption newOption)
    {
        gaugeOption = newOption;
    }

    public JGaugesOption getGaugeOption()
    {
        return gaugeOption;
    }

    public void setValue(Double value)
    {
        setGaugeValue(gaugeObject, value);
    }

    public Double getValue()
    {
        return getGaugeValue(gaugeObject, this);
    }

    public String getId()
    {
        return gaugeId;
    }

    public JavaScriptObject getGaugeObject()
    {
        return gaugeObject;
    }

    @Override
    protected void onRender(Element parent, int index)
    {
        super.onRender(parent, index);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand()
        {
            @Override
            public void execute()
            {
                gaugeObject = createGauge(JGauges.this, gaugeId);
                JGaugesHelper.applyOptionToGauge(gaugeObject, gaugeOption);
                initGauge(gaugeObject);
                doAfterRender();
            }
        });
    }

    /*
    * Override me to do more actions after gauge is rendered
    * */
    protected void doAfterRender()
    {

    }

    public void doOnClick()
    {
        Info.display(gaugeId, "clicked");
    }

    private native JavaScriptObject createGauge(JGauges instance, String gaugeId)/*-{
        var gauge = new $wnd.jGauge();
        gauge.id = gaugeId;
        gauge.ticks.end = 100;

        gauge.imagePath = 'resources/jgauges/img/jgauge_face_default.png';
        gauge.needle.imagePath = 'resources/jgauges/img/jgauge_needle_default.png';

        $wnd.$('#'+gaugeId).click(function(){
            instance.@com.clarity.core.base.client.view.jgauges.JGauges::doOnClick()();
        });

        return gauge;
    }-*/;

    private native void initGauge(JavaScriptObject gauge)/*-{
        gauge.init();
    }-*/;

    private native void setGaugeValue(JavaScriptObject gaugeObj, Double value)/*-{
        if (gaugeObj == null)
            return;
        gaugeObj.setValue(value);
    }-*/;

    public Double createWrapper(double d)
    {
        return Double.valueOf(d);
    }

    private native Double getGaugeValue(JavaScriptObject gaugeObj, JGauges instance)/*-{
        if (gaugeObj == null)
            return null;
        return gaugeObj.getValue();
    }-*/;
}
