package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.view.ClarityStyle;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.ui.Widget;

/**
 * Layout container that uses css floats to position components either to the left or the right of the container
 */
public class FloatContainer extends LayoutContainer
{
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String NONE = "none";

    public FloatContainer()
    {
        setLayout(new FlowLayout());
        addStyleName(ClarityStyle.FLOAT_CLEAR_FIX);
    }

    public void add(Component component, String floatPostion, Margins margins)
    {
        add(component);
        setFloatPosition(component, floatPostion);
        setMargins(component, margins);
    }

    public void add(Widget component, String floatPostion, Margins margins)
    {
        add(component, new FlowData(margins));
        setFloatPosition(component, floatPostion);
    }

    public void insert(Component component, String floatPostion, Margins margins, int index)
    {
        insert(component, index);
        setFloatPosition(component, floatPostion);
        setMargins(component, margins);
    }

    private void setFloatPosition(Component component, String floatPostion)
    {
        if (floatPostion.equals(LEFT))
            component.addStyleName(ClarityStyle.FLOAT_LEFT);
        else if (floatPostion.equals(RIGHT))
            component.addStyleName(ClarityStyle.FLOAT_RIGHT);
        else if (floatPostion.equals(NONE))
            component.addStyleName(ClarityStyle.FLOAT_NONE);
    }

    private void setFloatPosition(Widget component, String floatPostion)
    {
        if (floatPostion.equals(LEFT))
            component.addStyleName(ClarityStyle.FLOAT_LEFT);
        else if (floatPostion.equals(RIGHT))
            component.addStyleName(ClarityStyle.FLOAT_RIGHT);
        else if (floatPostion.equals(NONE))
            component.addStyleName(ClarityStyle.FLOAT_NONE);
    }

    private void setMargins(Component component, Margins margins)
    {
        if (margins != null)
        {
            component.setStyleAttribute("marginLeft", margins.left + "px");
            component.setStyleAttribute("marginTop", margins.top + "px");
            component.setStyleAttribute("marginRight", margins.right + "px");
            component.setStyleAttribute("marginBottom", margins.bottom + "px");
        }
    }
}
