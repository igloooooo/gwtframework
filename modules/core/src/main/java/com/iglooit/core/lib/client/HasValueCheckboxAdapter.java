package com.iglooit.core.lib.client;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

public class HasValueCheckboxAdapter implements HasValue<Boolean>
{
    private final CheckBox checkBox;
    private final HandlerManager eventBus = new HandlerManager(null);

    public HasValueCheckboxAdapter(CheckBox checkBox)
    {
        this.checkBox = checkBox;
    }

    public Boolean getValue()
    {
        return checkBox.getValue();
    }

    public void setValue(Boolean value)
    {
        checkBox.setValue(value);
    }

    public void setValue(Boolean value, boolean b)
    {
        setValue(value);
    }

    public void lockCheckBox(String lockMsg)
    {
        checkBox.hide();
//        checkBox.disable();
    }

    public void unlockCheckBox()
    {
        checkBox.show();
//        checkBox.enable();
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler)
    {
        HandlerRegistration handlerRegistration = eventBus.addHandler(ValueChangeEvent.getType(), handler);

        checkBox.addListener(Events.Change, new Listener<FieldEvent>()
        {
            public void handleEvent(FieldEvent event)
            {
                Boolean value = getValue();
                ValueChangeEvent.fire(HasValueCheckboxAdapter.this, value);
            }
        });
        return handlerRegistration;
    }

    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        eventBus.fireEvent(gwtEvent);
    }
}
