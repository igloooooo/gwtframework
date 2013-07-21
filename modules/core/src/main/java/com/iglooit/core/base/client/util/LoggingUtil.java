package com.iglooit.core.base.client.util;

import java.util.Date;
import java.util.logging.Logger;

public class LoggingUtil
{
    private static Logger logger = Logger.getLogger(LoggingUtil.class.getName());
    private static Long logTime = 0L;

    public static void resetLogTimer()
    {
        logTime = (new Date()).getTime();
        logger.fine("Start logging process time.");
    }

    public static Long getLogTime()
    {
        Long nowTime = (new Date()).getTime();
        return (nowTime - logTime) / 1000;
    }
}
