package com.iglooit.commons.server.util;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.type.Tuple2;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil
{
    private static final Log LOG = LogFactory.getLog(ReflectionUtil.class);

    public static List<Field> getAllFields(Class<?> type)
    {
        List<Field> lFields = new ArrayList<Field>();
        lFields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null)
            lFields.addAll(getAllFields(type.getSuperclass()));
        return lFields;
    }

    public static <T> List<Tuple2<String, String>> reflectiveData(T entity)
    {
        List<Tuple2<String, String>> results = new ArrayList<Tuple2<String, String>>();
        for (Method m : entity.getClass().getMethods())
            if (m.getName().startsWith("get")
                && m.getParameterTypes().length == 0)
            {
                Object o = null;

                try
                {
                    o = m.invoke(entity);
                }
                catch (IllegalAccessException e)
                {
                    LOG.error("Could not reflectively run getter on " + entity.getClass().getName() +
                        "." + m.getName() + "(): ", e);
                }
                catch (InvocationTargetException e)
                {
                    LOG.error("Could not reflectively run getter on " + entity.getClass().getName() +
                        "." + m.getName() + "(): ", e);
                }

                results.add(new Tuple2<String, String>(m.getName(), (o == null ? "" : o.toString())));
            }
        return results;
    }

    public static <X> String reflectiveToString(X entity)
    {
        StringBuilder sb = new StringBuilder("");
        for (Tuple2<String, String> tuple : reflectiveData(entity))
        {
            sb.append(tuple.getFirst());
            sb.append(": ");
            sb.append(tuple.getSecond());
            sb.append("\n");
        }
        return sb.toString();
    }

    public static Option<Meta> instantiateMeta(Class c)
    {
        // check for a no args constructor.
        try
        {
            Constructor ct = c.getConstructor();
            Meta meta = (Meta)ct.newInstance();
            if (meta == null)
                throw new AppX("Instantiated null?");
            return Option.option(meta);
        }
        catch (Exception e)
        {
            // this commonly happens, is not a drastic error.
            if (LOG.isTraceEnabled()) LOG.trace("Exception instantiating class: " + c.getName() +
                " " + e.getClass().getSimpleName());
        }
        return Option.none();
    }
}
