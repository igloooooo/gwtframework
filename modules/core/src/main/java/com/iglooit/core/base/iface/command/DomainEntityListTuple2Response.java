package com.iglooit.core.base.iface.command;

import com.clarity.core.base.iface.domain.DomainEntity;
import com.clarity.commons.iface.type.Tuple2;

import java.util.List;

public class DomainEntityListTuple2Response<de1 extends DomainEntity, de2 extends DomainEntity>
    extends ListResponse<Tuple2<de1, de2>>
{
    public DomainEntityListTuple2Response()
    {
    }

    public DomainEntityListTuple2Response(List<Tuple2<de1, de2>> tuple2s)
    {
        super(tuple2s);
    }
}
