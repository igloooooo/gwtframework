package com.iglooit.core.lib.server;

import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.Option;
import com.iglooit.commons.iface.util.StringUtil;
import com.iglooit.core.command.server.ExceptionAwareFn;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil
{
    private static final Log LOG = LogFactory.getLog(FileUtil.class);

    public static boolean hasXpdlExtension(String filename)
    {
        return FilenameUtils.getExtension(filename).toLowerCase(Locale.getDefault()).equals("xpdl");
    }

    /**
     * convert an input stream containing a file into a zipoutputstream, which has one file, whose contents is the
     * complete inputstream contents.
     * <p/>
     * there appears to be no non threaded (illegal) or temporary file (illegal) or non memory killing way (risky) to do
     * this, other than with bytearrayoutputstream buffers.
     * <p/>
     * for bonita to upload the file, it expects a bar / war / jar / zip with a single xpdl file entry, NOT just a
     * compressed input stream, so it is insufficient to go return ZipInputStream(is)
     */
    public static InputStream wrapZipInputStream(InputStream is) throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        zipOutputStream.putNextEntry(new ZipEntry("dummy.xpdl"));
        int c;
        while ((c = is.read()) >= 0) zipOutputStream.write(c);
        is.close();
        zipOutputStream.flush();
        zipOutputStream.closeEntry();
        zipOutputStream.close();
        byteArrayOutputStream.flush();
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    /**
     * search for 'filename' anywhere below pwd
     */
    public static Option<String> searchInPath(String filename)
    {
        return searchInPath(new File("."), filename);
    }

    /**
     * search for filename anywhere beneath path
     */
    public static Option<String> searchInPath(File path, String filename)
    {
        // first check if the filename is after this path
        String thisPath = path.getPath() + File.separator + filename;
        if (new File(thisPath).exists())
        {
            return Option.some(thisPath);
        }

        if (path.isDirectory())
        {
            final File[] children = path.listFiles();
            for (File child : children)
            {
                if (child.isDirectory())
                {
                    Option<String> recurse = searchInPath(child, filename);
                    if (recurse.isSome())
                    {
                        return recurse;
                    }
                }
            }
        }
        return Option.none();
    }

    public static List<File> findAllFilesInPath(String regex)
    {
        return findAllFilesInPath(new File("../"), regex);
    }

    public static List<File> findAllFilesInPath(File file, String regex)
    {
        List<File> result = new ArrayList<File>();
        if (file.isDirectory())
            for (File child : file.listFiles())
                result.addAll(findAllFilesInPath(child, regex));
        else
        {
            String filePath = StringUtil.emptyStringIfNull(file.getName());
            if (filePath.matches(regex))
                result.add(file);
        }
        return result;
    }

    public static List<String> listAllClassNames(Option<String> classNameFilter)
    {
        List<String> results = new ArrayList<String>();

        String[] thisClass = FileUtil.class.getName().split("\\.");
        String regexSeparator = "\\\\";
        if (File.separator.equals("/"))
            regexSeparator = "\\/";
        String regex = "^.*?" + "\\." + thisClass[0] + "\\." + thisClass[1] + "\\.";

        for (File file : FileUtil.findAllFilesInPath("^.*java\\s*$"))
        {
            String pathClassName = file.getPath().replaceAll(regexSeparator, ".").replaceAll("\\.java\\s*$", "");
            String javaClassName = pathClassName.replaceFirst(regex, thisClass[0] + "." + thisClass[1] + ".");
            if (classNameFilter.isNone() || javaClassName.matches(classNameFilter.value()))
                results.add(javaClassName);
        }
        return results;
    }

    public static List<Class> listAllClasses(Option<String> classNameFilter)
    {
        List<Class> result = new ArrayList<Class>();
        for (String className : listAllClassNames(classNameFilter))
        {
            try
            {
                Class javaClass = Class.forName(className);
                result.add(javaClass);
            }
            catch (Throwable e)
            {
                if (LOG.isTraceEnabled()) LOG.trace("Could not get class reference for className: " + className +
                    " " + e.getClass().getSimpleName());
            }
        }
        return result;
    }

    /**
     * Executes a specified Command object with a given {@link java.io.InputStream} as the single argument. The stream is closed
     * after the command finishes its job.
     *
     * @param <T> The result type returned by the given command.
     * @param in  The input stream which is handled inside the given command.
     * @param cmd The executor. It takes the single argument which is the {@link java.io.InputStream} instance. If it's
     *            <code>null</code>, nothing is executed and the given input stream is NOT touched.
     * @return Command execution result.
     */
    public static <T> T doWithStream(InputStream in, ExceptionAwareFn<T, Exception> cmd)
    {
        if (cmd != null)
        {
            try
            {
                return cmd.execute(in);
            }
            catch (Exception e)
            {
                throw new AppX("Unexpected error occurs while executing command [" + cmd.getClass().getName() + "]", e);
            }
            finally
            {
                IOUtils.closeQuietly(in);
            }
        }
        return null;
    }

    /**
     * Executes a specified Command object with a {@link java.io.InputStream} created from the given file path as the single
     * argument. The stream is closed after the command finishes its job.
     *
     * @param <T>      The result type returned by the given command.
     * @param filePath Full path (including name and extension) of the file to be handled.
     * @param cmd      The executor. It takes the single argument which is the {@link java.io.InputStream} instance for the
     *                 given file. If it's <code>null</code>, nothing is executed.
     * @return Command execution result.
     */
    public static <T> T doWithFile(String filePath, ExceptionAwareFn<T, Exception> cmd)
    {
        if (cmd != null)
        {
            InputStream in = null;
            try
            {
                in = new FileInputStream(filePath);
                return doWithStream(in, cmd);
            }
            catch (AppX e)
            {
                throw e;
            }
            catch (FileNotFoundException e)
            {
                throw new AppX("File [" + filePath + "] does not exist", e);
            }
            finally
            {
                IOUtils.closeQuietly(in);
            }
        }
        return null;
    }

    /**
     * As per doWithFile - but also accepts if filePath is a relative location on the classpath.
     * @see FileUtil#doWithFile(String, com.clarity.core.command.server.ExceptionAwareFn)
     */
    public static <T> T doWithFileOrResource(String filePath, ExceptionAwareFn<T, Exception> cmd)
    {
        if (cmd != null)
        {
            InputStream in = null;
            try
            {


                if (filePath.startsWith("classpath*:") || filePath.startsWith("classpath:"))
                {
                    //strip classpath: if exists.
                    String resourcePath = filePath.substring(filePath.indexOf(':') + 1);
                    in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
                    if (in == null)
                    {
                        throw new FileNotFoundException("Resource not found");
                    }
                }
                else
                {
                    in = new FileInputStream(filePath);
                }
                return doWithStream(in, cmd);
            }
            catch (AppX e)
            {
                throw e;
            }
            catch (FileNotFoundException e)
            {
                throw new AppX("File [" + filePath + "] does not exist", e);
            }
            finally
            {
                IOUtils.closeQuietly(in);
            }
        }
        return null;
    }

}
