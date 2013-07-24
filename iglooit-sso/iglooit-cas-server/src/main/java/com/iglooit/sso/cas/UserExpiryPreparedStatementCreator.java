package com.iglooit.sso.cas;

import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserExpiryPreparedStatementCreator implements PreparedStatementCreator
{
    private final String sql;
    private final String userId;

    public UserExpiryPreparedStatementCreator(String sql, String userId)
    {
        this.sql = sql;
        this.userId = userId;
    }

    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
    {
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, userId.toUpperCase());
        return ps;
    }
}
