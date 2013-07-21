package com.iglooit.core.base.client.widget.form;

import com.clarity.core.lib.iface.BssTimeUtil;
import com.clarity.core.base.client.view.resource.Resource;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.widget.form.DateField;

import java.util.Date;

public class ClarityDateField extends DateField
{
    private int listZIndex = -1;
    private boolean isValueValid = false;

    public ClarityDateField()
    {
        super();
        this.getImages().setInvalid(Resource.ICONS.exclamationRed());
    }

    @Override
    protected void onTriggerClick(ComponentEvent ce)
    {
        super.onTriggerClick(ce);
        if (listZIndex != -1)
        {
            getDatePicker().el().getParent().getParent().
                getParent().setZIndex(listZIndex);
        }
    }

    @Override
    protected boolean validateValue(String value)
    {
        isValueValid = super.validateValue(value);
        return isValueValid;
    }

    public boolean isValueValid()
    {
        return isValueValid;
    }

    public void setListZIndex(int listZIndex)
    {
        this.listZIndex = listZIndex;
    }

    @Override
    public Date getValue()
    {
        setValue(super.getValue());
        if (this.value == null)
            return null;
        Date value = this.value;
        return BssTimeUtil.getBeginningOfDay(value);
    }
}
