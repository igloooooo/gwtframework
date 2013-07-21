package com.iglooit.core.lib.server.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by IntelliJ IDEA.
 * User: zsun
 * Date: 15/12/2011
 * Time: 10:48:44 AM
 * To change this template use File | Settings | File Templates.
 */
/*
 * TODO zsun:
 * Plumbr reports a memory leak for this class.
 * It might be better to control the level in runtime via other means, like JMX.
 * See this: http://www.sureshpw.com/2012/04/dynamic-logging-with-log4j.html
 * H.A.
 */
public class FileMonitorServletContextListener implements ServletContextListener
{
    private static final Log LOG = LogFactory.getLog(FileMonitorServletContextListener.class);

    private Thread fileMonitorThread = null;

    public void contextInitialized(ServletContextEvent sce)
    {
        if (fileMonitorThread == null || (!fileMonitorThread.isAlive()))
        {
            if (LOG.isInfoEnabled())
                LOG.info("Start file monitor thread ... ");
            startFileMonitorThread();
        }
    }

    public void contextDestroyed(ServletContextEvent sce)
    {
        try
        {
            if (LOG.isInfoEnabled())
                LOG.info("Interrupting file monitor thread ... ");
            fileMonitorThread.interrupt();
        }
        catch (Exception ex)
        {
            if (LOG.isInfoEnabled())
            {
            	LOG.info(ex);
            }
        }
    }

    private void startFileMonitorThread()
    {
        // Start a thread to monitor log4j.xml file
        FileMonitor log4jFileMonitor = new Log4jFileMonitor();
        log4jFileMonitor.setCheckIntervalMillis(60000);
        fileMonitorThread = new Thread(log4jFileMonitor);
        fileMonitorThread.setDaemon(true);
        fileMonitorThread.start();
    }
}
