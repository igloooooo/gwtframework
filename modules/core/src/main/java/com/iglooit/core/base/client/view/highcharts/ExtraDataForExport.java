package com.iglooit.core.base.client.view.highcharts;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import java.util.Map;

@Deprecated
public class ExtraDataForExport extends BaseModel
{
    public static final String DATA = "data";
    public static final String OPTIONS = "options";
    private int extraTableHeight;

    /*

    data

    {
       "Basic Info":{
          "kpi name":"Gary01",
          "kpi value":"50"
       },
       "Dimentions":{
          "Area":"Sydney, Brisban",
          "Handset Model":"iphone5, nokia"
       },
       "Options":{
          "target time":"latest"
       }
    }
     */


    public ExtraDataForExport(Map<String, Map<String, String>> dataMap, ExtraDataTableOptions options)
    {
        set(DATA, convertToJSON(dataMap));

        if (options == null)
        {
            ExtraDataTableOptions defaultOptions = new ExtraDataTableOptions();
            set(OPTIONS, defaultOptions.convertToJSON());
            extraTableHeight = defaultOptions.getExtraTableHeight(dataMap);
        }
        else
        {
            set(OPTIONS, options.convertToJSON());
            extraTableHeight = options.getExtraTableHeight(dataMap);
        }
    }

    public JSONObject getJSON()
    {
        JSONObject json = new JSONObject();
        json.put(DATA, (JSONObject)get(DATA));
        json.put(OPTIONS, (JSONObject)get(OPTIONS));
        return json;
    }

    private JSONObject convertToJSON(Map<String, Map<String, String>> dataMap)
    {
        JSONObject json = new JSONObject();

        if (dataMap != null)
        {
            for (Map.Entry<String, Map<String, String>> entry : dataMap.entrySet())
            {
                JSONObject inner = new JSONObject();
                for (Map.Entry<String, String> entry1 : entry.getValue().entrySet())
                {
                    inner.put(entry1.getKey(), new JSONString(entry1.getValue()));
                }
                json.put(entry.getKey(), inner);
            }
        }

        return json;
    }

    public int getExtraTableHeight()
    {
        return extraTableHeight;
    }

    public static class ExtraDataTableOptions
    {
        private int tableTopPadding;

        private int tableLeftPadding;

        private int tableBottomPadding;

        private int rowHeight;

        private int rowsBetweenGroups;

        //the value column will be wrap by wrapToLength
        //no wrap if wrapToLength <= 0
        private int wrapToLength;

        //to prevent chopping a single word.
        //in effect only when wrapToLength > 0
        //no splitting by default
        private Character splitBy = null;

        //should be same as HighchartsExporter.EXTRA_EXPORT_GROUP
        private static final String GROUP_NAME = "extra_table_group";

        public ExtraDataTableOptions()
        {
            tableTopPadding = 20;
            tableLeftPadding = 20;
            tableBottomPadding = 0;
            rowHeight = 20;
            rowsBetweenGroups = 1;
            wrapToLength = 0;
        }

        public void setTableTopPadding(int padding)
        {
            tableTopPadding = padding;
        }

        public int getTableTopPadding()
        {
            return tableTopPadding;
        }

        public void setTableLeftPadding(int padding)
        {
            tableLeftPadding = padding;
        }

        public int getTableLeftPadding()
        {
            return tableLeftPadding;
        }

        public int getTableBottomPadding()
        {
            return tableBottomPadding;
        }

        public void setTableBottomPadding(int tableBottomPadding)
        {
            this.tableBottomPadding = tableBottomPadding;
        }

        public int getRowHeight()
        {
            return rowHeight;
        }

        public void setRowHeight(int rowHeight)
        {
            this.rowHeight = rowHeight;
        }

        public int getRowsBetweenGroups()
        {
            return rowsBetweenGroups;
        }

        public void setRowsBetweenGroups(int rowsBetweenGroups)
        {
            this.rowsBetweenGroups = rowsBetweenGroups;
        }

        public void setWrapToLength(int wrapToLength)
        {
            this.wrapToLength = wrapToLength;
        }

        public int getWrapToLength()
        {
            return wrapToLength;
        }

        public Character getSplitBy()
        {
            return splitBy;
        }

        public void setSplitBy(Character splitBy)
        {
            this.splitBy = splitBy;
        }

        public JSONObject convertToJSON()
        {
            JSONObject json = new JSONObject();
            json.put("groupName", new JSONString(GROUP_NAME));
            json.put("tableTopPadding", new JSONNumber(tableTopPadding));
            json.put("tableLeftPadding", new JSONNumber(tableLeftPadding));
            json.put("tableBottomPadding", new JSONNumber(tableBottomPadding));
            json.put("rowHeight", new JSONNumber(rowHeight));
            json.put("rowsBetweenGroups", new JSONNumber(rowsBetweenGroups));
            json.put("wrapToLength", new JSONNumber(wrapToLength));
            json.put("splitBy", new JSONString(splitBy == null ? "" : String.valueOf(splitBy)));
            return json;
        }

        public int getExtraTableHeight(Map<String, Map<String, String>> dataMap)
        {
            if (dataMap == null || dataMap.isEmpty())
                return 0;

            int row = 0;
            for (Map<String, String> values : dataMap.values())
            {
                row++;
                for (String value : values.values())
                {
                    row += lineCountAfterWrap(value, wrapToLength, splitBy);
                }
            }
            row = row + (dataMap.size() - 1) * rowsBetweenGroups;

            return row * rowHeight + tableTopPadding  + tableBottomPadding;
        }
    }
    private static native int lineCountAfterWrap(String str, int maxLineLen, Character splitter)/*-{
        return $wnd.HCExtend.splitStringByLength(str, maxLineLen, splitter).length;
    }-*/;
}
