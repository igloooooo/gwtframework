package com.iglooit.plugincore.process;

import org.apache.maven.plugin.logging.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class Receiver implements Runnable
{
    private final InputStream is;
    private Log log;

    Receiver(InputStream is, Log log)
    {
        this.is = is;
        this.log = log;
    }

    public void run()
    {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new InputStreamReader(is, "UTF8"));
            String line;
            while ((line = br.readLine()) != null)
            {
                if (log == null)
                    System.err.println("null log? : " + line);
                else
                    log.debug(line);
            }
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("IOException receiving data from child process.");
        }
        finally
        {
            try
            {
                if (br != null)
                    br.close();
            }
            catch (IOException e)
            {
                log.warn("closing buffered reader threw exception", e);
            }
        }
    }
}



