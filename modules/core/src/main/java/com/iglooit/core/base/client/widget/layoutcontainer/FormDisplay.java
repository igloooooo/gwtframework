package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.composition.display.FormMode;
import com.clarity.core.base.client.composition.display.FormWidgetContainer;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.widget.form.ClarityFieldSet;
import com.clarity.commons.iface.type.AppX;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.LayoutData;
import com.google.gwt.user.client.ui.Widget;

public abstract class FormDisplay extends GPanel implements Display
{
    private FieldSet currentFieldSet = null;

    public void addField(Widget field)
    {
        if (currentFieldSet == null)
            throw new AppX("FieldSet is null!");
        currentFieldSet.add(field, getFormData());
    }

    public FieldSet getCurrentFieldSet()
    {
        return currentFieldSet;
    }

    public FieldSet nextFieldSet(String title)
    {
        currentFieldSet = new ClarityFieldSet(title);
        return currentFieldSet;
    }

    public abstract void setup(FormMode formMode);

    public abstract FormWidgetContainer getWidgetContainer();

    public abstract LayoutContainer nextColumn();

    public abstract LayoutContainer nextColumn(LayoutData layoutData);

    public abstract LayoutContainer nextColumn(LayoutData layoutData, Layout formLayout);

    public abstract LayoutContainer getCurrentColumn();

    protected abstract FormData getFormData();

    public Widget asWidget()
    {
        return this;
    }
}
