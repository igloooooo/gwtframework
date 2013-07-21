package com.iglooit.core.base.client.view;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.lib.client.MetaModelData;
import com.clarity.core.security.client.ClarityRoleHelper;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public abstract class StoreFilterView<M extends MetaModelData, T extends Enum<T>>
        extends ContentPanel implements Display
{

    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private Widget masterViewItems;
    private Button addNewItemButton;
    private Button refreshButton;
    private Button cogButton;
    private Button loadMoreButton;

    private LayoutContainer filterFieldWrapper;
    private StoreFilterField<M> filterField;
    private String filterFieldName;

    private String filterEmptyText;

    private boolean showTotalItemsOnHead;

    private String heading;

    public void setFilterEmptyText(String filterEmptyText)
    {
        this.filterEmptyText = filterEmptyText;
    }

    public String getFilterEmptyText()
    {
        return filterEmptyText;
    }

    public void setFilterField(StoreFilterField<M> filterField)
    {
        this.filterField = filterField;
        addFilterField();
    }

    public String getFilterFieldName()
    {
        return filterFieldName;
    }

    public static final String EMPTY = "";

    public StoreFilterView(String heading, ClarityRoleHelper<T> clarityRoleHelper)
    {
        super(new FitLayout());
        this.heading = heading;
        setHeading(heading);
        addStyleName(ClarityStyle.PANEL_STYLE_3);
        getHeader().setStyleAttribute("line-height", "22px"); // this vertically aligns the header title correctly

        addNewItemButton = new Button(BVC.newButtonTxt(), Resource.ICONS_SIMPLE.plus());
        addNewItemButton.setStyleAttribute("margin-right", "5px");
        if (clarityRoleHelper.isCurrentUserMostPrivileged())
        {
            getHeader().addTool(addNewItemButton);
        }
        refreshButton = new Button(EMPTY, Resource.ICONS_SIMPLE.reload());
        getHeader().addTool(refreshButton);

        loadMoreButton = new Button(BVC.loadMore());
        getHeader().addTool(loadMoreButton);
        loadMoreButton.hide();

        cogButton = new Button(EMPTY, Resource.ICONS_SIMPLE.gear());
        getHeader().addTool(cogButton);
        hideCogButton();

        setBodyBorder(true);
    }

    public void hideAddNewItemButton()
    {
        addNewItemButton.hide();
    }

    public void hideRefreshButton()
    {
        refreshButton.hide();
    }

    public void hideLoadMoreButton()
    {
        loadMoreButton.hide();
    }

    public void hideCogButton()
    {
        cogButton.hide();
    }

    public void hideFilterField()
    {
        filterFieldWrapper.hide();
    }

    public void showFilterField()
    {
        filterFieldWrapper.show();
        layout(true);
    }

    public void setFilterFieldName(String filterFieldName)
    {
        this.filterFieldName = filterFieldName;
    }

    public void setupFilterField()
    {
        filterField = new StoreFilterField<M>()
        {
            @Override
            protected boolean doSelect(Store<M> store, M parent, M record, String property, String filter)
            {
                if (StringUtil.isEmpty(filter))
                    return true;
                return record.getMeta().get(filterFieldName).toString().toUpperCase().contains(filter.toUpperCase());
            }
        };
        addFilterField();
    }

    public void addFilterField()
    {
        filterField.setTriggerStyle("x-form-clear-trigger");

        filterFieldWrapper = new LayoutContainer(new FitLayout());
        filterFieldWrapper.add(filterField);
        filterFieldWrapper.setBorders(true);
        filterFieldWrapper.addStyleName(ClarityStyle.PADDING_5);
        filterFieldWrapper.addStyleName(ClarityStyle.PANEL_HIGHLIGHT_BASIC);
        filterFieldWrapper.addStyleName(ClarityStyle.NO_BORDER_TOP_TRANSPARENT);

        setTopComponent(filterFieldWrapper);
        layout(true);
    }

    public Widget getMasterViewItems()
    {
        return masterViewItems;
    }

    public StoreFilterField<M> getFilterField()
    {
        return filterField;
    }

    public void setViewItemsWidget(Widget widget)
    {
        this.masterViewItems = widget;
    }

    public void setViewItemsWidget()
    {
    }

    public void populateData(List<M> data)
    {
        if (showTotalItemsOnHead)
            this.setHeading(heading + "<span class='secondary'> (" + data.size() + ")</span>");
        else
            this.setHeading(heading);
    }

    public void setMasterItemsSelectionListener(SelectionChangedListener<M> listener)
    {
    }

    public void setDefaultSort(String field, Style.SortDir sortDir)
    {
    }

    public void addCogButtonClickListener(SelectionListener<ButtonEvent> listener)
    {
        cogButton.addSelectionListener(listener);
    }

    public void addNewItemButtonClickListener(SelectionListener<ButtonEvent> listener)
    {
        addNewItemButton.addSelectionListener(listener);
    }

    public void addRefreshButtonClickListener(SelectionListener<ButtonEvent> listener)
    {
        refreshButton.addSelectionListener(listener);
    }

    @Override
    public String getLabel()
    {
        return "";
    }

    //todo ch: make this look nicer
    public ListView getMasterItems()
    {
        return null;
    }

    public void cancelEdit()
    {
    }

    public void removeSelectedItem(ModelData unsavedModelData)
    {
    }

    public void refresh()
    {
    }

    public void disableAddButton()
    {
        addNewItemButton.setEnabled(false);
    }

    public void enableAddButton()
    {
        addNewItemButton.setEnabled(true);
    }

    public void bindStoreFilter()
    {
    }

    public void setSelectionAndSuppressEvent(List<M> data)
    {
    }

    public void setSelectionAndSuppressEvent(M data)
    {
    }

    public void deselectItem()
    {
    }

    public void deselectAllAndSuppressEvent()
    {
    }

    public void maskItemView()
    {
    }

    public void unmaskItemView()
    {
    }

    public void clearFilter()
    {
        filterField.clear();
    }

    public void setShowTotalItemsOnHead(boolean showTotalItemsOnHead)
    {
        this.showTotalItemsOnHead = showTotalItemsOnHead;
    }

    public void showCogButton()
    {
        cogButton.show();
    }

    public Button getCogButton()
    {
        return cogButton;
    }

    public Button getLoadMoreButton()
    {
        return loadMoreButton;
    }
}
