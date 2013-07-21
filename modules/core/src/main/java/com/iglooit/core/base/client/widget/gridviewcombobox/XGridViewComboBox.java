package com.iglooit.core.base.client.widget.gridviewcombobox;

import com.clarity.core.base.client.widget.grid.ClarityGrid;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ListModelPropertyEditor;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.List;

public abstract class XGridViewComboBox<D extends ModelData> extends ComboBox<D>
{
    private String valueFieldSeparator = ";";
    private String rawSeparator = ", ";
    private ToolBar toolBar;
    private ClarityGrid<D> grid;
    private ContentPanel list;

    public XGridViewComboBox()
    {
        grid = getGrid();
        toolBar = new ToolBar();
        toolBar.add(new Button("Select All"));
        toolBar.add(new Button("Unselect All"));
        setView(new CheckBoxListView<D>());
        messages = new ComboBoxMessages();
        setPropertyEditor(new ListModelPropertyEditor<D>());
        monitorWindowResize = true;
        windowResizeDelay = 0;
        initComponent();
        setTriggerAction(TriggerAction.ALL);
    }

    protected void bindStore(ListStore<D> store, boolean initial)
    {
        if (this.store != null && !initial)
        {
            this.store.removeStoreListener(getStoreListener());
            if (store == null)
            {
                this.store = null;
                if (getView() != null)
                {
                    getView().setStore(null);
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
            if (grid != null)
            {
                grid.setStore(store);
            }
            store.addStoreListener(getStoreListener());
        }
    }

    @Override
    public void collapse()
    {
        super.collapse();
        setRawValue(formatRawText(getSelection()));
        updateHiddenValue();
    }

    public String formatRawText(List<D> list)
    {
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
            getPagingToolBar().setBorders(false);
            getPagingToolBar().render(getFooter().dom);
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
        return grid.getGrid().getSelectionModel().getSelection();
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


    public void expand()
    {
        setExpandedState(true);
        if (!getInitialized())
        {
            createList(false, list);
        }
        else
        {
            RootPanel.get().add(list);
        }

        list.show();
        list.layout(true);
        list.el().updateZIndex(0);
        restrict();
        getEventPreview().add();
        fireEvent(Events.Expand, new FieldEvent(this));
    }

    protected void restrict()
    {
        list.el().setVisibility(false);
        grid.getGrid().getView().setAdjustForHScroll(true);
        list.setHeight(200);
        int w = Math.max(getWidth(), getMinListWidth());

        int fh = getFooter() != null ? getFooter().getHeight() : 0;
        int fw = list.el().getFrameWidth("tb") + fh;

        int h = grid.getGrid().getHeight() + fw;

        h = Math.min(h, getMaxHeight() - fw);
        list.setSize(w, 250);
        list.el().makePositionable(true);
        list.el().alignTo(getElement(), getListAlign(), null);

        h -= fh;

        int width = w - list.el().getFrameWidth("lr");
        grid.getGrid().syncSize();
//        grid.getGrid().setSize(width, h - list.el().getFrameWidth("tb"));
        grid.getGrid().setSize(width, 200);

        if (getPagingToolBar() != null)
        {
            getPagingToolBar().setWidth(width);
        }

        int y = list.el().getY();
        int b = y + h;
        int vh = XDOM.getViewportSize().height + XDOM.getBodyScrollTop();
        if (b > vh)
        {
            y = y - (b - vh) - 5;
            list.el().setTop(y);
        }
        list.el().setVisibility(true);
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void initList()
    {

        String style = getListStyle();


        //setAssetHeight(0);

        list = new ContentPanel()
        {
            @Override
            protected void onRender(Element parent, int index)
            {
                super.onRender(parent, index);
                getEventPreview().getIgnoreList().add(getElement());
            }
        };
        list.setScrollMode(Style.Scroll.AUTOY);
        list.setLayout(new FitLayout());
        list.setHeaderVisible(false);
        list.setShim(true);
        list.setShadow(true);
        list.setBorders(true);
        list.setStyleName(style);
        list.setBottomComponent(toolBar);
        list.hide();
        store = grid.getStore();
        assert store != null : "ComboBox needs a store";

        list.add(grid.asWidget());

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
    }

    public abstract ClarityGrid<D> getGrid();

    private native void setFooter(El footer) /*-{
            this.@com.extjs.gxt.ui.client.widget.form.ComboBox::footer = footer;
        }-*/;

    private native void setExpandedState(boolean expanded) /*-{
            this.@com.extjs.gxt.ui.client.widget.form.ComboBox::expanded = expanded;
        }-*/;

    private native void setInitialized(boolean initialized) /*-{
            this.@com.extjs.gxt.ui.client.widget.form.ComboBox::initialized = initialized;
        }-*/;

    private native boolean getInitialized() /*-{
            return this.@com.extjs.gxt.ui.client.widget.form.ComboBox::initialized;
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

        grid.getGrid().getSelectionModel().setSelection(selection);
        super.setSelection(selection);
    }

    @Override
    public void setValue(D value)
    {
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
}
