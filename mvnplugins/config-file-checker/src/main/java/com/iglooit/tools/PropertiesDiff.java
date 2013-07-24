package com.iglooit.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 31/05/13 3:32 PM
 */
public class PropertiesDiff
{
    public static final String BANNER = "------------------------------------------------------------------------\n" +
        "Comparing properties from %s to %s\n" +
        "Exclusions: %s\n" +
        "------------------------------------------------------------------------";
    public static final String VALID_OPTIONS = "tb";
    // program parameters
    private static String sourceDirectory;
    private static String checkDirectory;
    private static List<String> exclusions = Collections.emptyList();
    private static boolean fromTemplate;
    private static boolean bare;

    public static void main(String args[]) throws IOException
    {
        parseOptions(args);

        if (!bare)
            printBanner(checkDirectory, sourceDirectory, exclusions);

        Collection<String> templateList = getSortedFileTemplateCollection(sourceDirectory, fromTemplate);
        ArrayList<String> missingList = new ArrayList<String>();
        for (String templateFile : templateList)
        {
            String fileName = templateFile.substring(0, templateFile.lastIndexOf(".properties"));
            File homeFileName = new File(checkDirectory, fileName + ".properties");
            File templateFileName = new File(sourceDirectory, templateFile);
            if (homeFileName.exists())
            {
                if (exclusions.contains(fileName))
                    continue;
                compareFile(homeFileName, templateFileName);
            }
            else
                missingList.add(homeFileName.getAbsolutePath());
        }

        for (String missing : missingList)
            System.out.println("!!! Missing " + missing);
    }

    private static void printBanner(String homeDir, String templateDir, List<String> exclusions)
    {
        StringBuilder builder = new StringBuilder();
        for (String s : exclusions)
            builder.append(s).append(" ");
        System.out.println(String.format(BANNER, homeDir, templateDir, builder.toString()));
    }

    private static void parseOptions(String[] args)
    {
        // find options
        int optionArgCount = 0;
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].startsWith("-"))
            {
                optionArgCount++;
                parseOptions(args[i]);
            }
            else
                break;
        }

        if (args.length - optionArgCount < 2)
        {
            System.out.println("Usage: PropertyDiff [ -t ] {clarity_home directory} " +
                "{templates directory} [{exclusions for comparison ... }]");
            System.out.println("Example: java com.clarity.tools.PropertyDiff c:/clarity_home/ " +
                "c:/svn/trunk/modules/build-conf/templates/ modules");
            System.exit(1);
        }

        checkDirectory = args[optionArgCount];
        sourceDirectory = args[optionArgCount + 1];

        if (args.length > optionArgCount + 2)
            exclusions = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));
    }

    private static void parseOptions(String arg)
    {
        for (int i = 1; i < arg.length(); i++)
        {
            String option = arg.substring(i, i + 1);
            switch (arg.charAt(i))
            {
                case 't':
                    fromTemplate = true;
                    break;
                case 'b':
                    bare = true;
                    break;
                default:
                    System.out.println("Invalid option: " + option);
                    System.out.println("Supported options are: [" + VALID_OPTIONS + "]");
                    System.out.println("t: check against properties with template extension\t b: bare output only");
                    System.exit(1);
            }
        }
    }

    private static void compareFile(File homeFile, File templateFile) throws IOException
    {
        FileInputStream fHome = new FileInputStream(homeFile);
        FileInputStream fTemplate = new FileInputStream(templateFile);

        Properties propertyHome = new Properties();
        Properties propertyTemplate = new Properties();

        propertyHome.load(fHome);
        propertyTemplate.load(fTemplate);

        boolean titleShown = false;

        TreeSet<String> templateKeys = new TreeSet<String>(propertyTemplate.stringPropertyNames());
        for (String key : templateKeys)
        {
            if (propertyHome.containsKey(key))
                continue;
            if (!titleShown)
            {
                System.out.println("*** Properties below are missing from " + homeFile);
                titleShown = true;
            }
            System.out.println(key + "=" + propertyTemplate.get(key));
        }
    }

    private static Collection<String> getSortedFileTemplateCollection(String directoryName, final boolean fromTemplate)
    {
        File directory = new File(directoryName);
        String[] array = directory.list(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return name.endsWith(fromTemplate ? ".properties.template" : ".properties");
            }
        });

        if (array == null)
            return Collections.emptySet();

        Arrays.sort(array);
        return new TreeSet<String>(Arrays.asList(array));
    }
}
