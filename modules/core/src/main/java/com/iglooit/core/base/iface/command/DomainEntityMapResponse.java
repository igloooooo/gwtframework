package com.iglooit.core.base.iface.command;

import com.iglooit.core.base.iface.domain.DomainEntity;

import java.util.Map;

public class DomainEntityMapResponse<DE1 extends DomainEntity, DE2 extends DomainEntity> extends Response
{
    private Map<DE1, DE2> map;

    public DomainEntityMapResponse()
    {
     
    }

    public DomainEntityMapResponse(Map<DE1, DE2> map)
    {
        this.map = map;
    }

    public Map<DE1, DE2> getMap()
    {
        return map;
    }

    public void setMap(Map<DE1, DE2> strippedMap)
    {
        this.map = strippedMap;
    }
}
