package com.iglooit.core.base.client.widget.combobox;

import com.clarity.commons.iface.domain.OrderBy;
import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.model.SimpleModelValue;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.CommandPagingLoader;
import com.clarity.core.base.client.view.resource.Resource;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a lazy loading dynamic combo box with search functionality
 */
public class SearchComboBox<T extends ModelData> extends ComboBox<T>
{
    protected static final BaseViewConstants BVC = GWT.create(BaseViewConstants.class);
    private CommandPagingLoader<T> pagingLoader;
    private PagingLoader<PagingLoadResult<T>> gridLoader;
    private SearchPaging searchPaging;
    private static final String SEARCH_TRIGGER_STYLE = "x-form-search-trigger";
    private static final int PAGING_TOOLBAR_LENGTH = 350;
    private boolean noResultFlag = false;
    private boolean needNoResultElement = true;
    private boolean enablePagingToolbar = true;
    private final ComboModel noResultModel = new ComboModel(BVC.noResultFoundText());
    private Listener<ComponentEvent> dynamicComboWidthListener;
    private T lastSelection;

    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final String PRESERVED_VALUE = "preserved-value";

    public SearchComboBox()
    {
        super();
        // initiate store
        store = new ListStore<T>();
        setTriggerAction(ComboBox.TriggerAction.QUERY);
        setTriggerStyle(SEARCH_TRIGGER_STYLE);
        this.getImages().setInvalid(Resource.ICONS.exclamationRed());
        //default settings
        setMinChars(2);
        setDisplayField(ComboModel.NAME_PROPERTY);
//        setTemplate("<tpl for=\".\"><div class=x-combo-list-item><span>{Name}</span></div></tpl>");

//        addKeyBoardTriggerListener();
        dynamicComboWidthListener = ClarityComboUtil.enableDynamicSelectionWidthCalculation(this);
        addRefreshEventListener();
    }

    public void addRefreshEventListener()
    {
        getListView().addListener(Events.Refresh, new Listener<ComponentEvent>()
        {
            public void handleEvent(ComponentEvent componentEvent)
            {
                showOrHidePagingToolBar();
                // considering store data change, we need to re-calculate width every time
                LayoutContainer list = (LayoutContainer)getListView().getParent();
                list.fireEvent(Events.Show);
            }
        });

        this.addListener(Events.BeforeQuery, new Listener<FieldEvent>()
        {
            @Override
            public void handleEvent(FieldEvent fe)
            {
                searchPaging = null;
            }
        });
    }

    public T getLastSelection()
    {
        return lastSelection;
    }

    public void setLastSelection(T lastSelection)
    {
        this.lastSelection = lastSelection;
    }

    /**
     * This is created to get field value
     * somehow the flag "initialized" is messed up, can't get the value straight away
     * @return
     */
    public T getValueInField()
    {
        return value;
    }

    public void disableDynamicComboWidthListener()
    {
        if (dynamicComboWidthListener != null)
            ClarityComboUtil.disableDynamicSelectionWidthCalculation(this, dynamicComboWidthListener);
    }

    private void addKeyBoardTriggerListener()
    {
        addKeyListener(new KeyListener()
        {
            public void componentKeyDown(ComponentEvent event)
            {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER)
                {
                    gridLoader.load();
                }
            }
        });
    }

    public void manualLoad()
    {
        if (gridLoader != null)
            gridLoader.load();
    }

    @Override
    public void setStore(ListStore<T> tListStore)
    {
        super.setStore(tListStore);
    }

    /**
     * set the list width by dynamically calculating the maximum text length inside the combo box
     */
    @Override
    protected void onLoad(StoreEvent<T> se)
    {
        addNoResultElementToStore();
        this.repaint();
        super.onLoad(se);
    }

    private void showOrHidePagingToolBar()
    {
        if (getPagingToolBar() == null)
            return;
        if (!enablePagingToolbar)
        {
            getPagingToolBar().hide();
            return;
        }

        getPagingToolBar().show();
        if (getPageSize() >= ((PagingLoader)store.getLoader()).getTotalCount()/*searchPaging.getTotalHits()*/)
        {
            getPagingToolBar().hide();
        }
    }

    public void setSearchPaging(SearchPaging searchPaging)
    {
        this.searchPaging = searchPaging;
    }

    public void setEnablePagingToolbar(boolean enablePagingToolbar)
    {
        this.enablePagingToolbar = enablePagingToolbar;
    }

    private void addNoResultElementToStore()
    {
        if (!needNoResultElement)
            return;

        noResultFlag = false;
        if (store.getModels().size() < 1
            || (store.getModels().size() == 1 && store.getModels().get(0).toString().equals(BVC.noResultFoundText())))
        {
            if (store.getModels().size() < 1)
            {
                List modelList = new ArrayList();
                modelList.add(noResultModel);
                if (store.getModels().size() < 1)
                {
                    store.add(modelList);
                    noResultFlag = true;
                }
            }
            getListView().addStyleName("no-result");
        }
        else
        {
            for (int i = 0; i < store.getModels().size(); i++)
            {
                if (store.getModels().get(i) == noResultModel)
                {
                    store.remove(i);
                    break;
                }
            }
            getListView().removeStyleName("no-result");
        }
    }

    // since our search combo box is using simpleComboValue so this flag is suppose to be always true
    public void setNoResultElementToStore(boolean isSimpleComboValue)
    {
        noResultFlag = true;
        store.removeAll();
        ComboModel newModel = new ComboModel(BVC.noResultFoundText());
        if (isSimpleComboValue)
        {
            newModel.set(SimpleModelValue.VALUE, BVC.noResultFoundText());
            newModel.set(SimpleModelValue.DISPLAY_VALUE, BVC.noResultFoundText());
        }
        List modelList = new ArrayList();
        modelList.add(newModel);
        store.add(modelList);
        getListView().addStyleName("no-result");
    }

    public boolean hasNoElement()
    {
        return noResultFlag;
    }

    @Override
    public void updateOriginalValue(T value)
    {
        super.updateOriginalValue(value);
    }

    //private class

    public static class ComboModel extends BaseModel
    {
        public static final String NAME_PROPERTY = "Name";

        public ComboModel(String name)
        {
            set(NAME_PROPERTY, name);
        }

        @Override
        public String toString()
        {
            return get(NAME_PROPERTY);
        }
    }


    private int getMinWidth(boolean pagingTollBarVisible)
    {
        int minLength = getMinListWidth();
        if (pagingTollBarVisible)
        {
            if (minLength < PAGING_TOOLBAR_LENGTH)
                minLength = PAGING_TOOLBAR_LENGTH;
        }

        /*List<T> listStore = store.getModels();
        for (T t : listStore)
        {
            String value = t.toString();
            int valueLength = value.length();
            if (valueLength * 6 > minLength)
            {
                minLength = valueLength * 6;
            }
        }*/

        return minLength;
    }

    //Set this in the bind method of the presenter

    public void setPagingLoader(CommandPagingLoader<T> pagingLoader)
    {
        this.pagingLoader = pagingLoader;
    }

    public void setStore()
    {
        RpcProxy<PagingLoadResult<T>> rpcProxy = new RpcProxy<PagingLoadResult<T>>()
        {
            public void load(Object o, AsyncCallback<PagingLoadResult<T>> listAsyncCallback)
            {
                if (pagingLoader == null)
                    throw new AppX("Paging loader not set, implementation error");
                if (!(o instanceof PagingLoadConfig))
                    throw new AppX("Object isn't PagingLoadConfig");
                if (searchPaging == null)
                    searchPaging = getSearchPaging((PagingLoadConfig)o);
                else
                    searchPaging.setFirstHitIndex(((PagingLoadConfig)o).getOffset());
                if (searchPaging.getPageSize() == 0)
                    searchPaging.setPageSize(DEFAULT_PAGE_SIZE);
                Option<SearchSorting> searchSortingOption = getSearchSorting((PagingLoadConfig)o);
                pagingLoader.load(searchPaging, searchSortingOption, listAsyncCallback);
            }
        };

        gridLoader = new BasePagingLoader<PagingLoadResult<T>>(rpcProxy);
        ListStore<T> store = new ListStore<T>(gridLoader);
        store.addStoreListener(new StoreListener<T>()
        {
            @Override
            public void storeDataChanged(StoreEvent<T> se)
            {
                super.storeDataChanged(se);
            }
        });
        this.setStore(store);
    }

    private SearchPaging getSearchPaging(PagingLoadConfig searchData)
    {
        SearchPaging searchPaging = new SearchPaging(getPageSize(), searchData.getOffset(), searchData.getLimit(),
            searchData.<String>get("query"));
        searchPaging.setTotalHitsFound(false);
        return searchPaging;
    }

    private Option<SearchSorting> getSearchSorting(PagingLoadConfig searchData)
    {
        SearchSorting setSort = null;
        OrderBy searchDirection = searchData.getSortDir().equals(Style.SortDir.DESC)
            ? OrderBy.DESCENDING : OrderBy.ASCENDING;
        if (searchData.getSortField() != null && searchData.getSortField().length() > 0)
            setSort = new SearchSorting(searchData.getSortField(), searchDirection);
        return Option.option(setSort);
    }

    public void loadingMask()
    {
        this.mask("", ClarityStyle.MASK_FIELD);
    }

    @Override
    protected void onSelect(T model, int index)
    {
        if (model.toString().equals(BVC.noResultFoundText()))
        {
            setRawValue(getInputEl().getValue());
            collapse();
        }
        else
        {
            super.onSelect(model, index);
        }
    }

    @Override
    protected void onTriggerClick(ComponentEvent ce)
    {
        searchPaging = null;
        super.onTriggerClick(ce);
    }

    public void setNeedNoResultElement(boolean needNoResultElement)
    {
        this.needNoResultElement = needNoResultElement;
    }
}
