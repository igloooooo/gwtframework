package com.iglooit.core.base.client.widget.form;

import com.clarity.commons.iface.domain.DateCacheEntry;
import com.clarity.commons.iface.domain.SystemDateProvider;
import com.clarity.core.lib.iface.BssTimeUtil;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

public class DateTimeField extends LayoutContainer
{
    private ClarityDateField dateField;
    private TextTimeField timeField;

    public DateTimeField()
    {
        initWidgets();

        setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
        add(dateField, new RowData(0.58, -1));
        add(timeField.getAdapterField(), new RowData(0.42, -1, new Margins(0, 2, 0, 20)));
        setWidth(350);
        setHeight(22);
   }

    public void setCurrentDate()
    {
        SystemDateProvider.getDate(new SystemDateProvider.SystemDateCallback()
        {
            @Override
            public void dateReady(DateCacheEntry dateCacheEntry)
            {
                Date now = dateCacheEntry.now();
                dateField.setValue(now);

                try
                {
                    timeField.setValue(DateTimeFormat.getFormat("HHmmss").format(now));
                }
                catch (Exception e)
                {
                    timeField.setDefault();
                }
            }
        });
    }

    private void initWidgets()
    {
        dateField = new ClarityDateField();
        dateField.setValidateOnBlur(true);
        dateField.setAllowBlank(false);
        dateField.setAutoValidate(true);

        timeField = new TextTimeField();
    }

    public void markInvalid(String invalid)
    {
        dateField.markInvalid(invalid);
    }

    // this method validates the fields, returns null if any field is invalid
    public Date getDateTime()
    {
        if (dateField.getValue() == null || !dateField.isValueValid() || !timeField.isValueValid())
        {
            dateField.validate();
            timeField.validate();
            return null;
        }

        Date date = dateField.getValue();
        return BssTimeUtil.addMilliseconds(BssTimeUtil.getBeginningOfDay(date), timeField.getValue());
    }

    public void setTimeMode(TextTimeField.TimeMode mode)
    {
        timeField.setTimeMode(mode);
        if (mode == TextTimeField.TimeMode.HHMM24 || mode == TextTimeField.TimeMode.HHMMSS24)
        {
            setWidth(300);
        }
        else
        {
            setWidth(350);
        }
    }

    public void clear()
    {
        dateField.clear();
        timeField.clearData();
    }

    public void clearInvalids()
    {
        dateField.clearInvalid();
        timeField.setDefault();
    }

    public void setDateTime(Date dateTime, String time)
    {
        dateField.setValue(dateTime);
        try
        {
            timeField.setValue(time);
        }
        catch (Exception e)
        {
            timeField.setDefault();
        }
    }

    public void setMinutesEnabled(boolean enabled)
    {
        timeField.setMinutesEnabled(enabled);
    }

    public void setTimeFieldEnabled(boolean enabled)
    {
        timeField.setMinutesEnabled(enabled);
        timeField.setHoursEnabled(enabled);
    }

    public void setTimeFieldVisible(boolean visible)
    {
       timeField.getAdapterField().setVisible(visible);
       timeField.layout(true);
    }
}
