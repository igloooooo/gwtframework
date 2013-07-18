package com.iglooit.commons.iface.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class is used to prevent beanlib processing annotations for a setter
 * It is particularly useful for preventing beanlib invoking setters on an inner delegate
 * where the delegate may be null.
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeanLibIgnore
{
    
}
