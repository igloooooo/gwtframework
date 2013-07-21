package com.iglooit.core.base.iface.domain;


import com.extjs.gxt.ui.client.data.BaseModel;

public class HighChartPointModel extends BaseModel
{
    public static final String X = "x";
    public static final String Y = "y";
    public static final String SUM = "total";

    public HighChartPointModel()
    {

    }

    public HighChartPointModel(String x, Double y)
    {
        setX(x);
        setY(y);
    }

    public HighChartPointModel(String x, Double y, Double sum)
    {
        setX(x);
        setY(y);
        setTotal(sum);
    }


    public void setX(String x)
    {
        set(X, x);
    }

    public void setY(Double y)
    {
        set(Y, y);
    }

    public String getX()
    {
        return get(X);
    }

    public Double getY()
    {
        return get(Y);
    }

    public Double getTotal()
    {
        return get(SUM);
    }


    public void setTotal(Double total)
    {
        set(SUM, total);
    }


}
