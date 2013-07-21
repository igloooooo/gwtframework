package com.iglooit.core.base.client.widget;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Frame;

public class IFrameWindow extends Window
{
    private LayoutContainer frameWrapper;

    public IFrameWindow(String url, String title, int width, int height)
    {
        super();

        this.setLayout(new FitLayout());
        this.setModal(true);
        this.getElement().setId("newTaskWindow0");

        frameWrapper = new LayoutContainer(new FitLayout());
        frameWrapper.addStyleName("frame-wrapper");

        Frame frame = new Frame(url);
        frameWrapper.add(frame.asWidget());
        this.add(frameWrapper);

        this.setSize(width, height);
        this.setTitle(title);
        this.layout();

        this.show();
    }

    public El mask(String message)
    {
        return frameWrapper.mask(message);
    }

    public void unmask()
    {
        frameWrapper.unmask();
    }

    public void setHeading(String heading)
    {
        super.setHeading(heading);
    }
}