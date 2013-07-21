package com.iglooit.core.base.client.view;

import com.clarity.core.base.client.mvp.Display;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Frame;

public class UrlFrameDisplay extends LayoutContainer implements Display
{
    private Frame frame;
    private String url;
    private String label;

    public UrlFrameDisplay(String id, String url)
    {
        this.setLayout(new FitLayout());
        this.url = url;
        this.frame = new Frame(url);
    }

    public void setUrl(String url)
    {
        this.frame.setUrl(url);
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    @Override
    protected void onRender(Element parent, int pos)
    {
        super.onRender(parent, pos);
        this.add(frame);
    }

    @Override
    public String getLabel()
    {
        return label;
    }

    public String getUrl()
    {
        return url;
    }
}
