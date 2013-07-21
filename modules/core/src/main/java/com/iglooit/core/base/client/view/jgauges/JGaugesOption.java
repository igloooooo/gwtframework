package com.iglooit.core.base.client.view.jgauges;

public class JGaugesOption
{
    /* general paras */
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String VALUE = "value";
    public static final String SEGMENT_START = "segmentStart";
    public static final String SEGMENT_END = "segmentEnd";
    public static final String IMAGE_PATH = "imagePath";
    public static final String SHOW_ALERTS = "showAlerts";
    public static final String AUTO_PERFIX = "autoPrefix";
    public static final String SI_PERFIX = "siPrefixes";
    public static final String BINARY_PERFIX = "binaryPrefixes";

    /* needle paras */
    public static final String NEEDLE_IMAGE_PATH = "needle.imagePath";
    public static final String NEEDLE_LIMIT_ACTION = "needle.limitAction";
    public static final String NEEDLE_X_OFFSET = "needle.xOffset";
    public static final String NEEDLE_Y_OFFSET = "needle.yOffset";

    /* label paras */
    public static final String LABEL_COLOR = "label.color";
    public static final String LABEL_X_OFFSET = "label.xOffset";
    public static final String LABEL_Y_OFFSET = "label.yOffset";
    public static final String LABEL_PREFIX = "label.prefix";
    public static final String LABEL_SUFFIX = "label.suffix";
    public static final String LABEL_PRECISION = "label.precision";

    /* tick paras */
    public static final String TICK_COUNT = "ticks.count";
    public static final String TICK_START = "ticks.start";
    public static final String TICK_END = "ticks.end";
    public static final String TICK_COLOR = "ticks.color";
    public static final String TICK_THICKNESS = "ticks.thickness";
    public static final String TICK_RADIUS = "ticks.radius";
    public static final String TICK_LABEL_COLOR = "ticks.labelColor";
    public static final String TICK_LABEL_PRECISION = "ticks.labelPrecision";
    public static final String TICK_LABEL_RADIUS = "ticks.labelRadius";

    /* range paras */
    public static final String RANGE_RADIUS = "range.radius";
    public static final String RANGE_THICKNESS = "range.thickness";
    public static final String RANGE_START = "range.start";
    public static final String RANGE_END = "range.end";
    public static final String RANGE_COLOR = "range.color";

    private GaugeMode mode;
    private JGaugesTicksOption ticksOption;
    private JGaugesRangeOption rangeOption;

    /*
    * default paras:
    *
    * height = 114
    * width = 160
    *
    * */
    public JGaugesOption()
    {
        setMode(GaugeMode.DEFAULT);
        setTicksOption(new JGaugesTicksOption());
        setRangeOption(new JGaugesRangeOption());
    }

    public JGaugesRangeOption getRangeOption()
    {
        return rangeOption;
    }

    public void setRangeOption(JGaugesRangeOption rangeOption)
    {
        this.rangeOption = rangeOption;
    }

    public JGaugesTicksOption getTicksOption()
    {
        return ticksOption;
    }

    public void setTicksOption(JGaugesTicksOption ticksOption)
    {
        this.ticksOption = ticksOption;
    }

    public GaugeMode getMode()
    {
        return mode;
    }

    public void setMode(GaugeMode mode)
    {
        this.mode = mode;
    }

    public static enum GaugeMode
    {
        DEFAULT,
        MODE1,
        MODE2,
        MODE3;
    }
}
