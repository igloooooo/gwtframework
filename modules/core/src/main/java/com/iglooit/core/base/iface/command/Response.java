package com.iglooit.core.base.iface.command;

import com.iglooit.core.base.iface.command.response.VoidResponse;

import java.io.Serializable;

public abstract class Response implements Serializable
{
    public static final VoidResponse VOID_RESPONSE = new VoidResponse();

    private transient volatile Runnable postConstructCommand;

    /**
     * Sets a custom command which is executed just after the response object has been created using one of its
     * constructors and the transaction completes successfully.
     *
     * @param postReturnCommand The new value to set.
     */
    public void setPostConstructCommand(Runnable postReturnCommand)
    {
        this.postConstructCommand = postReturnCommand;
    }

    /**
     * Runs the post-construct command if it exists.
     * <p/>
     * Notice that this post-construction command is run <b>inside</b> the current database transaction scope. Also
     * notice that this command is executed only <b>ONCE</b> until another command is set via {@link
     * #setPostConstructCommand(Runnable)}.
     */
    public final void postConstruct()
    {
        if (postConstructCommand != null)
        {
            postConstructCommand.run();
            postConstructCommand = null;
        }
    }
}
