package com.iglooit.plugincore.process;

import org.apache.maven.plugin.logging.Log;

import java.io.File;

public class ProcessRunner
{
    public static void runCmd(Log log, String baseDir, String... args)
    {
        StringBuilder sb = new StringBuilder("running: ");
        for (String arg : args)
        {
            sb.append(arg).append(" ");
        }
        log.info(sb.toString());
        try
        {
            Process p = new ProcessBuilder(args).directory(new File(baseDir)).start();
            drain(log, p);
            int result = p.waitFor();
            if (result != 0)
            {
                StringBuilder argsString = new StringBuilder();
                for (String arg : args)
                    argsString.append(arg).append(" ");
                throw new RuntimeException("command " + argsString + " failed");
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    private static void drain(Log log, Process p)
    {
        new Thread(new Receiver(p.getErrorStream(), log)).start();
        new Thread(new Receiver(p.getInputStream(), log)).start();
    }


}
