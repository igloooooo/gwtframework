package com.iglooit.core.base.client.view.highcharts.charts;

import com.clarity.core.base.client.view.highcharts.config.HighchartType;

import java.util.ArrayList;
import java.util.List;

public enum ChartType
{
    BAR("BAR_WIDGET_VIEW"),
    PIE("PIE_WIDGET_VIEW"),
    SCORE_CARD("SCORE_CARD_VIEW"),
    MULTI("MULTI_WIDGET_VIEW"),
    COLUMN("COLUMN_WIDGET_VIEW"),
    LINE("LINE_WIDGET_VIEW");

    private String id;

    ChartType(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public static List<ChartType> chartTypes()
    {
        List<ChartType> types = new ArrayList<ChartType>();
        types.add(BAR);
        types.add(PIE);
        types.add(SCORE_CARD);
        types.add(MULTI);
        types.add(COLUMN);
        types.add(LINE);

        return types;
    }

    public static ChartType getType(String id)
    {
        for (ChartType chartType : chartTypes())
        {
            if (id.equals(chartType.getId()))
                return chartType;
        }

        return COLUMN;
    }

    public HighchartType getHighChartType()
    {
        switch (this)
        {
            case PIE:
                return HighchartType.PIE;
            case BAR:
                return HighchartType.BAR;
            case LINE:
                return HighchartType.LINE;
            case COLUMN:
                return HighchartType.COLUMN;
            default:
                return HighchartType.COLUMN;
        }
    }
}
