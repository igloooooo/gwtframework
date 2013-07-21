package com.iglooit.core.lib.client;

import com.clarity.commons.iface.type.AppX;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

public abstract class HasValueComboBoxAdapter<X, MD extends ModelData> implements HasValue<X>, HasValueChangeHandlers<X>
{
    private ComboBox<MD> comboBox;
    private HandlerManager handlerManager;
    private HasValueChangeHandlers<X> hasValueChangeHandlersWrapper;

    protected abstract X getValue(MD modelData);

    public HasValueComboBoxAdapter(ComboBox<MD> comboBox)
    {
        this.comboBox = comboBox;

        handlerManager = new HandlerManager(null);
        hasValueChangeHandlersWrapper = new HasValueChangeHandlers<X>()
        {
            public HandlerRegistration addValueChangeHandler(ValueChangeHandler<X> xValueChangeHandler)
            {
                return handlerManager.addHandler(ValueChangeEvent.getType(), xValueChangeHandler);
            }

            public void fireEvent(GwtEvent<?> gwtEvent)
            {
                handlerManager.fireEvent(gwtEvent);
            }
        };
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<X> xValueChangeHandler)
    {
        comboBox.addSelectionChangedListener(new SelectionChangedListener<MD>()
        {
            public void selectionChanged(SelectionChangedEvent<MD> selEvent)
            {
                ValueChangeEvent.<X>fire(hasValueChangeHandlersWrapper, getValue(selEvent.getSelectedItem()));
            }
        });

        return hasValueChangeHandlersWrapper.addValueChangeHandler(xValueChangeHandler);
    }

    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        hasValueChangeHandlersWrapper.fireEvent(gwtEvent);
    }

    public X getValue()
    {
        return getValue(comboBox.getValue());
    }

    public void setValue(X x)
    {
        throw new AppX("Should not call setValue for a combobox");
    }

    public void setValue(X x, boolean b)
    {
        setValue(x);
    }
}
