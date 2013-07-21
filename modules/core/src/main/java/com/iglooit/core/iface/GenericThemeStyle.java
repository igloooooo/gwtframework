package com.iglooit.core.iface;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 30/04/13 3:21 PM
 */
public class GenericThemeStyle
{
    private String borderColor;
    private String background;
    private static final String TEXT = "#ffffff";

    /**
     * @param borderColor this value can be rgb or hex, e.g. rgb(0,0,0) or #000000
     * @param background this is typically applied to a CSS style, so it can anything the 'background' attribute
     *                   will accept
     */
    public GenericThemeStyle(String borderColor, String background)
    {
        this.borderColor = borderColor;
        this.background = background;
    }


    public String getBackground()
    {
        return background;
    }


    public String getTextColor()
    {
        return TEXT;
    }

    public String getBorderColor()
    {
        return borderColor;
    }
}