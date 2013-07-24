package com.iglooit.coreum.base.server.domain;

import com.iglooit.commons.iface.type.UUID;
import com.iglooit.coreum.base.iface.domain.UUIDFactory;
import org.springframework.stereotype.Component;

@Component("serverUuidFactory")
public final class ServerUUIDFactory implements UUIDFactory
{
    public ServerUUIDFactory()
    {
    }

    public UUID generate()
    {
        java.util.UUID tmp = java.util.UUID.randomUUID();
        return new UUID(tmp.getMostSignificantBits(), tmp.getLeastSignificantBits());
    }
}
