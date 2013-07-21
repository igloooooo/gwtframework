package com.iglooit.core.base.client.widget.form;

import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.google.gwt.user.client.Element;


/**
 * Button bar container that renders a set of toggle buttons as one group of buttons. Buttons appear as a large
 * long button (similar to buttons displayed in gmail).
 *
 * Note: To configure a toggle button group to appear as an on/off component, simply add a Clarity style of
 * BUTTON_TOGGLE_ON to the 'On' ToggleButton and a style of BUTTON_TOGGLE_OFF to the 'Off' ToggleButton before
 * adding it to this button group component.
 */
public class ToggleButtonGroup extends ButtonBar
{
    private String groupName;

    public static final String BUTTON_TOGGLE_GROUP = "toggle-button-group";
    public static final String BUTTON_FIRST = "t-first";
    public static final String BUTTON_MID = "t-mid";
    public static final String BUTTON_LAST = "t-last";

    public ToggleButtonGroup(String groupName)
    {
        // use id to ensure this group name is unique to this button group 
        if (groupName != null && !groupName.isEmpty())
            this.groupName = groupName + "-" + getId();
        addStyleName(BUTTON_TOGGLE_GROUP);
        setSpacing(0);
    }

    /**
     * Use this constructor if you don't wish to link added toggle buttons together via a button group
     */
    public ToggleButtonGroup()
    {
        this(null);
    }

    public boolean add(ToggleButton toggleButton)
    {
        if (groupName != null && !groupName.isEmpty())
            toggleButton.setToggleGroup(groupName);
        return super.add(toggleButton);
    }

    protected void onRender(Element target, int index)
    {
        for (int i = 0; i < getItemCount(); i++)
        {
            ToggleButton button = (ToggleButton)getItem(i);
            if (i == 0)
                button.addStyleName(BUTTON_FIRST);
            else if (i == getItemCount() - 1)
                button.addStyleName(BUTTON_LAST);
            else
                button.addStyleName(BUTTON_MID);
        }
        super.onRender(target, index);
    }
}
