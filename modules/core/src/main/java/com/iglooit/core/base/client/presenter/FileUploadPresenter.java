package com.iglooit.core.base.client.presenter;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.base.client.controller.GAsyncCallback;
import com.clarity.core.base.client.event.FileUploadedEvent;
import com.clarity.core.base.client.mvp.DefaultPresenter;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.FileUploadDisplay;
import com.clarity.core.base.client.widget.ClarityConfirmMessageBox;
import com.clarity.core.base.iface.command.ClarityModulesReadListRequest;
import com.clarity.core.base.iface.command.ListResponse;
import com.clarity.core.base.iface.command.response.BooleanResponse;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.oss.iface.FileUploadServiceI;
import com.clarity.core.oss.iface.command.ReportTemplateExistsReadRequest;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FormPanel;

public class FileUploadPresenter extends DefaultPresenter<FileUploadDisplay>
{

    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private Timer timer;
    private CommandServiceClientImpl commandHandler;

    public FileUploadPresenter(FileUploadDisplay display, CommandServiceClientImpl commandHandler)
    {
        this(display, commandHandler, null);
    }

    public FileUploadPresenter(FileUploadDisplay display, CommandServiceClientImpl commandHandler,
                               HandlerManager eventBus)
    {
        super(display, eventBus);
        this.commandHandler = commandHandler;
    }

    @Override
    public void bind()
    {
        addSubmitButtonSelectionListener();
        addSubmitCompleteHandler();
        addProgressionBarHandler();
        addModuleListReadRequestHandler();
    }

    private void addModuleListReadRequestHandler()
    {
        commandHandler.run(new ClarityModulesReadListRequest(), new GAsyncCallback<ListResponse<String>>()
        {
            @Override
            public void onSuccess(ListResponse<String> response)
            {
                getDisplay().setSelectableModules(response.getList());
            }
        });
    }

    private void addProgressionBarHandler()
    {
        getDisplay().addProgressionBarHandler(new FormPanel.SubmitCompleteHandler()
        {
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent submitCompleteEvent)
            {
                String response = submitCompleteEvent.getResults();
                if (StringUtil.isEmpty(response))
                    return;
                String[] sList = response.split(",");
                if (sList.length < 2)
                    return;
                String p1 = StringUtil.isNumeric(sList[0]) ? sList[0] : "";
                String p2 = StringUtil.isNumeric(sList[1]) ? sList[1] : "";
                if (StringUtil.isEmpty(p1) || StringUtil.isEmpty(p2))
                    return;

                Double d1 = Double.valueOf(p1);
                Double d2 = Double.valueOf(p2);
                if (d2 == 0)
                    return;

                Double percentage = d1 / d2;

                getDisplay().updateProgress(percentage, ((Double)(percentage * 100.0)).toString() + "-%");
            }
        });
    }

    private void addSubmitCompleteHandler()
    {
        getDisplay().addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler()
        {
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent submitCompleteEvent)
            {
                getDisplay().unmaskButton();

                //collect response
                String response = submitCompleteEvent.getResults();
                if (response.contains(FileUploadServiceI.UPLOAD_SUCCEEDED))
                {
                    getDisplay().setStatusSuccess();
                    getDisplay().resetFile();
                    getSharedEventBus().fireEvent(new FileUploadedEvent());
                }
                else
                {
                    getDisplay().setStatusFail();
                }
                timer.cancel();
            }
        });
    }

    private void addSubmitButtonSelectionListener()
    {
        getDisplay().addSubmitButtonClickListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                if (getDisplay().isValid())
                {
                    final String reportName = getDisplay().getReportName();
                    final String moduleName = getDisplay().getModuleName();

                    commandHandler.run(
                        new ReportTemplateExistsReadRequest(reportName, moduleName),
                        new GAsyncCallback<BooleanResponse>()
                        {
                            @Override
                            public void onSuccess(BooleanResponse response)
                            {
                                if (response.isMatch())
                                {
                                    ClarityConfirmMessageBox box = new ClarityConfirmMessageBox(
                                        BVC.confirmMessageBoxDefaultTitle(),
                                        BVC.reportOverwrite(reportName, moduleName))
                                    {
                                        public void yesButtonClickHandler()
                                        {
                                            submitForm();
                                        }
                                    };
                                    box.show();
                                }
                                else
                                {
                                    submitForm();
                                }
                            }
                        });


                }
            }
        });
    }

    private void submitForm()
    {
        timer = new Timer()
        {
            public void run()
            {
                getDisplay().submitPB();
            }
        };
        timer.scheduleRepeating(1000);

        getDisplay().submit();
    }
}
