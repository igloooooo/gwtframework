package com.iglooit.core.base.client.view.jgauges;

public class JGaugesColorOption
{
    private Integer rgbRed;
    private Integer rgbGreen;
    private Integer rgbBlue;
    private Double alpha;

    /*
    * default color is just black with alpha = 1
    * */
    public JGaugesColorOption()
    {
        rgbRed = 255;
        rgbBlue = 255;
        rgbGreen = 255;
        alpha = 1.0;
    }

    public JGaugesColorOption(Integer red, Integer green,
                              Integer blue, Double a)
    {
        this.rgbRed = red;
        this.rgbGreen = green;
        this.rgbBlue = blue;
        this.alpha = a;

        if (rgbRed == null || rgbRed < 0)
            rgbRed = 0;
        if (rgbRed > 255)
            rgbRed = 255;

        if (rgbBlue == null || rgbBlue < 0)
            rgbBlue = 0;
        if (rgbBlue > 255)
            rgbBlue = 255;

        if (rgbGreen == null || rgbGreen < 0)
            rgbGreen = 0;
        if (rgbGreen > 255)
            rgbGreen = 255;

        if (alpha == null || alpha < 0)
            alpha = 0.0;
        if (alpha > 1)
            alpha = 1.0;
    }

    public String generateColorStr()
    {
        return "'rgba(" + rgbRed.toString() + ", " + rgbGreen.toString() + ", " +
            rgbBlue.toString() + ", " + alpha.toString() + ")'";
    }
}
