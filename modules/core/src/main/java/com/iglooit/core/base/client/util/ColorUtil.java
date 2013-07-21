package com.iglooit.core.base.client.util;

import com.clarity.core.base.client.view.FadeHighlightEffect;
import com.extjs.gxt.ui.client.widget.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorUtil
{
    public static final List<String> COLOR_SEVERITY_SAFE_LIST = Arrays.asList(
        new String[]{
            "#30978A", "#9D6B8F", "#4299C7", "#A6AF38", "#D85678", "#9D8169", "#EDA35C",
            "#1A7A7F", "#726595", "#0976A2", "#CABA35", "#BC3F63", "#725E58", "#D56B37",
            "#006C53", "#55405F", "#175E72", "#808161", "#891D47", "#594C3D", "#D85E42",
            "#B7C789", "#B697B7", "#7DA7D9", "#ACCBA5", "#F79980", "#BCA68E", "#F9C499",
            "#CBDFE0", "#BBCACD", "#B7D9F4", "#F8EC8A", "#F4CCC5", "#E9D9BF", "#FEE18A",
        });
    public static final String COLOR_RED = "#CE2D20";
    public static final String COLOR_ORANGE = "#F38417";
    public static final String COLOR_YELLOW = "#EBD300";
    public static final String COLOR_PURPLE = "#903B96";
    public static final String COLOR_INDIGO = "#594097";
    public static final String COLOR_PINK = "#EE5FB9";
    public static final String COLOR_BLUE = "#0F83D2";
    public static final String COLOR_GREEN = "#1DB12F";

    public static final List<String> COLOR_RED_BLUE = Arrays.asList(COLOR_RED, COLOR_BLUE);
    public static final List<String> COLOR_SEVERITY_LIST = Arrays.asList(COLOR_RED, COLOR_ORANGE, COLOR_YELLOW,
        COLOR_PURPLE, COLOR_INDIGO, COLOR_PINK, COLOR_BLUE, COLOR_GREEN);
    public static final List<String> COLOR_FULL_LIST = new ArrayList<String>();

    static
    {
        COLOR_FULL_LIST.addAll(COLOR_SEVERITY_LIST);
        COLOR_FULL_LIST.addAll(COLOR_SEVERITY_SAFE_LIST);
    }

    private static final String HEX_REG_EPR = "^#[0-9ABCDEFabcdef]*";
    private static final double DARKER_FACTOR = 0.80;

    public static String getColorCode(String colorName)
    {
        if (colorName.matches(HEX_REG_EPR))
        {
            // code already
            return colorName;
        }
        else
        {
            // its a color name , return grey if name does not recognize
            ClarityColorConstants color = ClarityColorConstants.getByName(colorName);
            if (color == null)
                return ClarityColorConstants.GRAY.getHexCode(); //better throw exception here!
            else
                return color.getHexCode();
        }
    }

    public static String getMeaningfulColorName(String value)
    {
        // if its hex code. translate code to color
        ClarityColorConstants color = null;
        if (value.matches(HEX_REG_EPR))
            color = ClarityColorConstants.getByHexCode(value);
        else
            color = ClarityColorConstants.getByName(value);

        return color == null ? value : color.getMeaningfulName();
    }


    // Returns a darker version of this color for border.
    private static String darkerColor(String color)
    {
        if (color.matches(HEX_REG_EPR))
        {
            int red = Integer.valueOf(color.substring(1, 3), 16);
            int green = Integer.valueOf(color.substring(3, 5), 16);
            int blue = Integer.valueOf(color.substring(5, 7), 16);

            red = Math.max((int)(red * DARKER_FACTOR), 0);
            green = Math.max((int)(green * DARKER_FACTOR), 0);
            blue = Math.max((int)(blue * DARKER_FACTOR), 0);
            return "#" + convertHex(red) + convertHex(green) + convertHex(blue);
        }
        else
        {
            return "#000000";
        }
    }

    /**
     * if input color is not recognizable, output default to grey color
     */
    public static String getDarkerColor(String color, Double factor)
    {
        if (factor >= 1)
            return color;

        String colorCode = getColorCode(color);

        int red = Integer.valueOf(colorCode.substring(1, 3), 16);
        int green = Integer.valueOf(colorCode.substring(3, 5), 16);
        int blue = Integer.valueOf(colorCode.substring(5, 7), 16);

        red = Math.max((int)(red * factor), 0);
        green = Math.max((int)(green * factor), 0);
        blue = Math.max((int)(blue * factor), 0);
        return "#" + convertHex(red) + convertHex(green) + convertHex(blue);
    }

    public static String getLighterColor(String color, Double factor)
    {
        if (factor <= 1)
            return color;

        String colorCode = getColorCode(color);

        int red = Integer.valueOf(colorCode.substring(1, 3), 16);
        int green = Integer.valueOf(colorCode.substring(3, 5), 16);
        int blue = Integer.valueOf(colorCode.substring(5, 7), 16);

        red = Math.min((int)(red * factor), 255);
        green = Math.min((int)(green * factor), 255);
        blue = Math.min((int)(blue * factor), 255);
        return "#" + convertHex(red) + convertHex(green) + convertHex(blue);
    }

    /**
     * flag - force black or white
     */
    public static String getInverseColor(String color, boolean forceBW)
    {
        String colorCode = getColorCode(color);

        int red = Integer.valueOf(colorCode.substring(1, 3), 16);
        int green = Integer.valueOf(colorCode.substring(3, 5), 16);
        int blue = Integer.valueOf(colorCode.substring(5, 7), 16);

        red = 255 - red;
        green = 255 - green;
        blue = 255 - blue;

        if (!forceBW)
            return "#" + convertHex(red) + convertHex(green) + convertHex(blue);
        else
        {
            int exColor = 0;
            if (red + green + blue > (255 * 3) / 2)
                exColor = 255;
            return "#" + convertHex(exColor) + convertHex(exColor) + convertHex(exColor);
        }
    }

    private static String convertHex(int decimal)
    {
        String hex = Integer.toHexString(decimal);
        if (hex.length() == 1)
        {
            hex = hex + "0";
        }
        return hex;
    }

    public static String getStyleForColor(String color)
    {
        String code = getColorCode(color);
        return "background-color: " + code + ";" +
            "border-color: " + darkerColor(code);
    }

    /**
     * This method takes a component and applies a highlight-fade effect to it. By default, the background colour of the
     * component is set to a light yellow colour which fades to white (then transparent) after a short period.
     * This effect is ideal for temporarily highlighting a component.
     *
     * @param component
     */
    public static void highlightFadeComponent(Component component)
    {
        String startBackgroundColor = "FFFCD5";
        String endBackgroundColor = "FFFFFF";
        int duration = 5000;
        FadeHighlightEffect.highlight(component.el(), "background-color",
            startBackgroundColor, endBackgroundColor, duration, true);
    }

    public static String getOpacityColor(String color, Double opacity)
    {
        if (opacity >= 1)
            return color;

        String colorCode = getColorCode(color);

        int red = Integer.valueOf(colorCode.substring(1, 3), 16);
        int green = Integer.valueOf(colorCode.substring(3, 5), 16);
        int blue = Integer.valueOf(colorCode.substring(5, 7), 16);

        return "rgba(" + red + ", " + green + ", " + blue + ", " + opacity + ")";
    }
}
