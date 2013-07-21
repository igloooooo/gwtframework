package com.iglooit.core.lib.server.util;

import com.clarity.core.lib.iface.Constants;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: zsun
 * Date: 15/12/2011
 * Time: 10:51:52 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * This class is created for monitoring Log4j.xml file, so that logging level
 * can be changed during the application runtime
 */
public class Log4jFileMonitor extends FileMonitor
{
    private static final String LOG4J_FILE_NAME = "log4j.xml";
    private static final String LOG4J_DEV_FILE_NAME = "log4j-dev.xml";
    
    private static final Log LOG = LogFactory.getLog(Log4jFileMonitor.class);

    /**
     * Searches the log4j configuration file. This method is called periodically. The algorithm is:
     * <ul>
     * <li>If there is a System property "log4j.configuration" specifying an existing log4j config file, use it.
     * <li>If not, try to use the config file from $CLARITY_HOME/log4j.xml.
     * <li>If not, try to use the config file from $CLARITY_HOME/log4j-dev.xml for development mode.
     * <li>If not, try to use the config file from the classpath root.
     * <li>If there is no file, a runtime exception is raised.
     * </ul>
     */
    @Override
    public String initFilePath()
    {
        String path = null;

        String log4jConfigLocation = System.getProperty("log4j.configuration");
        if (fileExists(log4jConfigLocation))
        {
            path = log4jConfigLocation;
        }
        else
        {
            String clarityHome = System.getProperty(Constants.CLARITY_HOME);
            if (!StringUtils.isEmpty(clarityHome))
            {
                path = FilenameUtils.concat(clarityHome, LOG4J_FILE_NAME);
                if (!fileExists(path))
                {
                    path = FilenameUtils.concat(clarityHome, LOG4J_DEV_FILE_NAME);
                }
            }

            if (!fileExists(path))
            {
                URL url = Log4jFileMonitor.class.getResource("/log4j.xml");
                path = url.getPath();
            }
        }

        if (!fileExists(path))
        {
            throw new IllegalStateException("Log4j configuration file [" + path + "] does not exist");
        }
        LOG.info("Log4j configuration file to monitor: " + path);

        return path;
    }

    private boolean fileExists(String path)
    {
        return !StringUtils.isEmpty(path) && new File(path).exists();
    }

    @Override
    public String getName()
    {
        return "Log4j File Monitor";
    }

    @Override
    public void init()
    {
        loadLog4jFile();
    }

    @Override
    public void doUpdate()
    {
        loadLog4jFile();
    }

    private void loadLog4jFile()
    {
        DOMConfigurator.configure(getFilePath());
    }

}
