package com.iglooit.core.base.client.widget.grid;

import com.clarity.core.base.client.widget.common.ClarityWidgetOption;

import java.util.ArrayList;
import java.util.List;

public class ClarityGridColumnOption
{
    private String columnHeader;
    private Integer columnWidth = 150; // default to 150
    private List<ClarityWidgetOption> widgetOptionList = new ArrayList<ClarityWidgetOption>();

    public ClarityGridColumnOption(String columnHeader)
    {
        this.columnHeader = columnHeader;
    }

    public String getColumnHeader()
    {
        return columnHeader;
    }

    public void addWidgetOption(ClarityWidgetOption widgetOption)
    {
        widgetOptionList.add(widgetOption);
    }

    public List<ClarityWidgetOption> getWidgetOptionList()
    {
        return widgetOptionList;
    }

    public Integer getColumnWidth()
    {
        return columnWidth;
    }

    public void setColumnWidth(Integer columnWidth)
    {
        this.columnWidth = columnWidth;
    }
}
