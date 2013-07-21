package com.iglooit.core.base.client.widget.combobox;

import com.clarity.core.base.client.model.SimpleModelValue;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.store.ListStore;

import java.util.ArrayList;
import java.util.List;

public class XSimpleFilterAllComboBox extends XComboBox<SimpleModelValue<String>> implements IComboBox<String>
{
    private static final BaseViewConstants BASEVC = I18NFactoryProvider.get(BaseViewConstants.class);
    private SelectionFormatter selectionFormatter;

    public XSimpleFilterAllComboBox()
    {
        super();
        init();
    }

    public XSimpleFilterAllComboBox(XComboBoxOptions xComboBoxOptions)
    {
        super(xComboBoxOptions);
        init();
    }

    private void init()
    {
        setDisplayField(SimpleModelValue.VALUE);
        setStore(new ListStore<SimpleModelValue<String>>());
        setSelectionFormatter(new DefaultSelectionFormatter());
    }

    @Override
    protected void onShow()
    {
        super.onShow();
    }

    @Override
    public void updateStore(List<String> list)
    {
        store.removeAll();
        List<SimpleModelValue<String>> modelList = new ArrayList<SimpleModelValue<String>>();
        if (list != null)
            for (String item : list)
            {
                if (StringUtil.isNotEmpty(item))
                {
                    modelList.add(new SimpleModelValue<String>(item));
                }
            }
        store.add(modelList);
        setRawValue(getEmptyText());
    }

    @Override
    public void maskCombo()
    {
        if (isRendered() && !isMasked())
            mask("", ClarityStyle.MASK_FIELD);
    }

    @Override
    public void unmaskCombo()
    {
        if (isRendered() && isMasked())
            unmask();
    }


    @Override
    public List<String> getSelectedItems()
    {
        List<SimpleModelValue<String>> selectedModels = getSelection();
        List<String> selectedStrings = new ArrayList<String>();
        if (selectedModels != null)
        {
            for (SimpleModelValue<String> model : selectedModels)
                selectedStrings.add(model.getValue());
        }
        return selectedStrings;
    }

    public void setSelectionItems(List<String> values)
    {
        List<SimpleModelValue<String>> modelList = new ArrayList<SimpleModelValue<String>>();
        if (store == null)
            return;
        if (values != null)
            for (String value : values)
                for (int i = 0; i < store.getCount(); i++)
                {
                    Object storeValue = store.getAt(i).getValue();
                    if ((storeValue == null && value == null)
                        || ((storeValue != null) && store.getAt(i).getValue().equals(value)))
                    {
                        modelList.add(store.getAt(i));
                        break;
                    }
                }
        setSelection(modelList);
    }

    public void setSelectionFormatter(SelectionFormatter selectionFormatter)
    {
        this.selectionFormatter = selectionFormatter;
    }

    @Override
    public String formatRawText(List<SimpleModelValue<String>> simpleModelValues)
    {
        if (this.selectionFormatter == null)
            return super.formatRawText(simpleModelValues);
        else
            return selectionFormatter.format(this, simpleModelValues);
    }

    interface SelectionFormatter
    {
        String format(XSimpleFilterAllComboBox comboBox, List<SimpleModelValue<String>> list);
    }

    public static final class DefaultSelectionFormatter implements SelectionFormatter
    {
        public String format(XSimpleFilterAllComboBox comboBox, List<SimpleModelValue<String>> list)
        {
            if (comboBox.getStore() == null || comboBox.getStore().getCount() == 0)
                return BASEVC.noAvailableValue();
            StringBuilder sb = new StringBuilder("");
            if (list == null || list.size() == 0)
                return "";
            String count = "(" + list.size() + ")";
            for (int i = 0; i < list.size(); i++)
            {
                if (i > 0)
                {
                    sb.append(",");
                }
                sb.append(list.get(i).getValue());
            }
            return count + " - " + sb.toString();
        }
    }
}
