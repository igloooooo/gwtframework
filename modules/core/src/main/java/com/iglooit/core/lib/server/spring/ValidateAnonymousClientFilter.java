package com.iglooit.core.lib.server.spring;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import java.io.IOException;

public class ValidateAnonymousClientFilter implements Filter
{
    private String birtIpRange;

    @Override
    public void destroy()
    {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        WebApplicationContext applicationContext =
            WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        birtIpRange = (String)applicationContext.getBean("birtIpRange");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        if (birtIpRange.contains(servletRequest.getRemoteAddr()))
        {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        throw new AccessDeniedException("Access denied for " + servletRequest.getRemoteAddr());
    }
}
