package com.iglooit.core.lib.server;

import com.clarity.commons.iface.domain.SystemDateProvider;
import com.clarity.commons.iface.util.TimeUtil;
import com.clarity.core.lib.iface.BssTimeUtil;
import org.pojava.datetime.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServerTimeUtil
{
    public static final String FORMAT_1 = "dd-MM-yyyy";
    public static final String FORMAT_2 = "dd-MM-yyyy HH:mm:sss";
    public static final String FORMAT_3 = "dd-MM-yyyy HH:mm:ss";
    public static final String UPDATE_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_S = "yyyy-MM-dd HH:mm:ss.s";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String getStringFromTime(String format, Date date)
    {
        if (date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static final Date getTimeFromString(String format, String date) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    public static final Date getTime(String dateString)
    {
        DateTime date = DateTime.parse(dateString);
        return date.toDate();
    }

    public static final String getNowString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(UPDATE_DATE_FORMAT);
        return sdf.format(SystemDateProvider.now());
    }

    // TODO HA: Examine users of this method
    @Deprecated
    public static Date adjustDateToMultiplesOfResolution(Date startDate, Date date, BssTimeUtil.TimeUnit timeUnit,
                                                         int resolution)
    {
        long diffDate = date.getTime() - startDate.getTime();
        if (diffDate <= 0)
            return date;
        long resolutionInMilliSeconds = (long)timeUnit.getMilliSeconds() * resolution;
        diffDate = (diffDate / resolutionInMilliSeconds) * resolutionInMilliSeconds;
        return new Date(startDate.getTime() + diffDate);
    }

    // TODO HA: Examine users of this method
    @Deprecated
    public static Date addTimeUnit(Date date, BssTimeUtil.TimeUnit timeUnit, int value)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(timeUnit.getCalendarUnit(), value);
        return cal.getTime();
    }

    // TODO HA: Examine users of this method
    @Deprecated
    public static List<Date> generateDateList(Date startDate, Date endDate, BssTimeUtil.TimeUnit timeUnit,
                                              int resolution)
    {
        GregorianCalendar cal = new GregorianCalendar();
        // assume the date has been adjusted
        ArrayList<Date> dates = new ArrayList<Date>();
        cal.setTime(startDate);
        do
        {
            dates.add(cal.getTime());
            cal.add(timeUnit.getCalendarUnit(), resolution);
        }
        while (cal.getTimeInMillis() <= endDate.getTime());

        return dates;
    }

    // TODO HA: Examine users of this method
    @Deprecated
    public static Date getClosestEarlierDate(Date date, BssTimeUtil.TimeUnit unit, int resolution)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        // Remove smaller time units first
        setCalendarResolution(cal, unit);

        if (TimeUtil.TimeUnit.compare(unit, TimeUtil.TimeUnit.DAYS) < 0)
        {
            // up to days, we can use our modulo trick
            long val = cal.get(unit.getCalendarUnit());
            int overflow = (int)val % resolution;
            cal.set(unit.getCalendarUnit(), (int)(val - overflow));
        }
        else if (unit.equals(TimeUtil.TimeUnit.WEEKS))
        {
            // move to start of week and work from there
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        }
        // if we are dealing in weeks then we must use day of year first then set it to the start

        return cal.getTime();
    }

    public static Date getClosestLaterDate(Date date, BssTimeUtil.TimeUnit unit, int resolution)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        // Remove smaller time units first
        setCalendarResolution(cal, unit);

        if (TimeUtil.TimeUnit.compare(unit, TimeUtil.TimeUnit.DAYS) < 0)
        {
            // up to days, we can use our modulo trick
            long val = cal.get(unit.getCalendarUnit());
            int overflow = (int)val % resolution;
            cal.set(unit.getCalendarUnit(), (int)(val - overflow));
            int step = cal.getTime().before(date) ? 1 : 0;
            int newValue = ((int)val / resolution + step) * resolution;
            cal.set(unit.getCalendarUnit(), newValue);
        }
        else if (unit.equals(TimeUtil.TimeUnit.WEEKS))
        {
            // move to start of week and work from there
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        }
        // if we are dealing in weeks then we must use day of year first then set it to the start

        return cal.getTime();
    }

    private static void setCalendarResolution(Calendar cal, BssTimeUtil.TimeUnit unit)
    {
        for (BssTimeUtil.TimeUnit tu : Arrays.asList(TimeUtil.TimeUnit.MILLI_SECONDS, TimeUtil.TimeUnit.SECONDS,
                TimeUtil.TimeUnit.MINUTES, TimeUtil.TimeUnit.HOURS, TimeUtil.TimeUnit.DAYS, TimeUtil.TimeUnit.WEEKS))
            if (TimeUtil.TimeUnit.compare(unit, tu) > 0)
                cal.set(tu.getCalendarUnit(), 0);
    }

    public static int getTimeUnit(Date date, BssTimeUtil.TimeUnit timeUnit)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(timeUnit.getCalendarUnit());
    }

    public static Date getBeginningOfYear(int year)
    {
        Calendar startCal = Calendar.getInstance();
        startCal.set(year, 0, 0, 0, 0, 0);

        return startCal.getTime();
    }

    public static Date getEndOfYear(int year)
    {
        Calendar endCal = Calendar.getInstance();
        endCal.set(year + 1, 0, 0, 0, 0, 0);
        endCal.add(Calendar.MILLISECOND, -1);

        return endCal.getTime();
    }

    public static int daysBetween(Date date1, Date date2)
    {
        return (int)((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static Date parseGmtDateString(String dateStr, String format) throws ParseException
    {
        SimpleDateFormat gmtFormat = new SimpleDateFormat(format);
        gmtFormat.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
        return gmtFormat.parse(dateStr);
    }
}
