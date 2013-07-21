package com.iglooit.core.lib.server.spring;

import com.clarity.core.lib.server.LocaleUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LocaleFilter extends OncePerRequestFilter
{
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException
    {
        LocaleUtils.setLocale(request);
        filterChain.doFilter(request, response);
    }
}
