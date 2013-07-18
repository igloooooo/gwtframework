package com.iglooit.commons.iface.domain;

import java.io.Serializable;
import java.util.Date;

public class DateCache implements Serializable
{
    private static final Integer CACHED_DATE_TTL_IN_MS = 300 * 1000;
    private static final Integer CACHED_DATE_HARD_TTL_IN_MS = 600 * 1000;
    private long initialStoredDate;
    private long initialLocalDate;

    public DateCache()
    {
    }

    public DateCache(Date serverDate)
    {
        refresh(serverDate);
    }

    public void refresh(Date serverDate)
    {
        initialLocalDate = new Date().getTime();
        initialStoredDate = serverDate.getTime();
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
}
