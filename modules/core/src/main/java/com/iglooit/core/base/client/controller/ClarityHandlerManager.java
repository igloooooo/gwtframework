package com.iglooit.core.base.client.controller;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;

public class ClarityHandlerManager extends HandlerManager
{
    private ClarityHandlerManager parentHM;
//    private boolean isRefire = false ;

    public ClarityHandlerManager()
    {
        super(null);
    }

    public ClarityHandlerManager(Object source)
    {
        super(source);
    }

    public ClarityHandlerManager(Object source, ClarityHandlerManager parent)
    {
        super(source);
        setParentHM(parent);
    }

    public void setParentHM(ClarityHandlerManager parent)
    {
        parentHM = parent;
    }

    public ClarityHandlerManager(Object source, boolean fireInReverseOrder)
    {
        super(source, fireInReverseOrder);
    }

    @Override
    public void fireEvent(GwtEvent<?> event)
    {
        super.fireEvent(event);
        //2nd model
//        if (!this.isEventHandled(event.getAssociatedType()))
//            isRefire = true;
//        if (isRefire && parentHM != null)
//        {
//            parentHM.fireEvent(event);
//            isRefire = false;
//        }

        //3rd model
        if (parentHM != null)
            parentHM.fireEvent(event);
    }

//    public void enableRefiring()
//    {
//        isRefire = true;
//    }
}
