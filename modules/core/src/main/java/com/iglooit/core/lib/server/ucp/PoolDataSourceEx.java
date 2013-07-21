package com.iglooit.core.lib.server.ucp;

import oracle.ucp.jdbc.PoolDataSource;

import java.sql.Connection;

/**
 * Extension for {@link PoolDataSource} to provide additional, advanced functions.
 *
 * @author Michael Truong
 */
public interface PoolDataSourceEx extends PoolDataSource
{
    /**
     * Closes a given physical JDBC connection as a result of a runtime error during a configuring phase for this
     * physical connection.
     *
     * @param physicalConnection Physical {@link java.sql.Connection} object.
     */
    void closeUsedPhysicalConnection(Connection physicalConnection);
}
