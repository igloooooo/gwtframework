package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.view.resource.Resource;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

/*  used for widget by itself.
*   to work with binder, see ValidatableTimeField
* */

public class ClarityTimeField extends TimeField
{
    public static final String COMMUNICATE_TIME_FORMAT = "HH:mm";
    public static final String DISPLAY_TIME_FORMAT = "hh:mm a";

    public ClarityTimeField()
    {
        super();
        this.setIncrement(15);
        this.getImages().setInvalid(Resource.ICONS.exclamationRed());
        this.setFormat(DateTimeFormat.getFormat(DISPLAY_TIME_FORMAT));
        this.setTriggerAction(ComboBox.TriggerAction.ALL);
    }

    @Override
    public Date getDateValue()
    {
        Date temp = super.getDateValue();
        if (temp == null && !getForceSelection())
        {
            try
            {
                temp = getFormat().parse(getRawValue());
            }
            catch (IllegalArgumentException ex)
            {
                temp = null;
            }
        }
        return temp;
    }
}
