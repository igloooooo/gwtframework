package com.iglooit.core.base.client.widget.form;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.ValidationResultHandler;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Fred Date: 26/06/12 Time: 4:48 PM
 */
public class ValidatableAdapterField<T, W extends Widget> implements ClarityField<T, W>
{
    private ValidationResultHandler validationResultHandler;
    private HandlerManager valueChangeHandler = new HandlerManager(this);
    private String label = "";
    private Option<String> usageHint = Option.none();
    private ValueProvider<T> valueProvider;
    private boolean fireChangeEvents;
    private W widget;

    public interface ValueProvider<T>
    {
        T get();

        void set(T value);

        void addHandleOnChange(Listener<BaseEvent> listener);

        void removeHandleOnChange(Listener<BaseEvent> listener);
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public ValidatableAdapterField(W widget, ValueProvider<T> valueProvider)
    {
        this(widget, valueProvider, null);
    }

    public ValidatableAdapterField(W widget, ValueProvider<T> valueProvider,
                                   ValidationResultHandler validationResultHandler)
    {
        this.widget = widget;
        this.valueProvider = valueProvider;
        valueProvider.addHandleOnChange(new Listener<BaseEvent>()
        {
            @Override
            public void handleEvent(BaseEvent be)
            {
                fireValidationChangeEvent();
            }
        });
        this.validationResultHandler = validationResultHandler;
    }

    @Override
    public W getField()
    {
        return widget;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        if (validationResultHandler != null)
            validationResultHandler.handleValidationResults(validationResultList);
    }

    @Override
    public void valueExternallyChangedFrom(T oldLocalValue)
    {
        // TODO: FT: what is this supposed to do?
    }

    @Override
    public String getFieldLabel()
    {
        return label;
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        label = fieldLabel;
    }

    @Override
    public Option<String> getUsageHint()
    {
        return usageHint;
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        this.usageHint = Option.some(usageHint);
    }

    @Override
    public T getValue()
    {
        return valueProvider.get();
    }

    @Override
    public void setValue(T t)
    {
        setValue(t, false);
    }

    @Override
    public void setValue(T t, boolean fireEvents)
    {
        fireChangeEvents = fireEvents;
        valueProvider.set(t);
        fireValidationChangeEvent();
        fireChangeEvents = true;

    }

    protected void fireValidationChangeEvent()
    {
        if (fireChangeEvents)
        {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> tValueChangeHandler)
    {
        return valueChangeHandler.addHandler(ValueChangeEvent.getType(), tValueChangeHandler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event)
    {
        valueChangeHandler.fireEvent(event);
    }
}
