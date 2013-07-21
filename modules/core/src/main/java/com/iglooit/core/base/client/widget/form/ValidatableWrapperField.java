package com.iglooit.core.base.client.widget.form;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.DelayedTimeValidationResultHandler;
import com.clarity.core.base.client.mvp.ValidationResultHandler;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.lib.iface.TypeConverter;
import com.clarity.core.lib.iface.TypeConverterTwoWay;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 25/06/12 Time: 3:42 PM
 */
public class ValidatableWrapperField<T, FT extends Field, FieldType> implements ClarityField<T, FT>
{
    private FT field;
    private boolean fireChangeEvents;
    private ValueChangedHandlerWrapper valueChangeValidationWrapper;
    private TypeConverter<T, ?> typeConverter;
    private TypeConverterTwoWay<T, FieldType> typeConverterTwoWay;

    public ValidatableWrapperField(FT field)
    {
        this(field, null, null, null, null);
    }

    public ValidatableWrapperField(FT field, TypeConverter<T, ?> typeConverter)
    {
        this(field, null, null, typeConverter, null);
    }

    public ValidatableWrapperField(FT field, TypeConverterTwoWay<T, FieldType> typeConverter)
    {
        this(field, null, null, null, typeConverter);
    }

    public ValidatableWrapperField(FT field, HasValueChangeHandlers<T> valueChangeHandler,
                                   ValidationResultHandler validationResultHandler,
                                   TypeConverter<T, ?> typeConverter,
                                   TypeConverterTwoWay<T, FieldType> typeConverterTwoWay)
    {
        this.field = field;
        this.valueChangeValidationWrapper = new ValueChangedHandlerWrapper(valueChangeHandler, validationResultHandler);

        if (typeConverter != null && typeConverterTwoWay != null)
            throw new AppX("Only one type converter allowed");
        this.typeConverter = typeConverter;
        this.typeConverterTwoWay = typeConverterTwoWay;
    }

    @Override
    public FT getField()
    {
        return field;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        valueChangeValidationWrapper.handleValidationResults(validationResultList);
    }

    @Override
    public void valueExternallyChangedFrom(T oldLocalValue)
    {
    }

    @Override
    public String getFieldLabel()
    {
        return StringUtil.emptyStringIfNull(field.getFieldLabel());
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        field.setFieldLabel(fieldLabel);
    }

    @Override
    public Option<String> getUsageHint()
    {
        return Option.option(field.getEmptyText());
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        field.setEmptyText(usageHint);
    }

    @Override
    public T getValue()
    {
        FieldType value = (FieldType)field.getValue();
        if (typeConverterTwoWay != null)
            return typeConverterTwoWay.convertToOld(value);
        else
            return (T)value;
    }

    @Override
    public void setValue(T value)
    {
        this.setValue(value, false);
    }

    protected void fireValidationChangeEvent()
    {
        if (fireChangeEvents)
        {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @Override
    public void setValue(T value, boolean fireEvents)
    {
        fireChangeEvents = fireEvents;

        if (typeConverter != null)
            field.setValue(typeConverter.convert(value));
        else if (typeConverterTwoWay != null)
            field.setValue(typeConverterTwoWay.convertToNew(value));
        else
            field.setValue(value);
        fireValidationChangeEvent();
        fireChangeEvents = true;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> tValueChangeHandler)
    {
        return valueChangeValidationWrapper.addValueChangeHandler(tValueChangeHandler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event)
    {
        valueChangeValidationWrapper.fireEvent(event);
    }

    private final class ValueChangedHandlerWrapper implements HasValueChangeHandlers<T>, ValidationResultHandler
    {
        private DelayedTimeValidationResultHandler delayedValidationResultHandler;
        private HasValueChangeHandlers<T> valueChangeHandler;
        private ValidationResultHandler validationResultHandler;

        private ValueChangedHandlerWrapper(HasValueChangeHandlers<T> valueChangeHandler,
                                           ValidationResultHandler validationResultHandler)
        {
            this.valueChangeHandler = valueChangeHandler;
            this.validationResultHandler = validationResultHandler;
            if (valueChangeHandler == null || validationResultHandler == null)
            {
                delayedValidationResultHandler = new DelayedTimeValidationResultHandler(
                    ValidatableWrapperField.this, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        fireValidationChangeEvent();
                    }
                });
            }
        }

        @Override
        public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> tValueChangeHandler)
        {
            if (valueChangeHandler != null)
                return valueChangeHandler.addValueChangeHandler(tValueChangeHandler);
            else
                return delayedValidationResultHandler.addValueChangeHandler(tValueChangeHandler);
        }

        @Override
        public void fireEvent(GwtEvent<?> event)
        {
            if (valueChangeHandler != null)
                valueChangeHandler.fireEvent(event);
            else
                delayedValidationResultHandler.fireEvent(event);
        }


        @Override
        public void handleValidationResults(List<ValidationResult> validationResultList)
        {
            if (validationResultHandler != null)
                validationResultHandler.handleValidationResults(validationResultList);
            else
                delayedValidationResultHandler.handleValidationResults(validationResultList);
        }
    }
}
