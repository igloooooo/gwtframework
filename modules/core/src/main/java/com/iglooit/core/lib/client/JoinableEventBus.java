package com.iglooit.core.lib.client;

import com.clarity.commons.iface.type.AppX;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 19/03/13 4:30 PM
 *
 * Helper class to fire a command only after all the tracked events are fired at least once
 * The command is fired once and then subsequent completion of events are ignored unless start() is called
 *
 * Can only fire command based on the completion of one set of event. Use multiple instances of this class
 * if more than one set of events need to be tracked as one.
 *
 */
public class JoinableEventBus extends HandlerManager
{
    private Set<GwtEvent.Type> trackedSet;
    private Set<GwtEvent.Type> firedSet;

    private Command notifyCommand;

    public JoinableEventBus()
    {
        this(null);
    }
    public JoinableEventBus(Object source)
    {
        super(source);
        trackedSet = new HashSet<GwtEvent.Type>();
    }

    /**
     * Adds event type to the set of events that need to be fired before command is executed.
     * Do not call if the joiner is tracking otherwise an exception would be thrown.
     * @param type
     * @return
     */
    public void track(final GwtEvent.Type type)
    {
        if (isTracking())
            throw new AppX("Must not call addHandler when joiner is actively tracking events");
        trackedSet.add(type);
    }

    /**
     * start() must be called after handlers are appended for the command to fire.
     * @param command
     */
    public void setOnNotifyCommand(Command command)
    {
        notifyCommand = command;
    }

    /**
     * Stops tracking as well as clear the set of events being tracked.
     */
    public void stop()
    {
        firedSet = null;
        trackedSet.clear();
    }

    public boolean isTracking()
    {
        return firedSet != null;
    }

    /**
     * Aborts the current tracking session.
     */
    public void abort()
    {
        firedSet = null;
    }

    /**
     * Starts tracking events being fired. After all tracked events are fired at least once, the notifyCommand
     * is called. Tracking is then disabled however can be resumed by calling this method again.
     */
    public void start()
    {
        if (!isTracking())
            firedSet = new HashSet<GwtEvent.Type>(trackedSet);
        else
            throw new AppX("Tracking already started");
    }

    public void fireEvent(GwtEvent<?> event)
    {
        super.fireEvent(event);
        if (!isTracking())
            return;

        firedSet.remove(event.getAssociatedType());

        if (firedSet.isEmpty())
        {
            abort(); // this resets the joiner so that the command
            // is executed only once per reset
            if (notifyCommand != null)
                notifyCommand.execute();
        }
    }
}
