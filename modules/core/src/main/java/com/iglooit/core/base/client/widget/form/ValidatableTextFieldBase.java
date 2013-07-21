package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.DelayedTimeValidationResultHandler;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public abstract class ValidatableTextFieldBase<D>
    extends TextField<D>
    implements ClarityField<D, ValidatableTextFieldBase<D>>
{
    private final DelayedTimeValidationResultHandler<D> delayedValidationResultHandler;

    protected abstract D convertType(String s);

    private boolean fireChangeEvents = true;


    public ValidatableTextFieldBase()
    {
        super();
        setFireChangeEventOnSetValue(false);
        delayedValidationResultHandler = new DelayedTimeValidationResultHandler<D>(this, new Runnable()
        {
            public void run()
            {
                if (fireChangeEvents)
                {
                    fireValueChangeEvent();
                }
            }
        });
        getImages().setInvalid(Resource.ICONS.exclamationRed());

        // component plugin to add description field below form imputs
        ComponentPlugin fieldDescPlugin = new ComponentPlugin()
        {
            public void init(Component component)
            {
                component.addListener(Events.Render, new Listener<ComponentEvent>()
                {
                    public void handleEvent(ComponentEvent be)
                    {
                        El elem = be.getComponent().el().findParent(".x-form-element", 3);
                        while (elem != null && elem.getChild(1) != null)
                        {
                            El child = elem.getChild(1);
                            child.remove();
                        }
                        if (elem != null && be.getComponent().getData("text") != null)
                            elem.appendChild(XDOM.create("<div class='form-description'>" +
                                be.getComponent().getData("text") + "</div>"));
                    }
                });
            }
        };

        addPlugin(fieldDescPlugin);
    }

    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        delayedValidationResultHandler.handleValidationResults(validationResultList);
    }

    public void valueExternallyChangedFrom(D oldLocalValue)
    {
        // override this
    }

    public boolean validate()
    {
        clearInvalid();
        return true;
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        delayedValidationResultHandler.fireEvent(gwtEvent);
    }


    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<D> dValueChangeHandler)
    {
        return delayedValidationResultHandler.addValueChangeHandler(dValueChangeHandler);
    }

    protected void fireValueChangeEvent()
    {
        D value = getValue();

        ValueChangeEvent.fire(ValidatableTextFieldBase.this, value);
    }

    @Override
    public D getValue()
    {
        D widgetValue = super.getValue();
        final D properlyTypedValue;
        if (super.getValue() instanceof String)
            properlyTypedValue = convertType((String)widgetValue);
        else
            properlyTypedValue = widgetValue;
        return properlyTypedValue;
    }

    @Override
    public void setValue(D d)
    {
        setValue(d, false);
    }

    public void setValue(D d, boolean fireChangeEvent)
    {
        super.setValue(d);
        if (fireChangeEvent)
        {
            fireValueChangeEvent();
        }
        fireChangeEvents = true;
    }

    @Override
    public String getFieldLabel()
    {
        return StringUtil.emptyStringIfNull(super.getFieldLabel());
    }

    public Option<String> getUsageHint()
    {
        return Option.option(getEmptyText());
    }

    public void setUsageHint(String usageHint)
    {
        setEmptyText(usageHint);
    }

    public ValidatableTextFieldBase<D> getField()
    {
        return this;
    }
}
