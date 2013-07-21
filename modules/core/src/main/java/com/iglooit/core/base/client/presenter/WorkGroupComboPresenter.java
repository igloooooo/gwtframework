package com.iglooit.core.base.client.presenter;

import com.clarity.core.account.iface.command.request.WorkGroupListReadRequest;
import com.clarity.core.account.iface.command.response.WorkGroupListReadResponse;
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

public class WorkGroupComboPresenter extends CustomComponentPresenter<WorkGroupComboView>
{
    private CommandServiceClientImpl commandService;
    private String value;
    private boolean restricted;

    public WorkGroupComboPresenter(CommandServiceClientImpl commandService, HandlerManager sharedEventBus,
                                   boolean restricted)
    {
        super(new WorkGroupComboView(), sharedEventBus);
        this.commandService = commandService;
        this.restricted = restricted;
        initComboData();
        bind();
    }

    public WorkGroupComboPresenter(CommandServiceClientImpl commandService, HandlerManager sharedEventBus)
    {
       this(commandService, sharedEventBus, false);
    }

    private void initComboData()
    {
        WorkGroupListReadRequest request = new WorkGroupListReadRequest();
        if (restricted)
        {
            //TODO SM: refactor this code to later when EmployeeWorkGroup is joined with employee, WorkGroup & class
            request.setStatus("ACTIVE");
            request.setWgClassTypes(WorkgroupClass.FAULTS);
        }

        commandService.run(request, new GAsyncCallback<WorkGroupListReadResponse>()
        {
            public void onSuccess(WorkGroupListReadResponse response)
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
