package com.iglooit.core.base.client;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.command.iface.LoginMessageConstants;
import com.google.gwt.core.client.GWT;

import java.util.HashMap;
import java.util.Map;

public class ClientI18NFactory implements I18NFactoryProvider.I18NFactory
{
    // gwt does this at compile time, not runtime
    private static Map<Class, Object> i18nIMPLS = new HashMap<Class, Object>()
    {
        {
            put(LoginMessageConstants.class, GWT.create(LoginMessageConstants.class));
        }
    };

    public synchronized <X> X get(Class<? extends X> implClass)
    {
        return (X)i18nIMPLS.get(implClass);
    }

    public <X> void addImplementation(Class<? extends X> validationConstantsClass, X gwtImpl)
    {
        i18nIMPLS.put(validationConstantsClass, gwtImpl);
    }
}
