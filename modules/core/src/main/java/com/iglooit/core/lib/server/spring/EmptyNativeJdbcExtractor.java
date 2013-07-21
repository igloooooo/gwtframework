package com.iglooit.core.lib.server.spring;

import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An implementation of NativeJdbcExtractor which does nothing but returns the original connection object.
 *
 * @author Michael Truong
 */
public class EmptyNativeJdbcExtractor extends NativeJdbcExtractorAdapter
{

    /**
     * {@inheritDoc}
     */
    @Override
    public final Connection getNativeConnection(Connection con) throws SQLException
    {
        if (con == null)
        {
            return null;
        }
        return doGetNativeConnection(con);
    }
}
