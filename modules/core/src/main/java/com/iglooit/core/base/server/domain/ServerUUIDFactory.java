package com.iglooit.core.base.server.domain;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.ClientUUID;
import com.clarity.commons.iface.type.UUID;
import com.clarity.core.base.iface.domain.JpaDomainEntity;
import com.clarity.core.base.iface.domain.UUIDFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Component
public final class ServerUUIDFactory implements UUIDFactory
{
    public ServerUUIDFactory()
    {

    }

    public UUID generate()
    {
        java.util.UUID tmp = java.util.UUID.randomUUID();
        return new UUID(tmp.getMostSignificantBits(), tmp.getLeastSignificantBits());
    }

    private List<Field> getAllFields(Object object)
    {
        ArrayList<Field> result = new ArrayList<Field>();

        Class objectClass = object.getClass();
        while (objectClass != null)
        {
            Collections.addAll(result, objectClass.getDeclaredFields());
            objectClass = objectClass.getSuperclass();
        }

        return result;
    }

    public void replaceUUIDs(JpaDomainEntity de)
    {
        replaceUUIDs2(de, new HashSet<JpaDomainEntity>());
    }

    private void replaceUUIDs2(JpaDomainEntity de, Set<JpaDomainEntity> visitedEntities)
    {
        if (visitedEntities.contains(de))
            return;
        else
            visitedEntities.add(de);

        if (de.getId() instanceof ClientUUID)
            de.setId(generate());

        try
        {
            for (Field field : getAllFields(de))
            {
                field.setAccessible(true);

                if (field.get(de) == null)
                    continue;
                Class fieldType = field.getType();
                if (JpaDomainEntity.class.isAssignableFrom(fieldType))
                {
                    replaceUUIDs2((JpaDomainEntity)field.get(de), visitedEntities);
                }
                else if (Map.class.isAssignableFrom(fieldType))
                {
                    Map<Object, Object> map = (Map)field.get(de);
                    Map<Object, Object> newMap = new HashMap<Object, Object>();

                    for (Map.Entry entry : map.entrySet())
                    {
                        Object key = entry.getKey();
                        if (key instanceof JpaDomainEntity)
                            replaceUUIDs2((JpaDomainEntity)key, visitedEntities);
                        Object value = entry.getValue();
                        if (value instanceof JpaDomainEntity)
                            replaceUUIDs2((JpaDomainEntity)value, visitedEntities);
                        newMap.put(key, value);
                    }

                    field.set(de, newMap);
                }
                else if (Set.class.isAssignableFrom(fieldType))
                {
                    Set<Object> set = (Set<Object>)field.get(de);
                    Set<Object> newSet = null;
                    if (SortedSet.class.isAssignableFrom(fieldType))
                        newSet = new TreeSet<Object>();
                    else
                        newSet = new HashSet<Object>();
                    for (Object o : set)
                    {
                        if (o instanceof JpaDomainEntity)
                            replaceUUIDs2((JpaDomainEntity)o, visitedEntities);
                        newSet.add(o);
                    }
                    field.set(de, newSet);
                }
                else if (List.class.isAssignableFrom(fieldType))
                {
                    List list = (List)field.get(de);

                    for (Object o : list)
                    {
                        if (o instanceof JpaDomainEntity)
                            replaceUUIDs2((JpaDomainEntity)o, visitedEntities);
                    }
                }
                else if (fieldType.isArray())
                {
                    if (!field.getName().contains("_filter_signature"))
                    {
                        Object[] array = (Object[])field.get(de);

                        for (Object o : array)
                        {
                            if (o instanceof JpaDomainEntity)
                                replaceUUIDs2((JpaDomainEntity)o, visitedEntities);
                        }
                    }
                }

            }
        }
        catch (IllegalAccessException e)
        {
            throw new AppX("Unexpected exception when replacing UUID");
        }
    }

    @PostConstruct
    void initialiseDomainEntityUUIDFactory()
    {
        JpaDomainEntity.setUuidFactory(this);
    }

}
