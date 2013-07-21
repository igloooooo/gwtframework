package com.iglooit.core.base.client.model;

import com.clarity.core.base.client.widget.common.ClarityWidgetOption;
import com.extjs.gxt.ui.client.data.BaseModelData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is created to be able to share configuration between different scheduler MD that can support UI
 *
 * @author Phobos MENG
 */
public abstract class ClarityGridBaseMd extends BaseModelData
{
    public ClarityGridBaseMd()
    {
        setAllowNestedValues(false);
    }

    public abstract List<String> getColumns();

    public Map<String, ClarityWidgetOption> getColumnWidgetOptionMap()
    {
        return new HashMap<String, ClarityWidgetOption>();
    }

    public String print()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        StringBuilder columnSb = new StringBuilder();
        for (String column : getColumns())
        {
            if (columnSb.length() > 0) columnSb.append(",");
            columnSb.append(column).append("=").append(get(column));
        }
        sb.append(columnSb.toString());
        sb.append("}");
        return sb.toString();
    }
}
