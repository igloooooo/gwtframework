package com.iglooit.core.base.client.widget.form;

import com.clarity.commons.iface.domain.DateCacheEntry;
import com.clarity.commons.iface.domain.SystemDateProvider;
import com.clarity.commons.iface.util.TimeUtil;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.lib.iface.BssTimeUtil;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.Date;

public class ClarityDateTimeField extends MultiField<Date>
{

    private final TimeField timeField;
    private final DateField dateField;
    private int second;

    private boolean forceValidate = true;

    public ClarityDateTimeField()
    {
        this(1);
    }

    public ClarityDateTimeField(int dateFieldWidth, int timeFieldWidth)
    {
        this(1, dateFieldWidth, timeFieldWidth);
    }

    public ClarityDateTimeField(int increment, int dateFieldWidth, int timeFieldWidth)
    {
        super();
        dateField = new DateField();
        dateField.getImages().setInvalid(Resource.ICONS.exclamationRed());
        dateField.setWidth(dateFieldWidth);

        add(dateField);
        timeField = new TimeField();
        timeField.setFormat(DateTimeFormat.getFormat("HH:mm"));
        timeField.setTriggerAction(ComboBox.TriggerAction.ALL);
        timeField.setIncrement(increment);
        timeField.setWidth(timeFieldWidth);
        timeField.getImages().setInvalid(Resource.ICONS.exclamationRed());

        timeField.setValidator(new Validator()
        {
            @Override
            public String validate(Field<?> field, String value)
            {
                RegExp regExp = RegExp.compile("^(([0-9])|([0-1][0-9])|([2][0-3])):(([0-9])|([0-5][0-9]))$");
                MatchResult matcher = regExp.exec(value);
                if (matcher == null)
                {
                    return timeField.getMessages().getAriaText();
                }
                return null;
            }
        });
        add(timeField);
        setSpacing(20);
        timeField.addSelectionChangedListener(new SelectionChangedListener<Time>()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent<Time> timeSelectionChangedEvent)
            {
                // if datefield is null, then set the default value as now.
                if (dateField.getValue() == null)
                {
                    SystemDateProvider.getDate(new SystemDateProvider.SystemDateCallback()
                    {
                        @Override
                        public void dateReady(DateCacheEntry dateCacheEntry)
                        {
                            Date now = dateCacheEntry.now();
                            dateField.setValue(now);
                        }
                    });
                }
            }
        });
    }

    public ClarityDateTimeField(int increment)
    {
        this(increment, 85, 85);
    }

    public boolean isForceValidate()
    {
        return forceValidate;
    }

    public void setForceValidate(boolean forceValidate)
    {
        this.forceValidate = forceValidate;
    }

    public TimeField getTimeField()
    {
        return timeField;
    }

    public DateField getDateField()
    {
        return dateField;
    }

    // set default date
    // get the system time from server side
    public void setDefaultValue()
    {
        SystemDateProvider.getDate(new SystemDateProvider.SystemDateCallback()
        {
            @Override
            public void dateReady(DateCacheEntry dateCacheEntry)
            {
                Date now = dateCacheEntry.now();
                setValue(now);
            }
        });
    }

    @Override
    public Date getValue()
    {
        Date selectedDay = dateField.getValue();
        Date selectedTime = timeField.getDateValue();
        if (selectedDay == null)
        {
            return null;
        }
        else
        {
            if (selectedTime == null)
            {
                return TimeUtil.addMilliseconds(BssTimeUtil.getBeginningOfDay(selectedDay), 0);
            }
            else
            {
                DateWrapper selectedTimeWrapper = new DateWrapper(selectedTime);
                return TimeUtil.addMilliseconds(BssTimeUtil.getBeginningOfDay(selectedDay),
                    selectedTimeWrapper.getHours() * TimeUtil.TimeUnit.HOURS.getMilliSeconds() +
                        selectedTimeWrapper.getMinutes() * TimeUtil.TimeUnit.MINUTES.getMilliSeconds() +
                        second * TimeUtil.TimeUnit.SECONDS.getMilliSeconds());
            }
        }
    }


    @Override
    public void setValue(Date value)
    {
        if (value == null)
        {
            dateField.setValue(null);
            timeField.setValue(null);
        }
        else
        {
            dateField.setValue(value);
            timeField.setDateValue(value);
            second = value.getSeconds();
        }
    }

    public void setAllowBlank(boolean bAllowBlank)
    {
        dateField.setAllowBlank(bAllowBlank);
        timeField.setAllowBlank(bAllowBlank);
    }

    @Override
    public boolean validate()
    {
        if (timeField.getValue() != null && dateField.getValue() == null)
        {
            dateField.setAllowBlank(false);
        }
        else
        {
            dateField.setAllowBlank(true);
        }
        return dateField.validate() && timeField.validate();
    }

    public void addSelectionListener(final Listener<FieldEvent> listener)
    {
//        //bubble change listener merely bubbles the event, it does not bubble the value.
//        //expects the value to be pulled direct from ClarityDateTimeField
        Listener<FieldEvent> bubbleChangeListener = new Listener<FieldEvent>()
        {
            @Override
            public void handleEvent(FieldEvent fieldEvent)
            {
                ClarityDateTimeField.this.fireEvent(Events.Change);
            }
        };

        dateField.addListener(Events.Change, bubbleChangeListener);
        timeField.addListener(Events.Change, bubbleChangeListener);

        this.addListener(Events.Change, listener);
    }
}