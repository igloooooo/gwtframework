package com.iglooit.core.base.client.view;

import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.view.resource.Resource;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.user.client.ui.Widget;

public class WorkGroupComboView implements Display
{

    private SimpleComboBox<String> workGroupCombo;

    public WorkGroupComboView()
    {
        workGroupCombo = new SimpleComboBox<String>();
        workGroupCombo.setTriggerAction(ComboBox.TriggerAction.ALL);
        workGroupCombo.setEditable(false);
        workGroupCombo.getImages().setInvalid(Resource.ICONS.exclamationRed());
    }

    @Override
    public String getLabel()
    {
        return "";
    }

    @Override
    public Widget asWidget()
    {
        return workGroupCombo;
    }

    public SimpleComboBox<String> getComboBox()
    {
        return workGroupCombo;
    }
}
