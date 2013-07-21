package com.iglooit.core.base.client.widget.htmlcontainer;

import com.clarity.core.base.iface.domain.HasValidatingValue;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class FsmFormFieldContainerWrapper
{
    private FsmFormFieldWrapper fsmFormFieldWrapper;

    private static final String FSM_FORM_FIELD_CONTAINER = "fsm-form-field-container";
    private LayoutContainer lc;
    private HtmlContainer hc;
    private Widget widget;


    public FsmFormFieldContainerWrapper()
    {
        hc = new HtmlContainer(
            "<span class='" + FSM_FORM_FIELD_CONTAINER + "'></span>"
        );
        lc = new LayoutContainer();
        FormLayout layout = new FormLayout();
        layout.setLabelAlign(FormPanel.LabelAlign.LEFT);
        layout.setLabelWidth(200);
        lc.setLayout(layout);
        hc.add(lc, "span." + FSM_FORM_FIELD_CONTAINER);
    }

    public FsmFormFieldContainerWrapper(String label)
    {
        this();
        fsmFormFieldWrapper = new FsmFormFieldWrapper(label);
    }

    public FsmFormFieldContainerWrapper(String label, List<String> storeValues)
    {
        this();
        fsmFormFieldWrapper = new FsmFormFieldWrapper(label, storeValues);
    }

    public void updateField(Widget widget)
    {
        this.widget = widget;
        lc.removeAll();
        lc.add(widget);
        lc.layout();
    }

    public List<String> getStoreValues()
    {
        return fsmFormFieldWrapper.getStoreValues();
    }

    public HtmlContainer getHc()
    {
        return hc;
    }

    // used to be an adaptor
    public HasValidatingValue asHasValidatingValue()
    {
        return (HasValidatingValue)widget;
    }


    public String getLabel()
    {
        return fsmFormFieldWrapper.getLabel();
    }
}


