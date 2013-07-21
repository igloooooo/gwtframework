package com.iglooit.core.base.client.view.highcharts.gauge;

import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Element;

public class HGauges extends GPanel
{
    private String gaugeId;
    private JavaScriptObject gaugeObject;
    private HGaugesOption gaugeOption;
    private Double preSetValue = 0.0;
    public static final EventType GAUGE_CLICK_EVENT = new EventType();

    public HGauges()
    {
        gaugeOption = new HGaugesOption();
        addStyleName("gauge");
    }

    public HGauges(String id)
    {
        this();
        setId(id);
    }

    public HGauges(int width, int height)
    {
        this();
        setWidth(width);
        setHeight(height);
        gaugeOption.setSize(width, height);
        setStyleAttribute("margin", "0 auto");
    }

    public HGaugesOption getGaugeOption()
    {
        return gaugeOption;
    }

    private JavaScriptObject getJsGaugeOption()
    {
        return Util.getJsObject(gaugeOption, 10);
    }

    private JavaScriptObject getJsGaugeRangeOption()
    {
        return Util.getJsObjects(gaugeOption.getRanges(), 10);
    }

    public void setGaugeValue(Double value)
    {
        if (gaugeObject == null)
        {
            preSetValue = value;
            return;
        }
        setGaugeValueJs(gaugeObject, value.isNaN() ? null : value);
    }

    public Double getGaugeValue()
    {
        if (gaugeObject == null)
            return 0.0;
        else
            return getGaugeValueJs(gaugeObject);
    }

    public void redrawGauge()
    {
        if (StringUtil.isNotEmpty(gaugeId))
        {
            clearGauge(gaugeId);
            gaugeObject = createGauge(this, gaugeId);
            drawGauge(gaugeObject);
        }
    }

    public void doOnClick()
    {
//        Info.display(gaugeId, "clicked");
        BaseEvent be = new BaseEvent(HGauges.GAUGE_CLICK_EVENT);
        be.setSource(this);
        this.fireEvent(GAUGE_CLICK_EVENT, be);
    }

    private native JavaScriptObject createGauge(HGauges ins, String gaugeId)/*-{
        var gOption = ins.@com.clarity.core.base.client.view.highcharts.gauge.HGauges::getJsGaugeOption()();
        var rOption = ins.@com.clarity.core.base.client.view.highcharts.gauge.HGauges::getJsGaugeRangeOption()();

        var gauge = new $wnd.HGauges({
            renderTo: gaugeId,
            width: gOption["width"],
            height: gOption["height"],
            value: gOption["minValue"],
            unit: gOption["unit"],
            min: gOption["minValue"],
            max: gOption["maxValue"],
            minAngle: -Math.PI*5/4,
            maxAngle: Math.PI/4,
            tickInterval: gOption["interval"],
            showText: gOption["showText"],
            showCurrentValue: gOption["showCurrentValue"],
            autoTextColor: gOption["autoTextColor"],
            ranges: rOption,
            innerSingleRange: gOption["innerSingleRange"]
        });
        
        return gauge;
    }-*/;

    private native void drawGauge(JavaScriptObject gauge)/*-{
        gauge.drawDial();
    }-*/;

    private native void addSelectionListener(HGauges ins, String gaugeId)/*-{
        $wnd.$('#'+gaugeId).click(function(){
            ins.@com.clarity.core.base.client.view.highcharts.gauge.HGauges::doOnClick()();
        });
    }-*/;

    private native void clearGauge(String gaugeId)/*-{
        $wnd.$('#'+gaugeId).empty();
    }-*/;

    private native void setGaugeValueJs(JavaScriptObject gauge, Double value)/*-{
        gauge.setValue(value);
    }-*/;

    private native Double getGaugeValueJs(JavaScriptObject gauge)/*-{
        return gauge.getValue();
    }-*/;

    @Override
    public void doOnRender(Element element, int i)
    {
        gaugeId = this.getId();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand()
        {
            @Override
            public void execute()
            {
                gaugeObject = createGauge(HGauges.this, gaugeId);
                addSelectionListener(HGauges.this, gaugeId);
                drawGauge(gaugeObject);
                if (preSetValue.isNaN())
                    setGaugeValueJs(gaugeObject, null);
                else
                    setGaugeValueJs(gaugeObject, preSetValue);
            }
        });
    }

    @Override
    public String getLabel()
    {
        return "HGauge";
    }
}
