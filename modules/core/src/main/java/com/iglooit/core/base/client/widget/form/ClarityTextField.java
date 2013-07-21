package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.view.resource.Resource;
import com.extjs.gxt.ui.client.widget.form.TextField;

public class ClarityTextField<D> extends TextField<D>
{
    public ClarityTextField()
    {
        super();
        this.getImages().setInvalid(Resource.ICONS.exclamationRed());
    }
}
