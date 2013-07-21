package com.iglooit.core.lib.server;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.HibernateProxyHelper;
import org.hibernate.proxy.LazyInitializer;

/**
 * Utility class for Hibernate operations.
 *
 * @author Michael Truong
 */
public final class HibernateUtil
{

    /**
     * Checks if an entity type matches with given type.
     *
     * @param entity     Entity to check.
     * @param superclass Expected type to be checked with.
     * @return If entity is of given superclass, return TRUE.
     */
    public static boolean isEntityOfType(Object entity, Class<?> superclass)
    {
        if (entity != null)
        {
            Class<?> entityClass = HibernateProxyHelper.getClassWithoutInitializingProxy(entity);
            if (superclass.isAssignableFrom(entityClass))
            {
                return true;
            }

            // extensively try to get the target entity (requires initialization) and check entity type
            // NOTE: a "!=" operation is required to avoid any redundant check if given entity is actually the target
            Object targetEntity = getEntityWithoutProxy(entity);
            if (targetEntity != entity)
            {
                return superclass.isAssignableFrom(targetEntity.getClass());
            }
        }
        return false;
    }

    /**
     * Returns the target entity behind a Hibernate proxy.
     * <p>
     * Caution: Executing this method causes the given entity to be initialized.
     * </p>
     *
     * @param entityOrProxy Original entity (not a proxy) or a proxy.
     * @return The given object if it's not a proxy or the target entity object proxied by the given object.
     */
    public static Object getEntityWithoutProxy(Object entityOrProxy)
    {
        if (entityOrProxy instanceof HibernateProxy)
        {
            HibernateProxy proxy = (HibernateProxy)entityOrProxy;
            LazyInitializer li = proxy.getHibernateLazyInitializer();
            Object target = li.getImplementation(); // the target entity is initialized after this statement
            return target;
        }
        return entityOrProxy;
    }
}
