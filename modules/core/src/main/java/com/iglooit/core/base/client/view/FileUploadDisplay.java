package com.iglooit.core.base.client.view;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.combobox.ClarityComboBox;
import com.clarity.core.base.client.widget.dialog.ActivityDialog;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.clarity.core.oss.iface.FileUploadServiceI;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class FileUploadDisplay extends LayoutContainer implements Display
{
    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private ActivityDialog activityDialog = new ActivityDialog("uploading", "Please wait...");
    private FormPanel form;
    private FormPanel formPb;
    private FileUploadField file;
    private Button submitBtn;
    private ProgressBar pb;
    private MessageBoxHtml messageBox;
    private TextField<String> descriptionField;
    private TextField<String> displayNameField;

    private ClarityComboBox<String> moduleComboBox = new ClarityComboBox<String>();

    public FileUploadDisplay()
    {
        file = new FileUploadField();
        file.setAllowBlank(false);
        file.setName(FileUploadServiceI.FILE_UPLOAD_FILE_FIELD);
        file.getImages().setInvalid(Resource.ICONS.exclamationRed());
        file.setFieldLabel(BVC.reportFile());
        file.addStyleName("field-set-bottom-no-padding");
        file.setAllowBlank(false);
        FormUtils.addMandatoryStyle(file);
        file.setWidth(350);

        displayNameField = new TextField<String>();
        displayNameField.setFieldLabel(BVC.reportDisplayName());
        displayNameField.setAllowBlank(false);
        displayNameField.getImages().setInvalid(Resource.ICONS.exclamationRed());
        displayNameField.setName(FileUploadServiceI.FILE_UPLOAD_REPORT_DISPLAY_NAME_FIELD);
        displayNameField.setAllowBlank(false);
        FormUtils.addMandatoryStyle(displayNameField);

        descriptionField = new TextField<String>();
        descriptionField.setFieldLabel(BVC.reportDescription());
        descriptionField.setName(FileUploadServiceI.FILE_UPLOAD_DESCRIPTION_FIELD);

        moduleComboBox.setFieldLabel(BVC.reportModule());
        moduleComboBox.setName(FileUploadServiceI.FILE_UPLOAD_MODULE_FIELD);
        moduleComboBox.setAllowBlank(false);
        FormUtils.addMandatoryStyle(moduleComboBox);


        FormLayout fl = new FormLayout();
        fl.setLabelWidth(120);
        LayoutContainer holder = new LayoutContainer(fl);
        holder.add(file, FormUtils.getDefaultFormData());
        holder.add(moduleComboBox, FormUtils.getDefaultFormData());
        holder.add(displayNameField, FormUtils.getDefaultFormData());
        holder.add(descriptionField, FormUtils.getDefaultFormData());
        List<Widget> additionalWidgetList = getAdditionalFileUploadingWidgets();
        if (additionalWidgetList != null && !additionalWidgetList.isEmpty())
        {
            for (Widget widget : additionalWidgetList)
                holder.add(widget);
        }

        form = new FormPanel();
        form.add(holder);
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);
        String baseUrl = GWT.getModuleBaseURL();
        baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
        baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
        form.setAction(baseUrl + "/clarity/FileUpload");

        submitBtn = new Button(BVC.reportUpload());
        submitBtn.addStyleName(ClarityStyle.BUTTON_PRIMARY);

        formPb = new FormPanel();
        formPb.setEncoding(FormPanel.ENCODING_MULTIPART);
        formPb.setMethod(FormPanel.METHOD_POST);
        // todo pm: progression bar is not working yet, fix fsm as well
        formPb.setAction(baseUrl + "/clarity/ProgressView");
        pb = new ProgressBar();
        pb.setWidth(300);
        pb.hide();

        messageBox = new MessageBoxHtml();
        messageBox.hide();

        setLayout(new RowLayout());
        add(form, new RowData(-1, -1, new Margins(10, 0, 0, 10)));
        add(formPb, new RowData(-1, -1, new Margins(0, 0, 0, 10)));
        add(pb, new RowData(-1, -1, new Margins(0, 0, 0, 10)));
        add(messageBox, new RowData(-1, -1, new Margins(0, 10, 0, 10)));

        /**
         * we can add submit button to the enclosing dialog box of this FileUploadDisplay.
         */
        add(submitBtn, new RowData(-1, -1));
    }

    public boolean isValid()
    {
        file.clearInvalid();
        displayNameField.clearInvalid();
        return file.validate() && moduleComboBox.validate()
            && displayNameField.validate() && descriptionField.validate();
    }

    public void setSelectableModules(List<String> modules)
    {
        moduleComboBox.removeAll();
        moduleComboBox.add(modules);
    }

    public void addSubmitButtonClickListener(SelectionListener<ButtonEvent> listener)
    {
        submitBtn.addSelectionListener(listener);
    }

    private void setStatusUploading()
    {
        messageBox.setType(MessageBoxHtml.MessageType.INFO);
        messageBox.setIcon(Resource.ICONS.upload());
        messageBox.setMessage(BVC.fileUploading());
        messageBox.show();
        layout(true);
    }

    public void setStatusSuccess()
    {
        messageBox.setType(MessageBoxHtml.MessageType.SUCCESS);
        messageBox.setIcon(Resource.ICONS.tick());
        messageBox.setMessage(BVC.fileUploadSuccess());
        messageBox.show();
        layout(true);
    }

    public void setStatusFail()
    {
        messageBox.setType(MessageBoxHtml.MessageType.ERROR);
        messageBox.setIcon(Resource.ICONS.exclamationRed());
        messageBox.setMessage(BVC.fileUploadFail());
        messageBox.show();
        layout(true);
    }

    public void submit()
    {
        setStatusUploading();
        submitBtn.mask();
        form.submit();
    }

    protected List<Widget> getAdditionalFileUploadingWidgets()
    {
        return null;
    }

    @Override
    public String getLabel()
    {
        return "File upload";
    }

    public void updateProgress(Double percentage, String s)
    {
        pb.updateProgress(percentage, s);
    }

    public void addSubmitCompleteHandler(FormPanel.SubmitCompleteHandler submitCompleteHandler)
    {
        form.addSubmitCompleteHandler(submitCompleteHandler);
    }

    public void submitPB()
    {
        formPb.submit();
    }

    public void unmaskButton()
    {
        submitBtn.unmask();
    }

    public void addProgressionBarHandler(FormPanel.SubmitCompleteHandler submitCompleteHandler)
    {
        formPb.addSubmitCompleteHandler(submitCompleteHandler);
    }

    public void resetFile()
    {
        file.clear();
        file.setValue(null);
        file.clearInvalid();
        moduleComboBox.clearSelections();
        displayNameField.reset();
        descriptionField.reset();
    }

    public void resetFileAndMessageBox()
    {
        resetFile();
        messageBox.hide();
    }

    public String getReportName()
    {
        return file.getRawValue();
    }

    public String getModuleName()
    {
        return moduleComboBox.getSimpleValue();
    }

    public void setDefaultModule(String defaultModule)
    {
        moduleComboBox.setValue(moduleComboBox.findModel(defaultModule));
    }

    public void setSubmitBtnText(String buttonName)
    {
        submitBtn.setText(buttonName);
    }

    public Button getSubmitBtn()
    {
        return submitBtn;
    }

    public void setFileLabel(String fieldLabel)
    {
        file.setFieldLabel(fieldLabel);
    }

    public void setDisplayNameLabel(String fieldLabel)
    {
        displayNameField.setFieldLabel(fieldLabel);
    }

    public void setDescriptionLabel(String fieldLabel)
    {
        descriptionField.setFieldLabel(fieldLabel);
    }
}
