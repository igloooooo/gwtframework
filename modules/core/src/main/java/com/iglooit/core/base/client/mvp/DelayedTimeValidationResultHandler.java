package com.iglooit.core.base.client.mvp;

import com.clarity.commons.iface.domain.ValidationResult;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;

import java.util.Arrays;
import java.util.List;

public class DelayedTimeValidationResultHandler<T> implements HasValueChangeHandlers<T>, ValidationResultHandler
{
    private final HandlerManager handlerManager = new HandlerManager(null);
    private final Runnable fireValueChangeRunnable;

    public static final int VALIDATE_DELAY_MILLIS = 500;
    private boolean typingTimeout = false;
    private String validationShowErrorsMessage = "";

    private final ClarityField<T, ? extends Field> field;

    public DelayedTimeValidationResultHandler(ClarityField<T, ? extends Field> field,
                                              Runnable fireValueChangeRunnable)
    {
        this.field = field;
        this.fireValueChangeRunnable = fireValueChangeRunnable;
    }

    private final Timer validationShowErrorsTimer = new Timer()
    {
        public void run()
        {
            field.getField().forceInvalid(validationShowErrorsMessage);
        }
    };

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        validationShowErrorsTimer.cancel();
        if (validationResultList.size() == 0)
        {
            field.getField().clearInvalid();
        }
        else
        {
            validationShowErrorsMessage = validationResultList.get(0).getReason();
            if (typingTimeout)
                validationShowErrorsTimer.schedule(VALIDATE_DELAY_MILLIS);
            else
                field.getField().forceInvalid(validationShowErrorsMessage);
        }
    }

    private static final List<Integer> SPECIAL_CHARS = Arrays.asList(
        //KeyCodes.KEY_ALT,
        //KeyCodes.KEY_BACKSPACE,
        //KeyCodes.KEY_CTRL,
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

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<T> dValueChangeHandler)
    {
        final HandlerRegistration handlerRegistration =
            handlerManager.addHandler(ValueChangeEvent.getType(), dValueChangeHandler);

        final EventType blurEventType = Events.OnBlur;
        final EventType changeEventType = Events.Change;
        final Listener<FieldEvent> changeListener = new Listener<FieldEvent>()
        {
            public void handleEvent(FieldEvent event)
            {
                typingTimeout = false;
                fireValueChangeRunnable.run();
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
                    fireValueChangeRunnable.run();
                }
            }
        };

        field.getField().addListener(blurEventType, changeListener);
        field.getField().addListener(changeEventType, changeListener);
        field.getField().addKeyListener(keyListener);

        return new HandlerRegistration()
        {
            public void removeHandler()
            {
                handlerRegistration.removeHandler();
                field.getField().removeListener(blurEventType, changeListener);
                field.getField().removeListener(changeEventType, changeListener);
                field.getField().removeKeyListener(keyListener);
            }
        };
    }

    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        handlerManager.fireEvent(gwtEvent);
    }
}
