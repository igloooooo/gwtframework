package com.iglooit.core.base.client.presenter;

import com.clarity.core.base.client.AbstractCommands;
import com.clarity.core.base.client.view.PlainListStoreFilterView;
import com.google.gwt.event.shared.HandlerManager;

import java.util.List;

public class ListStoreFilterPresenter extends StoreFilterPresenter
{
    public ListStoreFilterPresenter(
        PlainListStoreFilterView display, HandlerManager eventBus, AbstractCommands commands)
    {
        super(display, eventBus, commands);
    }

    public void setSelectionAndSuppressEvent(List data)
    {
        getDisplay().setSelectionAndSuppressEvent(data);
    }
}
