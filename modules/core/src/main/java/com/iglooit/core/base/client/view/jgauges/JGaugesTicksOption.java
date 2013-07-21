package com.iglooit.core.base.client.view.jgauges;

/*
* This option class is in charge for tick update for jGauges
*
* all parameters must be set to update tick values
*  1. start(min)
*  2. end(max)
*  3. count(number of ticks
* */
public class JGaugesTicksOption
{
    private Double min;
    private Double max;
    private Integer count;

    /*
    * set default to be
    * min = 0;
    * max = 100;
    * count = 5;
    * */
    public JGaugesTicksOption()
    {
        min = -50.0;
        max = 50.0;
        count = 5;
    }

    public JGaugesTicksOption(Double min, Double max, Integer count)
    {
        this.min = min;
        this.max = max;
        this.count = count;
    }

    public Double getMin()
    {
        return min;
    }

    public void setMin(Double min)
    {
        this.min = min;
    }

    public Double getMax()
    {
        return max;
    }

    public void setMax(Double max)
    {
        this.max = max;
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }
}
