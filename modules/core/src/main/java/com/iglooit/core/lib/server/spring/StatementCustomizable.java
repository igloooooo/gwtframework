package com.iglooit.core.lib.server.spring;

import com.clarity.core.command.server.Fn;

import java.sql.Statement;

/**
 * Interface for a SQL-related class which supports some kinds of SQL Statement customisation.
 */
public interface StatementCustomizable<T extends Statement>
{
    /**
     * Sets a customizer to allow external customization.
     *
     * @param customizer A customizer command which handles the customization and return a customized {@link
     *                   java.sql.Statement} object.
     */
    void setStatementCustomizer(Fn<T> customizer);
}
