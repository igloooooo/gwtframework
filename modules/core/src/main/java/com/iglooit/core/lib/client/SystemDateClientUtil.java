package com.iglooit.core.lib.client;

import com.clarity.commons.iface.domain.DateCacheEntry;
import com.clarity.commons.iface.domain.SystemDateProvider;
import com.clarity.commons.iface.type.AppX;
import com.clarity.core.base.client.controller.GAsyncCallback;
import com.clarity.core.base.iface.command.request.GetSystemDateRequest;
import com.clarity.core.base.iface.command.response.GetSystemDateResponse;
import com.clarity.core.command.client.CommandServiceClient;
import com.google.inject.Inject;

import java.util.Date;

public class SystemDateClientUtil implements SystemDateProvider.SystemDateUtil
{
    private static final boolean DEFAULT_USE_CACHE = true;

    private final boolean useCache;
    private final CommandServiceClient commandServiceClient;
    private DateCacheEntry dateCacheEntry;

    @Inject
    public SystemDateClientUtil(CommandServiceClient commandServiceClient)
    {
        this.useCache = DEFAULT_USE_CACHE;
        this.commandServiceClient = commandServiceClient;
    }

    private synchronized void setDateCacheEntry(DateCacheEntry dateCacheEntry)
    {
        this.dateCacheEntry = dateCacheEntry;
    }

    /**
     * Asynchronously returns the date to the caller, through the callback provided.
     *
     * @param callback Called when the date is ready
     * @return Always Null
     */
    @Override
    public DateCacheEntry getDate(Date localDate, final SystemDateProvider.SystemDateCallback callback) throws AppX
    {
        if (callback == null)
            throw new AppX("We don't support synchronous date for client-side code.");
        if (useCache && dateCacheEntry != null && !dateCacheEntry.isCachedDateStale(false))
            callback.dateReady(dateCacheEntry);
        else
        {
            commandServiceClient.run(new GetSystemDateRequest(localDate == null ? new Date() : localDate),
                new GAsyncCallback<GetSystemDateResponse>()
                {
                    @Override
                    public void onSuccess(GetSystemDateResponse result)
                    {
                        setDateCacheEntry(result.getDateCacheEntry());
                        callback.dateReady(dateCacheEntry);
                    }
                });
        }
        return null;
    }
}
