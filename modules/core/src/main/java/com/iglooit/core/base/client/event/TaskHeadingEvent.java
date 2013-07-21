package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class TaskHeadingEvent extends GwtEvent<TaskHeadingEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();


    private String taskHeading;

    public TaskHeadingEvent(String taskHeading)
    {
        this.taskHeading = taskHeading;
    }

    public void setTaskHeading(String taskHeading)
    {
        this.taskHeading = taskHeading;
    }

    public String getTaskHeading()
    {

        return taskHeading;
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler taskHeadingSentEventHandler)
    {
        taskHeadingSentEventHandler.handle(this);
    }


    public interface Handler extends EventHandler
    {
        void handle(TaskHeadingEvent event);
    }
}
