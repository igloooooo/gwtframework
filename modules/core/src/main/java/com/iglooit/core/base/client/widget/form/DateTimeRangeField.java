package com.iglooit.core.base.client.widget.form;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.TimeUtil;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.iface.domain.DateTimeRangeInfo;
import com.clarity.core.base.iface.domain.DateTimeRangeInfoMeta;
import com.clarity.core.lib.iface.BssTimeUtil;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 22/06/12 Time: 12:23 PM
 */
public class DateTimeRangeField<T extends DateTimeRangeInfo> extends DateTimeRangePanel
    implements ClarityField<T, DateTimeRangePanel>
{
    public enum FieldId
    {
        FROM_TIME_ID, TO_TIME_ID, IS_RELATIVE_ID, RELATIVE_UNIT_ID, RELATIVE_VALUE_ID, RELATIVE_DIRECTION
    }

    private HandlerManager localHandlers = new HandlerManager(this);
    private HashMap<FieldId, String> propertyFieldMapping;

    private T localValue;

    private static final HashMap<FieldId, String> DEFAULT_PROPERTY_FIELD_MAPPING = new HashMap<FieldId, String>()
    {
        {
            put(FieldId.FROM_TIME_ID, DateTimeRangeInfoMeta.FROM_DATE_PROPERTYNAME);
            put(FieldId.TO_TIME_ID, DateTimeRangeInfoMeta.TO_DATE_PROPERTYNAME);
            put(FieldId.IS_RELATIVE_ID, DateTimeRangeInfoMeta.IS_RELATIVE_PROPERTYNAME);
            put(FieldId.RELATIVE_UNIT_ID, DateTimeRangeInfoMeta.RELATIVE_TIME_UNIT_PROPERTYNAME);
            put(FieldId.RELATIVE_VALUE_ID, DateTimeRangeInfoMeta.RELATIVE_TIME_PROPERTYNAME);
            put(FieldId.RELATIVE_DIRECTION, DateTimeRangeInfoMeta.RELATIVE_TIME_DIRECTION_PROPERTYNAME);
        }
    };

    public DateTimeRangeField()
    {
        this(Arrays.asList(TimeUtil.TimeUnit.DAYS));
    }

    public DateTimeRangeField(List<TimeUtil.TimeUnit> units)
    {
        super(units);
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {

    }

    @Override
    public void valueExternallyChangedFrom(T oldLocalValue)
    {
    }

    @Override
    public String getFieldLabel()
    {
        return "";
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
    }

    @Override
    public Option<String> getUsageHint()
    {
        return Option.none();
    }

    @Override
    public void setUsageHint(String usageHint)
    {
    }

    @Override
    public T getValue()
    {
        return localValue;
    }

    @Override
    public void setValue(T value)
    {
        setValue(value, false);
    }

    @Override
    public void setValue(T value, boolean fireEvents)
    {
        if (value != null)
        {
            getFromDateField().setValue(value.getFromDate() == null ? null
                : BssTimeUtil.getBeginningOfDay(value.getFromDate()));
            getFromTimeField().setValue(value.getFromDate() == null ? null : value.getFromDate());
            getToDateField().setValue(value.getToDate() == null ? null : BssTimeUtil.getBeginningOfDay(
                value.getToDate()));
            getToTimeField().setValue(value.getToDate() == null ? null : value.getToDate());

            setRangeFormat(value.isRelative() ? DateTimeRangePanel.RangeFormat.Relative
                    : DateTimeRangePanel.RangeFormat.Specific);
            setRelativeTimeUnit(value.getRelativeTimeUnit());
            getRelativeTimeDirectionField().setSimpleValue(value.getRelativeTimeDirection());
            getRelativeTimeDirectionReadonly().setValue(value.getRelativeTimeDirection());
            getRelativeTimeField().setValue(value.getRelativeTime());
        }
        else
        {
            getFromDateField().setValue(null);
            getFromTimeField().setValue(null);
            getToDateField().setValue(null);
            getToTimeField().setValue(null);

            setRangeFormat(DateTimeRangePanel.RangeFormat.Relative);
            setRelativeTimeUnit(null);
            getRelativeTimeDirectionField().setSimpleValue(DateTimeRangeInfo.RelativeTimeDirection.BACKWARD);
            getRelativeTimeDirectionReadonly().setValue(DateTimeRangeInfo.RelativeTimeDirection.BACKWARD);
            getRelativeTimeField().setValue(null);
        }

        localValue = value;

        if (fireEvents)
            fireValueChangeEvent(value);
    }

    private void fireValueChangeEvent(T value)
    {
        ValueChangeEvent.fire(this, value);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> tValueChangeHandler)
    {
        return localHandlers.addHandler(ValueChangeEvent.getType(), tValueChangeHandler);
    }

    public String getFieldMapping(FieldId field)
    {
        String ret = propertyFieldMapping == null ? null : propertyFieldMapping.get(field);
        return ret == null ? DEFAULT_PROPERTY_FIELD_MAPPING.get(field) : ret;
    }

    public void setFieldMapping(FieldId field, String propertyName)
    {
        if (propertyFieldMapping == null)
            propertyFieldMapping = new HashMap<FieldId, String>();

        propertyFieldMapping.put(field, propertyName);
    }

    @Override
    public DateTimeRangePanel getField()
    {
        return this;
    }
}
