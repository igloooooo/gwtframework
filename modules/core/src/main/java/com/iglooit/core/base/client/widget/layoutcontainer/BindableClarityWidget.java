package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class BindableClarityWidget implements ClarityField<Object, Field>
{
    private Widget widget;
    private LayoutContainer container;

    public BindableClarityWidget(Widget widget)
    {
        container = new LayoutContainer();
        container.setVisible(true);
        container.add(widget, new RowData(-1, -1));

        this.widget = widget;
    }

    public LayoutContainer getContainer()
    {
        return container;
    }

    public Widget getWidget()
    {
        return widget;
    }

    @Override
    public Field getField()
    {
        return null;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {

    }

    @Override
    public void valueExternallyChangedFrom(Object oldLocalValue)
    {

    }

    @Override
    public String getFieldLabel()
    {
        return "";
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {

    }

    @Override
    public Option<String> getUsageHint()
    {
        return Option.option("");
    }

    @Override
    public void setUsageHint(String usageHint)
    {

    }

    @Override
    public Object getValue()
    {
        return null;
    }

    @Override
    public void setValue(Object o)
    {

    }

    @Override
    public void setValue(Object o, boolean b)
    {

    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Object> objectValueChangeHandler)
    {
        return null;
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {

    }
}
