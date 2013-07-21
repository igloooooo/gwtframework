package com.iglooit.core.lib.iface;

/**
 * utility interface to assist with matching behaviour
 */
public interface Matcher<M>
{
    boolean matches(M entity);
}
