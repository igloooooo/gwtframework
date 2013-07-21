package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.composition.display.FormMode;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.commons.iface.type.AppX;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.LayoutData;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Form display that lays out form fields in columns.
 * <p/>
 * Notes:
 * - The number of columns must be set during initialisation using setColumnCount()
 * - New columns are added using nextColumn(), with the width being either calculated automatically (based on the
 * column count) or set specifically via ColumnData
 * - FormLayout attributes (e.g. label width, label position, etc) can be modified on column by column basis
 * - To have a simple vertical form layout, use the default column count value or set it to 1
 * - Fieldsets are optional. You can add more than one fieldset to a column by using nextFieldSet()
 */
public abstract class ColumnFormDisplay extends FormDisplay
{
    private int columnCount = 1;
    private List<FieldSet> fieldSets = new ArrayList<FieldSet>();
    private LayoutContainer currentColContainer = null;
    private LayoutContainer formContainer = null;
    private FormData formData;

    public ColumnFormDisplay()
    {
        super();
        setLayout(new FitLayout());
        formData = new FormData("93%");
    }

    public void setColumnCount(int count)
    {
        this.columnCount = count;
    }

    public ColumnData getColumnData()
    {
        return new ColumnData(1.0 / columnCount);
    }

    public void setup(FormMode formMode)
    {
        setStyleName(formMode.value());
        clear();
        getWidgetContainer().setupForm(this, formMode);
        add(formContainer);
        layout();
    }

    public void clear()
    {
        for (FieldSet fs : fieldSets)
            fs.removeAll();
        fieldSets.clear();
        if (formContainer != null)
            formContainer.removeAll();
        removeAll();
        formContainer = makeNewFormLayoutContainer();
    }

    public LayoutContainer getFormContainer()
    {
        return formContainer;
    }

    /**
     * Creates a new column and automatically calculates the column width based on the number of columns set using
     * setColumnCount(). For exmample if you set up 2 columns, then the width of each will be 0.5
     */
    public LayoutContainer nextColumn()
    {
        FormLayout formLayout = new FormLayout();
        formLayout.setLabelAlign(FormPanel.LabelAlign.LEFT);
        formLayout.setLabelWidth(130);
        return nextColumn(getColumnData(), formLayout);
    }

    /**
     * Creates a new column and allows you to override the width of each column via columnData. This is useful if
     * you say set up two columns but when them to be different widths rather then the default 0.5, e.g:
     * nextColumn(new ColumnData(0.3));
     * nextColumn(new ColumnData(0.7));
     * <p/>
     * Alternatively you could even have one column width as a percentage and the other as a fixed width
     */
    public LayoutContainer nextColumn(LayoutData columnData)
    {
        FormLayout formLayout = new FormLayout();
        formLayout.setLabelAlign(FormPanel.LabelAlign.LEFT);
        return nextColumn(columnData, formLayout);
    }

    /**
     * Creates a new column and also allows you to overwrite the default FormLayout used to layout the form fields.
     * This is useful if you want to modify attributes such as label width, label positions, field width, etc.
     * on a column by column basis
     */
    public LayoutContainer nextColumn(LayoutData columnData, Layout layout)
    {
        currentColContainer = new LayoutContainer();
        currentColContainer.setLayout(layout);
        if (columnCount > 1)
            currentColContainer.addStyleName(ClarityStyle.FORM_COLUMN_CONTAINER);
        formContainer.add(currentColContainer, columnData);
        return currentColContainer;
    }

    public LayoutContainer getCurrentColumn()
    {
        return currentColContainer;
    }

    protected LayoutContainer makeNewFormLayoutContainer()
    {
        LayoutContainer formColumnContainer = new LayoutContainer();
        formColumnContainer.setLayout(new ColumnLayout());
        add(formColumnContainer);
        formColumnContainer.setAutoHeight(true);
        return formColumnContainer;
    }

    protected FormData getFormData()
    {
        return formData;
    }

    protected void setFormData(FormData formData)
    {
        this.formData = formData;
    }

    /**
     * Creates a new fieldset.
     * A column must exist in order to add a fieldset and you can added multiple fieldsets in a single column
     */
    public FieldSet nextFieldSet(String title)
    {
        if (getCurrentColumn() == null)
            throw new AppX("Current form column is null");
        FieldSet fs = super.nextFieldSet(title);
        getCurrentColumn().add(fs, new FormData("100%"));
        return fs;
    }

    public void addField(Widget field)
    {
        if (getCurrentFieldSet() != null)
            getCurrentFieldSet().add(field, getFormData());
        else if (getCurrentColumn() != null)
            getCurrentColumn().add(field, getFormData());
        else
            throw new AppX("FieldSet and Column are null!");
    }
}
