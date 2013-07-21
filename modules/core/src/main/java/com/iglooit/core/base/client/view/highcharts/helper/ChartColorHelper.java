package com.iglooit.core.base.client.view.highcharts.helper;


import com.clarity.core.base.client.util.ColorUtil;

import java.util.List;

public class ChartColorHelper
{
    public static String getNextAvailable(List<String> colorsInUse)
    {
        return getNextAvailable(colorsInUse, ColorRestrictionType.FULL);
    }

    public static String getNextAvailable(List<String> colorsInUse, ColorRestrictionType type)
    {
        List<String> colorList = getColorListByType(type);

        int minUsedCount = 99999;
        String minUsedColor = colorList.get(0);
        for (String availableColor : colorList)
        {
            int count = 0;
            for (String colorInUse : colorsInUse)
            {
                if (availableColor.equals(colorInUse))
                    count++;
            }
            if (count < minUsedCount)
            {
                minUsedCount = count;
                minUsedColor = availableColor;
            }
        }
        return minUsedColor;
    }

    public static List<String> getColorListByType(ColorRestrictionType type)
    {
        switch (type)
        {
            case FULL:
                return ColorUtil.COLOR_FULL_LIST;
            case SEVERITY_SAFE:
                return ColorUtil.COLOR_SEVERITY_SAFE_LIST;
            case RED_BLUE:
                return ColorUtil.COLOR_RED_BLUE;
            default:
                return ColorUtil.COLOR_FULL_LIST;
        }
    }

    public enum ColorRestrictionType
    {
        FULL,
        SEVERITY_SAFE,
        RED_BLUE
    }
}

















