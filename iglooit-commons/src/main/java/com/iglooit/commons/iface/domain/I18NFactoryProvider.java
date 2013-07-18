package com.iglooit.commons.iface.domain;

public class I18NFactoryProvider
{
    private static I18NFactory factory;

    public static <X> X get(Class<? extends X> implClass)
    {
        if (factory == null)
        {
            return null;
        }
        return factory.get(implClass);
    }

    public synchronized void setFactory(I18NFactory factory)
    {
        setFactoryStatic(factory);
    }

    public static void setFactoryStatic(I18NFactory i18nFactory)
    {
        I18NFactoryProvider.factory = i18nFactory;
    }

    public interface I18NFactory
    {
        <X> X get(Class<? extends X> implClass);

        <X> void addImplementation(Class<? extends X> validationConstantsClass, X gwtImpl);
    }
}
