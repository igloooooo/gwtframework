package com.iglooit.core.base.client.mvp;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public class GroupAdapterField implements ClarityField<Object, AdapterField>
{
    private AdapterField adapterField;
    private List<FieldInfo> fieldInfos;

    private LayoutContainer tableContainer;
    private int columnCount;

    public GroupAdapterField(List<FieldInfo> fieldInfos)
    {
        columnCount = fieldInfos.size();
        this.fieldInfos = fieldInfos;
        adapterField = onCreateAdapterField();
    }

    protected AdapterField onCreateAdapterField()
    {
        tableContainer = new LayoutContainer();
        TableLayout tableLayout = new TableLayout(fieldInfos.size());
        tableContainer.setLayout(tableLayout);
        tableContainer.setAutoHeight(true);
        for (FieldInfo fieldInfo : fieldInfos)
        {
            LayoutContainer fieldColumn = createContainerForField(fieldInfo.getField());
            tableContainer.add(fieldColumn);
        }
        AdapterField adapterField = new AdapterField(tableContainer);
        return adapterField;
    }

    private LayoutContainer createContainerForField(ClarityField field)
    {
        FormLayout formLayout = new FormLayout();
        formLayout.setLabelAlign(FormPanel.LabelAlign.TOP);
        LayoutContainer currentColContainer = new LayoutContainer();

        currentColContainer.setLayout(formLayout);
        currentColContainer.add(field.getField(), new FormData("0%"));
        return currentColContainer;
    }

    @Override
    public AdapterField getField()
    {
        return adapterField;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        for (FieldInfo fieldInfo : fieldInfos)
        {
            fieldInfo.getField().handleValidationResults(validationResultList);
        }
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
        return Option.some("");
    }

    @Override
    public void setUsageHint(String usageHint)
    {

    }

    @Override
    public Object getValue()
    {
        return "";
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

    public AdapterField getAdapterField()
    {
        return adapterField;
    }

    public List<FieldInfo> getFieldInfos()
    {
        return fieldInfos;
    }

    public LayoutContainer getTableContainer()
    {
        return tableContainer;
    }

    public int getColumnCount()
    {
        return columnCount;
    }

    public static final class FieldInfo
    {
        private String property;
        private ClarityField field;
        private double proportion;

        public FieldInfo(String property, ClarityField field,
                         double proportion)
        {
            this.property = property;
            this.field = field;
            this.proportion = proportion;
        }

        public String getProperty()
        {
            return property;
        }

        public ClarityField getField()
        {
            return field;
        }

        public double getProportion()
        {
            return proportion;
        }
    }
}
