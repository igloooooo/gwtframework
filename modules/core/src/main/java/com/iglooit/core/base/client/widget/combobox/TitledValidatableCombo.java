package com.iglooit.core.base.client.widget.combobox;

import com.clarity.core.lib.client.HasSelectableValue;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Label;

import java.util.ArrayList;
import java.util.List;

public class TitledValidatableCombo<T> extends HorizontalPanel implements HasSelectableValue<T>
{
    private Label titleLabel;
    private final SimpleComboBox combo = new SimpleComboBox<Entry<T>>();
    private Label errors = new Label("");

    private List<Entry<T>> options = new ArrayList<Entry<T>>();

    private HandlerManager comboHandlerManager = new HandlerManager(null);

    public TitledValidatableCombo(String title)
    {
        this.titleLabel = new Label(title);
    }

    @Override
    protected void onRender(Element parent, int pos)
    {
        add(titleLabel);
        add(combo);
        add(errors);
        super.onRender(parent, pos);
    }

    public void populateOptions(List<Entry<T>> options)
    {
        this.options = options;
        for (Entry<T> option : options)
            combo.add(option);
    }

    public void setDefaultOption(int defaultOptionIndex)
    {
        combo.select(defaultOptionIndex);
    }

    public Label getTitleLabel()
    {
        return titleLabel;
    }

    public SimpleComboBox getCombo()
    {
        return combo;
    }

    public Label getErrors()
    {
        return errors;
    }

    public T getValue()
    {
        if (combo.getSimpleValue() instanceof Entry)
            return ((Entry<T>)combo.getSimpleValue()).getDataValue();
        return null;
    }

    public void setValue(T value)
    {
        for (Entry<T> option : options)
            if (option.getDataValue().equals(value))
                combo.setSimpleValue(option);
    }

    public void setValue(T value, boolean b)
    {
        setValue(value);
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<T> accountValueChangeHandler)
    {
        HandlerRegistration hr = comboHandlerManager.addHandler(ValueChangeEvent.getType(),
            accountValueChangeHandler);

        combo.addSelectionChangedListener(new SelectionChangedListener()
        {
            public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
            {
                T selected = ((SimpleComboValue<Entry<T>>)selectionChangedEvent.getSelectedItem())
                    .getValue().getDataValue();
                fireEvent(new MockValueChangeEvent<T>(selected));
            }
        });
        return hr;
    }

    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        comboHandlerManager.fireEvent(gwtEvent);
    }

    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        if (validationResultList.size() == 0)
            errors.setText("");
        else
            errors.setText(validationResultList.get(0).getReason());
    }

    public void valueExternallyChangedFrom(T oldLocalValue)
    {
        // todo ms: example
    }

    public String getFieldLabel()
    {
        return StringUtil.emptyStringIfNull(titleLabel.getText());
    }

    public void setFieldLabel(String fieldLabel)
    {
        titleLabel.setText(fieldLabel);
    }

    public Option<String> getUsageHint()
    {
        return Option.none();
    }

    public void setUsageHint(String usageHint)
    {

    }

    private static class MockValueChangeEvent<T> extends ValueChangeEvent<T>
    {
        protected MockValueChangeEvent(T t)
        {
            super(t);
        }
    }
}
