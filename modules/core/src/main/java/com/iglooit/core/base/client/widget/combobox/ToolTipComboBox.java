package com.iglooit.core.base.client.widget.combobox;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.Selectable;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.form.GMultiField;
import com.clarity.core.lib.client.AsyncLoadingList;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public class ToolTipComboBox<T> implements Selectable<T>, ClarityField<T, GMultiField<SimpleComboBox>>
{
    private final ValidatableComboBox<T> comboBox;
    private final GMultiField<SimpleComboBox> multiField;
    private final Html toolTip;

    public ToolTipComboBox(AsyncLoadingList<T> loadingList, String title, String text)
    {
        comboBox = new ValidatableComboBox<T>(loadingList);
        multiField = new GMultiField<SimpleComboBox>(comboBox.getField());
        //multiField.setWidth(260);
        toolTip = new Html(Resource.ICONS.informationWhite().getHTML());
        toolTip.addStyleName("icon-padding");
        toolTip.setToolTip(new ToolTipConfig(title, text));
        multiField.setSpacing(20);
        multiField.add(new AdapterField(toolTip));
    }

    public void resetState()
    {
        comboBox.resetState();
    }

    public HandlerRegistration addSelectionHandler(SelectionHandler<T> tSelectionHandler)
    {
        return comboBox.addSelectionHandler(tSelectionHandler);
    }

    public void add(List<T> values)
    {
        comboBox.add(values);
    }

    public void select(T value)
    {
        comboBox.select(value);
    }

    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        comboBox.handleValidationResults(validationResultList);
    }

    public void valueExternallyChangedFrom(T oldLocalValue)
    {
        comboBox.valueExternallyChangedFrom(oldLocalValue);
    }

    public String getFieldLabel()
    {
        return comboBox.getFieldLabel();
    }

    public void setFieldLabel(String fieldLabel)
    {
        comboBox.setFieldLabel(fieldLabel);
    }

    public Option<String> getUsageHint()
    {
        return comboBox.getUsageHint();
    }

    public void setUsageHint(String usageHint)
    {
        comboBox.setUsageHint(usageHint);
    }

    public T getValue()
    {
        return comboBox.getValue();
    }

    public void setValue(T t)
    {
        comboBox.setValue(t);
    }

    public void setValue(T t, boolean b)
    {
        comboBox.setValue(t, b);
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler)
    {
        return comboBox.addValueChangeHandler(handler);
    }

    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        comboBox.fireEvent(gwtEvent);
    }

    public GMultiField<SimpleComboBox> getField()
    {
        return multiField;
    }

    public ValidatableComboBox<T> getComboBox()
    {
        return comboBox;
    }

    public Html getToolTip()
    {
        return toolTip;
    }
}
