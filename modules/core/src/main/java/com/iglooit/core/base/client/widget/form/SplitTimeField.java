package com.iglooit.core.base.client.widget.form;

import com.clarity.commons.iface.type.AppX;
import com.clarity.core.base.client.view.resource.Resource;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;

import java.util.Date;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 23/07/12 Time: 3:46 PM
 */
public class SplitTimeField extends MultiField<Date>
{

    public enum Format
    {
        HHMMSS(true, true, true, "HHmmss"), // hour : minute : second
        HHMM(true, true, false, "HHmm"),  // hour : minute
        MMSS(false, true, true, "mmss"); // minute : second

        private boolean enableHour;
        private boolean enableMinute;
        private boolean enableSecond;

        Format(boolean hour, boolean minute, boolean second, String formatText)
        {
            enableHour = hour;
            enableMinute = minute;
            enableSecond = second;
        }
    }

    private Format format;
    private NumberField hourField;
    private NumberField minuteField;
    private NumberField secondField;

    private boolean[] invalidStatus = new boolean[3];
    private int invalidCount = 0;

    public SplitTimeField(String fieldLabel, Format format)
    {
        super(fieldLabel);
        this.format = format;

        getImages().setInvalid(Resource.ICONS.exclamationRed());
        setSpacing(3);
        hourField = createNumberFieldTemplate(0, 23);
        minuteField = createNumberFieldTemplate(0, 59);
        secondField = createNumberFieldTemplate(0, 59);

        Listener<BaseEvent> eventForwarder = new Listener<BaseEvent>()
        {
            @Override
            public void handleEvent(BaseEvent baseEvent)
            {
                fireEvent(baseEvent.getType(), baseEvent);
            }
        };

        Listener<BaseEvent> validationForwarder = new Listener<BaseEvent>()
        {
            @Override
            public void handleEvent(BaseEvent baseEvent)
            {
                int index;
                if (baseEvent.getSource() == hourField)
                    index = 0;
                else if (baseEvent.getSource() == minuteField)
                    index = 1;
                else if (baseEvent.getSource() == secondField)
                    index = 2;
                else
                    throw new AppX("Invalid split time field index");

                if (baseEvent.getType() == Events.Invalid)
                {
                    if (!invalidStatus[index])
                    {
                        invalidStatus[index] = true;
                        invalidCount++;
                    }
                    fireEvent(Events.Invalid, baseEvent);
                }
                else if (baseEvent.getType() == Events.Valid)
                {
                    if (invalidStatus[index])
                    {
                        invalidStatus[index] = false;
                        invalidCount--;
                    }
                    if (invalidCount == 0)
                        fireEvent(Events.Valid, baseEvent);
                }
            }
        };

        hourField.addListener(Events.Change, eventForwarder);
        minuteField.addListener(Events.Change, eventForwarder);
        secondField.addListener(Events.Change, eventForwarder);
        hourField.addListener(Events.Invalid, validationForwarder);
        minuteField.addListener(Events.Invalid, validationForwarder);
        secondField.addListener(Events.Invalid, validationForwarder);
        hourField.addListener(Events.Valid, validationForwarder);
        minuteField.addListener(Events.Valid, validationForwarder);
        secondField.addListener(Events.Valid, validationForwarder);
    }

    private NumberField createNumberFieldTemplate(int min, int max)
    {
        NumberField field = new NumberField();
        field.setMinValue(min);
        field.setMaxValue(max);
        field.setAllowDecimals(false);
        field.setAllowNegative(false);
        field.setFormat(NumberFormat.getFormat("##0"));
        field.setWidth(25);
        field.setPropertyEditorType(Integer.class);
        field.getImages().setInvalid(Resource.ICONS.blank());

        return field;
    }

    @Override
    protected void onRender(Element target, int index)
    {
        if (format.enableHour)
        {
            add(hourField);
            add(new LabelField(":"));
        }

        if (format.enableMinute)
            add(minuteField);

        if (format.enableSecond)
        {
            add(new LabelField(":"));
            add(secondField);
        }

        super.onRender(target, index);
    }

    @Override
    public Date getValue()
    {
        Number hh = hourField.getValue();
        Number mm = minuteField.getValue();
        Number ss = secondField.getValue();
        if (hh == null && mm == null && ss == null)
            return null;

        DateWrapper wrapper = new DateWrapper();
        wrapper = wrapper.clearTime()
            .addHours(hh == null ? 0 : hh.intValue())
            .addMinutes(mm == null ? 0 : mm.intValue())
            .addSeconds(ss == null ? 0 : ss.intValue());

        return wrapper.asDate();
    }

    @Override
    public void setValue(Date value)
    {
        if (value == null)
        {
            hourField.setValue(null);
            minuteField.setValue(null);
            secondField.setValue(null);
        }
        else
        {
            hourField.setValue(value.getHours());
            minuteField.setValue(value.getMinutes());
            secondField.setValue(value.getSeconds());
        }
    }

    public NumberField getHourField()
    {
        return hourField;
    }

    public NumberField getMinuteField()
    {
        return minuteField;
    }

    public NumberField getSecondField()
    {
        return secondField;
    }
}
