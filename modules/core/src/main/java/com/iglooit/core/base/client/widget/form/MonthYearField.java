package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.combobox.ClarityComboBox;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.constants.DateTimeConstants;

import java.util.Arrays;
import java.util.List;

public class MonthYearField
{
    private DateTimeConstants constants;
    private String[] monthNames;

    private ClarityComboBox<String> monthComboBox;
    private ClarityComboBox<Integer> yearComboBox;

    private AdapterField adapterField;
    private LayoutContainer container;

    public MonthYearField()
    {
        initWidgets();
        container = new LayoutContainer(new RowLayout(Style.Orientation.HORIZONTAL));

        container.add(monthComboBox, new RowData(0.5, -1, new Margins(0, 5, 0, 0)));
        container.add(yearComboBox, new RowData(0.5, -1, new Margins(0, 0, 0, 0)));

        adapterField = new AdapterField(container);
        adapterField.getImages().setInvalid(Resource.ICONS.exclamationRed());
    }

    private void initWidgets()
    {
        constants = LocaleInfo.getCurrentLocale().getDateTimeConstants();
        monthNames = constants.months();

        monthComboBox = new ClarityComboBox<String>();
        monthComboBox.addSelectionChangedListener(new SelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent se)
            {
                clearInvalid();
            }
        });
        for (String monthName : monthNames)
        {
            monthComboBox.add(monthName);
        }

        yearComboBox = new ClarityComboBox<Integer>();
        yearComboBox.addSelectionChangedListener(new SelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent se)
            {
                clearInvalid();
            }
        });
    }

    public void addYears(List<Integer> years)
    {
        yearComboBox.removeAll();
        yearComboBox.add(years);
    }

    public void selectYear(Integer year)
    {
        yearComboBox.selectItem(year);
    }

    public void selectMonth(int month)
    {
        String monthName = monthNames[month - 1];
        monthComboBox.selectItem(monthName);
    }

    public Integer getYear()
    {
        return yearComboBox.getCurrentSelectedValue();
    }

    public int getMonth()
    {
        String selectedMonth = monthComboBox.getCurrentSelectedValue();
        return Arrays.asList(monthNames).indexOf(selectedMonth) + 1;
    }

    public void markInvalid(String invalid)
    {
        adapterField.markInvalid(invalid);
    }

    public AdapterField getAdapterField()
    {
        return adapterField;
    }

    public void clearInvalid()
    {
        adapterField.clearInvalid();
    }

    public void setVisible(boolean isVisible)
    {
        adapterField.setVisible(isVisible);
        if (isVisible)
            container.layout(true);
    }

    public void layout()
    {
        container.layout(true);
    }

    public void setSize(int width, int height)
    {
        adapterField.setSize(width, height);
        container.setSize(width, height);
    }

    public void setWidth(int width)
    {
      setSize(width, -1);
    }
}
