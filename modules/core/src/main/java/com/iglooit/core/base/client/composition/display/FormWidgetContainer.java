package com.iglooit.core.base.client.composition.display;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.widget.form.ClarityAdapterField;
import com.clarity.core.base.client.widget.layoutcontainer.BindableClarityWidget;
import com.clarity.core.base.client.widget.layoutcontainer.FormDisplay;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Tuple2;

import java.util.ArrayList;
import java.util.List;

public abstract class FormWidgetContainer extends WidgetContainer
{
    private final List<Tuple2<String, FormItem>> formConfig;

    protected ClarityField doGetField(String propertyName, FormMode formMode)
    {
        throw new AppX("PropertyName: " + propertyName + " not initialised for formMode: " + formMode);
    }

    protected FormWidgetContainer()
    {
        formConfig = new ArrayList<Tuple2<String, FormItem>>();
        doSetup();
    }

    protected abstract void doSetup();

    public void setupForm(FormDisplay display, FormMode formMode)
    {
        for (Tuple2<String, FormItem> tuple2 : formConfig)
        {
            tuple2.getSecond().setup(display, formMode);
        }
    }

    protected void addFields(String propertyName, ClarityField mutableField, ClarityField immutableField)
    {
        addField(propertyName, FormMode.CREATE, mutableField);
        addField(propertyName, FormMode.UPDATE, mutableField);
        addField(propertyName, FormMode.READ, immutableField);
        formConfig.add(new Tuple2<String, FormItem>(propertyName, new FormField(propertyName)));
    }

    protected void addFields(String propertyName, ClarityField mutableField, ClarityField immutableField,
                             boolean shown)
    {
        addField(propertyName, FormMode.CREATE, mutableField);
        addField(propertyName, FormMode.UPDATE, mutableField);
        addField(propertyName, FormMode.READ, immutableField);
        formConfig.add(new Tuple2<String, FormItem>(propertyName, new FormField(propertyName, shown)));
    }

    protected void addConfig(String propertyName, FormDisplaySetup callback)
    {
        formConfig.add(new Tuple2<String, FormItem>(propertyName, new FormConfig(callback)));
    }

    protected void addField(String propertyName)
    {
        formConfig.add(new Tuple2<String, FormItem>(propertyName, new FormField(propertyName)));
    }

    @Override
    public void clearWidgets()
    {
        formConfig.clear();
        super.clearWidgets();
        doSetup();
    }

    private abstract class FormItem
    {
        public abstract void setup(FormDisplay formDisplay, FormMode formMode);
    }

    private class FormField extends FormItem
    {
        private final String propertyName;
        private boolean shown = true;

        public FormField(String propertyName)
        {
            this.propertyName = propertyName;
        }

        public FormField(String propertyName, boolean shown)
        {
            this.propertyName = propertyName;
            this.shown = shown;
        }

        public boolean isShown()
        {
            return shown;
        }

        public String getPropertyName()
        {
            return propertyName;
        }

        public void setup(FormDisplay formDisplay, FormMode formMode)
        {
            if (!shown)
                return;
            ClarityField cf = getField(propertyName, formMode);

            if (cf instanceof ClarityAdapterField)
            {
                formDisplay.addField(((ClarityAdapterField)cf).getAdapterField());
                return;
            }
            if (cf instanceof BindableClarityWidget)
                formDisplay.addField(((BindableClarityWidget)cf).getContainer());
            else if (cf != null && cf.getField() != null)
                formDisplay.addField(cf.getField());
        }
    }

    private class FormConfig extends FormItem
    {
        private final FormDisplaySetup displaySetupCallback;

        public FormConfig(FormDisplaySetup displaySetupCallback)
        {
            this.displaySetupCallback = displaySetupCallback;
        }

        public FormDisplaySetup getDisplaySetupCallback(FormMode formMode)
        {
            return displaySetupCallback;
        }

        public void setup(FormDisplay formDisplay, FormMode formMode)
        {
            displaySetupCallback.setup(formDisplay);
        }
    }
}
