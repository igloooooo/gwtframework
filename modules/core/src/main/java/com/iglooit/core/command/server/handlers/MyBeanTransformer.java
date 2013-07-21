package com.iglooit.core.command.server.handlers;

import com.clarity.commons.iface.type.AppX;
import com.clarity.core.workflow.iface.ngdomain.versionable.VersionableDomainEntity;
import net.sf.beanlib.PropertyInfo;
import net.sf.beanlib.spi.CustomBeanTransformerSpi;
import org.hibernate.Hibernate;

public class MyBeanTransformer implements CustomBeanTransformerSpi
{
    private static final String PLANNED_PROPERTYNAME = "planned";

    public boolean isTransformable(Object from, Class<?> toClass, PropertyInfo propertyInfo)
    { // apply custom transformation for the uninitialized properties
        return !Hibernate.isInitialized(from);
    }

    public <T> T transform(Object in, Class<T> toClass, PropertyInfo propertyInfo)
    { // custom transform by not replicating the uninitialized properties

        //If we're a versioned VersionableDomainEntity, populate empty object to avoid the wrong type being loaded
        if (in instanceof VersionableDomainEntity && propertyInfo.getPropertyName().equals(PLANNED_PROPERTYNAME))
        {
            try
            {
                return (T)propertyInfo.getToBean().getClass().newInstance();
            }
            catch (Exception e)
            {
                throw new AppX("Could not invoke setter on VDE: " + toClass);
            }
        }

        return null;
    }
}
