package com.iglooit.core.lib.server.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 29/05/13 3:46 PM
 */
public class PerformanceStatsUtil
{
    private static final Log LOG = LogFactory.getLog(PerformanceStatsUtil.class);
    public static final long DEFAULT_LOG_INTERVAL = 60;
    private long max;
    private long min = Long.MAX_VALUE;
    private long topRate = 0;

    private long intervalMax;
    private long intervalMin = Long.MAX_VALUE;

    private long count;
    private long totalTime;

    private long lastReportedTime;

    private long lastStartTime;

    private boolean automaticLog = true;
    private long logInterval;
    private long lastIntervalCount;

    private String name;
    private boolean isRunning;

    public PerformanceStatsUtil()
    {
        this(null);
    }

    public PerformanceStatsUtil(String name)
    {
        this(name, DEFAULT_LOG_INTERVAL);
    }

    /**
     *
     * @param name title of this stats collector
     * @param logInterval time between logging report in seconds
     */
    public PerformanceStatsUtil(String name, long logInterval)
    {
        this.name = name;
        this.logInterval = logInterval * 1000;
    }

    public boolean isAutomaticLog()
    {
        return automaticLog;
    }

    public void setAutomaticLog(boolean automaticLog)
    {
        this.automaticLog = automaticLog;
    }

    public long getCount()
    {
        return count;
    }

    public void setCount(long count)
    {
        this.count = count;
    }

    public long getLogInterval()
    {
        return logInterval;
    }

    public void setLogInterval(long logInterval)
    {
        this.logInterval = logInterval;
    }

    public long getMax()
    {
        return max;
    }

    public void setMax(long max)
    {
        this.max = max;
    }

    public long getMin()
    {
        return min;
    }

    public void setMin(long min)
    {
        this.min = min;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void start()
    {
        lastStartTime = System.currentTimeMillis();
        isRunning = true;
    }

    public void stop(StringProvider extraMessage)
    {
        if (!isRunning)
            return;
        isRunning = false;

        long now = System.currentTimeMillis();
        long timeTaken = now - lastStartTime;

        max = Math.max(timeTaken, max);
        min = Math.min(timeTaken, min);
        intervalMax = Math.max(timeTaken, intervalMax);
        intervalMin = Math.min(timeTaken, intervalMin);
        totalTime += timeTaken;
        count++;

        tryReport(now, extraMessage);
    }

    public void stop()
    {
        stop(null);
    }

    private void tryReport(long currentTime, StringProvider extraMessage)
    {
        if (!automaticLog)
            return;

        if (!LOG.isInfoEnabled())
            return;

        if (lastReportedTime == 0)
            lastReportedTime = lastStartTime;

        if (currentTime - lastReportedTime > logInterval)
        {
            topRate = Math.max(topRate, (count - lastIntervalCount) * 1000 / logInterval);
            LOG.info(toString() + (extraMessage == null ? "" : extraMessage.getString()));
            lastIntervalCount = count;
            lastReportedTime = currentTime;
            intervalMax = 0;
            intervalMin = Long.MAX_VALUE;
        }
    }

    @Override
    public String toString()
    {
        return String.format("%s: { total=%d count=%d avg=%d min=%d max=%d top_rate=%d icount=%d " +
                "irate=%d/s imin=%d imax=%d }",
                getName(), totalTime, count, (count == 0) ? 0 : totalTime / count, min, max, topRate,
                count - lastIntervalCount, (count - lastIntervalCount) * 1000 / logInterval,
                intervalMin, intervalMax);
    }

    // for lazy eval
    public interface StringProvider
    {
        String getString();
    }
}
