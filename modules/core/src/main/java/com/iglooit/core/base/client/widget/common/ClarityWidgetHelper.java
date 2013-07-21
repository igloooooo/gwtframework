package com.iglooit.core.base.client.widget.common;

import com.clarity.core.base.client.view.ClarityStyle;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;

public class ClarityWidgetHelper
{
    private static final String DIRTY_STYLE_NAME = "x-grid3-dirty-cell";

    public static Component getWidgetFromOption(ClarityWidgetOption widgetOption)
    {
        ClarityWidgetOption.WidgetType widgetType = widgetOption.getWidgetClassString();
        switch (widgetType)
        {
            case BUTTON:
                Button button = new Button(widgetOption.getDisplayTitle());
                button.addStyleName(ClarityStyle.BUTTON_CSS);
                if (widgetOption.getPreClickBaseListener() != null)
                    button.addListener(Events.BeforeSelect, widgetOption.getPreClickBaseListener());
                if (widgetOption instanceof ClarityButtonOption)
                    button.addSelectionListener(((ClarityButtonOption)widgetOption).
                            getSelectionListener());
                return button;
            case INTEGER_NUMBER_FIELD:
                ClarityWidgetNumberField numberField = new ClarityWidgetNumberField();
                numberField.setPropertyEditorType(Integer.class);
                numberField.setAllowBlank(false);
                return numberField;
            default:
                return null;
        }
    }

    /**
     * if isApply = false, we will remove dirty style
     *
     * @param widget
     * @param isApply
     */
    public static void applyDirtyStyle(Component widget, boolean isApply)
    {
        if (widget instanceof ClarityWidgetNumberField)
            ((ClarityWidgetNumberField)widget).addDirtyStyle(isApply);
    }

    public static class ClarityWidgetNumberField extends NumberField
    {
        public void addDirtyStyle(boolean isApply)
        {
            if (isApply)
                getInputEl().addStyleName(DIRTY_STYLE_NAME);
            else if (getInputEl().getStyleName().contains(DIRTY_STYLE_NAME))
                getInputEl().removeStyleName(DIRTY_STYLE_NAME);
        }
    }
}
