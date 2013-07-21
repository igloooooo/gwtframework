package com.iglooit.core.lib.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class CopyFactory
{
    private static final Log LOG = LogFactory.getLog(CopyFactory.class);

    public static <To, From> void copy(From from, To to)
    {
        // find getters in 'from', and matching setters in 'to'
        Map<String, Method> fromGetters = new HashMap<String, Method>();
        for (Method m : from.getClass().getMethods())
        {
            if (m.getParameterTypes().length == 0
                && (m.getName().startsWith("get") || m.getName().startsWith("is")))
            {
                String methodName = m.getName().substring(3, m.getName().length() - 1);
                fromGetters.put(methodName, m);
                if (LOG.isTraceEnabled())
                    LOG.trace("Found getter: " +
                        from.getClass().getSimpleName() + "." + m.getName() +
                        " type " + m.getReturnType().getSimpleName());
            }
        }

        for (Method toSetter : to.getClass().getMethods())
        {
            if (toSetter.getName().startsWith("set") && toSetter.getParameterTypes().length == 1)
            {
                String methodName = toSetter.getName().substring(3, toSetter.getName().length() - 1);
                if (fromGetters.containsKey(methodName)
                    && fromGetters.get(methodName).getReturnType().equals(toSetter.getParameterTypes()[0]))
                {
                    Method fromGetter = fromGetters.get(methodName);
                    if (LOG.isTraceEnabled())
                        LOG.trace("Found matching setter: " +
                            from.getClass().getSimpleName() + "." + toSetter.getName());
                    try
                    {
                        toSetter.invoke(to, fromGetters.get(methodName).invoke(from));
                    }
                    catch (IllegalAccessException e)
                    {
                        LOG.error("Cannot reflectively copy from: " +
                            from.getClass() + "." + fromGetter.getName() + " -> " +
                            to.getClass() + "." + toSetter.getName(), e);
                    }
                    catch (InvocationTargetException e)
                    {
                        LOG.error("Cannot reflectively copy from: " +
                            from.getClass() + "." + fromGetter.getName() + " -> " +
                            to.getClass() + "." + toSetter.getName(), e);
                    }
                }
            }
        }
    }
}
