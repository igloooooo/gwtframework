package com.iglooit.core.base.client.widget;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.lib.iface.BssTimeUtil;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;

import java.util.Date;

public enum OutputDateFormats
{
    // In future this list might be expanded to include, say, DATE_TIME_LONG, DATE_TIME_WITH_TZ, etc.
    /* TODO gx/pm: change this to use DateTimeFormat.PredefinedFormat to be able to accept locale date format
    * instead of hard codes here */
    DATE, DATE_FRIENDLY, DATE_FRIENDLY_SHORT, DATE_TIME, DATE_TIME_FRIENDLY, DATE_TIME_FRIENDLY_SHORT,
    DATE_TIME_FRIENDLY_SHORT_24, DATE_TIME_FRIENDLY_SMART, DATE_TIME_FRIENDLY_SHORT_SMART,
    TIME, TIME_12, DURATION, YEAR, DATE_SIMPLE, DATE_TIME_SIMPLE, MONTH_ONLY, HOUR_ONLY, RDATE;

    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    public static String format(Date date, String format)
    {
        if (date == null)
            return null;
        DateTimeFormat formatter = DateTimeFormat.getFormat(format);
        return formatter.format(date);
    }

    // this can be refactored to using parse function above
    public static String format(Date date, OutputDateFormats formatType)
    {
        if (date == null)
            return "";

        if (formatType.isSmartFormat())
        {
            return smartFormat(formatType, date);
        }
        else
        {
            DateTimeFormat formatter = toDateTimeFormat(formatType);
            return formatter.format(date);
        }
    }

    public static Date parse(String dateStr, String format)
    {
        if (StringUtil.isBlank(dateStr))
            return null;
        DateTimeFormat formatter = DateTimeFormat.getFormat(format);
        return formatter.parse(dateStr);
    }

    // this can be refactored to using parse function above
    public static Date parse(String dateStr, OutputDateFormats formatType)
    {
//        return parse(dateStr, formatType.toString());
        if (StringUtil.isBlank(dateStr))
            return null;

        DateTimeFormat formatter = toDateTimeFormat(formatType);
        return formatter.parse(dateStr);
    }

    public static String formatDateString(String dateStr, String fromFormat, String toFormat)
    {
        if (StringUtil.isBlank(dateStr))
            return null;
        DateTimeFormat fromFormatter = DateTimeFormat.getFormat(fromFormat);
        DateTimeFormat toFormatter = DateTimeFormat.getFormat(toFormat);

        return toFormatter.format(fromFormatter.parse(dateStr));
    }

    private static DateTimeFormat toDateTimeFormat(OutputDateFormats formatType)
    {
        return DateTimeFormat.getFormat(getDateFormatString(formatType));
    }

    private static String getDateFormatString(OutputDateFormats formatType)
    {
        String pattern = null;
        // KEEP THIS LOOSE COUPLING OF FORMAT TO PATTERN so that in future we may instead get the
        // pattern from, say, the user's preferences, eg. from the DB or a cookie.
        switch (formatType)
        {
            case DATE:
                pattern = "yyyy-MM-dd";
                break;
            case DATE_SIMPLE:
                pattern = "yyyyMMdd";
                break;
            case DATE_TIME_SIMPLE:
                pattern = "yyyyMMddHHmm";
                break;
            case DATE_FRIENDLY:
                pattern = "EEE, dd MMM yyyy";
                break;
            case DATE_FRIENDLY_SHORT:
                pattern = "dd MMM yyyy";
                break;
            case DATE_TIME:
                pattern = "yyyy-MM-dd HH:mm";
                break;
            case DATE_TIME_FRIENDLY:
                pattern = "EEE dd MMM yyyy, hh:mm a";
                break;
            case DATE_TIME_FRIENDLY_SHORT:
                pattern = "dd MMM yyyy, hh:mm a";
                break;
            case DATE_TIME_FRIENDLY_SHORT_24:
                pattern = "dd MMM yyyy, HH:mm";
                break;
            case DATE_TIME_FRIENDLY_SMART:
            case DATE_TIME_FRIENDLY_SHORT_SMART:
                throw new IllegalArgumentException("Use format(OutputDateFormats, Date) for " + formatType.toString());
            case TIME:
                pattern = "HH:mm";
                break;
            case TIME_12:
                pattern = "HH:mm a";
                break;
            case DURATION:
                pattern = "HH'h' MM'm'";
                break;
            case YEAR:
                pattern = "yyyy";
                break;
            case MONTH_ONLY:
                pattern = "MMM yyyy";
                break;
            case HOUR_ONLY:
                pattern = "dd MMM yyyy HH:00";
                break;
            case RDATE:
                pattern = "dd-MM-yyyy";
                break;
            default:
                throw new IllegalArgumentException(formatType.toString());
        }
        return pattern;
    }

    private static String smartFormat(OutputDateFormats formatType, Date date)
    {

        // KEEP THIS LOOSE COUPLING OF FORMAT TO PATTERN so that in future we may instead get the
        // pattern from, say, the user's preferences, eg. from the DB or a cookie.

        switch (formatType)
        {
            case DATE_TIME_FRIENDLY_SMART:
            case DATE_TIME_FRIENDLY_SHORT_SMART:

                // This format gives special treatment to yesterday, today, and tomorrow, displaying special text.

                Date today = BssTimeUtil.createInaccurateClientDateInvestigateMe();
//                today = new Date(2012 - 1900, 0, 24);   // for test only - delete me
                int diffDays = CalendarUtil.getDaysBetween(today, date);
                String pattern = null;

                if (diffDays >= -1 && diffDays <= 1)
                {
                    if (diffDays == -1)
                    {
                        pattern = "'" + BVC.yesterdayDateText() + "', hh:mm a";
                    }
                    else if (diffDays == 0)
                    {
                        pattern = "'" + BVC.todayDateText() + "', hh:mm a";
                    }
                    else if (diffDays == 1)
                    {
                        pattern = "'" + BVC.tomorrowDateText() + "', hh:mm a";
                    }
                }
                else
                {
                    if (formatType == DATE_TIME_FRIENDLY_SMART)
                    {
                        pattern = "EEE, dd MMM yyyy, hh:mm a";
                    }
                    else if (formatType == DATE_TIME_FRIENDLY_SHORT_SMART)
                    {
                        pattern = "dd MMM yyyy, hh:mm a";
                    }
                }

                DateTimeFormat formatter = DateTimeFormat.getFormat(pattern);
                return formatter.format(date);

            default:
                throw new IllegalArgumentException("Use format(Date) instead for " + formatType.toString());
        }
    }

    /**
     * Intended for use by DateColumnConfig only: if returns false then DateColumnConfig can set the column's
     * format to the output of toDateTimeFormat(); otherwise it must format each cell in the column independently by
     * creating a cell renderer that calls format(date).
     *
     * @param formatType
     * @return
     */
    public static boolean isFormatVariesWithDateValue(OutputDateFormats formatType)
    {
        return (formatType == DATE_TIME_FRIENDLY_SMART || formatType == DATE_TIME_FRIENDLY_SHORT_SMART);
    }

    public String format(Date date)
    {
        return OutputDateFormats.format(date, this);
    }

    /**
     * Intended for use by DateColumnConfig only - others should use format(Date) or OutputDateFormats.format(Date,
     * OutputDateFormats) instead.
     *
     * @return
     */
    public DateTimeFormat toDateTimeFormat()
    {
        return OutputDateFormats.toDateTimeFormat(this);
    }

    /**
     * This method is used by DateColumnConfig to determine whether it can set a fixed format for the column as
     * opposed to creating a cell renderer.
     *
     * @return
     */
    public boolean isSmartFormat()
    {
        return OutputDateFormats.isFormatVariesWithDateValue(this);
    }


    @Override
    public String toString()
    {
        return getDateFormatString(this);
    }
}
