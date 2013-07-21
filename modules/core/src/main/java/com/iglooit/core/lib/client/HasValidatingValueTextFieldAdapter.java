package com.iglooit.core.lib.client;

import com.clarity.core.base.iface.domain.HasValidatingValue;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class HasValidatingValueTextFieldAdapter<X> implements HasValidatingValue<X>, HasValueChangeHandlers<X>
{
    private final TextField<X> textField;
    private HandlerManager handlerManager = new HandlerManager(null);

    public HasValidatingValueTextFieldAdapter(TextField<X> textField)
    {
        this.textField = textField;
    }

    public X getValue()
    {
        return textField.getValue();
    }

    public void setValue(X x)
    {
        textField.setValue(x);
    }

    public void setValue(X x, boolean b)
    {
        textField.setValue(x);
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<X> xValueChangeHandler)
    {
        HandlerRegistration handlerRegistration =
            handlerManager.addHandler(ValueChangeEvent.getType(), xValueChangeHandler);

        textField.addListener(Events.Detach, new Listener<BaseEvent>()
        {
            public void handleEvent(BaseEvent baseEvent)
            {
                ValueChangeEvent.fire(HasValidatingValueTextFieldAdapter.this, getValue());
            }
        });
        return handlerRegistration;
    }

    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        handlerManager.fireEvent(gwtEvent);
    }

    protected TextField<X> getTextField()
    {
        return textField;
    }
}
