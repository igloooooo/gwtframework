package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;

import java.util.Arrays;
import java.util.List;

public class TestValidatableTimeField implements ClarityField<String, TimeField>
{
    private TimeField timeField;
    private static final String TIME_REG = "(^([0-1][0-9]|[2][0-3]):([0-5][0-9])$)";

    public TestValidatableTimeField()
    {
        this.timeField = new TimeField();
        this.timeField.setRegex(TIME_REG);
        this.timeField.setTriggerAction(ComboBox.TriggerAction.ALL);
    }

    public static final int VALIDATE_DELAY_MILLIS = 500;

    private boolean typingTimeout = false;
    private String validationShowErrorsMessage = "";

    private final Timer validationShowErrorsTimer = new Timer()
    {
        public void run()
        {
            timeField.forceInvalid(validationShowErrorsMessage);
        }
    };

    private HandlerManager handlerManager = new HandlerManager(null);
    private boolean fireChangeEvents = true;

    @Override
    public TimeField getField()
    {
        return timeField;
    }


    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        validationShowErrorsTimer.cancel();
        if (validationResultList.size() == 0)
        {
            timeField.clearInvalid();
        }
        else
        {
            validationShowErrorsMessage = validationResultList.get(0).getReason();
            if (typingTimeout)
                validationShowErrorsTimer.schedule(VALIDATE_DELAY_MILLIS);
            else
                timeField.forceInvalid(validationShowErrorsMessage);
        }
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        handlerManager.fireEvent(gwtEvent);
    }

    private static final List<Integer> SPECIAL_CHARS = Arrays.asList(
        KeyCodes.KEY_ALT,
        //KeyCodes.KEY_BACKSPACE,
        KeyCodes.KEY_CTRL,
        //KeyCodes.KEY_DELETE,
        KeyCodes.KEY_DOWN,
        KeyCodes.KEY_END,
        KeyCodes.KEY_ENTER,
        KeyCodes.KEY_ESCAPE,
        KeyCodes.KEY_HOME,
        KeyCodes.KEY_LEFT,
        KeyCodes.KEY_PAGEDOWN,
        KeyCodes.KEY_PAGEUP,
        KeyCodes.KEY_RIGHT,
        KeyCodes.KEY_SHIFT,
        KeyCodes.KEY_TAB,
        KeyCodes.KEY_UP
    );

    private static boolean isSpecialChar(int c)
    {
        return SPECIAL_CHARS.contains(Integer.valueOf(c));
    }

    @Override
    public void valueExternallyChangedFrom(String oldLocalValue)
    {

    }


    @Override
    public String getFieldLabel()
    {
        return timeField.getFieldLabel();
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        timeField.setFieldLabel(fieldLabel);
    }


    @Override
    public Option<String> getUsageHint()
    {
        return Option.option(timeField.getEmptyText());
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        timeField.setEmptyText(usageHint);
    }

    public String getValue()
    {
        return timeField.getRawValue();
    }

    @Override
    public void setValue(String s)
    {
        timeField.setRawValue(s);
    }

    @Override
    public void setValue(String s, boolean b)
    {
        fireChangeEvents = b;
        setValue(s);
        fireValidationChangeEvent();
        fireChangeEvents = true;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> sValueChangeHandler)
    {
        final HandlerRegistration handlerRegistration =
            handlerManager.addHandler(ValueChangeEvent.getType(), sValueChangeHandler);

        final EventType eventType = Events.Change;
        final Listener<FieldEvent> changeListener = new Listener<FieldEvent>()
        {
            public void handleEvent(FieldEvent event)
            {
                typingTimeout = false;
                fireValidationChangeEvent();
            }
        };

        final KeyListener keyListener = new KeyListener()
        {
            public void componentKeyUp(ComponentEvent event)
            {
                super.componentKeyUp(event);
                // ignore special characters like tab etc,
                // we're only concerned with actual typing
                if (!isSpecialChar((char)event.getKeyCode()))
                {
                    typingTimeout = true;
                    fireValidationChangeEvent();
                }
            }
        };

        timeField.addListener(eventType, changeListener);
        timeField.addKeyListener(keyListener);

        return new HandlerRegistration()
        {
            public void removeHandler()
            {
//                handlerRegistration.removeHandler();
                timeField.removeListener(eventType, changeListener);
                timeField.removeKeyListener(keyListener);
            }
        };
    }

    private void fireValidationChangeEvent()
    {
        if (fireChangeEvents)
        {
            String value = getValue();
            ValueChangeEvent.fire(TestValidatableTimeField.this, value);
        }
    }
}
