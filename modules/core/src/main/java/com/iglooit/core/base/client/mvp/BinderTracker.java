package com.iglooit.core.base.client.mvp;

import com.clarity.core.lib.iface.BssTimeUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class BinderTracker
{
    private List<Item> boundElements = new ArrayList<Item>();

    public void addBinder(Binder b)
    {
        boundElements.add(new Item(b));
    }

    public void removeBinder(Binder b)
    {
        Iterator<Item> iterator = boundElements.iterator();
        while (iterator.hasNext())
            if (iterator.next().getBinder() == b)
                iterator.remove();
    }

    public List<Item> getBoundElements()
    {
        return Collections.unmodifiableList(boundElements);
    }

    public void dumpBoundElements()
    {
        for (Item boundElement : getBoundElements())
            GWT.log(boundElement.toString(), null);
    }

    public static final class Item
    {
        private final Binder binder;
        private final String instantiatedFrom;
        private final Date date;

        public Item(Binder binder)
        {
            this.binder = binder;
            this.instantiatedFrom = getInstantiatingClass();
            this.date = BssTimeUtil.createInaccurateClientDateInvestigateMe();
        }

        private String getInstantiatingClass()
        {
            StackTraceElement[] stes = new RuntimeException().getStackTrace();
            for (StackTraceElement ste : stes)
                if (ste.getClassName().contains("com.clarity") && !ste.getClassName().contains("Binder"))
                    return ste.toString();
            return "";
        }

        public Binder getBinder()
        {
            return binder;
        }

        public String getInstantiatedFrom()
        {
            return instantiatedFrom;
        }

        public Date getDate()
        {
            return date;
        }

        @Override
        public String toString()
        {
            return "Bound property at " + DateTimeFormat.getMediumTimeFormat().format(getDate()) +
                ": " + getBinder().getMetaFieldName() +
                " bound at: " + getInstantiatedFrom();
        }
    }
}
