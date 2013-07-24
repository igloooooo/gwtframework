package com.iglooit.depchecker;

import classycle.Analyser;
import classycle.dependency.DefaultResultRenderer;
import classycle.dependency.DependencyChecker;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*

misc notes

to invoke from the command line while working on it
mvn com.clarity:depchecker:1.0-SNAPSHOT:depcheck \
   -Ddepcheck.basePackage=com.clarity.performance \
   -Ddepcheck.outputDirectory=/home/mg/_work/branches/v1_6/modules/performance/target/classes

run it from the base directory of the module you are testing (performance initially)

 */

/**
 * Uses the classycle library to check for cyclic dependencies. If any are found then it
 * breaks the build, and prints the details of the dependencies to stderr.
 *
 * @goal depcheck
 * @phase process-classes
 */
public class DepCheckerPlugin extends AbstractMojo
{
    private static Logger logger = Logger.getLogger(DepCheckerPlugin.class.toString());

    /**
     * location of the war file
     *
     * @parameter expression="${depcheck.basePackage}"
     * @required
     */
    private String basePackage;

    /**
     * The directory for compiled classes.
     * Note that the outputDirectory
     *
     * @parameter expression="${depcheck.outputDirectory}" default-value="${project.build.outputDirectory}"
     * @required
     * @readonly
     */
    private File outputDirectory;

    public void setOutputDirectory(File outputDirectory)
    {
        this.outputDirectory = outputDirectory;
    }

    public void setBasePackage(String basePackage)
    {
        this.basePackage = basePackage;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        boolean ok = false;
        Analyser analyser = new Analyser(new String[]{outputDirectory.getAbsolutePath()});
        try
        {
            analyser.createClassGraph();
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Problem determining dependencies", e);
        }
        analyser.createPackageGraph();

        Map properties = System.getProperties();
        DependencyChecker dependencyChecker = new DependencyChecker(analyser,
            "check absenceOfPackageCycles > 1 in " + basePackage + ".*",
            properties,
            new DefaultResultRenderer());


        try
        {
            ok = dependencyChecker.check(new PrintWriter(new OutputStreamWriter(System.out, "UTF-8")));
        }
        catch (UnsupportedEncodingException e)
        {
            logger.log(Level.INFO, e.getMessage(), e);
        }

        if (!ok)
        {
            try
            {
                //important that this goes to system.err - if you use system.out then the mojo
                //exception is never thrown.
                analyser.printXML("Dependency Report", true,
                    new PrintWriter(new OutputStreamWriter(System.err, "UTF-8")));
            }
            catch (Throwable e)
            {
                logger.log(Level.INFO, e.getMessage(), e);
            }
            throw new MojoFailureException("Unwanted dependencies found. See output for details.");
        }

    }


}
