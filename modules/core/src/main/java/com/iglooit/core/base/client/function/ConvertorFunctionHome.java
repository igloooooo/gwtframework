package com.iglooit.core.base.client.function;

import com.clarity.core.lib.client.MetaModelData;
import com.clarity.core.base.iface.domain.MetaBasedDomainEntity;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConvertorFunctionHome
{
    public static final <DE extends MetaBasedDomainEntity, MD extends MetaModelData<DE>>
    List<DE> getDomainListFromModelList(List<MD> modelList)
    {
        List<DE> deList = new ArrayList<DE>();
        if (modelList == null)
            return deList;
        for (MD modelData : modelList)
        {
            deList.add(modelData.getMeta());
        }
        return deList;
    }

    public static final <DE extends MetaBasedDomainEntity, MD extends MetaModelData<DE>>
    List<DE> getDomainListFromModelList(List<MD> modelList, Class<DE> classType)
    {
        List<DE> deList = new ArrayList<DE>();
        if (modelList == null)
            return deList;
        for (MD modelData : modelList)
        {
            deList.add(modelData.getMeta());
        }

        return deList;
    }


    public static final Time getTimeFromString(String timeString, String format)
    {
        DateTimeFormat fmt = DateTimeFormat.getFormat(format);
        if (StringUtil.isNotEmpty(timeString))
        {
            Date date = fmt.parse(timeString);
            Time time = new Time(date);
            return time;
        }
        return null;
    }

    public static final String getStringFromTime(Time time, String format)
    {
        DateTimeFormat fmt = DateTimeFormat.getFormat(format);
        if (time != null)
            return fmt.format(time.getDate());
        return "";
    }
}
