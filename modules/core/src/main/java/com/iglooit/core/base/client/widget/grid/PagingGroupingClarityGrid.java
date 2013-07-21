package com.iglooit.core.base.client.widget.grid;

import com.clarity.commons.iface.domain.OrderBy;
import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.CommandPagingLoader;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PagingGroupingClarityGrid<MD extends ModelData> extends NonPagingGroupingClarityGrid<MD>
{
    private PagingToolBar toolBar;
    private int pagingPageSize;

    private String checkedStyle = "x-grid3-group-check";
    private String uncheckedStyle = "x-grid3-group-uncheck";
    private CommandPagingLoader<MD> pagingLoader;
    private PagingLoader<PagingLoadResult<MD>> gridLoader;
    private boolean checkColumn;
    private boolean enableClickAnywhereSelect = false;
    private boolean reloadOnAttach = true;

    private boolean preserveSelections = false;
    private ArrayList<MD> preservedSelections;
    private boolean restoringSelections = false;

    public PagingGroupingClarityGrid(final int pagingPageSize)
    {
        this(pagingPageSize, false);
    }

    private El findCheck(Element group)
    {
        return El.fly(group).selectNode(".x-grid3-group-checker").firstChild();
    }

    private void setGroupChecked(Element group, boolean checked)
    {
        findCheck(group).replaceStyleName(checked ? uncheckedStyle : checkedStyle,
            checked ? checkedStyle : uncheckedStyle);
    }

    public boolean isPreserveSelections()
    {
        return preserveSelections;
    }

    public void setPreserveSelections(boolean preserveSelections)
    {
        if (preserveSelections != this.preserveSelections)
        {
            this.preserveSelections = preserveSelections;
            if (!preserveSelections)
                preservedSelections = null;
            else
                preservedSelections = new ArrayList<MD>();
        }
    }

    public PagingGroupingClarityGrid(final int pagingPageSize, boolean checkColumn)
    {
        this.pagingPageSize = pagingPageSize;
        this.checkColumn = checkColumn;
        initGroupStore();
        initGrid();
        getGrid().addListener(Events.Attach, new Listener<GridEvent<MD>>()
        {
            public void handleEvent(GridEvent<MD> be)
            {
                if (reloadOnAttach)
                    reloadGridLoader();
            }
        });

        getGroupStore().getLoader().addListener(Loader.Load, new Listener<BaseEvent>()
        {
            @Override
            public void handleEvent(BaseEvent be)
            {
                if (preserveSelections)
                    restoreSelections();
            }
        });

        if (checkColumn)
        {
            final CheckBoxSelectionModel<MD> sm = new CheckColumnSelectionModel();

            sm.setSelectionMode(Style.SelectionMode.MULTI);

            ArrayList<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
            columnConfigs.add(sm.getColumn());
            columnConfigs.addAll(getCm().getColumns());

            getGrid().reconfigure(getGroupStore(), new ColumnModel(columnConfigs));
            getGrid().addPlugin(sm);
            getGrid().setSelectionModel(sm);
        }
        else
        {
            final GridSelectionModel<MD> gm = new GridSelectionModel<MD>()
            {
                @Override
                protected void onSelectChange(MD model, boolean select)
                {
                    super.onSelectChange(model, select);
                    if (preserveSelections)
                        preserveSelection(model, select);
                }
            };
            getGrid().setSelectionModel(gm);
        }

        getContentPanel().setBottomComponent(toolBar);
    }

    public boolean isEnableClickAnywhereSelect()
    {
        return enableClickAnywhereSelect;
    }

    /**
     * Enables additive selection of items by clicking anywhere in a row. By default, only clicking on the checkbox
     * would add to the current selection, this allows the user to more easily add to the current selection. Note: this
     * is only available when the check column flag is true and selection mode is set to multi.
     * @param enableClickAnywhereSelect
     */
    public void setEnableClickAnywhereSelect(boolean enableClickAnywhereSelect)
    {
        this.enableClickAnywhereSelect = enableClickAnywhereSelect;
    }

    public boolean isReloadOnAttach()
    {
        return reloadOnAttach;
    }

    public void setReloadOnAttach(boolean reloadOnAttach)
    {
        this.reloadOnAttach = reloadOnAttach;
    }

    public void forgetSelections()
    {
        boolean requiresChange = preservedSelections != null && preservedSelections.size() > 0;
        // trick any listeners to receive a selection changed event
        if (requiresChange)
        {
            preservedSelections.clear();
            GridSelectionModel<MD> sm = getGrid().getSelectionModel();
            if (sm.getSelectedItems().size() > 0)
                sm.deselectAll();
            else
                sm.fireEvent(Events.SelectionChange,
                    new SelectionChangedEvent<MD>(getGrid().getSelectionModel(), preservedSelections));
        }
    }

    private void restoreSelections()
    {
        restoringSelections = true;
        if (preservedSelections != null)
            getGrid().getSelectionModel().select(preservedSelections, false);
        restoringSelections = false;
    }

    private void preserveSelection(MD item, boolean selected)
    {
        if (restoringSelections)
            return;
        if (selected)
            preservedSelections.add(item);
        else
            preservedSelections.remove(item);
    }

    public List<MD> getSelections()
    {
        if (preserveSelections)
            return preservedSelections;
        else
            return getGrid().getSelectionModel().getSelectedItems();
    }

    @Override
    protected void initGroupStore()
    {
        RpcProxy<PagingLoadResult<MD>> rpcProxy = new RpcProxy<PagingLoadResult<MD>>()
        {
            public void load(Object o, AsyncCallback<PagingLoadResult<MD>> listAsyncCallback)
            {
                if (pagingLoader == null)
                    throw new AppX("Paging loader not set, implementation error");
                if (!(o instanceof PagingLoadConfig))
                    throw new AppX("Object isn't PagingLoadConfig");
                SearchPaging searchPaging = getSearchPaging((PagingLoadConfig)o);
                Option<SearchSorting> searchSortingOption = getSearchSorting((PagingLoadConfig)o);
                pagingLoader.load(searchPaging, searchSortingOption, listAsyncCallback);
            }
        };

        gridLoader = new BasePagingLoader<PagingLoadResult<MD>>(rpcProxy);
        gridLoader.setRemoteSort(true);
        gridLoader.setOffset(0);
        gridLoader.setLimit(pagingPageSize);
        toolBar = new PagingToolBar(pagingPageSize);
        toolBar.addStyleName(ClarityStyle.TOOLBAR_BASIC);
        toolBar.bind(gridLoader);
        toolBar.setEnabled(true);

        //store
        GroupingStore<MD> groupStore = new GroupingStore<MD>(gridLoader);

        if (StringUtil.isNotEmpty(getGroupByField()))
            groupStore.groupBy(getGroupByField());
        else
            groupStore.clearGrouping();

        setGroupStore(groupStore);
    }

    public void setPagingBarEnable()
    {
        toolBar.setEnabled(true);
    }

    public void reloadGridLoader()
    {
        PagingLoadConfig config = new BasePagingLoadConfig();
        config.setOffset(0);
        config.setLimit(pagingPageSize);

        Map<String, Object> state = getGrid().getState();
        if (state.containsKey("offset"))
        {
            int offset = (Integer)state.get("offset");
            int limit = (Integer)state.get("limit");
            config.setOffset(offset);
            config.setLimit(limit);
        }
        if (state.containsKey("sortField"))
        {
            config.setSortField((String)state.get("sortField"));
            config.setSortDir(Style.SortDir.valueOf((String)state.get("sortDir")));
        }
        gridLoader.load(config);
    }


    public void setPagingLoader(CommandPagingLoader<MD> pagingLoader)
    {
        this.pagingLoader = pagingLoader;
    }

    public SearchPaging getSearchPaging(PagingLoadConfig searchData)
    {
        return new SearchPaging(pagingPageSize, searchData.getOffset());
    }

    public Option<SearchSorting> getSearchSorting(PagingLoadConfig searchData)
    {
        SearchSorting setSort = null;
        OrderBy searchDirection = searchData.getSortDir().equals(Style.SortDir.DESC)
            ? OrderBy.DESCENDING : OrderBy.ASCENDING;
        if (searchData.getSortField() != null && searchData.getSortField().length() > 0)
            setSort = new SearchSorting(searchData.getSortField(), searchDirection);
        else
        {
            String sortField = null;
            for (int column = 0; column < getGrid().getColumnModel().getColumnCount(); column++)
            {
                String columnId = getGrid().getColumnModel().getColumnId(column);
                if (columnId.contains("clarity"))
                {
                    sortField = columnId;
                    break;
                }
            }
            if (sortField != null)
                setSort = new SearchSorting(sortField, searchDirection);
        }
        return Option.option(setSort);
    }

    @Override
    protected GroupingView initGroupingView()
    {
        GroupingView view = new PagingGroupingView();
        view.setShowGroupedColumn(false);
        view.setForceFit(true);
        view.setGroupRenderer(getGridGroupRenderer());

        return view;
    }

    public GridGroupRenderer getGridGroupRenderer()
    {
        return new GridGroupRenderer()
        {
            public String render(GroupColumnData data)
            {
                String f = getCm().getColumnById(data.field).getHeader();
                String l = data.models.size() == 1 ? "Item" : "Items";
                return (checkColumn
                    ? "<div class='x-grid3-group-checker'><div class='" + uncheckedStyle + "'> </div></div>&nbsp;"
                    : "") +
                    f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
            }
        };
    }

    public static <M> PagingLoadResult<M> getPagingLoadResult(final List<M> data, final SearchPaging searchPaging)
    {
        return new PagingLoadResult<M>()
        {
            public int getOffset()
            {
                return searchPaging.getFirstHitIndex();
            }

            public int getTotalLength()
            {
                return (int)searchPaging.getTotalHits();
            }

            public void setOffset(int i)
            {
                throw new AppX("Cannot set offset for paging result");
            }

            public void setTotalLength(int i)
            {
                throw new AppX("Cannot set total length for paging result");
            }

            public List<M> getData()
            {
                return data;
            }
        };
    }

    public void refreshGrid()
    {
        getGrid().getView().refresh(false);
    }

    private final class CheckColumnSelectionModel extends CheckBoxSelectionModel<MD>
    {
        @Override
        public void deselectAll()
        {
            super.deselectAll();
            NodeList<com.google.gwt.dom.client.Element> groups = groupingView.getGroups();
            for (int i = 0; i < groups.getLength(); i++)
            {
                com.google.gwt.dom.client.Element group = groups.getItem(i).getFirstChildElement();
                setGroupChecked((Element)group, false);
            }
        }

        @Override
        public void selectAll()
        {
            super.selectAll();
            NodeList<com.google.gwt.dom.client.Element> groups = groupingView.getGroups();

            search:
            for (int i = 0; i < groups.getLength(); i++)
            {
                com.google.gwt.dom.client.Element group = groups.getItem(i);
                NodeList<Element> rows = El.fly(group).select(".x-grid3-row");
                for (int j = 0, len = rows.getLength(); j < len; j++)
                {
                    Element r = rows.getItem(j);
                    int idx = grid.getView().findRowIndex(r);
                    MD m = grid.getStore().getAt(idx);
                    if (!isSelected(m))
                    {
                        setGroupChecked((Element)group, false);
                        continue search;
                    }
                }
                setGroupChecked((Element)group, true);
            }
        }

        @Override
        protected void doDeselect(List<MD> models, boolean supressEvent)
        {
            super.doDeselect(models, supressEvent);
            NodeList<com.google.gwt.dom.client.Element> groups = groupingView.getGroups();
            search:
            for (int i = 0; i < groups.getLength(); i++)
            {
                com.google.gwt.dom.client.Element group = groups.getItem(i);
                NodeList<Element> rows = El.fly(group).select(".x-grid3-row");
                for (int j = 0, len = rows.getLength(); j < len; j++)
                {
                    Element r = rows.getItem(j);
                    int idx = grid.getView().findRowIndex(r);
                    MD m = grid.getStore().getAt(idx);
                    if (!isSelected(m))
                    {
                        setGroupChecked((Element)group, false);
                        continue search;
                    }
                }
            }
        }

        @Override
        protected void doSelect(List<MD> models, boolean keepExisting, boolean supressEvent)
        {
            super.doSelect(models, keepExisting, supressEvent);

            if (models.size() == 0)
                return;
            NodeList<com.google.gwt.dom.client.Element> groups = groupingView.getGroups();
            search:
            for (int i = 0; i < groups.getLength(); i++)
            {
                com.google.gwt.dom.client.Element group = groups.getItem(i);
                NodeList<Element> rows = El.fly(group).select(".x-grid3-row");
                for (int j = 0, len = rows.getLength(); j < len; j++)
                {
                    Element r = rows.getItem(j);
                    int idx = grid.getView().findRowIndex(r);
                    MD m = grid.getStore().getAt(idx);
                    if (!isSelected(m))
                    {
                        setGroupChecked((Element)group, false);
                        continue search;
                    }
                }
                setGroupChecked((Element)group, true);
            }
        }

        @Override
        protected void handleMouseDown(GridEvent<MD> e)
        {
            if ((e.getEvent().getButton() == Event.BUTTON_LEFT
                && e.getTarget().getClassName().equals("x-grid3-row-checker"))
                || enableClickAnywhereSelect)
            {
                MD m = listStore.getAt(e.getRowIndex());
                if (m != null)
                {
                    if (isSelected(m))
                        deselect(m);
                    else
                        select(m, true);
                }
            }
            else
                super.handleMouseDown(e);
        }

        @Override
        protected void handleMouseClick(GridEvent<MD> e)
        {
            if (e.getTarget().getClassName().equals("x-grid3-row-checker") || enableClickAnywhereSelect)
                return;
            super.handleMouseClick(e);
        }

        @Override
        protected void onSelectChange(MD model, boolean select)
        {
            super.onSelectChange(model, select);
            if (preserveSelections)
                preserveSelection(model, select);
        }
    }
}
