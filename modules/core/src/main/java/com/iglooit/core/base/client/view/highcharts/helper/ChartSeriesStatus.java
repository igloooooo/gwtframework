package com.iglooit.core.base.client.view.highcharts.helper;

public class ChartSeriesStatus
{
    private String name;
    private String color;
    private boolean visible = true;

    public ChartSeriesStatus(String name, String color, boolean visible)
    {
        this.name = name;
        this.color = color;
        this.visible = visible;
    }

    public ChartSeriesStatus()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
