package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.widget.ClarityConfirmMessageBox;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.widget.ComponentManager;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Accessibility;

import java.util.List;

/*
* Function override:
*  1. onClick
*  2. toggle(boolean, boolean)
* And also inject a confirm message box into the class
* */
public class ClarityConfirmToggleButton extends ToggleButton
{
    private String boxTitle;
    private String boxMessage;

    public ClarityConfirmToggleButton()
    {
        super();
    }

    public ClarityConfirmToggleButton(String text)
    {
        this();
        setText(text);
    }

    public ClarityConfirmToggleButton(String text, String boxTitle, String boxMessage)
    {
        this();
        setText(text);
        this.boxTitle = boxTitle;
        this.boxMessage = boxMessage;
    }

    /*
    * **** Notice here reason why we re-defining the following var is
    * because there is no access method in parent class ToggleButton.java
    * that can set pressed state for var pressed in parent class
    * where it used in method: toggle() and we need to override that method
    * to let toggleGroup var working properly when click the button
    * */
    private boolean pressed;
    private String toggleGroup;
    private boolean allowDepress = true;

    /**
     * Returns the toggle group name.
     *
     * @return the toggle group name
     */
    public String getToggleGroup()
    {
        return toggleGroup;
    }

    public boolean isAllowDepress()
    {
        return allowDepress;
    }

    /**
     * Returns true if the button is pressed.
     *
     * @return the pressed state
     */
    public boolean isPressed()
    {
        return pressed;
    }

    /**
     * Toggles the current state.
     */
    public void toggle()
    {
        toggle(!pressed, false);
    }

    /**
     * Sets the current pressed state.
     *
     * @param state true to set pressed state
     */
    public void toggle(boolean state)
    {
        toggle(state, false);
    }

    /**
     * True to allow a toggle item to be depressed (default to true).
     *
     * @param allowDepress true to allow depressing
     */
    public void setAllowDepress(boolean allowDepress)
    {
        this.allowDepress = allowDepress;
    }

    /**
     * Optionally, set the the button's toggle group name.
     *
     * @param toggleGroup the toggle group name
     */
    public void setToggleGroup(String toggleGroup)
    {
        this.toggleGroup = toggleGroup;
    }

    @Override
    protected void onFocus(ComponentEvent ce)
    {
        if (!pressed)
        {
            super.onFocus(ce);
        }
    }

    @Override
    protected void onMouseDown(ComponentEvent ce)
    {
        // do nothing
    }

    @Override
    protected void onMouseOver(ComponentEvent ce)
    {
        if (!pressed)
        {
            super.onMouseOver(ce);
        }
    }

    @Override
    protected void onMouseUp(ComponentEvent ce)
    {
        // do nothing
    }

    @Override
    protected void onRender(Element parent, int pos)
    {
        super.onRender(parent, pos);
        if (pressed)
        {
            toggle(pressed, true);
        }
    }

    /**
     * Sets the current pressed state.
     *
     * @param state  true to set pressed state
     * @param silent true to not fire the toggle event
     */
    protected void toggle(boolean state, boolean silent)
    {
        this.pressed = state;
        if (rendered)
        {
            ButtonEvent be = new ButtonEvent(this);
            el().setStyleName("x-btn-pressed", state);
            if (state)
            {
                removeStyleName(baseStyle + "-over");
                removeStyleName(baseStyle + "-blur");
            }
            else
            {
            }
            if (state && toggleGroup != null && toggleGroup.length() > 0)
            {
                List<ClarityConfirmToggleButton> list = ComponentManager.get().get(ClarityConfirmToggleButton.class);
                for (ClarityConfirmToggleButton tb : list)
                {
                    if (tb != this && tb.getToggleGroup() != null && tb.getToggleGroup().equals(toggleGroup))
                    {
                        tb.toggle(false, silent);
                    }
                }
            }
            Accessibility.setState(buttonEl.dom, "aria-pressed", String.valueOf(state));
            if (!silent)
            {
                fireEvent(Events.Toggle, be);
            }
        }
    }

    @Override
    protected void onClick(ComponentEvent ce)
    {
        if (isPressed())
            return;

        ce.preventDefault();
        focus();
        hideToolTip();
        if (!disabled)
        {
            final ButtonEvent be = new ButtonEvent(this);
            be.setEvent(ce.getEvent());

            //modify this put confirm message box
            ClarityConfirmMessageBox box = new ClarityConfirmMessageBox(boxTitle, boxMessage)
            {
                public void yesButtonClickHandler()
                {
                    if (isAllowDepress() || !isPressed())
                    {
                        toggle();
                    }
                    if (menu != null && !menu.isVisible())
                    {
                        showMenu();
                    }
                    fireEvent(Events.Select, be);
                }
            };
            box.show();
        }
    }

    public void setConfirmBoxHeading(String heading)
    {
        this.boxTitle = heading;
    }

    public void setConfirmBoxMessage(String text)
    {
        this.boxMessage = text;
    }
}
