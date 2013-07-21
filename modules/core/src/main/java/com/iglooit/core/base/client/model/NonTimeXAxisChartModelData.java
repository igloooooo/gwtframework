package com.iglooit.core.base.client.model;

import com.clarity.core.base.client.view.highcharts.config.HighchartType;
import com.clarity.core.base.client.view.highcharts.data.PointDataArray;

import java.util.List;
import java.util.Map;

public class NonTimeXAxisChartModelData extends ChartModelData
{
    public static final String X_CATEGORIES = "x_categories";
    public static final String SHOW_PERCENTAGE = "is_show_percentage";
    public static final String POINT_STYLE_DATA = "pointStyleSeriesData";

    public NonTimeXAxisChartModelData()
    {
        set(CHART_TYPE, HighchartType.COLUMN);
        set(X_AXIS_TITLE, "");
        set(Y_AXIS_TITLE, "");

        set(CHART_HEIGHT, 500);
        set(CHART_WIDTH, 1000);
        set(SHARED, Boolean.FALSE);
        set(CHART_TITLE, "");
        set(CHART_SUB_TITLE, "");

        set(STACKING, Boolean.FALSE);
        set(SHOW_PERCENTAGE, Boolean.FALSE);
        set(SHOW_PIE_CHART, Boolean.FALSE);
    }

    public NonTimeXAxisChartModelData(HighchartType type)
    {
        this();
        set(CHART_TYPE, type);
    }

    public void setSeriesData(Map<String, PointDataArray> series)
    {
        super.setSeriesData(series);
        set(POINT_STYLE_DATA, true);
    }

    public void setXCategories(List<String> categories)
    {
        set(X_CATEGORIES, categories);
    }

    public List<String> getXCategories()
    {
        return get(X_CATEGORIES);
    }

    public void setShowPercentage(Boolean isShowPercentage)
    {
        set(SHOW_PERCENTAGE, isShowPercentage);
    }

    public Boolean isShowPercentage()
    {
        return get(SHOW_PERCENTAGE);
    }

    public boolean isPointStyleSeriesData()
    {
        return get(POINT_STYLE_DATA);
    }
}
