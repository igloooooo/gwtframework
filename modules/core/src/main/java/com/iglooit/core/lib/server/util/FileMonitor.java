package com.iglooit.core.lib.server.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

public abstract class FileMonitor implements Runnable
{
    private static final Log LOG = LogFactory.getLog(FileMonitor.class);
    private volatile boolean interruped;
    private long checkIntervalMillis = 10000;
    private String filePath; // the full path of the file to be monitored
    // The timestamp when the monitored file is last modified
    private long lastModified = 0;

    /** Init the file path */
    public abstract String initFilePath();

    /** The name of the FileMonitor */
    public abstract String getName();

    /** Perform some necessary initialisation work before monitoring file */
    public abstract void init();
    
    /** Do something when monitored file is updated */
    public abstract void doUpdate();

    public String getFilePath()
    {
        return filePath;
    }

    /**
     * Sets the interval in milli seconds for checking the changes of monitored file.
     * @param checkIntervalMillis
     */
    public void setCheckIntervalMillis(long checkIntervalMillis)
    {
        this.checkIntervalMillis = checkIntervalMillis;
    }

    public boolean isInterruped()
    {
        return interruped;
    }

    public void setInterruped(boolean interruped)
    {
        this.interruped = interruped;
    }

    public void run()
    {
        filePath = initFilePath();
        if (filePath == null)
        {
            if (LOG.isWarnEnabled())
                LOG.warn("File path not set. " + getName() + " is not able to start");
            return;
        }
        File file = new File(filePath);
        if (!file.exists())
        {
            if (LOG.isWarnEnabled())
                LOG.warn("File " + file + " not exist. " + getName() + " is not able to start");
            return;
        }
        init();
        lastModified = file.lastModified();
        LOG.info("Starting " + getName() + " thread ... ");
        while (!interruped)
        {
            // check if file's timestamp has changed
            long currentTimeStamp = file.lastModified();
            if (lastModified != currentTimeStamp)
            {
                if (LOG.isInfoEnabled())
                    LOG.info("File " + filePath + " changed.");
                doUpdate();
                lastModified = currentTimeStamp;
            }
            else
            {
                if (LOG.isDebugEnabled())
                    LOG.debug("File " + filePath + " not changed");
            }
            try
            {
                Thread.currentThread().sleep(checkIntervalMillis);
            }
            catch (InterruptedException e)
            {
                interruped = true;
            }
        }
        LOG.info(getName() + " thread stopped.");
    }

}
