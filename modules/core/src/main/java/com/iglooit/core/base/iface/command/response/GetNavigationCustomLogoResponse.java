package com.iglooit.core.base.iface.command.response;

import com.iglooit.core.base.iface.command.Response;

public class GetNavigationCustomLogoResponse extends Response
{
    private String url;
    private String width;

    public GetNavigationCustomLogoResponse()
    {
    }

    public GetNavigationCustomLogoResponse(String url, String width)
    {
        this.url = url;
        this.width = width;
    }

    public String getUrl()
    {
        return url;
    }

    public String getWidth()
    {
        return width;
    }
}
