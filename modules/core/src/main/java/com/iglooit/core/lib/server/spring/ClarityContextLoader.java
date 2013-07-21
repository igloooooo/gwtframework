package com.iglooit.core.lib.server.spring;

import com.clarity.commons.iface.type.AppX;
import com.clarity.core.lib.iface.Constants;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ClarityContextLoader extends ContextLoader
{
    @Override
    public WebApplicationContext initWebApplicationContext(ServletContext servletContext)
    {
        WebApplicationContext result = super.initWebApplicationContext(servletContext);
        new ClarityAppCtx(result);

        return result;
    }

    @Override
    protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext appContext)
    {
        // ignores the app contexts set up in the web.xml file using the 'contextConfigLocation'
        List<String> finalLocations = loadConfigLocations();

        appContext.setConfigLocations(finalLocations.toArray(new String[finalLocations.size()]));
    }

    static List<String> loadConfigLocations()
    {
        Properties modules = loadClarityModuleConfigLocations(Constants.CLARITY_HOME);
        List<String> finalLocations = getCoreConfigLocations();

        String inboxAppCtx = null;
        for (Object moduleAppCtx : modules.values())
        {
            if (((String)moduleAppCtx).contains("applicationContext-inbox.xml"))
                inboxAppCtx = (String)moduleAppCtx;
        }

        if (inboxAppCtx != null)
        {
            finalLocations.add(inboxAppCtx);
        }

        for (Object moduleAppCtx : modules.values())
        {
            if (!((String)moduleAppCtx).contains("applicationContext-inbox.xml"))
                finalLocations.add((String)moduleAppCtx);
        }

        //Move inbox to front (if enabled), because it is a load order dependency


        return finalLocations;
    }

    /**
     * Loads all configuration locations from a given system property which specifies the "Clarity home" directory.
     *
     * @param clarityHome System property name which specifies the "Clarity home" directory. Required.
     * @return A {@link java.util.Properties} object containing all locations per modules.
     */
    public static Properties loadClarityModuleConfigLocations(String clarityHome)
    {
        String clarityHomeDir = System.getProperty(clarityHome);
        if (StringUtils.isEmpty(clarityHomeDir))
        {
            throw new AppX("Unable to continue - please set the '" + clarityHome +
                    "' environment variable when running the application");
        }

        String modulesPropertyFilename = FilenameUtils.concat(clarityHomeDir, "modules.properties");
        OrderedProperties modules = new OrderedProperties();
        InputStream modulesFile = null;
        try
        {
            if (modulesPropertyFilename.startsWith("classpath*:") || modulesPropertyFilename.startsWith("classpath:"))
            {
                String modulesPropertyResourcename =
                modulesPropertyFilename.substring(modulesPropertyFilename.indexOf(':') + 1);
                modulesFile = ClassLoader.getSystemClassLoader().getResourceAsStream(modulesPropertyResourcename);
            }
            else
            {
            modulesFile = new FileInputStream(modulesPropertyFilename);
            }
            modules.load(modulesFile);
        }
        catch (IOException e)
        {
            throw new AppX("unable to load modules.properties file in " + modulesPropertyFilename);
        }
        finally
        {
            IOUtils.closeQuietly(modulesFile);
        }
        return modules;
    }

    /**
     * Returns all core (mandatory) configuration locations.
     *
     * @return A list which can be modified later.
     */
    public static List<String> getCoreConfigLocations()
    {
        List<String> finalLocations = new LinkedList<String>();
        finalLocations.add("classpath:spring/applicationContext-core.xml");
        finalLocations.add("classpath:spring/applicationContext-oss.xml");
        finalLocations.add("classpath:spring/applicationContext-monitor.xml");
        return finalLocations;
    }
}