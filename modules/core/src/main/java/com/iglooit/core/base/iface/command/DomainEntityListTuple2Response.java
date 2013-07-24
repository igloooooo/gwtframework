package com.iglooit.core.base.iface.command;

import com.iglooit.commons.iface.type.Tuple2;
import com.iglooit.core.base.iface.domain.DomainEntity;

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
