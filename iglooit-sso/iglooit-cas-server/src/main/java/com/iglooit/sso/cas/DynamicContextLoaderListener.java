package com.iglooit.sso.cas;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

public class DynamicContextLoaderListener extends ContextLoaderListener
{
    @Override
    protected ContextLoader createContextLoader()
    {
        return new DynamicContextLoader();
    }
}
