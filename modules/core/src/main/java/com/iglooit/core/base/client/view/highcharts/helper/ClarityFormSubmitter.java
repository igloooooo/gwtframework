package com.iglooit.core.base.client.view.highcharts.helper;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper method for submitting data to a form
 */
public class ClarityFormSubmitter
{

    private List<Widget> submitFormWidgets;
    private FormPanel gwtFormPanel;
    private Panel panel;

    /**
     * Name of the servlet or location to submit form to
     *
     * Please remember to include GWT.getHostPageBaseURL()!!
     * @param submitURL
     */
    public ClarityFormSubmitter(String submitURL)
    {

        gwtFormPanel = new FormPanel("_blank");
        panel = new VerticalPanel();
        gwtFormPanel.setWidget(panel);
        gwtFormPanel.setAction(submitURL);
        gwtFormPanel.setMethod(FormPanel.METHOD_POST);
        submitFormWidgets = new ArrayList<Widget>();
        gwtFormPanel.setVisible(false);
    }

    public void clearForm()
    {
        for (Widget widget : submitFormWidgets)
        {
            panel.remove(widget);
        }
    }

    public void submitForm()
    {
        gwtFormPanel.submit();

    }

    public void setParameter(String value, String parameter)
    {
        setParameters(Arrays.asList(value), parameter);
    }

    public void setParameters(List<String> values, String parameter)
    {
        for (String value : values)
        {
            Hidden hidden = new Hidden();
            hidden.setName(parameter);
            hidden.setValue(value);
            panel.add(hidden);
            submitFormWidgets.add(hidden);
        }
    }

    public Widget getWidget()
    {
        return gwtFormPanel;
    }

    public FormPanel getFormPanel()
    {
        return gwtFormPanel;
    }
}
