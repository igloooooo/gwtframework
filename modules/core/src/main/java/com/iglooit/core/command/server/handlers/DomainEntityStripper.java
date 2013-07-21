package com.iglooit.core.command.server.handlers;

import com.clarity.commons.iface.annotation.BeanLibIgnore;
import com.clarity.core.base.iface.command.ComposedDomainEntity;
import net.sf.beanlib.hibernate.HibernateBeanReplicator;
import net.sf.beanlib.hibernate3.Hibernate3BeanReplicator;
import net.sf.beanlib.provider.collector.PublicSetterMethodCollector;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// todo ms: as part of spring annottationaonoany autowiring spike, dependency inject this. 
public class DomainEntityStripper
{
    public <DE> DE strip(DE domainEntity)
    {
        HibernateBeanReplicator replicator = getReplicator();
        return replicate(domainEntity, replicator);
    }

    public <DE> List<DE> strip(List<DE> domainEntityList)
    {
        List<DE> responseList = new ArrayList<DE>();

        HibernateBeanReplicator replicator = getReplicator();
        for (DE domainEntity : domainEntityList)
            responseList.add(replicate(domainEntity, replicator));

        return responseList;
    }

    public <CDE extends ComposedDomainEntity> List<CDE> stripComposition(List<CDE> list)
    {
        List<CDE> responseList = new ArrayList<CDE>();

        HibernateBeanReplicator replicator = getReplicator();
        for (CDE entity : list)
        {
            entity.setDomainEntity(replicate(entity.getDomainEntity(), replicator));
            responseList.add(entity);
        }
        return responseList;
    }

    private HibernateBeanReplicator getReplicator()
    {

        HibernateBeanReplicator replicator = new Hibernate3BeanReplicator();
        replicator.initCustomTransformerFactory(new MyBeanTransformerFactory());
        replicator.getBeanPopulatorBaseConfig().setSetterMethodCollector(new AnnotationAwareSetterMethodCollector());
        //update the setter method collector to not collect annotated methods.
        return replicator;

    }

    private <DE> DE replicate(DE domainEntity, HibernateBeanReplicator replicator)
    {
        return replicator.deepCopy(domainEntity);
    }


    private static class AnnotationAwareSetterMethodCollector extends PublicSetterMethodCollector
    {
        @Override
        public Method[] collect(Object bean)
        {
            Method[] allSetters = bean.getClass().getMethods();
            List<Method> eligibleSetters = new ArrayList<Method>();

            for (Method method : allSetters)
            {
                if (method.getParameterTypes().length == 1
                    && method.getAnnotation(BeanLibIgnore.class) == null
                    && method.getName().startsWith(getMethodPrefix()))
                {
                    eligibleSetters.add(method);
                }
            }
            return eligibleSetters.toArray(new Method[eligibleSetters.size()]);
        }

    }

}
