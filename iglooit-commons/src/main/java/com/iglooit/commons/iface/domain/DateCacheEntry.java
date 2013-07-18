package com.iglooit.commons.iface.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Warning: Don't pass the date cache entry between systems with different local dates, for example don't pass this
 * object between client and server side code..
 */
public class DateCacheEntry implements Serializable
{
    private static final Integer CACHED_DATE_TTL_IN_MS = 300 * 1000;
    private static final Integer CACHED_DATE_HARD_TTL_IN_MS = 600 * 1000;

    private long initialStoredDate;
    private long initialLocalDate;

    public DateCacheEntry()
    {
    }

    public DateCacheEntry(Date localDate, Date serverDate)
    {
        initialLocalDate = localDate.getTime();
        initialStoredDate = serverDate.getTime();
    }

    public long getInitialStoredDate()
    {
        return initialStoredDate;
    }

    public void setInitialStoredDate(long initialStoredDate)
    {
        this.initialStoredDate = initialStoredDate;
    }

    public long getInitialLocalDate()
    {
        return initialLocalDate;
    }

    public void setInitialLocalDate(long initialLocalDate)
    {
        this.initialLocalDate = initialLocalDate;
    }

    public Date now()
    {
        Date now = new Date();
        Long milliSecondsLapsed = now.getTime() - initialLocalDate;
        return new Date(initialStoredDate + milliSecondsLapsed);
    }

    public boolean isCachedDateStale(boolean useHardLimit)
    {
        Integer limit = useHardLimit ? CACHED_DATE_HARD_TTL_IN_MS : CACHED_DATE_TTL_IN_MS;
        long timePassed = new Date().getTime() - initialLocalDate;
        return timePassed < 0 || timePassed > limit;
    }

    public String toString()
    {
        return "[" + new Date(initialStoredDate) + " | " + new Date(initialLocalDate) + "] -> " + now();
    }
}
