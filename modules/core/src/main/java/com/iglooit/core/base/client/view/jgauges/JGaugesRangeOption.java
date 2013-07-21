package com.iglooit.core.base.client.view.jgauges;

/*
* This option class is in charge for range update for jGauges
*
* all parameters must be set to update tick values
*  1. start
*  2. end
*  3. color
* */
public class JGaugesRangeOption
{
    private Integer startAngle;
    private Integer endAngle;
    private JGaugesColorOption colorOption;

    /*
    * default range to -10 - 10 with default color
    * */
    public JGaugesRangeOption()
    {
        startAngle = -10;
        endAngle = 10;
        colorOption = new JGaugesColorOption(255, 32, 0, 0.2);
    }

    public JGaugesRangeOption(Integer startAngle, Integer endAngle,
                              JGaugesColorOption colorOption)
    {
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.colorOption = colorOption;
    }

    public Integer getStartAngle()
    {
        return startAngle;
    }

    public Integer getEndAngle()
    {
        return endAngle;
    }

    public JGaugesColorOption getColorOption()
    {
        return colorOption;
    }

    public void setStartAngle(Integer startAngle)
    {
        this.startAngle = startAngle;
    }

    public void setEndAngle(Integer endAngle)
    {
        this.endAngle = endAngle;
    }

    public void setColorOption(JGaugesColorOption colorOption)
    {
        this.colorOption = colorOption;
    }
}
