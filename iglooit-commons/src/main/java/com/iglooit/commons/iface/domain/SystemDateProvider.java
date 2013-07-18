package com.iglooit.commons.iface.domain;

import com.clarity.commons.iface.type.AppX;

import java.util.Date;

public class SystemDateProvider
{
    private static SystemDateUtil systemDateUtil;

    /**
     * For server-side code only.
     */
    public static Date now() throws AppX
    {
        return getDate(null).now();
    }

    public static DateCacheEntry getDate(SystemDateCallback callback) throws AppX
    {
        return getDate(null, callback);
    }

    public static DateCacheEntry getDate(Date localDate, SystemDateCallback callback) throws AppX
    {
        if (systemDateUtil == null)
            throw new AppX("System date provider is not set up properly.");
        return systemDateUtil.getDate(localDate, callback);
    }

    public synchronized void setSystemDateUtil(SystemDateUtil systemDateUtil)
    {
        setSystemDateUtilStatic(systemDateUtil);
    }

    public static void setSystemDateUtilStatic(SystemDateUtil systemDateUtil)
    {
        SystemDateProvider.systemDateUtil = systemDateUtil;
    }

    public interface SystemDateCallback
    {
        void dateReady(DateCacheEntry dateCacheEntry);
    }

    public interface SystemDateUtil
    {
        DateCacheEntry getDate(Date localDate, SystemDateCallback callback) throws AppX;
    }
}
