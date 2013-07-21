package com.iglooit.core.base.client.widget.grid;

import com.clarity.commons.iface.domain.OrderBy;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.lib.client.MetaModelData;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;

import java.util.List;

public abstract class ActivityListGridDisplay<T extends Meta> extends FilteredSearchResultListDisplay<MetaModelData<T>>
    implements Display
{
    private ClarityFilterLocalPagingGrid<MetaModelData<T>> activityGrid;

    /* TODO: Work in progress
    Idea is to change the page size according to window height. Since most of the activitylistGridDisplay
    occupies about 70% of height & calculated on a rough approximation 1200 px height activitylistGridDisplay can
    contain about 30 items. The correct size is calculated as 30 * (current window height) / 1200
    public static final int PAGE_SIZE =  Window.getClientHeight()  * 27 /900;
    */

    public static final int PAGE_SIZE = 20;

    private Button refreshButton;
    private String label;

    public ActivityListGridDisplay(int pageSize, List<ColumnConfig> columnConfig, String title)
    {
        super(pageSize, true, columnConfig, title, "Filter By: ");
        initActivity(title);
    }

    public ActivityListGridDisplay(List<ColumnConfig> columnConfig, String title)
    {
        super(PAGE_SIZE, true, columnConfig, title, "Filter By: ");
        initActivity(title);
    }

    private void initActivity(String title)
    {
        setSearchFieldEmptyText(getSearchFieldLabelEmptyText());
        //getContentPanel().addStyleName("inboxpoc-activity-list-panel");
        getContentPanel().setHeaderVisible(false);
        getGrid().setAutoExpandColumn(getAutoExpandColumn());
        getGrid().setHeight(200);

        // below block is required to enable horizontal baron grid when required space is not sufficient
        getGrid().getView().setAdjustForHScroll(true);
        getGrid().setAutoWidth(false);
        getGrid().getView().setForceFit(false);
        getGrid().setAutoExpandMin(40);
        getGrid().setMinColumnWidth(40);

        label = title;
        setDefaultStyle();

        refreshButton = new Button("");
        refreshButton.setIcon(Resource.ICONS_SIMPLE.reload());
        getSearchBar().add(new FillToolItem());
        getSearchBar().add(refreshButton);

        setRemoteSorting(false);
    }

    protected String getSearchFieldLabelEmptyText()
    {
        return "Empty";
    }

    private void setDefaultStyle()
    {
        getGrid().setBorders(false);
        getContentPanel().setBorders(true);
        getContentPanel().setBodyBorder(false);
        getContentPanel().setFrame(false);
    }

    protected abstract String getAutoExpandColumn();

    public void addGridSelectionListener(SelectionChangedListener<MetaModelData<T>> listener)
    {
        getGrid().getSelectionModel().addSelectionChangedListener(listener);
    }

    @Override
    public String getLabel()
    {
        return label;
    }

    @Override
    public Option<SearchSorting> getSearchSorting(PagingLoadConfig searchData)
    {
        SearchSorting setSort = null;
        OrderBy searchDirection = searchData.getSortDir().equals(Style.SortDir.DESC)
            ? OrderBy.DESCENDING : OrderBy.ASCENDING;
        if (searchData.getSortField() != null && searchData.getSortField().length() > 0)
            setSort = new SearchSorting(searchData.getSortField(), searchDirection);
        else
        {
            return Option.none();
        }
        return Option.option(setSort);
    }
}
