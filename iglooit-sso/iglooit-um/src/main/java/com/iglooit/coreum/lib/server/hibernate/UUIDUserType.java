package com.iglooit.coreum.lib.server.hibernate;

import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.UUID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Maps the Hibernate UUID objects into a database representation Some ideas gleaned from
 * http://forum.springsource.org/showthread.php?t=71508
 * http://www.bigsoft.co.uk/blog/index.php/2008/11/01/java-util-uuid-primary-keys-in-hibernate
 * https://forum.hibernate.org/viewtopic.php?f=1&t=996879&start=0 https://www.hibernate.org/169.html
 * http://forum.springsource.org/showthread.php?t=46445
 */
public class UUIDUserType implements UserType
{
    public static final String CLASS_NAME = "com.clarity.coreum.lib.server.hibernate.UUIDUserType";

    //todo mg: need to test this thoroughly - got code from web and hasn't been extensively checked
    //got it from http://www.bigsoft.co.uk/blog/index.php/2008/11/01/java-util-uuid-primary-keys-in-hibernate

    private static final int[] SQL_TYPES = {Types.BINARY};

    @Override
    public int[] sqlTypes()
    {
        return SQL_TYPES;
    }

    @Override
    public Class returnedClass()
    {
        return UUID.class;
    }

    @Override
    public boolean equals(Object x, Object y)
    {
        if (!(x instanceof UUID && y instanceof UUID))
            return false;
        return x.equals(y);
    }

    @Override
    public int hashCode(Object o)
    {
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SessionImplementor sessionImplementor, Object o)
        throws HibernateException, SQLException
    {
        byte[] value = resultSet.getBytes(strings[0]);
        if (value == null)
            return null;
        else
            return UUID.fromBytes(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SessionImplementor sessionImplementor)
        throws HibernateException, SQLException
    {
        if (o == null)
        {
            preparedStatement.setNull(i, Types.BINARY);
            return;
        }

        if (!UUID.class.isAssignableFrom(o.getClass()))
        {
            throw new HibernateException(o.getClass().toString() + " : cast exception");
        }

        preparedStatement.setBytes(i, ((UUID)o).toBytes());
    }

    @Override
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

    @Override
    public boolean isMutable()
    {
        return true;
    }

    @Override
    public Serializable disassemble(Object value)
    {
        if (!UUID.class.isAssignableFrom(value.getClass()))
        {
            throw new HibernateException(value.getClass().toString() + " : cast exception");
        }
        UUID uuid = (UUID)value;
        return uuid.toBytes();
    }

    @Override
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

    @Override
    public Object replace(Object original, Object target, Object owner)
    {
        return deepCopy(original);
    }
}
