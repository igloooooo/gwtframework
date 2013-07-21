package com.iglooit.core.command.server;

/**
 * A general-purpose functor object.
 *
 * @param <R> Return type.
 * @author Michael Truong
 */
public interface Fn<R>
{

    /**
     * Executes current functor object with given arbitrary arguments.
     *
     * @param args
     * @return
     */
    R execute(Object... args);
}
