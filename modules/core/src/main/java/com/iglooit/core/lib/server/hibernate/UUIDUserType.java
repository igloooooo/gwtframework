package com.iglooit.core.lib.server.hibernate;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.UUID;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

/**
 * Maps the Hibernate UUID objects into a database representation
 * Some ideas gleaned from
 * http://forum.springsource.org/showthread.php?t=71508
 * http://www.bigsoft.co.uk/blog/index.php/2008/11/01/java-util-uuid-primary-keys-in-hibernate
 * https://forum.hibernate.org/viewtopic.php?f=1&t=996879&start=0
 * https://www.hibernate.org/169.html
 * http://forum.springsource.org/showthread.php?t=46445
 */
public class UUIDUserType implements UserType, Serializable
{
    //todo mg: need to test this thoroughly - got code from web and hasn't been extensively checked
    //got it from http://www.bigsoft.co.uk/blog/index.php/2008/11/01/java-util-uuid-primary-keys-in-hibernate

    private static final int[] SQL_TYPES = {Types.BINARY};

    public int[] sqlTypes()
    {
        return SQL_TYPES;
    }

    public Class returnedClass()
    {
        return UUID.class;
    }

    public boolean equals(Object x, Object y)
    {
        if (x == y) return true;

        if (x instanceof byte[] && y instanceof byte[])
            return Arrays.equals((byte[]) x, (byte[]) y);

        if (!(x instanceof UUID && y instanceof UUID))
            return false;
        return x.equals(y);
    }

    public int hashCode(Object o)
    {
        if (o instanceof byte[])
            return Arrays.hashCode((byte[]) o);

        return o.hashCode();
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws SQLException
    {
        byte[] value = rs.getBytes(names[0]);
        if (value == null)
            return null;
        else
            return UUID.fromBytes(value);
    }

    public void nullSafeSet(PreparedStatement ps, Object value, int index) throws SQLException
    {
        if (value == null)
        {
            ps.setNull(index, Types.BINARY);
            return;
        }

        if (!UUID.class.isAssignableFrom(value.getClass()))
        {
            throw new HibernateException(value.getClass().toString() + " : cast exception");
        }

        ps.setBytes(index, ((UUID)value).toBytes());
    }

    public Object deepCopy(Object value)
    {
        if (value == null) return null;
        if (!UUID.class.isAssignableFrom(value.getClass()))
        {
            throw new HibernateException(value.getClass().toString() + " : cast exception");
        }
        UUID other = (UUID)value;
        return UUID.fromBytes(other.toBytes());
    }

    public boolean isMutable()
    {
        return true;
    }

    public Serializable disassemble(Object value)
    {
        if (!UUID.class.isAssignableFrom(value.getClass()))
        {
            throw new HibernateException(value.getClass().toString() + " : cast exception");
        }
        UUID uuid = (UUID)value;
        return uuid.toBytes();
    }

    public Object assemble(Serializable cached, Object owner)
    {
        if (cached instanceof byte[])
        {
            return UUID.fromBytes((byte[])cached);
        }
        else
        {
            throw new AppX("Attempt to make a UUID from a non-byte type: " + cached);
        }
    }

    public Object replace(Object original, Object target, Object owner)
    {
        return deepCopy(original);
    }
}
