package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

import java.util.List;

public class XAxisOptions extends ChartConfig
{
    public static final String ALLOWDECIMALS = "allowDecimals";
    public static final String ALTERNATEGRIDCOLOR = "alternateGridColor";
    public static final String ENDONTICK = "endOnTick";
    public static final String GRIDLINECOLOR = "gridLineColor";
    public static final String GRIDLINEDASHSTYLE = "gridLineDashStyle";
    public static final String GRIDLINEWIDTH = "gridLineWidth";
    public static final String ID = "id";
    public static final String LINECOLOR = "lineColor";
    public static final String LINEWIDTH = "lineWidth";
    public static final String LINKEDTO = "linkedTo";
    public static final String MAX = "max";
    public static final String MAXPADDING = "maxPadding";
    public static final String MAXZOOM = "maxZoom";
    public static final String MIN = "min";
    public static final String MINORGRIDLINECOLOR = "minorGridLineColor";
    public static final String MINORGRIDLINEDASHSTYLE = "minorGridLineDashStyle";
    public static final String MINORGRIDLINEWIDTH = "minorGridLineWidth";
    public static final String MINORTICKCOLOR = "minorTickColor";
    public static final String MINORTICKINTERVAL = "minorTickInterval";
    public static final String MINORTICKLENGTH = "minorTickLength";
    public static final String MINORTICKPOSITION = "minorTickPosition";
    public static final String MINORTICKWIDTH = "minorTickWidth";
    public static final String MINPADDING = "minPadding";
    public static final String OFFSET = "offset";
    public static final String OPPOSITE = "opposite";
    public static final String REVERSED = "reversed";
    public static final String SHOWFIRSTLABEL = "showFirstLabel";
    public static final String SHOWLASTLABEL = "showLastLabel";
    public static final String STARTOFWEEK = "startOfWeek";
    public static final String STARTONTICK = "startOnTick";
    public static final String TICKCOLOR = "tickColor";
    public static final String TICKINTERVAL = "tickInterval";
    public static final String TICKLENGTH = "tickLength";
    public static final String TICKMARKPLACEMENT = "tickmarkPlacement";
    public static final String TICKPIXELINTERVAL = "tickPixelInterval";
    public static final String TICKPOSITION = "tickPosition";
    public static final String TICKWIDTH = "tickWidth";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String CATEGORIES = "categories";
    public static final String LABELS = "labels";

    public static final String ENABLED = "enabled";

    public XAxisOptions()
    {
        this(true);
    }

    public XAxisOptions(boolean preConfigure)
    {
        if (!preConfigure)
            return;

        set(ALLOWDECIMALS, Boolean.TRUE);

        set(ENDONTICK, Boolean.FALSE);
        set(GRIDLINECOLOR, "#C0C0C0");
        set(GRIDLINEDASHSTYLE, "ShortDot");
        set(GRIDLINEWIDTH, 0);
        
        set(LINECOLOR, "#C0D0E0");
        set(LINEWIDTH, 1);


        set(MAXPADDING, 0.01);


        set(MINORGRIDLINECOLOR, "#E0E0E0");
        set(MINORGRIDLINEDASHSTYLE, "Solid");
        set(MINORGRIDLINEWIDTH, 1);
        set(MINORTICKCOLOR, "#A0A0A0");

        set(MINORTICKLENGTH, 2);
        set(MINORTICKPOSITION, "outside");
        set(MINORTICKWIDTH, 0);
        set(MINPADDING, 0.01);
        set(OFFSET, 0);
        set(OPPOSITE, Boolean.FALSE);
        set(REVERSED, Boolean.FALSE);
        set(SHOWFIRSTLABEL, Boolean.TRUE);
        set(SHOWLASTLABEL, Boolean.FALSE);
        set(STARTOFWEEK, 1);
        set(STARTONTICK, Boolean.FALSE);
        set(TICKCOLOR, "#C0D0E0");

        set(TICKLENGTH, 5);
        set(TICKMARKPLACEMENT, "between");
        set(TICKINTERVAL, null);
        set(TICKPOSITION, "outside");
        set(TICKWIDTH, 1);
        set(TYPE, "linear");
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public String[] getCategories()
    {
        JsArrayString arr = get(CATEGORIES);
        String[] category = new String[arr.length()];

        for (int i = 0; i < arr.length(); ++i)
        {
            category[i] = arr.get(i);
        }

        return category;
    }
    public void setCategories(String[] categories)
    {
        JsArrayString arr = JavaScriptObject.createArray().cast();
        for (String s : categories)
        {
            arr.push(s);
        }

        set(CATEGORIES, arr);
    }
    public void setCategories(List<String> categories)
    {
        set(CATEGORIES, categories);
    }

    public ChartTitleOptions getTitle()
    {
        return (ChartTitleOptions)get(TITLE);
    }

    public void setTitle(ChartTitleOptions title)
    {
        set(TITLE, title);
    }
    public boolean getAllowDecimals()
    {
        return (Boolean)get(ALLOWDECIMALS);
    }
    public void setAllowDecimals(boolean data)
    {
        set(ALLOWDECIMALS, data);
    }
    public String getAlternateGrIDColor()
    {
        return get(ALTERNATEGRIDCOLOR);
    }
    public void setAlternateGridColor(String data)
    {
        set(ALTERNATEGRIDCOLOR, data);
    }
    public boolean getEndOnTick()
    {
        return (Boolean)get(ENDONTICK);
    }
    public void setEndOnTick(boolean data)
    {
        set(ENDONTICK, data);
    }
    public String getGrIDLineColor()
    {
        return get(GRIDLINECOLOR);
    }
    public void setGridLineColor(String data)
    {
        set(GRIDLINECOLOR, data);
    }
    public String getGrIDLineDashStyle()
    {
        return get(GRIDLINEDASHSTYLE);
    }
    public void setGridLineDashStyle(String data)
    {
        set(GRIDLINEDASHSTYLE, data);
    }
    public int getGrIDLineWIDth()
    {
        return (Integer)get(GRIDLINEWIDTH);
    }
    public void setGridLineWidth(int data)
    {
        set(GRIDLINEWIDTH, data);
    }
    public String getId()
    {
        return get(ID);
    }
    public void setId(String data)
    {
        set(ID, data);
    }
    public String getLineColor()
    {
        return get(LINECOLOR);
    }
    public void setLineColor(String data)
    {
        set(LINECOLOR, data);
    }
    public int getLineWIDth()
    {
        return (Integer)get(LINEWIDTH);
    }
    public void setLineWidth(int data)
    {
        set(LINEWIDTH, data);
    }
    public String getLinkedTo()
    {
        return get(LINKEDTO);
    }
    public void setLinkedTo(String data)
    {
        set(LINKEDTO, data);
    }
    public int getMax()
    {
        return (Integer)get(MAX);
    }
    public void setMax(int data)
    {
        set(MAX, data);
    }
    public int getMaxPadding()
    {
        return (Integer)get(MAXPADDING);
    }
    public void setMaxPadding(int data)
    {
        set(MAXPADDING, data);
    }
    public int getMaxZoom()
    {
        return (Integer)get(MAXZOOM);
    }
    public void setMaxZoom(int data)
    {
        set(MAXZOOM, data);
    }
    public int getMin()
    {
        return (Integer)get(MIN);
    }
    public void setMin(int data)
    {
        set(MIN, data);
    }
    public String getMinorGrIDLineColor()
    {
        return get(MINORGRIDLINECOLOR);
    }
    public void setMinorGridLineColor(String data)
    {
        set(MINORGRIDLINECOLOR, data);
    }
    public String getMinorGrIDLineDashStyle()
    {
        return get(MINORGRIDLINEDASHSTYLE);
    }
    public void setMinorGridLineDashStyle(String data)
    {
        set(MINORGRIDLINEDASHSTYLE, data);
    }
    public int getMinorGrIDLineWIDth()
    {
        return (Integer)get(MINORGRIDLINEWIDTH);
    }
    public void setMinorGridLineWidth(int data)
    {
        set(MINORGRIDLINEWIDTH, data);
    }
    public String getMinorTickColor()
    {
        return get(MINORTICKCOLOR);
    }
    public void setMinorTickColor(String data)
    {
        set(MINORTICKCOLOR, data);
    }
    public int getMinorTickInterval()
    {
        return (Integer)get(MINORTICKINTERVAL);
    }
    public void setMinorTickInterval(int data)
    {
        set(MINORTICKINTERVAL, data);
    }
    public int getMinorTickLength()
    {
        return (Integer)get(MINORTICKLENGTH);
    }
    public void setMinorTickLength(int data)
    {
        set(MINORTICKLENGTH, data);
    }
    public String getMinorTickPosition()
    {
        return get(MINORTICKPOSITION);
    }
    public void setMinorTickPosition(String data)
    {
        set(MINORTICKPOSITION, data);
    }
    public int getMinorTickWIDth()
    {
        return (Integer)get(MINORTICKWIDTH);
    }
    public void setMinorTickWidth(int data)
    {
        set(MINORTICKWIDTH, data);
    }
    public int getMinPadding()
    {
        return (Integer)get(MINPADDING);
    }
    public void setMinPadding(int data)
    {
        set(MINPADDING, data);
    }
    public int getOffset()
    {
        return (Integer)get(OFFSET);
    }
    public void setOffset(int data)
    {
        set(OFFSET, data);
    }
    public boolean getOpposite()
    {
        return (Boolean)get(OPPOSITE);
    }
    public void setOpposite(boolean data)
    {
        set(OPPOSITE, data);
    }
    public boolean getReversed()
    {
        return (Boolean)get(REVERSED);
    }
    public void setReversed(boolean data)
    {
        set(REVERSED, data);
    }
    public boolean getShowFirstLabel()
    {
        return (Boolean)get(SHOWFIRSTLABEL);
    }
    public void setShowFirstLabel(boolean data)
    {
        set(SHOWFIRSTLABEL, data);
    }
    public boolean getShowLastLabel()
    {
        return (Boolean)get(SHOWLASTLABEL);
    }
    public void setShowLastLabel(boolean data)
    {
        set(SHOWLASTLABEL, data);
    }
    public int getStartOfWeek()
    {
        return (Integer)get(STARTOFWEEK);
    }
    public void setStartOfWeek(int data)
    {
        set(STARTOFWEEK, data);
    }
    public boolean getStartOnTick()
    {
        return (Boolean)get(STARTONTICK);
    }
    public void setStartOnTick(boolean data)
    {
        set(STARTONTICK, data);
    }
    public String getTickColor()
    {
        return get(TICKCOLOR);
    }
    public void setTickColor(String data)
    {
        set(TICKCOLOR, data);
    }
    public Long getTickInterval()
    {
        return (Long)get(TICKINTERVAL);
    }
    public void setTickInterval(Long data)
    {
        set(TICKINTERVAL, data);
    }
    public int getTickLength()
    {
        return (Integer)get(TICKLENGTH);
    }
    public void setTickLength(int data)
    {
        set(TICKLENGTH, data);
    }
    public String getTickmarkPlacement()
    {
        return get(TICKMARKPLACEMENT);
    }

    /**
     * parameter data accepts "between" and "on"
     * @param data
     */
    public void setTickmarkPlacement(String data)
    {
        set(TICKMARKPLACEMENT, data);
    }
    public int getTickPixelInterval()
    {
        return (Integer)get(TICKPIXELINTERVAL);
    }
    public void setTickPixelInterval(int data)
    {
        set(TICKPIXELINTERVAL, data);
    }
    public String getTickPosition()
    {
        return get(TICKPOSITION);
    }
    public void setTickPosition(String data)
    {
        set(TICKPOSITION, data);
    }
    public int getTickWIDth()
    {
        return (Integer)get(TICKWIDTH);
    }
    public void setTickWidth(int data)
    {
        set(TICKWIDTH, data);
    }
    public String getType()
    {
        return get(TYPE);
    }
    public void setType(String data)
    {
        set(TYPE, data);
    }

    public AxisLabel getLabels()
    {
        return get(LABELS);
    }

    public void setLabels(AxisLabel data)
    {
        set(LABELS, data);
    }
}
