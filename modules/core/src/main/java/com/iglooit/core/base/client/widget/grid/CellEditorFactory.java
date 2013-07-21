package com.iglooit.core.base.client.widget.grid;

import com.clarity.core.base.client.function.ConvertorFunctionHome;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.widget.combobox.ClaritySearchComboBox2;
import com.clarity.core.base.client.widget.combobox.SearchComboBox;
import com.clarity.core.base.client.widget.combobox.ValidatableComboBox;
import com.clarity.core.base.client.widget.combobox.ValidatableComplexComboBox;
import com.clarity.core.base.client.widget.form.ClarityAdapterField;
import com.clarity.core.base.client.widget.form.ClarityTimeField;
import com.clarity.core.base.client.widget.form.ValidatableNumberField;
import com.clarity.core.base.client.widget.form.ValidatableTimeField;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;

public class CellEditorFactory
{
    public static final CellEditor createCellEditor(ClarityField field)
    {
        if (field instanceof ValidatableTimeField)
            return createTimeFieldEditor((ValidatableTimeField)field);
        if (field instanceof ValidatableNumberField)
            return createNumberFieldEditor((ValidatableNumberField)field);
        else if (field instanceof ValidatableComboBox)
            return createComboboxEditor((ValidatableComboBox)field);
        else if (field instanceof ValidatableComplexComboBox)
            return createComplexComboboxEditor((ValidatableComplexComboBox)field);
        else if (field instanceof ClaritySearchComboBox2)
            return createSearchComboboxEditor((ClaritySearchComboBox2)field);
        else if (field instanceof ClarityAdapterField)
            return createAdaptorFieldEditor((ClarityAdapterField)field);
        else
            return new CellEditor((Field)field);
    }

    private static CellEditor createAdaptorFieldEditor(ClarityAdapterField field)
    {
        CellEditor editor = new CellEditor(field.getField())
        {
            public Object preProcessValue(Object value)
            {
                return value;
            }

            public Object postProcessValue(Object value)
            {
                return value;
            }
        };
        return editor;
    }

    private static CellEditor createComboboxEditor(final ValidatableComboBox validatableComboBox)
    {
        final SimpleComboBox simpleComboBox = validatableComboBox.getComboBox();
        CellEditor editor = new CellEditor(simpleComboBox)
        {
            public Object preProcessValue(Object value)
            {
                if (value == null)
                {
                    return value;
                }
                return simpleComboBox.findModel(value.toString());
            }

            public Object postProcessValue(Object value)
            {
                if (value == null)
                {
                    return value;
                }
                return validatableComboBox.getValue();
            }
        };
        return editor;
    }

    private static CellEditor createComplexComboboxEditor(final ValidatableComplexComboBox validatableComplexComboBox)
    {
        final ComboBox comboBox = validatableComplexComboBox.getComboBox();
        CellEditor editor = new CellEditor(comboBox)
        {
            public Object preProcessValue(Object value)
            {
                if (value == null)
                {
                    return value;
                }
                return comboBox.getData(value.toString());
            }

            public Object postProcessValue(Object value)
            {
                if (value == null)
                {
                    return value;
                }
                return validatableComplexComboBox.getValue();
            }
        };
        return editor;
    }

    private static CellEditor createSearchComboboxEditor(final ClaritySearchComboBox2 scb2)
    {
        final SearchComboBox scb = scb2.getField();
        CellEditor editor = new CellEditor(scb)
        {
            public Object preProcessValue(Object value)
            {
                if (value == null || scb.hasNoElement())
                {
                    return null;
                }
                for (Object s : scb.getStore().getModels())
                {
                    if (((ModelData)s).get("value").equals(value))
                    {
                        return s;
                    }
                }
                return null;
            }

            public Object postProcessValue(Object value)
            {
                if (value == null)
                {
                    return value;
                }
                return scb.getValue().get("value");
            }
        };
        return editor;
    }

    private static CellEditor createNumberFieldEditor(final ValidatableNumberField validatableNumberField)
    {
        final NumberField numberField = validatableNumberField.getField();
        CellEditor editor = new CellEditor(numberField)
        {
            public Object preProcessValue(Object value)
            {
                return (value == null ? null : Double.valueOf((String)value));
            }

            @Override
            public Object getValue()
            {
                Number number = numberField.getValue();
                return number == null ? "" : number.toString();
            }

            @Override
            public void setValue(Object value)
            {
                if (value == null)
                    numberField.setValue(null);
                else
                    numberField.setValue(Integer.valueOf((String)value));
            }

            @Override
            public Object postProcessValue(Object value)
            {
                return numberField.getRawValue();
            }
        };
        return editor;
    }

    private static CellEditor createTimeFieldEditor(ValidatableTimeField validatableTimeField)
    {
        final TimeField timeField = validatableTimeField.getField();
        CellEditor editor = new CellEditor(timeField)
        {
            public Object preProcessValue(Object value)
            {
//                return value == null ? null : value.toString();
                return ConvertorFunctionHome.getTimeFromString(
                    value == null ? null : value.toString(),
                    ClarityTimeField.DISPLAY_TIME_FORMAT);
            }


            @Override
            public Object getValue()
            {
                Time time = timeField.getValue();
                return time == null ? "" : ConvertorFunctionHome.getStringFromTime(
                    time,
                    ClarityTimeField.DISPLAY_TIME_FORMAT);
            }

            @Override
            public void setValue(Object value)
            {
                if (value == null)
                    timeField.setValue(null);
                else
                    timeField.setValue(ConvertorFunctionHome.getTimeFromString(
                        timeField.getRawValue(),
                        ClarityTimeField.DISPLAY_TIME_FORMAT));
            }

            @Override
            public Object postProcessValue(Object value)
            {
                return timeField.getRawValue();
            }
        };
        return editor;
    }
}
