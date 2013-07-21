package com.iglooit.core.lib.server.util;

public class ServletContextInfo
{
    private static final String PATH_SEPARATOR = "/";

    private String contextPath;
    private String applicationServerUrl;
    private String casSecurityFilter;

    public String getContextPath()
    {
        return contextPath;
    }

    public void setContextPath(String contextPath)
    {
        this.contextPath = contextPath;
    }

    public String getApplicationServerUrl()
    {
        return applicationServerUrl;
    }

    public void setApplicationServerUrl(String applicationServerUrl)
    {
        this.applicationServerUrl = applicationServerUrl;
    }

    public String getCasSecurityFilter()
    {
        return casSecurityFilter;
    }

    public void setCasSecurityFilter(String casSecurityFilter)
    {
        this.casSecurityFilter = casSecurityFilter;
    }

    public String getServiceUrl()
    {
        return trimTrailingSeparator(trimTrailingSeparator(applicationServerUrl) + prependSeparator(contextPath)) +
            prependSeparator(casSecurityFilter);
    }

    private String trimTrailingSeparator(String str)
    {
        if (str.endsWith(PATH_SEPARATOR))
            return str.substring(0, str.lastIndexOf(PATH_SEPARATOR));
        return str;
    }

    private String prependSeparator(String str)
    {
        if (str.startsWith(PATH_SEPARATOR))
            return str;
        return PATH_SEPARATOR + str;
    }
}
