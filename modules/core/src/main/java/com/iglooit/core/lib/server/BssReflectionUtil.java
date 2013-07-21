package com.iglooit.core.lib.server;

import com.clarity.commons.iface.type.AppX;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class BssReflectionUtil extends ReflectionUtil
{
    private static final Log LOG = LogFactory.getLog(BssReflectionUtil.class);

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
}
