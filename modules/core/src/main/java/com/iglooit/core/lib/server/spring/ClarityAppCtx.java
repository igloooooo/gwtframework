package com.iglooit.core.lib.server.spring;

import com.clarity.commons.iface.type.AppX;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

public class ClarityAppCtx
{
    private ApplicationContext appCtx;
    private static ClarityAppCtx instance;

    private static final Log LOG = LogFactory.getLog(ClarityAppCtx.class);

    public ClarityAppCtx(ApplicationContext appCtx)
    {
        if (instance != null)
        {
            //the AppX is there to allow easier debugging (ie the log will show a stack trace), but we don't actually
            //want to throw an exception here.
            LOG.warn("It appears that the " + this.getClass().getName() +
                " Singleton has been created twice - this suggests that there is 2 copies of the " +
                "Spring application context in play", new AppX());

            //we'll keep the first instnce
            return;
        }

        this.appCtx = appCtx;
        instance = this;
    }

    public static ClarityAppCtx getInstance()
    {
        if (instance == null)
            throw new AppX(ClarityAppCtx.class.getName() + " has not been initialised. Check that " +
                ClarityContextLoader.class.getName() + " is in the web.xml file");

        return instance;
    }

    public ApplicationContext getAppCtx()
    {
        return appCtx;
    }
}
