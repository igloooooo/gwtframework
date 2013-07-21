package com.iglooit.core.lib.server.spring;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Spring Security filter that preserves the URL anchor if the authentication process
 * contains redirects (e.g. if the login is performed via CAS or form login).
 * <p/>
 * With standard redirects (default Spring Security behaviour),
 * Internet Explorer (6.0 and 8.0) discard the anchor
 * part of the URL such that e.g. GWT bookmarking does not work properly.
 * Firefox re-appends the anchor part.
 * <p/>
 * This filter replaces redirects to URLs that match a certain pattern (<code>storeUrlPattern</code>)
 * with a Javascript page that stores the URL anchor in a cookie, and replaces redirects to
 * URLs that match another pattern (<code>restoreUrlPattern</code>) with a Javascript page
 * that restores the URL anchor from that cookie. The cookie name can be set via the attribute
 * <code>cookieName</code>.
 * <p/>
 * See also http://forum.springsource.org/showthread.php?101421-Preserving-URL-anchor-when-redirecting
 */
public class RetainAnchorFilter extends GenericFilterBean
{

    private String storeUrlPattern;
    private String restoreUrlPattern;
    private String cookieName;

    public void setStoreUrlPattern(String storeUrlPattern)
    {
        this.storeUrlPattern = storeUrlPattern;
    }

    public void setRestoreUrlPattern(String restoreUrlPattern)
    {
        this.restoreUrlPattern = restoreUrlPattern;
    }

    public void setCookieName(String cookieName)
    {
        this.cookieName = cookieName;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException
    {

        ServletResponse wrappedResponse = response;
        if (response instanceof HttpServletResponse)
        {
            wrappedResponse = new RedirectResponseWrapper((HttpServletResponse)response);
        }

        chain.doFilter(request, wrappedResponse);
    }

    /**
     * HttpServletResponseWrapper that replaces the redirect by appropriate Javascript code.
     */
    private class RedirectResponseWrapper extends HttpServletResponseWrapper
    {

        public RedirectResponseWrapper(HttpServletResponse response)
        {
            super(response);
        }

        @Override
        public void sendRedirect(String location) throws IOException
        {

            HttpServletResponse response = (HttpServletResponse)getResponse();
            String redirectPageHtml = "";
            if (location.matches(storeUrlPattern))
            {
                redirectPageHtml = generateStoreAnchorRedirectPageHtml(location);
            }
            else if (location.matches(restoreUrlPattern))
            {
                redirectPageHtml = generateRestoreAnchorRedirectPageHtml(location);
            }
            else
            {
                super.sendRedirect(location);
                return;
            }
            response.setContentType("text/html;charset=UTF-8");
            response.setContentLength(redirectPageHtml.length());
            response.getWriter().write(redirectPageHtml);
        }

        private String generateStoreAnchorRedirectPageHtml(String location)
        {

            StringBuilder sb = new StringBuilder();

            sb.append("<html><head><title>Redirect Page</title>\n");
            sb.append("<script type=\"text/javascript\">\n");

            // store anchor
            sb.append("document.cookie = '").append(cookieName)
                .append("=' + escape(window.location.hash) + '; path=/';\n");

            // redirect
            sb.append("window.location = '").append(location).append("' + window.location.hash;\n");
            sb.append("</script>\n</head>\n");
            sb.append("<body><h1>Redirect Page (Store Anchor)</h1>\n");
            sb.append("<!-- {{{LOGIN_PAGE}}} -->\n");
            sb.append("Should redirect to ").append(location).append("\n");
            sb.append("</body></html>\n");

            return sb.toString();
        }

        private String generateRestoreAnchorRedirectPageHtml(String location)
        {

            StringBuilder sb = new StringBuilder();

            sb.append("<html><head><title>Redirect Page</title>\n");
            sb.append("<script type=\"text/javascript\">\n");

            // generic Javascript function to get cookie value
            sb.append("function getCookie(name) {\n");
            sb.append("var cookies = document.cookie;\n");
            sb.append("if (cookies.indexOf(name + '=') != -1) {\n");
            sb.append("var startpos = cookies.indexOf(name)+name.length+1;\n");
            sb.append("var endpos = cookies.indexOf(\";\",startpos);\n");
            sb.append("if (endpos == -1) endpos = cookies.length;\n");
            sb.append("return cookies.substring(startpos,endpos);\n");
            sb.append("} else {\n");
            sb.append("return false;\n");
            sb.append("}}\n");

            // get anchor from cookie
            sb.append("var targetAnchor = getCookie('").append(cookieName).append("');\n");

            // append to URL and redirect
            sb.append("if (targetAnchor) {\n");
            sb.append("window.location = '").append(location).append("' + unescape(targetAnchor);\n");
            sb.append("} else {\n");
            sb.append("window.location = '").append(location).append("';\n");
            sb.append("}\n");
            sb.append("</script></head>\n");
            sb.append("<body><h1>Redirect Page (Restore Anchor)</h1>\n");
            sb.append("<!-- {{{LOGIN_PAGE}}} -->\n");
            sb.append("Should redirect to ").append(location).append("\n");
            sb.append("</body></html>\n");

            return sb.toString();
        }

    }
}