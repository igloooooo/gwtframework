package com.iglooit.core.base.client.event;

import com.clarity.core.base.client.mvp.Presenter;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.Widget;

public interface ClosePanelEventHandler extends EventHandler
{
    void closePanel(Widget panel);

    void closePresenter(Presenter presenter);
}
