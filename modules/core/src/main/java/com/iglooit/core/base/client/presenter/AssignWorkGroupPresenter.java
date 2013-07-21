package com.iglooit.core.base.client.presenter;

import com.clarity.core.account.iface.command.request.FaultsTicketsOrdersWGReassignListRequest;
import com.clarity.core.account.iface.command.response.FaultsTicketsOrdersWGReassignListResponse;
import com.clarity.core.account.iface.domain.WorkgroupClass;
import com.clarity.core.base.client.controller.GAsyncCallback;
import com.clarity.core.base.client.event.WorkGroupComboChangeEvent;
import com.clarity.core.base.client.view.WorkGroupComboView;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.google.gwt.event.shared.HandlerManager;

public class AssignWorkGroupPresenter extends CustomComponentPresenter<WorkGroupComboView>
{
    private CommandServiceClientImpl commandService;
    private String currentWorkGroup;
    private boolean newInstance;
    private WorkgroupClass workgroupClass;
    private String value;

    public AssignWorkGroupPresenter(CommandServiceClientImpl commandService, HandlerManager sharedEventBus,
                                    String currentWorkGroup, boolean newInstance, WorkgroupClass workgroupClass)
    {
        super(new WorkGroupComboView(), sharedEventBus);
        this.commandService = commandService;
        this.currentWorkGroup = currentWorkGroup;
        this.newInstance = newInstance;
        this.workgroupClass = workgroupClass;
        initComboData();
        bind();
    }

    private void initComboData()
    {
        commandService.run(new FaultsTicketsOrdersWGReassignListRequest(
            getDisplay().getComboBox().getRawValue(), newInstance, currentWorkGroup,
            workgroupClass),
            new GAsyncCallback<FaultsTicketsOrdersWGReassignListResponse>()
            {
                @Override
                public void onSuccess(FaultsTicketsOrdersWGReassignListResponse response)
                {
                    getDisplay().getComboBox().add(response.getWorkgroupNameList());
                    getComboBox().setSimpleValue(value);
                }
            });

    }

    @Override
    public Field getField()
    {
        return getComboBox();
    }

    @Override
    public String getValue()
    {
        return getSelectedWorkGroup();
    }

    @Override
    protected void makeFieldRequired()
    {
        getComboBox().setAllowBlank(false);
    }

    public SimpleComboBox<String> getComboBox()
    {
        return getDisplay().getComboBox();
    }

    public String getSelectedWorkGroup()
    {
        return getComboBox().getSimpleValue();
    }

    public void setSelectionChangedListener(SelectionChangedListener<SimpleComboValue<String>> listener)
    {
        getComboBox().addSelectionChangedListener(listener);
    }

    @Override
    public void setValue(String value)
    {
        this.value = value;
        getComboBox().setSimpleValue(value);
    }

    @Override
    public void bind()
    {
        fireWorkGroupChangedEvent();
    }

    private void fireWorkGroupChangedEvent()
    {
        setSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> event)
            {
                getSharedEventBus().fireEvent(new WorkGroupComboChangeEvent(event.getSelectedItem().getValue()));
            }
        });
    }
}
