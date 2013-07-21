package com.iglooit.core.base.client.widget.htmlcontainer;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.commons.iface.util.NumberUtil;
import com.clarity.commons.iface.type.Tuple3;
import com.extjs.gxt.ui.client.widget.HtmlContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Html width that displays a single horizontal bar chart, very similar in style to a progress bar
 */
public class BarGraphHtmlContainer extends HtmlContainer
{
    // default colour range list (colours are CSS style names)
    private static List<Tuple3> rangeList = new ArrayList<Tuple3>()
    { {
        add(new Tuple3<Integer, Integer, String>(0, 30, ClarityStyle.EventStyle.BLUE.getCss() + " " +
                ClarityStyle.WHITE_OVERLAY_1));
        add(new Tuple3<Integer, Integer, String>(31, 50, ClarityStyle.EventStyle.BLUE.getCss() + " " +
                ClarityStyle.WHITE_OVERLAY_2));
        add(new Tuple3<Integer, Integer, String>(51, 70, ClarityStyle.EventStyle.BLUE.getCss() + " " +
                ClarityStyle.WHITE_OVERLAY_3));
        add(new Tuple3<Integer, Integer, String>(71, 90, ClarityStyle.EventStyle.BLUE.getCss() + " " +
                ClarityStyle.WHITE_OVERLAY_4));
        add(new Tuple3<Integer, Integer, String>(91, 100, ClarityStyle.EventStyle.BLUE.getCss() + " " +
                ClarityStyle.WHITE_OVERLAY_5));
    } };

    public BarGraphHtmlContainer(int value, int total, List<Tuple3> rangeList)
    {
        Integer percentageInt = 0;

        if (total > 0)
        {
            Double percentage = NumberUtil.getPercentage(value, total, 0);
            percentageInt = percentage.intValue();
        }

        String barGraphTemplate =
            "<div class='graph-bar'>" +
                "<div class='bar corners'>" +
                    "<div class='bar-used percent-" + percentageInt +
                        " corners' style='width:" + percentageInt + "%'>" +
                    "</div>" +
                "</div>" +
            "</div>" +
            "<div class='bar-info'>" +
                "<div class='value'>" + percentageInt + "%</div>" +
                "<div class='secondary'>" + value + "/" + total + " Used</div>" +
            "</div>";

        // check for the bar colour
        String rangeColour = calculateRangeColour(percentageInt, rangeList);

        addStyleName("graph-bar-container " + rangeColour);
        setHtml(barGraphTemplate);
    }

    public BarGraphHtmlContainer(int value, int total)
    {
        this(value, total, null);
    }

    private String calculateRangeColour(Integer value, List<Tuple3> rangeList)
    {
        String rangeColour = "";
        if (rangeList != null && !rangeList.isEmpty())
        {
            for (Tuple3<Integer, Integer, String> range : rangeList)
            {
                if (range.getFirst() <= value && value <= range.getSecond())
                    rangeColour = range.getThird();
            }
        }
        return rangeColour;
    }

    /**
     * Creates a default range list that is used to change the bar's colour when it is in a certain range,
     * for example 'green' between 0 and 30.
     */
    public static List<Tuple3> getDefaultRangeList()
    {
        return rangeList;
    }
}
