package com.iglooit.core.lib.server.util;

import org.springframework.security.cas.ServiceProperties;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextRootListener implements ServletContextListener
{
    private static final String CONTEXT_INFO_BEAN_NAME = "contextInfo";
    private static final String CAS_SERVICE_PROPERTIES_BEAN_NAME = "serviceProperties";

    @Override
    public void contextDestroyed(final ServletContextEvent event)
    {
    }

    @Override
    public void contextInitialized(ServletContextEvent event)
    {
        ServletContext ctx = event.getServletContext();
        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(ctx);
        ServletContextInfo contextInfo = (ServletContextInfo)springContext.getBean(CONTEXT_INFO_BEAN_NAME);
        contextInfo.setContextPath(event.getServletContext().getContextPath());
        ServiceProperties serviceProperties =
            (ServiceProperties)springContext.getBean(CAS_SERVICE_PROPERTIES_BEAN_NAME);
        serviceProperties.setService(contextInfo.getServiceUrl());
    }
}
