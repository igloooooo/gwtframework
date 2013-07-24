package com.iglooit.core.base.iface.command;

import com.iglooit.commons.iface.domain.meta.Meta;

import java.util.List;
import java.util.Map;

public class DomainEntityListAndMapResponse<ListDomainType extends Meta, MapValueDomainType extends Meta>
    extends ListAndMapResponse<ListDomainType, MapValueDomainType>
{
    public DomainEntityListAndMapResponse()
    {
    }

    public DomainEntityListAndMapResponse(List<ListDomainType> list, Map<String, List<MapValueDomainType>> map)
    {
        super(list, map);
    }
}
