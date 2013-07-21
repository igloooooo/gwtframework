package com.iglooit.core.base.client.view;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.MarginData;

/**
 * General utility class for UI view/display  related functions.
 */

public class ViewUtils
{

    public static BorderLayoutData applyMargins(BorderLayoutData data)
    {
        data.setMargins(ClarityStyle.DEFAULT_MARGINS);
        return data;
    }

    public static BorderLayoutData applyMargins(BorderLayoutData data, int top, int right, int bottom, int left)
    {
        data.setMargins(new Margins(top, right, bottom, left));
        return data;
    }

//    public static <T extends MarginData> T applyMargins(T data)

    public static MarginData applyMargins(MarginData data)
    {
        data.setMargins(ClarityStyle.DEFAULT_MARGINS);
        return data;
    }

    public static MarginData applyMargins(MarginData data, int top, int right, int bottom, int left)
    {
        data.setMargins(new Margins(top, right, bottom, left));
        return data;
    }

    public static void disable(Container<Component> container)
    {
        for (Component c : container.getItems())
        {
            if (c instanceof Container)
                disable((Container<Component>)c);
            else
                c.setEnabled(false);
        }
    }
}