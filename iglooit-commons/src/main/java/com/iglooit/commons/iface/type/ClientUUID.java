package com.iglooit.commons.iface.type;

public class ClientUUID extends UUID
{
    public ClientUUID()
    {
    }

    public ClientUUID(long mostSigBits, long leastSigBits)
    {
        super(mostSigBits, leastSigBits);
    }

    @Override
    public boolean isValid()
    {
        return false;
    }
}
