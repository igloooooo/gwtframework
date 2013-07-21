package com.iglooit.core.base.client.event;

import com.clarity.core.base.client.mvp.Presenter;
import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.google.gwt.event.shared.EventHandler;

public interface AddPanelEventHandler extends EventHandler
{
    void addPanel(GPanel panel);

    void addPresenter(Presenter presenter);
}
