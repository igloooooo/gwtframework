package com.iglooit.core.lib.server.ucp;

import oracle.ucp.UniversalConnectionPool;
import oracle.ucp.jdbc.JDBCConnectionPool;
import oracle.ucp.jdbc.PoolDataSourceImpl;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Implementation for {@link PoolDataSourceEx} which relies on internal API of Oracle UCP's {@link PoolDataSourceImpl}
 * to close used physical connections.
 * <p>
 * <b>Caution</b>: While this implementation class may not be used explicitly inside Clarity TC codebase,
 * it is a part of container (e.g. Tomcat) shared library list. Thus, DON'T REMOVE this class without a double care!
 * </p>
 *
 * @author Michael Truong
 */
public class PoolDataSourceExImpl extends PoolDataSourceImpl implements PoolDataSourceEx
{

    private static final String CLOSE_USED_PHYSICAL_CONN_METHOD = "closeUsedPhysicalConnection";

    @Override
    public void closeUsedPhysicalConnection(Connection physicalConnection)
    {
        if (m_cp == null)
        {
            return;
        }

        Method closeUsedPhysicalConnectionMethod = getCloseUsedPhysicalConnectionMethod(m_cp);
        try
        {
            closeUsedPhysicalConnectionMethod.setAccessible(true);
            closeUsedPhysicalConnectionMethod.invoke(m_cp, physicalConnection);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error occurs while closing used physical connection", e);
        }
    }

    static Method getCloseUsedPhysicalConnectionMethod(UniversalConnectionPool connectionPool)
    {
        Method closeUsedPhysicalConnectionMethod = null;
        Class<?> cpClass = connectionPool.getClass();
        while (closeUsedPhysicalConnectionMethod == null && cpClass != null)
        {
            try
            {
                closeUsedPhysicalConnectionMethod = cpClass.getDeclaredMethod(CLOSE_USED_PHYSICAL_CONN_METHOD,
                    JDBCConnectionPool.class);
            }
            catch (NoSuchMethodException e)
            {
                closeUsedPhysicalConnectionMethod = null;
            }

            cpClass = cpClass.getSuperclass();
        }

        if (closeUsedPhysicalConnectionMethod == null)
        {
            throw new IllegalStateException(
                "No method '" + CLOSE_USED_PHYSICAL_CONN_METHOD + "' exists in the PoolDataSourceImpl class; UCP " +
                    "version has probably been changed");
        }

        return closeUsedPhysicalConnectionMethod;
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Hack to overcome the inconsistency between production use (java 6) and development use (java 7).
     */
    @Override
    @Deprecated
    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        return Logger.getLogger(getClass().getName());
    }
}
