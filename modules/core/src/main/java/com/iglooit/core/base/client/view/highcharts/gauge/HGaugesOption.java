package com.iglooit.core.base.client.view.highcharts.gauge;

import com.extjs.gxt.ui.client.data.BaseModel;

import java.util.ArrayList;
import java.util.List;

public class HGaugesOption extends BaseModel
{
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String MIN_VALUE = "minValue";
    public static final String MAX_VALUE = "maxValue";
    public static final String INTERVAL = "interval";
    public static final String UNIT = "unit";
    public static final String SHOW_TEXT = "showText";
    public static final String AUTO_TEXT_COLOR = "autoTextColor";

    public static final String RANGE_OPTIONS = "ranges";
    public static final String INNER_SIMGLE_RANGE = "innerSingleRange";

    public static final String SHOW_CURRENT_VALUE = "showCurrentValue";

    /*
    * default values
    * */
    public HGaugesOption()
    {
        set(MIN_VALUE, 0.0);
        set(MAX_VALUE, 0.0);
        set(INTERVAL, 0.0);
        set(WIDTH, 200);
        set(HEIGHT, 200);
        set(UNIT, "");
        set(SHOW_TEXT, Boolean.TRUE);
        set(SHOW_CURRENT_VALUE, Boolean.TRUE);
        set(AUTO_TEXT_COLOR, Boolean.TRUE);
        setDefaultRange();
    }

    public void setShowText(boolean show)
    {
        set(SHOW_TEXT, show);
    }

    /* default range will be set in .js */
    public void setDefaultRange()
    {
        List<HGaugesRangeOption> ranges = new ArrayList<HGaugesRangeOption>();
        set(RANGE_OPTIONS, ranges);
    }

    public void setMinMaxInterval(Double min, Double max, Double interval)
    {
        set(MIN_VALUE, min);
        set(MAX_VALUE, max);
        set(INTERVAL, interval);
    }

    public void setGaugeUnit(String unit)
    {
        set(UNIT, unit);
    }

    public void setRangeOptions(List<HGaugesRangeOption> ranges)
    {
        set(RANGE_OPTIONS, ranges);
    }

    public void setInnerSingleRange(HGaugesRangeOption range)
    {
        set(INNER_SIMGLE_RANGE, range);
    }

    public void setSize(Integer width, Integer height)
    {
        set(WIDTH, width);
        set(HEIGHT, height);
    }

    public Double getMinValue()
    {
        return get(MIN_VALUE);
    }

    public Double getMaxValue()
    {
        return get(MAX_VALUE);
    }

    public Double getInterval()
    {
        return get(INTERVAL);
    }

    public Double getGaugeSize()
    {
        return ((Integer)get(HEIGHT)).doubleValue();
    }

    public List<HGaugesRangeOption> getRanges()
    {
        return get(RANGE_OPTIONS);
    }

    public HGaugesRangeOption getInnerSingleRange()
    {
        return get(INNER_SIMGLE_RANGE);
    }

    /**
     * show/hide the embed current value in the hgauge chart
     *
     * @param show
     */
    public void setShowCurrentValue(Boolean show)
    {
        set(SHOW_CURRENT_VALUE, show);
    }
}
