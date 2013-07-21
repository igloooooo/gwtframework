package com.iglooit.core.base.client.model;

import com.clarity.core.base.client.view.highcharts.charts.ButtonOptions;
import com.clarity.core.base.client.view.highcharts.config.HighchartType;
import com.clarity.core.base.client.view.highcharts.data.PointDataArray;
import com.extjs.gxt.ui.client.data.BaseModelData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ChartModelData extends BaseModelData
{
    public static final String CHART_TYPE = "chart_type";
    public static final String X_AXIS_TITLE = "x_axis_title";
    public static final String Y_AXIS_TITLE = "y_axis_title";
    public static final String CHART_HEIGHT = "chart_height";
    public static final String CHART_WIDTH = "chart_width";
    public static final String CHART_TITLE = "chart_title";
    public static final String CHART_SUB_TITLE = "chart_sub_title";
    public static final String TITLE_FONT_SIZE = "title_font_size";
    public static final String STACKING = "stacking";
    public static final String SHARED = "shared";
    public static final String SHOW_PIE_CHART = "show_pie_chart";
    public static final String PIE_CHART_SIZE = "pie_chart_size";
    public static final String BUTTONS = "chart_buttons";
    public static final String SERIES_DATA = "series_data";

    public static final String COLLAPSIBLE = "collapsible";

    protected ChartModelData()
    {
        setCollapsible(false);
    }

    public void setChartType(HighchartType chartType)
    {
        set(CHART_TYPE, chartType);
    }

    public HighchartType getChartType()
    {
        return get(CHART_TYPE);
    }

    public void setXAxisTitle(final String title)
    {
        set(X_AXIS_TITLE, title);
    }

    public String getXAxisTitle()
    {
        return get(X_AXIS_TITLE);
    }

    public void setYAxisTitle(final String title)
    {
        set(Y_AXIS_TITLE, title);
    }

    public String getYAxisTitle()
    {
        return get(Y_AXIS_TITLE);
    }

    public void setChartHeight(Integer height)
    {
        set(CHART_HEIGHT, height);
    }

    public Integer getChartHeight()
    {
        return get(CHART_HEIGHT);
    }

    public void setChartWidth(Integer width)
    {
        set(CHART_WIDTH, width);
    }

    public Integer getChartWidth()
    {
        return get(CHART_WIDTH);
    }

    public void setChartTitle(String title)
    {
        set(CHART_TITLE, title);
    }

    public String getChartTitle()
    {
        return get(CHART_TITLE);
    }

    public void setChartSubTitle(String subTitle)
    {
        set(CHART_SUB_TITLE, subTitle);
    }

    public String getChartSubTitle()
    {
        return get(CHART_SUB_TITLE);
    }

    public void setShared(Boolean shared)
    {
        set(SHARED, shared);
    }

    public Boolean getShared()
    {
        return get(SHARED);
    }

    /**
     * @param stacking Sets the stacking option to "normal" in the series when true.
     */
    public void setStacking(Boolean stacking)
    {
        set(STACKING, stacking);
    }

    public Boolean isStacking()
    {
        return get(STACKING);
    }

    public void setShowPieChart(Boolean show)
    {
        set(SHOW_PIE_CHART, show);
    }

    public Boolean isShowPieChart()
    {
        return get(SHOW_PIE_CHART);
    }

    public List<ButtonOptions> getButtons()
    {
        return get(BUTTONS);
    }

    public void setButtons(List<ButtonOptions> buttons)
    {
        set(BUTTONS, buttons);
    }

    public void addButton(ButtonOptions button)
    {
        if (getButtons() == null)
            setButtons(new ArrayList<ButtonOptions>());

        getButtons().add(button);
    }

    public void setPieChartSize(String pieChartSize)
    {
        set(PIE_CHART_SIZE, pieChartSize);
    }

    public String getPieChartSize()
    {
        return get(PIE_CHART_SIZE);
    }

    public void setTitleFontSize(String titleFontSize)
    {
        set(TITLE_FONT_SIZE, titleFontSize);
    }

    public String getTitleFontSize()
    {
        return get(TITLE_FONT_SIZE);
    }

    public void setSeriesData(final Map<String, PointDataArray> series)
    {
        set(SERIES_DATA, series);
    }

    public Map<String, PointDataArray> getSeriesData()
    {
        return get(SERIES_DATA);
    }

    public void setCollapsible(boolean isCollapsible)
    {
        set(COLLAPSIBLE, isCollapsible);
    }

    public boolean isCollapsible()
    {
        return get(COLLAPSIBLE);
    }
}
