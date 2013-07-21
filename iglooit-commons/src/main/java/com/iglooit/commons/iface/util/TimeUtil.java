package com.iglooit.commons.iface.util;

import com.iglooit.commons.iface.type.AppX;

import java.util.Date;

public class TimeUtil
{
    protected static final long MILLISECOND_IN_SECOND = TimeUnit.SECONDS.getMilliSeconds();
    protected static final long SECONDS_IN_MINUTE = TimeUnit.MINUTES.getSeconds();
    protected static final long SECONDS_IN_HOUR = TimeUnit.HOURS.getSeconds();
    protected static final long SECONDS_IN_DAY = TimeUnit.HOURS.getSeconds();
    protected static final long SECONDS_IN_WEEK = TimeUnit.WEEKS.getSeconds();
    protected static final long SECONDS_IN_MONTH = SECONDS_IN_DAY * 30;
    protected static final long SECONDS_IN_YEAR = SECONDS_IN_DAY * 365;

    // Constants in java.util.Calendar
    protected static final int MILLISECOND = 14;
    protected static final int SECOND = 13;
    protected static final int MINUTE = 12;
    protected static final int HOUR_OF_DAY = 11;
    protected static final int DAY_OF_MONTH = 5;
    protected static final int DAY_OF_YEAR = 6;
    protected static final int DAY_OF_WEEK = 7;
    protected static final int WEEK_OF_YEAR = 3;
    protected static final int MONTH = 2;
    protected static final int YEAR = 1;

    // Date creation

    public static Date createInaccurateClientDateIKnowWhatImDoing()
    {
        return new Date();
    }

    @Deprecated
    public static Date createInaccurateClientDateInvestigateMe()
    {
        return new Date();
    }

    public static Date constructDate(long milliSeconds)
    {
        return new Date(milliSeconds);
    }

    public static Date constructDateFromSeconds(long seconds)
    {
        return new Date(seconds * MILLISECOND_IN_SECOND);
    }

    public static Date createMaxDate()
    {
        return new Date(Long.MAX_VALUE);
    }

    public static int getTimezoneOffset()
    {
        return new Date().getTimezoneOffset();
    }

    // Basic calendar calculations

    public static long getSeconds(Date date)
    {
        return date.getTime() / MILLISECOND_IN_SECOND;
    }

    public static Date addMilliseconds(Date date, long milliSeconds)
    {
        return new Date(date.getTime() + milliSeconds);
    }

    public static Date addSeconds(Date date, long seconds)
    {
        return new Date(date.getTime() + TimeUnit.SECONDS.getMilliSeconds() * seconds);
    }

    public static Date getBeginningOfHour(Date d)
    {
        return new Date(d.getTime() - (d.getTime() % TimeUnit.HOURS.getMilliSeconds()));
    }

    public static Date getPreviousDayDate(Date date)
    {
        if (date == null)
            return null;
        return new Date(date.getTime() - TimeUnit.DAYS.getMilliSeconds());
    }

    public static Date getNextDayDate(Date date)
    {
        if (date == null)
            return null;
        return new Date(date.getTime() + TimeUnit.DAYS.getMilliSeconds());
    }

    // Utility methods and calculations

    public static String getDiffInMinutesAndHours(Date d1, Date d2)
    {
        long timeDiff = d1.before(d2) ? d2.getTime() - d1.getTime() : d1.getTime() - d2.getTime();
        Long numOfHour = (timeDiff % TimeUnit.DAYS.getMilliSeconds()) / TimeUnit.HOURS.getMilliSeconds();
        Long numOfMin = (timeDiff % TimeUnit.HOURS.getMilliSeconds()) / TimeUnit.MINUTES.getMilliSeconds();
        String hrStr = numOfHour.toString().length() == 1 ? "0" + numOfHour.toString() : numOfHour.toString();
        String minStr = numOfMin.toString().length() == 1 ? "0" + numOfMin.toString() : numOfMin.toString();
        return hrStr + ":" + minStr;
    }


    /**
     * Return the duration difference of two input durations.
     *
     * @param duration1, format should be [0-9]+:[0-9][0-9] to represent HH:MM, or [0-9] to represent H.
     * @param duration2, refer to duration1
     * @return actual duration difference or null if the format is not right for either of the parameters.
     */
    public static String calculateDurationDifference(String duration1, String duration2)
    {
        if (duration1 == null)
            throw new IllegalArgumentException();
        if (duration2 == null)
            throw new IllegalArgumentException();
        Integer minutesOfPeriod1 = convertStrHHMMtoIntMins(duration1);
        Integer minutesOfPeriod2 = convertStrHHMMtoIntMins(duration2);
        int difference = minutesOfPeriod1 - minutesOfPeriod2;
        return convertIntMinsToStrHHMM(difference);
    }

    /**
     * Convert input duration string to integer minutes the string represents
     *
     * @param hhmm, format should be [0-9]+:[0-9][0-9] to represent HH:MM, or [0-9] to represent H.
     * @return actual minutes the string represents.
     */
    public static Integer convertStrHHMMtoIntMins(String hhmm)
    {
        if (hhmm == null || hhmm.isEmpty())
            throw new IllegalArgumentException(hhmm);
        if (hhmm.matches("[0-9]+:[0-9][0-9]"))
        {
            String[] arrHHMM = hhmm.split(":");
            int hours = Integer.parseInt(arrHHMM[0]);
            int mins = Integer.parseInt(arrHHMM[1]);
            return hours * 60 + mins;
        }
        else if (hhmm.matches("[0-9]+"))
        {
            return Integer.parseInt(hhmm) * 60;
        }
        else
        {
            throw new IllegalArgumentException(hhmm);
        }
    }

    /**
     * Convert input integer mintues to string format for displaying
     *
     * @return H if minute part is 0, actual regex for H is [0-9]+ HH:MM otherwise, actual regex for HH is [0-9]+
     */
    public static String convertIntMinsToStrHHMM(int mins)
    {
        if (mins < 60)
        {
            return "00:" + mins;
        }
        else
        {
            int hours = mins / 60;
            String strHours = formatDigitToTimeStyle(hours);
            int minutes = mins % 60;
            if (minutes == 0)
                return strHours;
            else
            {
                String strMins = formatDigitToTimeStyle(minutes);
                return strHours + ":" + strMins;
            }
        }
    }

    /**
     * Converts a duration in minutes into ?d ?h ?m format
     * @param mins
     * @return
     */
    public static String convertMinsToStrDHHMM(long mins)
    {
        long d = mins / 24 / 60;
        long h = (mins % (24 * 60)) / 60;
        long m = mins % 60;
        StringBuffer output = new StringBuffer();
        if (d > 0)
            output.append(d + "d ");
        if (h > 0)
            output.append(h + "h ");
        if (m > 0)
            output.append(m + "m");
        if (m == 0 && h == 0 && d == 0)
            output.append("0");
        return output.toString();
    }

    /**
     * Format digit to Time Style, not strictly
     */
    private static String formatDigitToTimeStyle(int digit)
    {
        if (digit < 10)
            return "0" + digit;
        else
            return String.valueOf(digit);
    }

    public static Date parseDate(String dateStr)
    {
        return new Date(Date.parse(dateStr));
    }


    public static Long getDateDiff(Date date1, Date date2, TimeUnit timeUnit)
    {
        Long diffInMillies = date2.getTime() - date1.getTime();
        return (diffInMillies / timeUnit.getMilliSeconds());
    }

    public static String getDateDiffStr(Date date1, Date date2)
    {
        Long timeDiff = getDateDiff(date1, date2, TimeUnit.MINUTES);
        return convertMinsToStrDHHMM(timeDiff);
    }


    // Time Unit
    public static enum TimeUnit
    {
        MILLI_SECONDS(1, TimeUtil.MILLISECOND),
        SECONDS(1000, TimeUtil.SECOND),
        MINUTES(60 * 1000, TimeUtil.MINUTE),
        HOURS(3600 * 1000, TimeUtil.HOUR_OF_DAY),
        DAYS(86400 * 1000, TimeUtil.DAY_OF_MONTH),
        WEEKS(604800 * 1000, TimeUtil.WEEK_OF_YEAR),
        DAY_OF_WEEK(0, TimeUtil.DAY_OF_WEEK),
        DAY_OF_YEAR(0, TimeUtil.DAY_OF_YEAR),
        MONTH(0, TimeUtil.MONTH),
        YEAR(0, TimeUtil.YEAR);

        private int milliSeconds;
        private int calendarUnit;

        private TimeUnit(int milliSeconds, int calendarUnit)
        {
            this.milliSeconds = milliSeconds;
            this.calendarUnit = calendarUnit;
        }

        public int getMilliSeconds()
        {
            if (milliSeconds < 1)
                throw new AppX(this + " is not a measurable time unit!");
            return milliSeconds;
        }

        public int getSeconds()
        {
            if (milliSeconds < 1)
                throw new AppX(this + " is not a measurable time unit!");
            return milliSeconds / 1000;
        }

        public int getCalendarUnit()
        {
            return calendarUnit;
        }

        public static int compare(TimeUnit tu1, TimeUnit tu2)
        {
            if (tu1 == tu2)
                return 0;
            if (tu1 == null)
                return -1;
            if (tu2 == null)
                return 1;
            return tu1.milliSeconds - tu2.milliSeconds;
        }
    }
}
