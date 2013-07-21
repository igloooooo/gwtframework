package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.iface.command.Request;

import java.util.List;

public interface TaskPanelPresenter
{
    List<Request> getOnCompleteRequests();
}
