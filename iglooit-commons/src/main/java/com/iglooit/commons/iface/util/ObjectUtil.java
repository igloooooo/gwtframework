package com.iglooit.commons.iface.util;

import java.util.Date;

/*
* A class that handle all object operations
* */
public class ObjectUtil
{
   public static int objectCompareTo(Object o1, Object o2)
   {
       if (o1 == null && o2 == null)
           return 0;
       else if (o1 == null)
           return -1;
       else if (o2 == null)
           return 1;

       if (o1 instanceof Number && o2 instanceof Number)
       {
           Double o1L = ((Number)o1).doubleValue();
           Double o2L = ((Number)o2).doubleValue();
           return o1L.compareTo(o2L);
       }
       else if (o1 instanceof Date && o2 instanceof Date)
       {
           return ((Date)o1).compareTo((Date)o2);
       }
       else
       {
           return o1.toString().compareTo(o2.toString());
       }
   }
}
