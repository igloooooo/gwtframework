package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class ClarityAdapterField<GxtFieldType extends Field> implements ClarityField<Object, GxtFieldType>
{
    private AdapterField adapterField;

    private ClarityField mainField;
    private Widget widget;
    private LayoutContainer container;
    private boolean oneField;

    public ClarityAdapterField(ClarityField mainField, Widget widget,
                               double mainFieldProportion, double widgetProportion)
    {
        oneField = false;

        container = new LayoutContainer();
        container.setVisible(true);
        container.setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
        container.add(mainField.getField(), new RowData(mainFieldProportion, 1));
        container.add(widget, new RowData(widgetProportion, 1));

        //TODO: current height is restricted as 30 in order to prevent the css: "overflow:Hidden"
        //Need to figure out a better way to do it
        container.setHeight(30);

        adapterField = new AdapterField(container);

        this.widget = widget;
        this.mainField = mainField;
    }

    public ClarityAdapterField(Widget widget)
    {
        oneField = true;

        adapterField = new AdapterField(widget);
        this.widget = widget;
    }

    public boolean isOneField()
    {
        return oneField;
    }

    public ClarityField getMainField()
    {
        return mainField;
    }

    public Widget getWidget()
    {
        return widget;
    }

    public AdapterField getAdapterField()
    {
        return adapterField;
    }

    @Override
    public GxtFieldType getField()
    {
        if (oneField)
            return (GxtFieldType)adapterField;
        else
            return (GxtFieldType)mainField.getField();
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        mainField.handleValidationResults(validationResultList);
    }

    @Override
    public void valueExternallyChangedFrom(Object oldLocalValue)
    {

    }

    @Override
    public String getFieldLabel()
    {
        return adapterField.getFieldLabel();
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        adapterField.setFieldLabel(fieldLabel);
    }

    @Override
    public Option<String> getUsageHint()
    {
        if (oneField)
        {
            if (adapterField != null && adapterField.getEmptyText() != null)
                return Option.some(adapterField.getEmptyText());
            else
                return Option.none();
        }
        else
            return mainField.getUsageHint();
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        if (oneField)
            adapterField.setEmptyText(usageHint);
        else
            mainField.setUsageHint(usageHint);
    }

    @Override
    public Object getValue()
    {
        if (oneField)
            return widget.getTitle();
        else
            return mainField.getValue();
    }

    @Override
    public void setValue(Object o)
    {
        if (oneField)
            widget.setTitle(o.toString());
        else
            mainField.setValue(o);
    }

    @Override
    public void setValue(Object o, boolean b)
    {
        if (oneField)
            this.setValue(o);
        else
            mainField.setValue(o, b);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Object> objectValueChangeHandler)
    {
        if (mainField != null)
            return mainField.addValueChangeHandler(objectValueChangeHandler);
        else
            return null;
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        mainField.fireEvent(gwtEvent);
    }
}
