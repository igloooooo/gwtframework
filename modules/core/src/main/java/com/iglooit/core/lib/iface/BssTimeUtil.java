package com.iglooit.core.lib.iface;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.util.TimeUtil;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.scheduler.client.core.DateUtil;

import java.util.Calendar;
import java.util.Date;

public class BssTimeUtil extends TimeUtil
{
    private static final TimeConstants CONSTANTS = I18NFactoryProvider.get(TimeConstants.class);

    public static final String DATEFORMAT_1 = "yyyy-MM-dd HH:mm";
    public static final String DATEFORMAT_2 = "yyyy-MM-dd HH:mm:SS";

    // Utility methods and calculations

    public static String getClientDateStringInFormatYYYYMMDDHHMM(Date date)
    {
        return DateTimeFormat.getFormat(BssTimeUtil.DATEFORMAT_1).format(date);
    }

    public static TimeZone getTimezone()
    {
        return TimeZone.createTimeZone(getTimezoneOffset());
    }

    public static Date getBeginningOfDay(Date d)
    {
        Date myDate = new Date(d.getTime());
        DateUtil.clearTime(myDate);
        return myDate;
    }

    public static Date getEndOfDay(Date d)
    {
        Date myDate = getBeginningOfDay(d);
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.HOUR, 23);
        cal.add(Calendar.MINUTE, 59);
        cal.add(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static Date getBeginningOfDayBefore(Date date)
    {
        if (date == null)
            return null;
        return getBeginningOfDay(getPreviousDayDate(date));
    }

    public static Date getBeginningOfDayAfter(Date date)
    {
        if (date == null)
            return null;
        return getBeginningOfDay(getNextDayDate(date));
    }

    public static Date getBeginningOfWeek(Date date)
    {
        if (date == null)
            return null;
        return getBeginningOfDay(DateUtil.getFistDayInWeek(date));
    }

    public static Date getBeginningOfMonth(Date date)
    {
        if (date == null)
            return null;
        String cheatDateFormat = "MM-01-yyyy";
        String dateFormat = "MM-dd-yyyy";
        String dateString = DateTimeFormat.getFormat(cheatDateFormat).format(date);
        return DateTimeFormat.getFormat(dateFormat).parse(dateString);
    }

    public static Date getBeginningOfLastMonth(Date date)
    {
        if (date == null)
            return null;
        Date thisMonth = getBeginningOfMonth(date);
        // start of this month minus a week goes back into last month
        Date lastMonth = getBeginningOfMonth(new Date(thisMonth.getTime() - 7 * 24 * 60 * 60 * MILLISECOND_IN_SECOND));
        return lastMonth;
    }

    public static Date appendDateAndTime(Date date, Date time)
    {
        Date dateOnly = getBeginningOfDay(date);
        Date timeCopy = getBeginningOfDay(time);
        long timeComponent = time.getTime() - timeCopy.getTime();
        return new Date(dateOnly.getTime() + timeComponent);
    }

    public static int getDayOfWeek(Date currDate)
    {
        DateTimeFormat fmt = DateTimeFormat.getFormat("c");
        int dayOfWeek = Integer.parseInt(fmt.format(currDate));
        return (dayOfWeek + 6) % 7;
    }

    public static int getHourOfDay(Date currDate)
    {
        DateTimeFormat fmt = DateTimeFormat.getFormat("HH");
        return Integer.parseInt(fmt.format(currDate));
    }

    public static Date getDateFromTimeStr(String time)
    {
        // time string has to be in format HH:mm
        if (!time.matches("[0-9][0-9].[0-9][0-9]"))
            return null;
        String hourStr = time.substring(0, 2);
        String minuteStr = time.substring(3, 5);
        Integer hour = Integer.valueOf(hourStr);
        Integer minute = Integer.valueOf(minuteStr);

        Time myTime = new Time(hour, minute);
        return myTime.getDate();
    }

    /**
     * This function removes the time component from a date
     *
     * @param date a Date object that has time component
     * @return a Date object that does not have the time component (i.e. The time component is 00:00:00)
     */
    public static Date truncateTime(Date date)
    {
        DateTimeFormat sdf = DateTimeFormat.getFormat("yy/MM/dd");
        return sdf.parse(sdf.format(date));
    }

    /**
     * Returns the differens in millis between only the Time portion of the date objects (a - b)
     *
     * @return long - different in millis between only the time portion of the dates
     */
    public static long timeDiffInMillis(Date a, Date b)
    {
        long timeA = a.getTime() - getBeginningOfDay(a).getTime();
        long timeB = b.getTime() - getBeginningOfDay(b).getTime();
        return timeA - timeB;
    }

    /* This function calculates the number of days between date1 and date2
    *
    * @param date1
    * @param date2
    * @return returns the number of days between two dates
    */
    public static int daysBetween(Date date1, Date date2)
    {
        return (int)((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
    }

    // Stopwatch, to measure time lapse

    public static Stopwatch getStopwatch()
    {
        return new Stopwatch();
    }

    /**
     * Please don't use this class to schedule something in the future. You can use the appropriate Timer class Java or
     * GWT SDK.
     *
     * @see java.util.Timer
     * @see com.google.gwt.user.client.Timer
     */
    public static class Stopwatch
    {
        private long initialTimestamp;

        public Stopwatch()
        {
            reset();
        }

        public long getTimePassed()
        {
            return new Date().getTime() - initialTimestamp;
        }

        public void reset()
        {
            initialTimestamp = new Date().getTime();
        }
    }

    public static String timeDifference(Date date, Date now)
    {
        String absoluteString;
        String unitName = "";
        long unitCount = 0;

        long secondDifference = (date.getTime() - now.getTime()) / MILLISECOND_IN_SECOND;

        absoluteString = secondDifference < 0 ? CONSTANTS.timeBefore() : CONSTANTS.timeAfter();

        if (secondDifference < SECONDS_IN_MINUTE)
            unitName = CONSTANTS.timeNow(); //E.g. 'moments from now'
        else if (secondDifference < SECONDS_IN_HOUR)
            unitName = secondDifference > SECONDS_IN_MINUTE ? CONSTANTS.timeMinutes() : CONSTANTS.timeMinute();
        else if (secondDifference < SECONDS_IN_DAY)
            unitName = secondDifference > SECONDS_IN_HOUR ? CONSTANTS.timeHours() : CONSTANTS.timeHour();
        else if (secondDifference < SECONDS_IN_WEEK)
            unitName = secondDifference > SECONDS_IN_DAY ? CONSTANTS.timeDays() : CONSTANTS.timeDay();
        else if (secondDifference < SECONDS_IN_MONTH)
            unitName = secondDifference > SECONDS_IN_WEEK ? CONSTANTS.timeWeeks() : CONSTANTS.timeWeek();
        else if (secondDifference < SECONDS_IN_YEAR)
            unitName = secondDifference > SECONDS_IN_MONTH ? CONSTANTS.timeMonths() : CONSTANTS.timeMonth();
        else if (secondDifference > SECONDS_IN_YEAR)
            unitName = secondDifference > SECONDS_IN_YEAR ? CONSTANTS.timeYears() : CONSTANTS.timeYear();

        if (unitCount > 0)
            return unitCount + " " + unitName + " " + absoluteString;
        else
            return unitName + " " + absoluteString;
    }
}
