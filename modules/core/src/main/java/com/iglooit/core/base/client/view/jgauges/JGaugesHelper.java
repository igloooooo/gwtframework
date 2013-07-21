package com.iglooit.core.base.client.view.jgauges;

import com.clarity.commons.iface.type.AppX;
import com.google.gwt.core.client.JavaScriptObject;

public class JGaugesHelper
{
    public static void applyOptionToGauge(JavaScriptObject gauge, JGaugesOption option)
    {
        if (gauge == null || option == null)
            return;

        /*
        * set gauge mode
        *
        * what mode provides you are basic gauge parameters
        * e.g. width/height, bg image/needle image and also some radius info
        * */
        JGaugesOption.GaugeMode mode = option.getMode();
        switch (mode)
        {
            case MODE1:
                setGaugeMode1(gauge);
                break;
            case MODE2:
                setGaugeMode2(gauge);
                break;
            case MODE3:
                setGaugeMode3(gauge);
                break;
            default:
                break;
        }

        /*
        * set gauge default options
        * */
        applyTicksToGauge(gauge, option.getTicksOption(), false);
        applyRangeToGauge(gauge, option.getRangeOption(), false);
    }

    public static void updateGaugeTicks(JGauges gauge, JGaugesTicksOption ticksOption)
    {
        if (gauge == null || ticksOption == null)
            throw new AppX("Fail to update tick value for Gauge, null object found.");
        gauge.getGaugeOption().setTicksOption(ticksOption);
        applyTicksToGauge(gauge.getGaugeObject(), ticksOption, true);
    }

    public static void updateGaugeRange(JGauges gauge, JGaugesRangeOption rangeOption)
    {
        if (gauge == null || rangeOption == null)
            throw new AppX("Fail to update range value for Gauge, null object found.");
        gauge.getGaugeOption().setRangeOption(rangeOption);
        applyRangeToGauge(gauge.getGaugeObject(), rangeOption, true);
    }

    private static void applyTicksToGauge(JavaScriptObject gauge,
                                          JGaugesTicksOption ticksOption,
                                          boolean isUpdate)
    {
        setGaugeTickJs(gauge, ticksOption.getMin(),
            ticksOption.getMax(), ticksOption.getCount());
        if (isUpdate)
            updateGaugeTickJs(gauge);
    }

    private static void applyRangeToGauge(JavaScriptObject gauge,
                                          JGaugesRangeOption rangeOption,
                                          boolean isUpdate)
    {
        setGaugeRangeJs(gauge, rangeOption.getStartAngle(),
            rangeOption.getEndAngle(), rangeOption.getColorOption().generateColorStr());
        if (isUpdate)
            updateGaugeRangeJs(gauge);
    }

    private static native  void setGaugeRangeJs(JavaScriptObject gauge,
                                              Integer startR,
                                              Integer endR,
                                              String color)/*-{
        gauge.range.start = startR;
        gauge.range.end = endR;
        gauge.range.color = color;
    }-*/;

    private static native void updateGaugeRangeJs(JavaScriptObject gauge)/*-{
        gauge.updateRange();
    }-*/;

    /* seems like we cannot update min and max at the same time
    * do 2 updates if you want */
    private static native void setGaugeTickJs(JavaScriptObject gauge,
                                              Double tickMin,
                                              Double tickMax,
                                              Integer tickCount)/*-{
        <!--gauge.ticks.start = tickMin;-->
        gauge.ticks.end = tickMax;
        gauge.ticks.count = tickCount;
    }-*/;

    private static native void updateGaugeTickJs(JavaScriptObject gauge)/*-{
        gauge.updateTicks();
    }-*/;

    private static native void setGaugeMode1(JavaScriptObject gauge)/*-{
        gauge.width = 90;
        gauge.height = 240;
        gauge.ticks.count = 4;
        gauge.ticks.start = -50;
        gauge.ticks.end = 150;
        gauge.ticks.radius = 30;
        gauge.ticks.labelRadius = 25;
    }-*/;

    private static native void setGaugeMode2(JavaScriptObject gauge)/*-{
        gauge.width = 160;
        gauge.height = 50;
        gauge.ticks.start = 0;
        gauge.ticks.end = 100;

        gauge.needle.yOffset = 0;
        gauge.label.yOffset = 0;

        gauge.imagePath = 'resources/images/marker.png';
        gauge.needle.imagePath = 'resources/images/arrow_right.png';
    }-*/;

    private static native void setGaugeMode3(JavaScriptObject gauge)/*-{
        gauge.width = 160;
        gauge.height = 114;
        gauge.label.suffix = ' P';
        gauge.ticks.start = 0;
        gauge.ticks.end = 100;

        gauge.imagePath = 'resources/images/marker.png';
        gauge.needle.imagePath = 'resources/images/arrow_right.png';
    }-*/;
}
