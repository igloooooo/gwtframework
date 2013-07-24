package com.iglooit.sso.cas;

import com.iglooit.commons.iface.type.AppX;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class DynamicContextLoader extends ContextLoader
{
    private static final String CLARITY_HOME = "clarity_home";
    private static final String CONFIGURATION_FILE = "cas-runtime.properties";
    private static final String AUTHENTICATION_MECHANISM_PROPERTY = "clarity.sso.auth.mechanism";

    @Override
    protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext appContext)
    {
        List<String> finalLocations = loadConfigLocations();
        appContext.setConfigLocations(finalLocations.toArray(new String[finalLocations.size()]));
    }

    private List<String> loadConfigLocations()
    {
        List<String> finalLocations = new LinkedList<String>();
        finalLocations.add("/WEB-INF/spring-configuration/*.xml");
        String deployerConfigurationBaseName = "/WEB-INF/deployerConfigContext";
        // Checking clarity home now
        Properties properties = loadClarityModuleConfigLocations(CLARITY_HOME);
        String mechanism = (properties == null) ? null : properties.getProperty(AUTHENTICATION_MECHANISM_PROPERTY);
        if (mechanism != null)
            deployerConfigurationBaseName += "-" + mechanism;
        finalLocations.add(deployerConfigurationBaseName + ".xml");
        return finalLocations;
    }

    public static Properties loadClarityModuleConfigLocations(String clarityHome)
    {
        String clarityHomeDir = System.getProperty(clarityHome);
        if (StringUtils.isEmpty(clarityHomeDir))
            clarityHomeDir = System.getenv(clarityHome);
        if (StringUtils.isEmpty(clarityHomeDir))
            return null;

        String configurationFile = FilenameUtils.concat(clarityHomeDir, CONFIGURATION_FILE);
        Properties modules = new Properties();
        InputStream modulesFile = null;
        try
        {
            if (configurationFile.startsWith("classpath*:") || configurationFile.startsWith("classpath:"))
            {
                String modulesPropertyResourcename =
                    configurationFile.substring(configurationFile.indexOf(':') + 1);
                modulesFile = ClassLoader.getSystemClassLoader().getResourceAsStream(modulesPropertyResourcename);
            }
            else
            {
                modulesFile = new FileInputStream(configurationFile);
            }
            modules.load(modulesFile);
        }
        catch (IOException e)
        {
            throw new AppX("unable to load " + CONFIGURATION_FILE + " file in " + configurationFile);
        }
        finally
        {
            IOUtils.closeQuietly(modulesFile);
        }
        return modules;
    }
}
