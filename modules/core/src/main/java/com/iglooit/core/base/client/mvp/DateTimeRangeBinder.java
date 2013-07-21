package com.iglooit.core.base.client.mvp;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.util.TimeUtil;
import com.clarity.core.base.client.composition.display.FormMode;
import com.clarity.core.base.client.composition.display.WidgetContainer;
import com.clarity.core.base.client.composition.presenter.BinderContainer;
import com.clarity.core.base.client.composition.presenter.BinderFactory;
import com.clarity.core.base.client.widget.form.DateTimeRangeField;
import com.clarity.core.base.client.widget.form.DateTimeRangePanel;
import com.clarity.core.base.client.widget.form.SimpleComboBoxValueProvider;
import com.clarity.core.base.client.widget.form.SplitTimeField;
import com.clarity.core.base.client.widget.form.ValidatableAdapterField;
import com.clarity.core.base.client.widget.form.ValidatableWrapperField;
import com.clarity.core.base.iface.domain.DateTimeRangeInfo;
import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.core.lib.iface.BssTimeUtil;
import com.clarity.core.lib.iface.TypeConverterTwoWay;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.clarity.core.base.client.widget.form.DateTimeRangeField.FieldId.FROM_TIME_ID;
import static com.clarity.core.base.client.widget.form.DateTimeRangeField.FieldId.IS_RELATIVE_ID;
import static com.clarity.core.base.client.widget.form.DateTimeRangeField.FieldId.RELATIVE_DIRECTION;
import static com.clarity.core.base.client.widget.form.DateTimeRangeField.FieldId.RELATIVE_UNIT_ID;
import static com.clarity.core.base.client.widget.form.DateTimeRangeField.FieldId.RELATIVE_VALUE_ID;
import static com.clarity.core.base.client.widget.form.DateTimeRangeField.FieldId.TO_TIME_ID;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 22/06/12 Time: 4:32 PM
 */
public class DateTimeRangeBinder extends MutableBinder<DateTimeRangeInfo> implements HasChildBindings
{
    private ArrayList<BinderContainer> childBinders;
    private DateTimeRangeField dateTimeRangeField;

    public static class SegmentedDateTimeBinder<T> extends MutableBinder<T>
    {
        private DateField dateField;
        private SplitTimeField timeField;

        public SegmentedDateTimeBinder(ClarityField<T, ?> dateHasValidatingValue,
                                       DateField dateField, SplitTimeField timeField,
                                       ValidatableMeta validatableMeta,
                                       String metaFieldName)
        {
            super(dateHasValidatingValue, validatableMeta, metaFieldName);
            this.dateField = dateField;
            this.timeField = timeField;
        }

        private boolean isBoundToTime()
        {
            return ((ClarityField)getValidatingValue()).getField() instanceof SplitTimeField;
        }

        private Date mergeDateTimeParts(Date datePart, Date timePart)
        {
            if (datePart == null)
                return null;
            else if (timePart == null)
                return datePart;
            else
                return BssTimeUtil.appendDateAndTime(datePart, timePart);
        }

        // restore only the correct segment of the date value
        @Override
        public void restoreOriginalValue()
        {
            Date original = (Date)getOriginalMetaValue();
            getValidatableMeta().set(getMetaFieldName(), getOriginalMetaValue());
            getValidatingValue().setValue((T)original, false);

            Date validatingDateAfterSet = (Date)getValidatingValue().getValue();
            if (original != null
                && (isBoundToTime()
                ? BssTimeUtil.timeDiffInMillis(original, validatingDateAfterSet) != 0
                : BssTimeUtil.daysBetween(original, validatingDateAfterSet) != 0))
                handleUnsettableValue();
            setModified(false);
        }

        @Override
        public void onValueChange(ValueChangeEvent<T> dateValueChangeEvent)
        {
            Date aggregatedDateTime = mergeDateTimeParts(dateField.getValue(), timeField.getValue());
            getValidatableMeta().set(getMetaFieldName(), aggregatedDateTime);
            setModified(true);
            forceRevalidate();
        }

        @Override
        public void saveValue()
        {
            setModified(false);
            setOriginalMetaValue((T)mergeDateTimeParts(dateField.getValue(), timeField.getValue()));
            getValidatableMeta().set(getMetaFieldName(), getOriginalMetaValue());
        }
    }

    public DateTimeRangeBinder(final DateTimeRangeField field, ValidatableMeta validatableMeta, String propertyName)
    {
        super(field, validatableMeta, propertyName);
        this.dateTimeRangeField = field;

        final DateTimeRangeInfo dateTimeRangeInfo = (DateTimeRangeInfo)validatableMeta.get(propertyName);

        BinderContainer mainBinder = new BinderContainer(new MainWidgetContainer());
        DateTimeRangePanel widget = field.getField();
        BinderFactory fromDateTimeFactory = createSegmentedDateTimeBinderFactory(widget.getFromDateField(),
                widget.getFromTimeField());
        BinderFactory toDateTimeFactory = createSegmentedDateTimeBinderFactory(widget.getToDateField(),
                widget.getToTimeField());

        mainBinder.add(field.getFieldMapping(FROM_TIME_ID), dateTimeRangeInfo, fromDateTimeFactory);
        mainBinder.add(field.getFieldMapping(TO_TIME_ID), dateTimeRangeInfo, toDateTimeFactory);
        mainBinder.add(field.getFieldMapping(RELATIVE_VALUE_ID), dateTimeRangeInfo);
        mainBinder.add(field.getFieldMapping(RELATIVE_UNIT_ID), dateTimeRangeInfo);
        mainBinder.add(field.getFieldMapping(IS_RELATIVE_ID), dateTimeRangeInfo);
        mainBinder.add(field.getFieldMapping(RELATIVE_DIRECTION), dateTimeRangeInfo);

        BinderContainer timeBinder = new BinderContainer(new TimeWidgetContainer());
        timeBinder.add(field.getFieldMapping(FROM_TIME_ID), dateTimeRangeInfo, fromDateTimeFactory);
        timeBinder.add(field.getFieldMapping(TO_TIME_ID), dateTimeRangeInfo, toDateTimeFactory);

        childBinders = new ArrayList<BinderContainer>();
        childBinders.add(mainBinder);
        childBinders.add(timeBinder);

    }

    private BinderFactory createSegmentedDateTimeBinderFactory(final DateField dateField,
                                                               final SplitTimeField timeField)
    {
        return new BinderFactory()
        {
            @Override
            public Binder createBinder(String propertyName, ValidatableMeta validatableMeta, ClarityField clarityField)
            {
                return new SegmentedDateTimeBinder(clarityField, dateField, timeField, validatableMeta, propertyName);
            }
        };
    }

    @Override
    public Collection<BinderContainer> getChildBinderContainers()
    {
        return childBinders;
    }

    private class MainWidgetContainer extends WidgetContainer
    {
        @Override
        public void selectFirstField()
        {
            DateField fromDateField = dateTimeRangeField.getField().getFromDateField();
            NumberField relativeTimeField = dateTimeRangeField.getField().getRelativeTimeField();

            if (fromDateField.isVisible())
                fromDateField.focus();
            else
                relativeTimeField.focus();
        }

        @Override
        protected ClarityField doGetField(String propertyName, FormMode formMode)
        {
            if (propertyName.equals(dateTimeRangeField.getFieldMapping(FROM_TIME_ID)))
                return new ValidatableWrapperField(dateTimeRangeField.getField().getFromDateField());
            else if (propertyName.equals(dateTimeRangeField.getFieldMapping(TO_TIME_ID)))
                return new ValidatableWrapperField(dateTimeRangeField.getField().getToDateField());
            else if (propertyName.equals(dateTimeRangeField.getFieldMapping(RELATIVE_VALUE_ID)))
                return new ValidatableWrapperField(dateTimeRangeField.getField().getRelativeTimeField(),
                    new TypeConverterTwoWay<Integer, Double>()
                    {
                        @Override
                        public Integer convertToOld(Double doubleValue)
                        {
                            return doubleValue == null ? null : doubleValue.intValue();
                        }

                        @Override
                        public Double convertToNew(Integer intValue)
                        {
                            return intValue == null ? null : intValue.doubleValue();
                        }
                    });
            else if (propertyName.equals(dateTimeRangeField.getFieldMapping(RELATIVE_UNIT_ID)))
                return new ValidatableAdapterField<TimeUtil.TimeUnit, SimpleComboBox<TimeUtil.TimeUnit>>(
                    dateTimeRangeField.getField().getRelativeTimeUnitField(),
                    new SimpleComboBoxValueProvider<TimeUtil.TimeUnit>(
                        dateTimeRangeField.getField().getRelativeTimeUnitField())
                );
            else if (propertyName.equals(dateTimeRangeField.getFieldMapping(IS_RELATIVE_ID)))
                return new ValidatableAdapterField<Boolean, Widget>(null,
                    new RangeFormatValueProvider());
            else if (propertyName.equals(dateTimeRangeField.getFieldMapping(RELATIVE_DIRECTION)))
                    return new ValidatableAdapterField<DateTimeRangeInfo.RelativeTimeDirection,
                            SimpleComboBox<DateTimeRangeInfo.RelativeTimeDirection>>(
                        dateTimeRangeField.getField().getRelativeTimeDirectionField(),
                        new SimpleComboBoxValueProvider<DateTimeRangeInfo.RelativeTimeDirection>(
                            dateTimeRangeField.getField().getRelativeTimeDirectionField())
                    );
            else
                throw new AppX("Not supported binding field: " + propertyName);
        }
    }

    private class TimeWidgetContainer extends WidgetContainer
    {
        @Override
        public void selectFirstField()
        {
            dateTimeRangeField.getField().getFromTimeField().focus();
        }

        @Override
        protected ClarityField doGetField(String propertyName, FormMode formMode)
        {
            if (propertyName.equals(dateTimeRangeField.getFieldMapping(FROM_TIME_ID)))
                return new ValidatableWrapperField(dateTimeRangeField.getField().getFromTimeField());
            else if (propertyName.equals(dateTimeRangeField.getFieldMapping(TO_TIME_ID)))
                return new ValidatableWrapperField(dateTimeRangeField.getField().getToTimeField());
            else
                throw new AppX("Not supported binding field: " + propertyName);
        }
    }

    private class RangeFormatValueProvider implements ValidatableAdapterField.ValueProvider<Boolean>
    {
        @Override
        public Boolean get()
        {
            return dateTimeRangeField.getField().getRangeFormat() == DateTimeRangePanel.RangeFormat.Relative;
        }

        @Override
        public void set(Boolean value)
        {
            dateTimeRangeField.getField().setRangeFormat(value
                ? DateTimeRangePanel.RangeFormat.Relative
                : DateTimeRangePanel.RangeFormat.Specific);
        }

        @Override
        public void addHandleOnChange(final Listener<BaseEvent> listener)
        {
            dateTimeRangeField.getField().addListener(DateTimeRangePanel.RANGEFORMATCHANGED, listener);
        }

        @Override
        public void removeHandleOnChange(final Listener<BaseEvent> listener)
        {
            dateTimeRangeField.getField().removeListener(DateTimeRangePanel.RANGEFORMATCHANGED, listener);
        }
    }

    private static final class DelayedValidationResultHandler implements ValidationResultHandler
    {
        public static final int VALIDATE_DELAY_MILLIS = 500;
        private Field field;
        private String validationShowErrorsMessage = "";
        private final Timer validationShowErrorsTimer = new Timer()
        {
            public void run()
            {
                field.forceInvalid(validationShowErrorsMessage);
            }
        };

        private DelayedValidationResultHandler(Field f)
        {
            this.field = f;
        }

        @Override
        public void handleValidationResults(List<ValidationResult> validationResultList)
        {
            validationShowErrorsTimer.cancel();
            if (validationResultList.size() == 0)
            {
                field.clearInvalid();
            }
            else
            {
                validationShowErrorsMessage = validationResultList.get(0).getReason();
                validationShowErrorsTimer.schedule(VALIDATE_DELAY_MILLIS);
            }
        }
    }
}
