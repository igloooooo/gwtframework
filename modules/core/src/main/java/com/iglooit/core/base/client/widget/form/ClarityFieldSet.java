package com.iglooit.core.base.client.widget.form;

import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class ClarityFieldSet extends FieldSet
{
    public ClarityFieldSet(String title)
    {
        FormLayout formLayout = new FormLayout();
        formLayout.setLabelAlign(FormPanel.LabelAlign.LEFT);
        formLayout.setLabelWidth(130);
        setHeading(title);
        setLayout(formLayout);
    }

    public ClarityFieldSet(String title, FormLayout formLayout)
    {
        setHeading(title);
        setLayout(formLayout);
    }
}
