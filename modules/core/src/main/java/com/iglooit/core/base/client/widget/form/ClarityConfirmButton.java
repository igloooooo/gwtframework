package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.ClarityConfirmMessageBox;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.widget.button.Button;

public class ClarityConfirmButton extends Button
{
    private String boxTitle;
    private String boxMessage;

    public ClarityConfirmButton()
    {
        super();
        this.addStyleName(ClarityStyle.TOOLBAR_BUTTON);
    }

    public ClarityConfirmButton(String text)
    {
        this();
        setText(text);
    }

    public ClarityConfirmButton(String text, String boxTitle, String boxMessage)
    {
        this();
        setText(text);
        this.boxTitle = boxTitle;
        this.boxMessage = boxMessage;
    }

    @Override
    protected void onClick(ComponentEvent ce)
    {
//        super.onClick(ce);
        ce.preventDefault();
        focus();
        hideToolTip();
        if (!disabled)
        {
            final ButtonEvent be = new ButtonEvent(this);
            if (!fireEvent(Events.BeforeSelect, be))
            {
                return;
            }
            if (menu != null && !menu.isVisible())
            {
                showMenu();
            }

            //modify this put confirm message box
            final ClarityConfirmMessageBox box = new ClarityConfirmMessageBox(boxTitle, boxMessage)
            {
                public void yesButtonClickHandler()
                {
                    fireEvent(Events.Select, be);
                }
            };
            box.show();
        }
    }

    public void setConfirmBoxTitle(String title)
    {
        this.boxTitle = title;
    }

    public void setConfirmBoxMessage(String text)
    {
        this.boxMessage = text;
    }
}
