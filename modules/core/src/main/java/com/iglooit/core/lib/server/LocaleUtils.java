package com.iglooit.core.lib.server;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.lib.iface.LocaleCookieName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class LocaleUtils
{
    private static final Log LOG = LogFactory.getLog(LocaleUtils.class);

    public static final String LOCALE_COOKIE = LocaleCookieName.NAME;

    private static ThreadLocal<String> locale = new ThreadLocal<String>();
    private static Option<String> globalLocaleOpt = Option.none();

    /**
     * sets up the client locale for the server to use for error messages etc
     */
    public static void setLocale(HttpServletRequest request)
    {
        /*
        * The following code is to avoid reset locale back to en
        * 
        * When birt coming back with Soap request, there is no locale set there
        * to Fix it go to BssHome.java but we haven't find a way yet
        * So here we by pass the locale reset when we visit through web services
        * */
        String requestURI = request.getRequestURI();
        if (StringUtil.isNotEmpty(requestURI)
            && (requestURI.equalsIgnoreCase("/webservices/ReportWs")
            && StringUtil.isEmpty(request.getParameter("locale"))
            && StringUtil.isEmpty(request.getHeader("Accept-Language")))
            && globalLocaleOpt.isSome())
        {
            return;
        }

        String localeStr = LocaleUtils.deriveLocale(request);
        locale.set(localeStr);
        globalLocaleOpt = Option.option(localeStr);

        if (request.getSession() != null && localeStr != null && localeStr.length() > 0)
            request.getSession().setAttribute(LOCALE_COOKIE, localeStr);

        if (LOG.isTraceEnabled()) LOG.trace("Setting locale: " + localeStr);
    }

    /**
     * returns the locale if it has been set
     */
    public static Option<String> getLocale()
    {
        return Option.option(locale.get());
    }

    /**
     * returns the global locale if it has been set
     */
    public static Option<String> getGlobalLocale()
    {
        return globalLocaleOpt;
    }

    public static String getRequiredLocale()
    {
        String result = locale.get();
        if (result == null)
            throw new AppX("locale should not be null at this point. Check that the " +
                "Locale filter is set up correctly and is being invoked.");

        return result;
    }


    /**
     * Check request url for ?locale=bl_AH?foo=bar parameter. gwt also prioritises this the highest.
     * then check the session, as this is only set by user specified locale.
     * Then check cookies; these may be obsolete from an old user.
     * Then check request attributes for a browser specified locale.
     */
    public static String deriveLocale(HttpServletRequest request)
    {
        String userLocale = null;

        userLocale = request.getParameter("locale");
        if (userLocale == null || userLocale.length() == 0)
        {
            userLocale = (String)request.getSession().getAttribute(LOCALE_COOKIE);
        }
        if (userLocale == null || userLocale.length() == 0)
        {
            userLocale = getLocaleCookieByName(request);
        }
        if (userLocale == null || userLocale.length() == 0)
        {
            userLocale = (String)request.getAttribute(LOCALE_COOKIE);
            if (userLocale == null)
            {
                Locale locale = request.getLocale();
                if (locale != null)
                    userLocale = locale.toString();
            }
        }

        return userLocale;
    }

    private static String getLocaleCookieByName(HttpServletRequest request)
    {
        String userLocale = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cooky : cookies)
                if (LOCALE_COOKIE.equals(cooky.getName()))
                    userLocale = cooky.getValue();
        return userLocale;
    }


    public static boolean hasLocaleCookie(HttpServletRequest request)
    {
        return getLocaleCookieByName(request) != null;
    }
}
