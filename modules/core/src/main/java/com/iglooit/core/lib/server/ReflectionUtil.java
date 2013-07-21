package com.iglooit.core.lib.server;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.type.Tuple2;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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

                results.add(new Tuple2<String, String>(m.getName(), String.valueOf(o)));
            }
        return results;
    }

    public static <X> String reflectiveToString(X entity)
    {
        StringBuilder sb = new StringBuilder("");
        for (Tuple2<String, String> tuple : reflectiveData(entity))
            sb.append(tuple.getFirst()).append(": ").append(tuple.getSecond()).append("\n");
        return sb.toString();
    }


    public static void mapPluginsToTypes(String basePackage, Class pluginParentClass,
                                         Map pluginToTypeMap, Set<String> allTypes, String typeMethodName)
    {
        //convert the package name into the resource name ie turn the dots to slashes . -> /
        String resourceNameBasePackage2 = basePackage.replaceAll("\\.", "/");

        //uses spring classpath scanning utils - checkout
        //http://mcoj.wordpress.com/2009/03/13/classpath-scanning
        //http://www.nofluffjuststuff.com/blog/scott_leberknight/2008/
        //06/just_how_does_spring_do_its_classpath_component_scanning_magic_.html
        ClassPathScanningCandidateComponentProvider provider =
            new ClassPathScanningCandidateComponentProvider(true);
        provider.addIncludeFilter(new AssignableTypeFilter(pluginParentClass));

        Set<BeanDefinition> components = provider.findCandidateComponents(resourceNameBasePackage2);
        for (BeanDefinition component : components)
        {
            LOG.info("loading attribute plugin - " + component.getBeanClassName());

            try
            {
                Class subType = Class.forName(component.getBeanClassName());
                if (!pluginParentClass.isAssignableFrom(subType))
                    continue;
                Method getNodeMethod = subType.getMethod(typeMethodName);
                String nodeType = (String)getNodeMethod.invoke(null);
                pluginToTypeMap.put(nodeType, subType);
                allTypes.add(nodeType);
            }
            catch (Exception e)
            {
                throw new AppX("error while loading component '" + component.getBeanClassName() + "'", e);
            }
        }
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
