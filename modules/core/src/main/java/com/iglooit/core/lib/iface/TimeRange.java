package com.iglooit.core.lib.iface;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.util.TimeUtil;
import com.clarity.core.base.client.model.IModelDataEntity;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class TimeRange implements IModelDataEntity, Serializable, IsSerializable
{
    private Integer quantity;
    private TimeUtil.TimeUnit unit;

    public TimeRange()
    {
    }

    public TimeRange(Integer quantity, TimeUtil.TimeUnit unit)
    {
        if (quantity == null || unit == null)
            throw new AppX("Null value is not acceptable in TimeRange");
        this.quantity = quantity;
        this.unit = unit;
    }

    public Long getTime()
    {
        Integer ms = unit.getMilliSeconds();
        Long msL = Long.valueOf(ms.toString());
        return msL * quantity;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public TimeUtil.TimeUnit getUnit()
    {
        return unit;
    }

    @Override
    public String toString()
    {
        return quantity.toString() + " " + unit.toString();
    }

    @Override
    public String getDisplayString()
    {
        return toString();
    }
}
