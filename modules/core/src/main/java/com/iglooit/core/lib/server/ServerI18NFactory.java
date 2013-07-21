package com.iglooit.core.lib.server;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.type.Tuple2;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.*;

public class ServerI18NFactory implements I18NFactoryProvider.I18NFactory
{
    private static final Log LOG = LogFactory.getLog(ServerI18NFactory.class);

    private static class ResourceBundleCache
    {
        private static Map<Tuple2<Class, String>, ResourceBundle> implCache =
            new HashMap<Tuple2<Class, String>, ResourceBundle>();

        public static synchronized Object addImpl(Class<?> cl, String locale, ResourceBundle rb)
        {
            return implCache.put(new Tuple2<Class, String>(cl, locale), rb);
        }

        public static synchronized boolean containsImpl(Class<?> cl, String locale)
        {
            return implCache.containsKey(new Tuple2<Class, String>(cl, locale));
        }

        public static synchronized ResourceBundle get(Class<?> cl, String locale)
        {
            return implCache.get(new Tuple2<Class, String>(cl, locale));
        }
    }

    private static class ProxyCache
    {
        private static Map<Class, Object> implCache = new HashMap<Class, Object>();

        public static synchronized <X> Object addImpl(Class<? extends X> cl, X impl)
        {
            return implCache.put(cl, impl);
        }

        public static synchronized <X> boolean containsImpl(Class<? extends X> cl)
        {
            return implCache.containsKey(cl);
        }

        public static synchronized <X> X get(Class<? extends X> cl)
        {
            return (X)implCache.get(cl);
        }
    }

    public synchronized <X> X get(Class<? extends X> implClass)
    {
        if (!ProxyCache.containsImpl(implClass))
        {
            X proxy = (X) I18NServerProxy.createProxy(implClass);
            ProxyCache.addImpl(implClass, proxy);
        }
        return ProxyCache.get(implClass);
    }

    public <X> void addImplementation(Class<? extends X> validationConstantsClass, X gwtImpl)
    {

    }

    private static final class I18NServerProxy implements InvocationHandler
    {
        private final Class<?> objClass;

        private synchronized Locale getLocale()
        {
            Option<String> localeOption = LocaleUtils.getGlobalLocale();
            String localeName = "";
            if (localeOption.isSome())
                localeName = localeOption.value();
            return new Locale(localeName);
        }


        private synchronized ResourceBundle getResourceBundle()
        {
            Locale locale = getLocale();
            final String localeName = locale.toString();
            if (!ResourceBundleCache.containsImpl(objClass, locale.toString()))
            {
                final String newResourceBundleName = objClass.getName();
                final ResourceBundle newResourceBundle = ResourceBundle.getBundle(newResourceBundleName,
                        locale, new UTF8Control());
                ResourceBundleCache.addImpl(objClass, localeName, newResourceBundle);
            }
            return ResourceBundleCache.get(objClass, localeName);
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            ResourceBundle resourceBundle = getResourceBundle();

            Object result = null;

            if (LOG.isTraceEnabled()) LOG.trace("i18n: resourceBundle: " + objClass.getSimpleName() +
                " locale: " + resourceBundle.getLocale().getLanguage() +
                " spring locale: " + (LocaleUtils.getLocale().isSome() ? LocaleUtils.getLocale().value() : "none"));

            boolean isLookup = method.getDeclaringClass().isAssignableFrom(ConstantsWithLookup.class)
                    && method.getName().equals("getString");
            String intlString =
                    isLookup
                  ? resourceBundle.getString((String)args[0])
                  : resourceBundle.getString(method.getName());
            result = intlString;
            if (args != null && ((isLookup && args.length > 1) || (!isLookup && args.length > 0)))
            {
                MessageFormat formatter = new MessageFormat("");
                formatter.setLocale(getLocale());
                formatter.applyPattern(intlString);
                result = formatter.format(args);
            }
            return result;
        }

        public static Object createProxy(Class<?> objClass)
        {
            return java.lang.reflect.Proxy.newProxyInstance(
                objClass.getClassLoader(),
                new Class<?>[]{objClass},
                new I18NServerProxy(objClass));
        }

        private I18NServerProxy(Class<?> objClass)
        {
            this.objClass = objClass;
        }
    }

    public static class UTF8Control extends ResourceBundle.Control
    {
        protected static final String BUNDLE_EXTENSION = "properties";
        protected static final String CHARSET = "UTF-8";

        public ResourceBundle newBundle(String baseName, Locale locale, String format,
                                        ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException
        {
            // The below code is copied from default Control#newBundle() implementation.
            // Only the PropertyResourceBundle line is changed to read the file as UTF-8.
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload)
            {
                URL url = loader.getResource(resourceName);
                if (url != null)
                {
                    URLConnection connection = url.openConnection();
                    if (connection != null)
                    {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            }
            else
            {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null)
            {
                try
                {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, CHARSET));
                }
                finally
                {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}
