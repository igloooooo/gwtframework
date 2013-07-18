package com.iglooit.commons.iface.type;

import java.io.Serializable;

/**
 * Our UUID class.
 * Using our own instead of the java.util one so that it plays nicely with GWT.
 * The code is basically a stripped down versioon of the java.util one, so if later we
 * need any further info - such as the version or variant, then we can easily find the code for it.
 */
public class UUID<Type> implements Serializable
{
    private static final long serialVersionUID = -1156841361193249489L;

    private long mostSigBits = 0L;
    private long leastSigBits = 0L;

    public UUID()
    {
        //for GWT serialization
    }

    public UUID(long mostSigBits, long leastSigBits)
    {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }

    public static UUID fromString(String name)
    {
        //normalize the string - oracle generated guid contains no dashes

        String[] components;
        long mostSigBits;
        long leastSigBits;
        if (name.length() == 36)
        {
            //java generated
            components = name.split("-");
            if (components.length != 5)
                throw new IllegalArgumentException("Invalid UUID string: " + name);
        }
        else if (name.length() == 32)
        {
            //oracle generated
            components = new String[5];
            components[0] = name.substring(0, 8);
            components[1] = name.substring(8, 12);
            components[2] = name.substring(12, 16);
            components[3] = name.substring(16, 20);
            components[4] = name.substring(20, 32);
        }
        else
            throw new IllegalArgumentException("Invalid UUID string: " + name);

        for (int i = 0; i < 5; i++)
            components[i] = "0x" + components[i];

        try
        {
            mostSigBits = Long.decode(components[0]);
            mostSigBits <<= 16;
            mostSigBits |= Long.decode(components[1]);
            mostSigBits <<= 16;
            mostSigBits |= Long.decode(components[2]);

            leastSigBits = Long.decode(components[3]);
            leastSigBits <<= 48;
            leastSigBits |= Long.decode(components[4]);
        }
        catch (NumberFormatException ex)
        {
            throw new IllegalArgumentException("Invalid UUID string: " + name);
        }

        return new UUID(mostSigBits, leastSigBits);
    }

    private static void toBytes(byte[] bytes, final int startindex, final long val)
    {
        for (int i = 0; i < 8; i++)
            bytes[(7 - i) + startindex] = (byte)(val >> (i * 8));
    }

    public byte[] toBytes()
    {
        byte[] bytes = new byte[16];
        toBytes(bytes, 0, mostSigBits);
        toBytes(bytes, 8, leastSigBits);
        return bytes;
    }

    private static long fromBytes(byte[] bytes, final int startindex)
    {
        long l = 0;
        for (int i = 0; i < 8; i++)
        {
            l <<= 8;
            l |= (long)bytes[i + startindex] & 0xFFL;
        }
        return l;
    }

    public static UUID fromBytes(byte[] bytes)
    {
        long msb = fromBytes(bytes, 0);
        long lsb = fromBytes(bytes, 8);
        return new UUID(msb, lsb);
    }

    public String toString()
    {
        return (digits(mostSigBits >> 32, 8) +
            digits(mostSigBits >> 16, 4) +
            digits(mostSigBits, 4) +
            digits(leastSigBits >> 48, 4) +
            digits(leastSigBits, 12));
    }

    private String digits(long val, int digits)
    {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1).toUpperCase();
    }

    /**
     * Returns a hash code for this <code>UUID</code>.
     *
     * @return a hash code value for this <tt>UUID</tt>.
     */
    public int hashCode()
    {
        return (int)((mostSigBits >> 32)
            ^ mostSigBits
            ^ (leastSigBits >> 32)
            ^ leastSigBits);
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof UUID))
            return false;
        UUID id = (UUID)obj;
        return (mostSigBits == id.mostSigBits
            && leastSigBits == id.leastSigBits);
    }

    //todo mg: make these private

    public long getMostSigBits()
    {
        return mostSigBits;
    }

    public void setMostSigBits(long mostSigBits)
    {
        this.mostSigBits = mostSigBits;
    }

    public long getLeastSigBits()
    {
        return leastSigBits;
    }

    public void setLeastSigBits(long leastSigBits)
    {
        this.leastSigBits = leastSigBits;
    }

    private boolean isNotZeroUUID()
    {
        return !(mostSigBits == 0L && leastSigBits == 0L);
    }

    public boolean isValid()
    {
        return isNotZeroUUID();
    }
}
