package com.iglooit.core.base.iface.command.response;

import com.iglooit.commons.iface.domain.DateCacheEntry;
import com.iglooit.core.base.iface.command.Response;

import java.util.Date;

public class GetSystemDateResponse extends Response
{
    private DateCacheEntry dateCacheEntry;

    public GetSystemDateResponse()
    {
    }

    public GetSystemDateResponse(DateCacheEntry dateCacheEntry)
    {
        this.dateCacheEntry = dateCacheEntry;
    }

    /**
     * You can use {@link #getDateCacheEntry()} to have a more accurate timestamp available if you need to pass this
     * date around in the application. The {@link com.clarity.commons.iface.domain.DateCacheEntry} class gives you an
     * updated timestamp according to the date retrieved from server and its internal timer.
     *
     * @return Date re
     */
    public Date getDate()
    {
        return dateCacheEntry == null ? null : dateCacheEntry.now();
    }

    public DateCacheEntry getDateCacheEntry()
    {
        return dateCacheEntry;
    }

    public void setDateCacheEntry(DateCacheEntry dateCacheEntry)
    {
        this.dateCacheEntry = dateCacheEntry;
    }
}
