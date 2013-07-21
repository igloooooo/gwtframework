package com.iglooit.core.base.iface.command;

import java.util.List;

public class ComposedDomainEntityListResponse<CDE extends ComposedDomainEntity> extends ListResponse<CDE>
{
    public ComposedDomainEntityListResponse()
    {
    }

    public ComposedDomainEntityListResponse(List<CDE> des)
    {
        super(des);
    }

    public List<CDE> getComposedDomainEntityList()
    {
        return getList();
    }
}