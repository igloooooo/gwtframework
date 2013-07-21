package com.iglooit.core.command.server;

/**
 * A general-purpose functor object which throws an {@link Exception} if there is something wrong. It differs from
 * {@link Fn} in that it allows propagation of raised exceptions to higher levels.
 *
 * @param <R> Return type.
 * @param <E> Expected exception type.
 * @author Michael Truong
 */
public interface ExceptionAwareFn<R, E extends Exception>
{

    /**
     * Executes current functor object with given arbitrary arguments.
     *
     * @param args
     * @return
     * @throws E if there is an error while executing this command.
     */
    R execute(Object... args) throws E;
}
