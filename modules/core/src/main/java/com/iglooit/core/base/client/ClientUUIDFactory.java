package com.iglooit.core.base.client;

import com.clarity.core.base.iface.domain.UUIDFactory;
import com.clarity.commons.iface.type.ClientUUID;
import com.clarity.commons.iface.type.UUID;
import com.google.gwt.user.client.Random;

public class ClientUUIDFactory implements UUIDFactory
{
    private static long uuidCounter = 0;

    public static UUID generateStatic()
    {
        return new ClientUUID(uuidCounter++, 1);
    }

    public static UUID generateRandom()
    {
        return new ClientUUID(Random.nextInt(), Random.nextInt());
    }

    public UUID generate()
    {
        return generateStatic();
    }
}
