package com.iglooit.core.lib.iface;

import java.util.Map;

public class MapUtil
{
    /**
     * By using this method we transfer all the stuff in source to target
     * @param source the map contains all the mapping we need
     * @param target the target map we finally want to operate
     * @throws IllegalArgumentException if the target map is null
     */
    public static void setMap(Map source, Map target)
    {
        if (source == target)
            return;
        if (source == null)
            return;
        if (target == null)
            throw new IllegalArgumentException();

        target.clear();
        target.putAll(source);
    }
}
