package com.iglooit.core.base.client.widget.combobox;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class ClarityComboUtil
{
    public static void enableDynamicSelectionWidthCalculation(final ComboBox comboBox, final String[] properties)
    {
        enableDynamicSelectionWidthCalculation(comboBox);
    }

    public static void enableClearFilterTextOnBlueIfNotSelected(final ComboBox comboBox)
    {
        comboBox.addListener(Events.OnBlur, new Listener<BaseEvent>()
        {
            @Override
            public void handleEvent(BaseEvent be)
            {
                if (comboBox.getValue() == null)
                {
                    comboBox.clear();
                    if (comboBox.getValidateOnBlur())
                        comboBox.validate();
                }
            }
        });
    }

    public static Listener<ComponentEvent> enableDynamicSelectionWidthCalculation(final ComboBox comboBox)
    {
        return enableDynamicSelectionWidthCalculation(comboBox, -1);
    }

    public static void disableDynamicSelectionWidthCalculation(final ComboBox comboBox,
                                                               Listener<ComponentEvent> listener)
    {
        if (comboBox != null && listener != null)
        {
            final ListView listView = comboBox.getListView();
            listView.removeListener(Events.Render, listener);
        }
    }

    public static Listener<ComponentEvent> enableDynamicSelectionWidthCalculation(final ComboBox comboBox,
                                                                                  final int maxWidth)
    {
        final ListView listView = comboBox.getListView();

        Listener<ComponentEvent> listener = new Listener<ComponentEvent>()
        {
            public void handleEvent(ComponentEvent componentEvent)
            {
                final LayoutContainer list = (LayoutContainer)listView.getParent();
                list.addListener(Events.Show, new Listener<ComponentEvent>()
                {
                    public void handleEvent(ComponentEvent componentEvent)
                    {
                        if (listView.el().getChild(0) == null)
                            return;
                        adjustListWidth();
                    }

                    private void adjustListWidth()
                    {
                        int min = comboBox.getMinLength();
                        for (int i = 0; i < listView.getStore().getCount(); i++)
                        {
                            El el = listView.el().getChild(i);
                            int childWidth = el.getTextWidth() + 5;
                            if ((childWidth) > min)
                                min = childWidth;

                            // consider all next level data for a model
                            int j = 0;
                            El elInner = el.getChild(0);
                            while (elInner != null)
                            {
                                childWidth = el.getWidth();
                                if ((childWidth) > min)
                                    min = childWidth;
                                elInner = el.getChild(++j);
                            }
                        }
                        min += listView.el().getChild(0).getPadding("lr");
                        min += listView.el().getChild(0).getBorderWidth("lr");
                        if (list.el().getChild(0).isScrollableY())
                        {
                            min += 19;
                        }
                        if (maxWidth > 0 && min > maxWidth)
                            min = maxWidth;
                        int defaultMin = comboBox.getMinListWidth();
                        if (min > defaultMin)
                            comboBox.setMinListWidth(min);
                    }
                });
            }
        };

        listView.addListener(Events.Render, listener);
        return listener;
    }
}
