package com.iglooit.core.base.client.widget.dialog;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;

public class ActivityDialog extends Dialog
{
    private static final String ACTIVITY_ICON = "/images/icons/activity.gif";

    public ActivityDialog(String title, String activityMessage)
    {
        setHeading(title);
        add(new Image(ACTIVITY_ICON));
        addText(activityMessage);
    }

    @Override
    protected final void onRender(Element element, int i)
    {
        super.onRender(element, i);
        center();
    }

}
