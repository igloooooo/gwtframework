package com.iglooit.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FilenameFilter;
import java.util.UUID;

import static com.iglooit.plugincore.process.ProcessRunner.runCmd;


/*

misc notes

to invoke from the command line while working on it
mvn com.clarity:sourcestripper:1.0-SNAPSHOT:stripwar \
-Dstripwar.warfile=/home/mg/_work/trunk/modules/core-war/target/clarity.war \
-Dstripwar.jarFilenameRegex=clarity.*

If the war file does not exist, run mvn package from /home/mg/_work/trunk/modules/core-war

 */

/**
 * Strips java source code from a specified war files and jar files located within the
 * war. Note that this function only runs on linux at present
 *
 * @goal stripwar
 * @phase process-sources
 */
public class StripWarSourcePlugin extends AbstractMojo
{
    /**
     * location of the war file
     *
     * @parameter expression="${stripwar.warfile}"
     * @required
     */
    private File war;

    /**
     * location of the war file
     *
     * @parameter expression="${stripwar.jarFilenameRegex}"
     * @required
     */
    private String jarFilenameRegex;
    private static final String SYSTEM_TMP_DIR = System.getProperty("java.io.tmpdir");

    public void execute() throws MojoExecutionException
    {
        if (war == null)
            throw new MojoExecutionException("either the war parameter, or the stripwar.warfile property must be set");

        if (jarFilenameRegex == null || "".equals(jarFilenameRegex))
            throw new MojoExecutionException("either the jarFilenameRegex parameter or the stripwar.jarFilenameRegex " +
                "property must be set");

        boolean usesWellKnownTempDir = isWellKnownTempDirectory();

        //not sure how reliable a way this is to determine which style of os we are running on
        boolean onLinux = System.getProperties().getProperty("path.separator").equals(":");

        String jarCmd = onLinux ? "jar" : "jar.exe";

        //log parameters
        getLog().info("Running StripWarSourcePlugin");
        getLog().info("warFile = " + war.toString());

        if (war == null || !war.exists())
            throw new MojoExecutionException("unable to find war : " + war);


        //setup working directories
        File warTmpDir = createTempDir("war");
        File jarsTmpDir = createTempDir("jar");
        getLog().info("extracting war into " + warTmpDir);

        runCmd(getLog(), warTmpDir.getAbsolutePath(), jarCmd, "xf", war.getAbsolutePath());

        //remove .java files for the war/web-inf/classes directory
        getLog().info("removing java files from the WEB-INF/classes directory");
        File webinfClassesDir = new File(warTmpDir.getAbsolutePath() + "/WEB-INF/classes");
        stripExtensionFilesFrom(webinfClassesDir, ".java");

        //Remove symbolmap from war/WEB-INF/deploy
        getLog().info("removing java files from the WEB-INF/deploy directory");
                File webinfDeployDir = new File(warTmpDir.getAbsolutePath() + "/WEB-INF/deploy");
        stripExtensionFilesFrom(webinfDeployDir, ".symbolMap");

        //for each jar file in lib
        //  expand into jar_tmp directory
        //  remove .java files
        //  rejar and replace the original jar file
        File webinfLibDir = new File(warTmpDir.getAbsolutePath() + "/WEB-INF/lib");
        getLog().info("removing java files from the jars from the WEB-INF/lib directory - " +
            webinfLibDir.getAbsolutePath());

        final File[] jarFiles = webinfLibDir.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.matches(jarFilenameRegex);
            }
        });

        if (jarFiles != null)
        {
            for (File jarFile : jarFiles)
            {
                emptyContentsOfDirectory(jarsTmpDir);

                runCmd(getLog(), jarsTmpDir.getAbsolutePath(), jarCmd, "xf", jarFile.getAbsolutePath());
                stripExtensionFilesFrom(jarsTmpDir, ".java");
                runCmd(getLog(), jarsTmpDir.getAbsolutePath(), jarCmd, "cf",
                    jarFile.getAbsolutePath(),
                    ".");
            }
        }
        else
            getLog().info("no jars found withing WEB-INF/lib");


        //rejar the war
        runCmd(getLog(), warTmpDir.getAbsolutePath(), jarCmd, "cf",
            war.getAbsolutePath(),
            ".");

        //remove all of the temporary directories if the temp dir is determined "safe" to delete (well known format)
        if (usesWellKnownTempDir)
        {
            recursiveDelete(warTmpDir);
            recursiveDelete(jarsTmpDir);
        }
        else
        {
            getLog().warn("unsure of the safety of the specified temp directory - " +
                SYSTEM_TMP_DIR + ". This task will not clean up generated temporary files");
        }
    }

    private boolean isWellKnownTempDirectory()
    {
        return SYSTEM_TMP_DIR.endsWith("/tmp")
            || SYSTEM_TMP_DIR.endsWith("/Temp")
            || SYSTEM_TMP_DIR.startsWith("/tmp/");
    }

    private static boolean emptyContentsOfDirectory(File directory)
    {
        if (directory == null)
            return false;
        if (!directory.exists())
            return true;
        if (!directory.isDirectory())
            return false;

        String[] list = directory.list();

        // Some JVMs return null for File.list() when the
        // directory is empty.
        if (list != null)
        {
            for (String filename : list)
            {
                File entry = new File(directory, filename);

                if (entry.isDirectory())
                {
                    if (!emptyContentsOfDirectory(entry))
                        return false;
                }
                else
                {
                    if (!entry.delete())
                        return false;
                }
            }
        }

        return true;
    }

    private void stripExtensionFilesFrom(File dir, String extension) throws MojoExecutionException
    {
        //listfiles returns null rather than an empty list when dir is empty
        if (dir.listFiles() == null)
        {
            getLog().info("Not stripping empty directory " + dir.getPath());
            return;
        }

        for (File file : dir.listFiles())
        {
            if (file.isDirectory())
                stripExtensionFilesFrom(file, extension);
            else
            {
                if (file.getName().endsWith(extension))
                {
                    getLog().info("Stripping: " + file.getName());
                    boolean result = file.delete();
                    if (!result)
                        throw new MojoExecutionException("unable to delete file - " + file.getAbsolutePath());
                }
            }
        }
    }

    public static File createTempDir(String seedName) throws MojoExecutionException
    {
        final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
        File newTempDir;
        final int maxAttempts = 9;
        int attemptCount = 0;
        do
        {
            attemptCount++;
            if (attemptCount > maxAttempts)
            {
                throw new MojoExecutionException(
                    "The highly improbable has occurred! Failed to " +
                        "create a unique temporary directory after " +
                        maxAttempts + " attempts.");
            }
            String dirName = UUID.randomUUID().toString();
            newTempDir = new File(sysTempDir, seedName + "_" + dirName);
        } while (newTempDir.exists());

        if (newTempDir.mkdirs())
        {
            return newTempDir;
        }
        else
        {
            throw new MojoExecutionException(
                "Failed to create temp dir named " +
                    newTempDir.getAbsolutePath());
        }
    }

    /**
     * Recursively delete file or directory
     *
     * @param fileOrDir the file or dir to delete
     * @return true iff all files are successfully deleted
     */
    public boolean recursiveDelete(File fileOrDir)
    {
        if (fileOrDir.isDirectory())
        {
            // recursively delete contents
            for (File innerFile : fileOrDir.listFiles())
            {
                if (!recursiveDelete(innerFile))
                {
                    getLog().warn("Failed to delete fileOrDir: " + fileOrDir);
                    return false;
                }
            }
        }

        return fileOrDir.delete();
    }


}
