package com.iglooit.core.base.client.view.highcharts.handlers;

import com.clarity.core.base.client.view.highcharts.data.Point;

public interface TooltipFormatter
{
    String format(Point point);
}
