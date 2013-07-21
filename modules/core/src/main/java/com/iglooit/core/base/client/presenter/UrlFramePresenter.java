package com.iglooit.core.base.client.presenter;

import com.clarity.core.base.client.mvp.DefaultPresenter;
import com.clarity.core.base.client.view.UrlFrameDisplay;
import com.google.gwt.event.shared.HandlerManager;

public class UrlFramePresenter extends DefaultPresenter<UrlFrameDisplay>
{
    public UrlFramePresenter(UrlFrameDisplay display, HandlerManager sharedEventBus)
    {
        super(display, sharedEventBus);
    }

    @Override
    public void bind()
    {

    }
}
