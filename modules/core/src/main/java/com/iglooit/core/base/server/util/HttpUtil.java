package com.iglooit.core.base.server.util;

import com.clarity.commons.iface.type.AppX;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class HttpUtil
{
    public static String getSessionCookieFromRequest(HttpServletRequest req)
    {
        Cookie[] cookies = req.getCookies();
        String sessionCookieValue = null;

        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if ("JSESSIONID".equals(cookie.getName()))
                {
                    sessionCookieValue = cookie.getValue();
                    break;
                }
            }
        }
        return sessionCookieValue;
    }

    /**
     * this method can only be invoked within a spring web app that
     * has had request attributes setup by the filter chain.
     *
     * @return "" if no jsessionid, or  the string value of the sessionid if there is
     */
    public static String getSessionCookieFromRequest()
    {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes)
        {
            HttpServletRequest req = ((ServletRequestAttributes)requestAttributes).getRequest();
            return getSessionCookieFromRequest(req);
        }
        else
            return "";
    }

    /**
     * this method can only be invoked within a spring web app that
     * has had request attributes setup by the filter chain.
     *
     * @return the string value of the sessionid if there is
     */
    public static String getSessionCookieFromServerSide()
    {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes)
        {
            HttpServletRequest req = ((ServletRequestAttributes)requestAttributes).getRequest();
            return req.getRequestedSessionId();
        }
        else
            throw new AppX("getSessionCookieFromServerSide should always have a session");
    }

    public static HttpServletRequest getServletRequest()
    {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes)
        {
            HttpServletRequest req = ((ServletRequestAttributes)requestAttributes).getRequest();
            return req;
        }
        else
            return null;
    }

    public static String getRequestLoggingInfo()
    {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes)
        {
            HttpServletRequest req = ((ServletRequestAttributes)requestAttributes).getRequest();
            return "remoteaddr: " + req.getRemoteAddr() + ", uri:" + req.getRequestURI();
        }
        else
            return "";
    }
}
