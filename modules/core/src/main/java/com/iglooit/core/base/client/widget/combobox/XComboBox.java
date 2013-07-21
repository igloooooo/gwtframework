package com.iglooit.core.base.client.widget.combobox;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ListModelPropertyEditor;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XComboBox<D extends ModelData> extends ComboBox<D>
{
    private static final BaseViewConstants BASEVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private XComboBoxOptions options;

    private String valueFieldSeparator = ";";
    private String rawSeparator = ", ";
    private ToolBar toolBar;
    private XCheckBoxListView xListView;
    private Button selectAllButton;
    private Button deSelectAllButton;
    private Button applyButton;
    private List<D> selectedItems;
    private boolean selectAll;
    private LayoutContainer list;
    private int listZIndex = -1;
    private String allSelectedText;

    public XComboBox()
    {
        this(new XComboBoxOptions());
    }

    public XComboBox(XComboBoxOptions options)
    {
        this.options = options;

//        setLazyRender(false);
        createSelectAllToolBar();
        allSelectedText = BASEVC.all();
        messages = new ComboBoxMessages();
        xListView = new XCheckBoxListView<D>();
        setView(xListView);
        store = new ListStore<D>();
        setStore(store);
        setPropertyEditor(new ListModelPropertyEditor<D>());
        monitorWindowResize = true;
        windowResizeDelay = 0;
        initComponent();
        setTriggerAction(TriggerAction.ALL);
    }


    public ToolBar getToolBar()
    {
        return toolBar;
    }

    protected void bindStore(ListStore<D> store, boolean initial)
    {
        if (this.store != null && !initial)
        {
            this.store.removeStoreListener(getStoreListener());
            if (store == null)
            {
                this.store = null;
                if (xListView != null)
                {
                    xListView.setStore(null);
                }
            }
        }
        if (store != null)
        {
            this.store = store;
            if (store.getLoader() == null)
            {
                setMode("local");
            }
            if (xListView != null)
            {
                xListView.setStore(store);
            }
            store.addStoreListener(getStoreListener());
        }
    }

    @Override
    public void collapse()
    {
        if (xListView.isVisible())
            xListView.updateSelectionState();
        super.collapse();
        formatText();
        updateHiddenValue();
    }


    public void formatText()
    {
        selectedItems = getSelection();
        setRawValue(formatRawText(selectedItems));
        removeEmptyText();
        removeAllSelectedText();
        applyEmptyText();
        applyAllSelectedText();
    }


    public String formatRawText(List<D> list)
    {
        if (list == null)
            return "";
        StringBuilder sb = new StringBuilder("");
        for (D d : list)
        {
            if (sb.length() > 0)
            {
                sb.append(rawSeparator);
            }
            sb.append(d.get(getDisplayField()));
        }
        return sb.toString();
    }

    private void createList(boolean remove, LayoutContainer list)
    {
        RootPanel.get().add(list);

        setInitialized(true);

        if (getPagingToolBar() != null)
        {
            setFooter(list.el().createChild("<div class='" + getListStyle() + "-ft'></div>"));
            toolBar.setBorders(false);
            toolBar.render(getFooter().dom);
        }
        if (remove)
        {
            RootPanel.get().remove(list);
        }
    }

    @Override
    protected void doForce()
    {
        return;
    }


    private native BaseEventPreview getEventPreview() /*-{
            return this.@com.extjs.gxt.ui.client.widget.form.ComboBox::eventPreview;
        }-*/;

    private native El getFooter() /*-{
            return this.@com.extjs.gxt.ui.client.widget.form.ComboBox::footer;
        }-*/;

    private native InputElement getHiddenInput() /*-{
            return this.@com.extjs.gxt.ui.client.widget.form.ComboBox::hiddenInput;
        }-*/;

    public String getRawSeparator()
    {
        return rawSeparator;
    }

    @Override
    public List<D> getSelection()
    {
        if (xListView.isRendered())
            return xListView.getChecked();
        else
            return selectedItems;
    }

    private native StoreListener<D> getStoreListener() /*-{
            return this.@com.extjs.gxt.ui.client.widget.form.ComboBox::storeListener;
        }-*/;

    @Override
    public D getValue()
    {
        return null;
    }

    public String getValueFieldSeparator()
    {
        return valueFieldSeparator;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initList()
    {

        ListView<D> listView = xListView;

        String style = getListStyle();

        listView.setStyleAttribute("overflowX", "hidden");
        listView.setStyleName(style + "-inner");
        listView.setStyleAttribute("padding", "0px");
        listView.setSelectOnOver(true);
        listView.setBorders(false);
        listView.setStyleAttribute("backgroundColor", "white");
        listView.setSelectStyle(getSelectedStyle());
        listView.setLoadingText(getLoadingText());

        if (getTemplate() == null)
        {
            listView.setDisplayProperty(getDisplayField());
        }
        else
        {
            listView.setTemplate(getTemplate());
        }

        list = new LayoutContainer()
        {
            @Override
            protected void onRender(Element parent, int index)
            {
                super.onRender(parent, index);
                getEventPreview().getIgnoreList().add(getElement());
                setFooter(this.el().createChild("<div class='" + getListStyle() + "-ft'></div>"));
                toolBar.setBorders(false);
                toolBar.render(getFooter().dom);
            }

            @Override
            protected void doAttachChildren()
            {
                super.doAttachChildren();
                ComponentHelper.doAttach(toolBar);
            }

            @Override
            protected void doDetachChildren()
            {
                super.doDetachChildren();
                ComponentHelper.doDetach(toolBar);
            }

            @Override
            public void show()
            {
                super.show();
                int toolbarWidth = selectAllButton.getWidth() + deSelectAllButton.getWidth() +
                    applyButton.getWidth() + toolBar.el().getPadding("lr");

                int min = 0;
                for (int i = 0; i < xListView.getStore().getCount(); i++)
                {
                    int childWidth = xListView.el().getChild(i).getChild(0).getWidth() +
                        xListView.el().getChild(i).getPadding("lr");
                    if ((childWidth) > min)
                        min = childWidth;
                }
                int width = toolbarWidth > min ? toolbarWidth : min + 2;
                if (list.el().getChild(0).isScrollableY())
                {
                    width += 19;
                }
                setMinListWidth(width);

                setSelection(selectedItems);
            }
        };

        list.setScrollMode(Scroll.NONE);
        list.setShim(true);
        list.setShadow(true);
        list.setBorders(true);
        list.setStyleName(style);
        list.hide();
        list.addStyleName("x-ignore");

        assert store != null : "ComboBox needs a store";

        list.add(listView);

        setList(list);

        if (getPageSize() > 0)
        {
            PagingToolBar pageTb = new PagingToolBar(getPageSize());
            pageTb.bind((PagingLoader)store.getLoader());
            setPagingToolBar(pageTb);
        }

        if (!isLazyRender())
        {
            createList(true, list);
        }

        bindStore(store, true);
        setSelection(selectedItems);
    }

    private ToolBar createSelectAllToolBar()
    {
        if (toolBar != null)
            return toolBar;
        toolBar = new ToolBar();
        toolBar.setSpacing(5);

        applyButton = new Button(BASEVC.apply());
        applyButton.addStyleName(ClarityStyle.BUTTON_PRIMARY);
        applyButton.addStyleName(ClarityStyle.TOOLBAR_BUTTON);
        applyButton.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                fireEvent(Events.SelectionChange, new BaseEvent(this));
            }
        });

        selectAllButton = new Button(BASEVC.selectAll());
        selectAllButton.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                checkAll();
                setSelectAll(true);
                if (!options.isShowApplyBtn())
                    fireEvent(Events.SelectionChange, new BaseEvent(this));
            }
        });

        deSelectAllButton = new Button(BASEVC.deSelectAll());
        deSelectAllButton.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                deCheckAll();
                setSelectAll(false);
                if (!options.isShowApplyBtn())
                    fireEvent(Events.SelectionChange, new BaseEvent(this));
            }
        });

        if (options.isShowApplyBtn())
        {
            toolBar.add(applyButton);
            toolBar.add(new FillToolItem());
            if (options.isShowSelectAllBtn())
            {
                toolBar.add(selectAllButton);
            }
            toolBar.add(deSelectAllButton);
        }
        else
        {
            if (options.isShowSelectAllBtn())
            {
                toolBar.add(selectAllButton);
                toolBar.add(new FillToolItem());
            }
            toolBar.add(deSelectAllButton);
        }
        return toolBar;
    }


    public void checkAll()
    {
        xListView.checkAll();
        selectedItems = xListView.getStoreList();
        setSelectAll(true);
    }

    public void deCheckAll()
    {
        xListView.deCheckAll();
        selectedItems = Collections.emptyList();
    }

    public void setSelectAll(boolean selectAll)
    {
        this.selectAll = selectAll;
        if (selectAll)
            selectedItems = store.getModels();
    }

    private native void setFooter(El footer) /*-{
            this.@com.extjs.gxt.ui.client.widget.form.ComboBox::footer = footer;
        }-*/;

    private native void setInitialized(boolean initialized) /*-{
            this.@com.extjs.gxt.ui.client.widget.form.ComboBox::initialized = initialized;
        }-*/;

    private native void setList(LayoutContainer list)/*-{
            this.@com.extjs.gxt.ui.client.widget.form.ComboBox::list = list;
        }-*/;

    private native void setMode(String mode)/*-{
            this.@com.extjs.gxt.ui.client.widget.form.ComboBox::mode = mode;
        }-*/;

    private native void setPagingToolBar(PagingToolBar pageTb)/*-{
            this.@com.extjs.gxt.ui.client.widget.form.ComboBox::pageTb = pageTb;
        }-*/;

    public void setRawSeparator(String rawSeparator)
    {
        this.rawSeparator = rawSeparator;
    }

    @Override
    public void setSelection(List<D> selection)
    {
        if (selection == null)
            return;
        deCheckAll();
        for (D d : selection)
        {
            xListView.setChecked(d, true);
        }
        selectedItems = selection;
        super.setSelection(selection);
        formatText();
    }

    @Override
    public void setValue(D value)
    {
        removeAllSelectedText();
        applyAllSelectedText();
        return;
    }

    public void setValueFieldSeparator(String valueFieldSeparator)
    {
        this.valueFieldSeparator = valueFieldSeparator;
    }

    private void updateHiddenValue()
    {
        if (getHiddenInput() != null)
        {

            StringBuilder sb = new StringBuilder("");
            for (D d : getSelection())
            {

                if (sb.length() > 0)
                {
                    sb.append(valueFieldSeparator);
                }

                sb.append(d.get(getValueField()));
            }

            getHiddenInput().setValue(sb.toString());
        }
    }

    public boolean isSelectAll()
    {
        return selectAll;
    }

    @Override
    public void expand()
    {
        super.expand();
        if (selectAll)
        {
            checkAll();
        }
    }


    public final class XCheckBoxListView<D extends ModelData> extends CheckBoxListView<D>
    {
        public XCheckBoxListView()
        {
        }

        public void preCheckAll()
        {
            if (checkedPreRender == null)
                checkedPreRender = new ArrayList<D>();
            for (int i = 0; i < store.getCount(); i++)
            {
                checkedPreRender.add(store.getAt(i));
            }
            setSelectAll(true);
        }

        public void preDeCheckAll()
        {
            if (checkedPreRender == null)
                checkedPreRender = new ArrayList<D>();
            for (int i = 0; i < store.getCount(); i++)
            {
                checkedPreRender.remove(store.getAt(i));
            }
        }

        public void deCheckAll()
        {
            if (store == null)
                return;
            for (int i = 0; i < store.getCount(); i++)
            {
                setChecked(store.getAt(i), false);
            }
        }

        public void checkAll()
        {
            if (store == null)
                return;
            for (int i = 0; i < store.getCount(); i++)
            {
                setChecked(store.getAt(i), true);
            }
            setSelectAll(true);
        }

        public List<D> getStoreList()
        {
            if (store == null)
                return Collections.emptyList();
            List<D> storeList = new ArrayList<D>();
            for (int i = 0; i < store.getCount(); i++)
            {
                storeList.add(getStore().getAt(i));
            }
            return storeList;
        }

        public void updateSelectionState()
        {
            if (isAllSelected())
                setSelectAll(true);
            else
                setSelectAll(false);
        }

        private boolean isAllSelected()
        {
            NodeList<Element> nodes = el().select(getCheckBoxSelector());
            if (nodes.getLength() == 0)
                return false;
            for (int i = 0; i < nodes.getLength(); i++)
            {
                InputElement e = nodes.getItem(i).cast();
                if (!e.isChecked())
                {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void setChecked(D d, boolean checked)
        {
            // todo pm: refactor this to a generalized widget instead of hacked one
            if (!options.isUsePhobosSpecialTemplate())
                super.setChecked(d, checked);
            else if (rendered)
            {
                int index = store.indexOf(d);
                Boolean isSelectable = d.get("isSelectable");
                if (isSelectable == null || isSelectable)
                {
                    NodeList<Element> nodes = el().select(".x-phobos-item");
                    InputElement e = nodes.getItem(index).cast();
                    e.setChecked(checked);
                    if (checked)
                        getSelectionModel().select(d, true);
                    else
                        getSelectionModel().deselect(d);
                }
            }
            else
            {
                super.setChecked(d, checked);
            }
        }


        @Override
        public List<D> getChecked()
        {
            // todo pm: refactor this to a generalized widget instead of hacked one
            if (!options.isUsePhobosSpecialTemplate())
                return super.getChecked();

            List<D> checkedList = new ArrayList<D>();
            if (rendered)
            {
                NodeList<Element> nodes = el().select(".x-phobos-item");
                for (int i = 0; i < nodes.getLength(); i++)
                {
                    InputElement e = nodes.getItem(i).cast();
                    if (e.isChecked())
                        checkedList.add(store.getAt(i));
                }
                return checkedList;
            }
            else
                return checkedPreRender != null ? new ArrayList<D>(checkedPreRender) : checkedList;
        }
    }

    @Override
    protected void onTriggerClick(ComponentEvent ce)
    {
        super.onTriggerClick(ce);
        if (listZIndex != -1)
        {
            getListView().el().getParent().setZIndex(listZIndex);
        }
    }

    public void setListZIndex(int listZIndex)
    {
        this.listZIndex = listZIndex;
    }

    public String getAllSelectedText()
    {
        return allSelectedText;
    }

    public void setAllSelectedText(String allSelectedText)
    {
        removeAllSelectedText();
        this.allSelectedText = allSelectedText;
        applyAllSelectedText();
    }

    // Based on TextField.removeEmptyText().
    protected void removeAllSelectedText()
    {
        if (rendered)
        {
            getInputEl().removeStyleName(ClarityStyle.COMBO_ALL_SELECTED);
            if ("".equals(getRawValue()))
            {
                setRawValue("");
            }
        }
    }

    // Based on TextField.applyEmptyText().
    protected void applyAllSelectedText()
    {
        if (rendered && isSelectAll())
        {
            setRawValue(allSelectedText);
            getInputEl().addStyleName(ClarityStyle.COMBO_ALL_SELECTED);
        }
    }

    public void setApplyButtonLabel(String label)
    {
        if (applyButton != null)
        {
            applyButton.setText(label);
        }
    }

    public void setStore(List<D> list)
    {
        store.removeAll();
        store.add(list);
    }
}
