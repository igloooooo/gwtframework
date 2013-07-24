package com.iglooit.core.base.iface.command;

import com.iglooit.commons.iface.domain.meta.Meta;

import java.util.List;

public class DomainEntityListResponse<DE extends Meta> extends ListResponse<DE>
{
    public DomainEntityListResponse()
    {
    }

    public DomainEntityListResponse(List<DE> des)
    {
        super(des);
    }

    public List<DE> getDomainEntityList()
    {
        return getList();
    }
}
