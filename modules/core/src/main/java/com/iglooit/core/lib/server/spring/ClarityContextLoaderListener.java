package com.iglooit.core.lib.server.spring;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

public class ClarityContextLoaderListener extends ContextLoaderListener
{
    @Override
    protected ContextLoader createContextLoader()
    {
        return new ClarityContextLoader();
    }
}
