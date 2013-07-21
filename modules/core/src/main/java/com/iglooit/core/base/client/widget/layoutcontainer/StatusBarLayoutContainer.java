package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.clarity.core.base.iface.domain.Validatable;
import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.core.base.iface.validation.ValidationConstants;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatusBarLayoutContainer extends LayoutContainer
{
    private StatusDisplay statusDisplay;
    private LayoutContainer widgetContainer;
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    public StatusBarLayoutContainer()
    {
        setLayout(new VBoxLayout(VBoxLayout.VBoxLayoutAlign.STRETCH));
        addStyleName(ClarityStyle.STATUS_BAR_LAYOUT_CONTAINER);


        statusDisplay = new StatusDisplay();
        statusDisplay.setEffectCompleteListener(new Listener<FxEvent>()
        {
            public void handleEvent(FxEvent be)
            {
                layout(true);
            }
        });

        VBoxLayoutData layoutData = new VBoxLayoutData();
        layoutData.setFlex(1);

        widgetContainer = new LayoutContainer(new FitLayout());
        add(widgetContainer, layoutData);
    }

    public void setWidget(Widget widget)
    {
        removeWidgets();
        widgetContainer.add(widget);
        layout(true);
    }

    public void removeWidgets()
    {
        widgetContainer.removeAll();
        layout(true);
    }

    public StatusDisplay getStatusDisplay()
    {
        return statusDisplay;
    }

    public void updateStatusDescription(String status, MessageBoxHtml.MessageType messageType)
    {
        statusDisplay.updateStatusDescription(status, messageType);

        VBoxLayoutData layoutData = new VBoxLayoutData(new Margins(0, 0, 5, 0));
        //layoutData.setFlex(1);

        insert(statusDisplay, 0, layoutData);
        layout(true);
    }

    public void updateStatusDescription(String status, String message,
                                        MessageBoxHtml.MessageType messageType)
    {
        statusDisplay.updateStatusDescription(status, message, messageType);

        VBoxLayoutData layoutData = new VBoxLayoutData(new Margins(0, 0, 5, 0));
        //layoutData.setFlex(1);

        insert(statusDisplay, 0, layoutData);
        layout(true);
    }

    public void updateStatusDescription(Map<Validatable, List<ValidationResult>> results,
                                        MessageBoxHtml.MessageType messageType)
    {
        statusDisplay.updateStatusDescription(getValidationFailEventString(results), messageType);

        VBoxLayoutData layoutData = new VBoxLayoutData(new Margins(0, 0, 5, 0));
        //layoutData.setFlex(1);

        insert(statusDisplay, 0, layoutData);
        layout(true);
    }

    public String getValidationFailEventString(Map<Validatable, List<ValidationResult>> results)
    {
        // sort the list based on the index in the binderList,
        // so validation results appear in the same order as the form
        List<ValidationResult> allResults = new ArrayList<ValidationResult>();
        ValidatableMeta meta = (ValidatableMeta)results.keySet().toArray(new Validatable[0])[0];
        for (List<ValidationResult> res : results.values())
            allResults.addAll(res);

        StringBuilder sb = new StringBuilder();
        sb.append(VC.validationFailedOnServer());
        sb.append("<ul class='error-list'>");
        for (ValidationResult validationResult : allResults)
        {
            if (validationResult.isDisplayInErrorSummary())
            {
                sb.append("<li>");
                sb.append(meta.getDefaultFieldLabel(validationResult.getTags().get(0))).append(
                    ": ").append(validationResult.getReason());
                sb.append("</li>");
            }
        }
        sb.append("</ul>");
        return sb.toString();
    }


    public void clearStatusDescription()
    {
        statusDisplay.clearStatusDescription();
        if (findComponent(statusDisplay) != null)
            remove(statusDisplay);
        layout(true);
    }
}
