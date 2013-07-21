package com.iglooit.core.base.server.util;

import com.clarity.core.base.iface.domain.DomainEntity;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.util.StringUtil;

import java.util.List;
import java.util.Map;

public class DomainEntityPrevalidationUtil
{

    public static void trimStringValues(List<? extends DomainEntity> list, boolean dieIfEmpty)
    {
        for (DomainEntity entity : list)
        {
            trimStringValues(entity, dieIfEmpty);
        }
    }

    public static void trimStringValues(DomainEntity entity, boolean dieIfEmpty)
    {
        for (Map.Entry<String, Object> propertyEntry : entity.getProperties().entrySet())
        {
            final Object value = propertyEntry.getValue();
            if (value instanceof String)
            {
                final String key = propertyEntry.getKey();
                final String valueStr = StringUtil.trim((String)value);
                if (dieIfEmpty && StringUtil.isEmpty(valueStr)) throw new AppX(key + " cannot be empty");
                entity.set(key, valueStr);
            }
        }
    }

}
