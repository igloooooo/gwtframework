package com.iglooit.core.base.client.composition.presenter.factory.binder;

import com.clarity.core.base.client.composition.presenter.BinderFactory;
import com.clarity.core.base.client.composition.presenter.CRUDPresenter;
import com.clarity.core.base.client.mvp.BindableClarityWidgetBinder;
import com.clarity.core.base.client.mvp.Binder;
import com.clarity.core.base.client.mvp.ClarityAdapterFieldBinder;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.ClaritySearchComboBox2Binder;
import com.clarity.core.base.client.mvp.ClaritySearchComboBoxBinder;
import com.clarity.core.base.client.mvp.ClarityXSimpleComboBoxBinder;
import com.clarity.core.base.client.mvp.DateTimeRangeBinder;
import com.clarity.core.base.client.mvp.GroupAdapterField;
import com.clarity.core.base.client.mvp.GroupAdapterFieldBinder;
import com.clarity.core.base.client.mvp.ImmutableBinder;
import com.clarity.core.base.client.mvp.MutableBinder;
import com.clarity.core.base.client.mvp.NullBinder;
import com.clarity.core.base.client.mvp.NullClarityField;
import com.clarity.core.base.client.mvp.PasswordConfirmBindingTarget;
import com.clarity.core.base.client.mvp.ValidatableCheckBoxBinder;
import com.clarity.core.base.client.mvp.ValidatableComboBoxBinder;
import com.clarity.core.base.client.mvp.ValidatableComplexComboBoxBinder;
import com.clarity.core.base.client.mvp.ValidatableDateFieldBinder;
import com.clarity.core.base.client.mvp.ValidatableDoubleSpinnerFieldBinder;
import com.clarity.core.base.client.mvp.ValidatableNumberFieldBinder;
import com.clarity.core.base.client.mvp.ValidatableSliderBinder;
import com.clarity.core.base.client.mvp.ValidatableSpinnerFieldBinder;
import com.clarity.core.base.client.mvp.ValidatableTextAreaBinder;
import com.clarity.core.base.client.mvp.ValidatableTextFieldBaseBinder;
import com.clarity.core.base.client.mvp.ValidatableTimeFieldBinder;
import com.clarity.core.base.client.widget.combobox.ClaritySearchComboBox;
import com.clarity.core.base.client.widget.combobox.ClaritySearchComboBox2;
import com.clarity.core.base.client.widget.combobox.ClarityXSimpleComboBox;
import com.clarity.core.base.client.widget.combobox.ToolTipComboBox;
import com.clarity.core.base.client.widget.combobox.TypeConvertingValidatableComboBox;
import com.clarity.core.base.client.widget.combobox.ValidatableComboBox;
import com.clarity.core.base.client.widget.combobox.ValidatableComplexComboBox;
import com.clarity.core.base.client.widget.form.ClarityAdapterField;
import com.clarity.core.base.client.widget.form.DateTimeRangeField;
import com.clarity.core.base.client.widget.form.GMultiField;
import com.clarity.core.base.client.widget.form.LabelFormField;
import com.clarity.core.base.client.widget.form.PasswordConfirmField;
import com.clarity.core.base.client.widget.form.ValidatableAdapterField;
import com.clarity.core.base.client.widget.form.ValidatableCheckBox;
import com.clarity.core.base.client.widget.form.ValidatableDateField;
import com.clarity.core.base.client.widget.form.ValidatableDoubleSpinnerField;
import com.clarity.core.base.client.widget.form.ValidatableNumberField;
import com.clarity.core.base.client.widget.form.ValidatableSlider;
import com.clarity.core.base.client.widget.form.ValidatableSpinnerField;
import com.clarity.core.base.client.widget.form.ValidatableTextArea;
import com.clarity.core.base.client.widget.form.ValidatableTextFieldBase;
import com.clarity.core.base.client.widget.form.ValidatableTimeField;
import com.clarity.core.base.client.widget.form.ValidatableWrapperField;
import com.clarity.core.base.client.widget.layoutcontainer.BindableClarityWidget;
import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.commons.iface.type.AppX;

public class WidgetDerivedBinderFactory
{
    private static DefaultBinderFactory defaultBinderFactory = new DefaultBinderFactory();

    public static BinderFactory getDefault()
    {
        return defaultBinderFactory;
    }

    private static class DefaultBinderFactory implements BinderFactory
    {
        public Binder createBinder(String propertyName,
                                   ValidatableMeta validatableMeta, ClarityField clarityField)
        {
            ClarityField field = clarityField;
            if (field instanceof ClarityXSimpleComboBox)
                return new ClarityXSimpleComboBoxBinder((ClarityXSimpleComboBox)field, validatableMeta, propertyName);
            if (field instanceof ClaritySearchComboBox)
                return new ClaritySearchComboBoxBinder((ClaritySearchComboBox)field, validatableMeta, propertyName);
            if (field instanceof ClaritySearchComboBox2)
                return new ClaritySearchComboBox2Binder((ClaritySearchComboBox2)field, validatableMeta, propertyName);
            if (field instanceof ClarityAdapterField)
                return new ClarityAdapterFieldBinder((ClarityAdapterField)field, validatableMeta, propertyName);
            if (field instanceof GMultiField)
                field = (ClarityField)((GMultiField)field).getMainField();
            if (field instanceof GroupAdapterField)
                return new GroupAdapterFieldBinder((GroupAdapterField)field, validatableMeta, propertyName);
            if (field instanceof ValidatableComboBox)
                return new ValidatableComboBoxBinder((ValidatableComboBox)field, validatableMeta, propertyName);
            if (field instanceof ValidatableComplexComboBox)
                return new ValidatableComplexComboBoxBinder(
                        (ValidatableComplexComboBox)field, validatableMeta, propertyName);
            if (field instanceof ToolTipComboBox)
                return new ValidatableComboBoxBinder(((ToolTipComboBox)field).getComboBox(),
                    validatableMeta, propertyName);
            if (field instanceof TypeConvertingValidatableComboBox)
                return new ValidatableComboBoxBinder((TypeConvertingValidatableComboBox)field,
                    validatableMeta, propertyName);
            if (field instanceof PasswordConfirmField)
            {
                PasswordConfirmField passwordConfirmField = (PasswordConfirmField)field;
                String normalPropertyName = CRUDPresenter.getOriginalPropertyNameFromConfirm(propertyName);
                return PasswordConfirmBindingTarget.getBinder(
                    passwordConfirmField, passwordConfirmField.getPasswordField(),
                    normalPropertyName, validatableMeta);
            }
            if (field instanceof DateTimeRangeField)
                return new DateTimeRangeBinder((DateTimeRangeField)field, validatableMeta, propertyName);
            if (field instanceof ValidatableTextArea)
                return new ValidatableTextAreaBinder((ValidatableTextArea)field, validatableMeta, propertyName);
            if (field instanceof ValidatableTextFieldBase)
                return new ValidatableTextFieldBaseBinder((ValidatableTextFieldBase)field,
                    validatableMeta, propertyName);
            if (field instanceof ValidatableSpinnerField)
                return new ValidatableSpinnerFieldBinder((ValidatableSpinnerField)field,
                    validatableMeta, propertyName);
            if (field instanceof ValidatableDoubleSpinnerField)
                return new ValidatableDoubleSpinnerFieldBinder((ValidatableDoubleSpinnerField)field,
                        validatableMeta, propertyName);
            if (field instanceof LabelFormField)
                return new ImmutableBinder(field, validatableMeta, propertyName);
            if (field instanceof ValidatableDateField)
                return new ValidatableDateFieldBinder((ValidatableDateField)field, validatableMeta, propertyName);
            if (field instanceof ValidatableNumberField)
                return new ValidatableNumberFieldBinder((ValidatableNumberField)field, validatableMeta, propertyName);
            if (field instanceof ValidatableTimeField)
                return new ValidatableTimeFieldBinder((ValidatableTimeField)field, validatableMeta, propertyName);
            if (field instanceof NullClarityField)
                return new NullBinder(propertyName);
            if (field instanceof ValidatableSlider)
                return new ValidatableSliderBinder(field, validatableMeta, propertyName);
            if (field instanceof ValidatableCheckBox)
                return new ValidatableCheckBoxBinder((ValidatableCheckBox)field, validatableMeta, propertyName);
            if (field instanceof ValidatableAdapterField || field instanceof ValidatableWrapperField)
                return new MutableBinder(field, validatableMeta, propertyName);
            if (field instanceof BindableClarityWidget)
                return new BindableClarityWidgetBinder(field, validatableMeta, propertyName);
            throw new AppX("Cannot find binder for propertyName: " + propertyName);
        }
    }
}
