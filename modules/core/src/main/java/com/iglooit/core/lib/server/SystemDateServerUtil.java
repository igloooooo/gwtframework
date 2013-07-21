package com.iglooit.core.lib.server;

import com.clarity.commons.iface.domain.DateCacheEntry;
import com.clarity.commons.iface.domain.SystemDateProvider;
import com.clarity.commons.iface.type.AppX;
import com.clarity.core.command.server.domain.SystemDateHome;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component("systemDateUtil")
public class SystemDateServerUtil implements SystemDateProvider.SystemDateUtil
{
    private static final boolean DEFAULT_USE_CACHE = true;

    private final boolean useCache;
    private DateCacheEntry dateCacheEntry;

    @Resource
    private SystemDateHome systemDateHome;

    public SystemDateServerUtil()
    {
        this.useCache = DEFAULT_USE_CACHE;
    }

    @Resource
    public void setSystemDateHome(SystemDateHome systemDateHome)
    {
        this.systemDateHome = systemDateHome;
    }

    /**
     * Synchronously returns the date to the caller, callback is also called if provided.
     *
     * @param callback Called when the date is ready
     * @return A DateCache instance containing the date if no callback is provided
     */
    public DateCacheEntry getDate(Date localDate, final SystemDateProvider.SystemDateCallback callback) throws AppX
    {
        if (localDate == null)
        {
            // We only use cache when no local date is provided.
            if (useCache && dateCacheEntry != null && !dateCacheEntry.isCachedDateStale(false))
                return result(dateCacheEntry, callback);
            dateCacheEntry = systemDateHome.getDate(new Date());
            return result(dateCacheEntry, callback);
        }
        DateCacheEntry localDateCacheEntry = systemDateHome.getDate(localDate);
        return result(localDateCacheEntry, callback);
    }

    private DateCacheEntry result(DateCacheEntry dateCacheEntry, SystemDateProvider.SystemDateCallback callback)
    {
        if (callback != null)
            callback.dateReady(dateCacheEntry);
        return dateCacheEntry;
    }
}
