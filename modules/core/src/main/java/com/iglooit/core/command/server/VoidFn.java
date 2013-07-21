package com.iglooit.core.command.server;

/**
 * A convenient implementation of {@link Fn} interface for the case when there is no return value is required. All
 * the work is moved to {@link #doExecute(Object...)} internal method.
 *
 * @author Michael Truong
 */
public abstract class VoidFn implements Fn<Void>
{

    /**
     * {@inheritDoc}
     */
    @Override
    public Void execute(Object... args)
    {
        doExecute(args);
        return null;
    }

    /**
     * Executes current functor object with given arbitrary arguments.
     *
     * @param args
     */
    protected abstract void doExecute(Object... args);
}
