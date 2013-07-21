package com.iglooit.core.base.client.view;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.lib.client.MetaModelData;
import com.clarity.core.security.client.ClarityRoleHelper;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.ListViewSelectionModel;

import java.util.List;

public abstract class ListStoreFilterView<M extends MetaModelData, T extends Enum<T>> extends StoreFilterView
{
    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    public static final int LIST_ITEM_THRESHOLD = 100;

    private String nameProperty;
    private String descriptionProperty;

    private ListStore<M> backupStore = new ListStore<M>();

    public ListStoreFilterView(String heading, ClarityRoleHelper<T> clarityRoleHelper)
    {
        super(heading, clarityRoleHelper);
    }

    public void setNameProperty(String nameProperty)
    {
        this.nameProperty = nameProperty;
    }

    public void setDescriptionProperty(String descriptionProperty)
    {
        this.descriptionProperty = descriptionProperty;
    }

    public String getTemplate()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<tpl for=\".\">");
        sb.append("<div class='x-view-item list-item-row'");
        sb.append("qtip='");
        sb.append("<b>{");
        sb.append(nameProperty);
        sb.append("}</b>");
        sb.append("<tpl if=\"!hasEmptyDescription\">");
        sb.append("<br/>{");
        sb.append(descriptionProperty);
        sb.append("}");
        sb.append("</tpl>");
        sb.append("'");
        sb.append(">");
        sb.append("<tpl if=\"!hasEmptyName\">");
        sb.append("<div class='name ellipsis'>{");
        sb.append(nameProperty);
        sb.append("}</div>");
        sb.append("</tpl>");
        sb.append("<tpl if=\"hasEmptyName\">");
        sb.append("<div class='name'>");
        sb.append("New Entry");
        sb.append("</div>");
        sb.append("</tpl>");
        sb.append("<tpl if=\"!hasEmptyDescription\">");
        sb.append("<div class='secondary ellipsis'>{");
        sb.append(descriptionProperty);
        sb.append("}</div>");
        sb.append("</tpl>");
        sb.append("<tpl if=\"hasEmptyDescription\">");
        sb.append("<div>&nbsp;</div>");
        sb.append("</tpl>");
        sb.append("</div>");
        sb.append("</tpl>");
        return sb.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void populateData(List data)
    {
        super.populateData(data);
        if (getMasterItems().getStore() == null)
            getMasterItems().setStore(new ListStore<M>());
        getMasterItems().getStore().removeAll();
        getMasterItems().getStore().add(data);
        getMasterItems().getStore().sort(
            getMasterItems().getStore().getSortField(), getMasterItems().getStore().getSortDir());
    }

    public List<M> getBackupModels()
    {
        return backupStore.getModels();
    }

    public ListStore<M> getBackupStore()
    {
        return backupStore;
    }

    public abstract ListView<M> setupMasterViewItems();

    @Override
    public void setViewItemsWidget()
    {
        super.setViewItemsWidget();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setMasterItemsSelectionListener(SelectionChangedListener listener)
    {
        super.setMasterItemsSelectionListener(listener);
        getMasterItems().getSelectionModel().addSelectionChangedListener(listener);
    }

    public void setTemplate()
    {
        getMasterItems().setTemplate(getTemplate());
    }

    public void setTemplate(String template)
    {
        getMasterItems().setTemplate(template);
    }

    public void setDisplayProperty(String displayProperty)
    {
        getMasterItems().setDisplayProperty(displayProperty);
    }

    @Override
    public void setDefaultSort(String field, Style.SortDir sortDir)
    {
        super.setDefaultSort(field, sortDir);
        getMasterItems().getStore().setDefaultSort(field, sortDir);
    }

    @Override
    public void refresh()
    {
        super.refresh();
        getMasterItems().getStore().sort(
            getMasterItems().getStore().getSortField(), getMasterItems().getStore().getSortDir());
        getMasterItems().refresh();
    }

    public void refreshWithoutSort()
    {
        super.refresh();
        getMasterItems().refresh();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeSelectedItem(ModelData unsavedModelData)
    {
        super.removeSelectedItem(unsavedModelData);
        getMasterItems().getStore().remove((M)unsavedModelData);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bindStoreFilter()
    {
        getFilterField().bind(getMasterItems().getStore());
//        getFilterField().bind(backupStore);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setSelectionAndSuppressEvent(List data)
    {
        super.setSelectionAndSuppressEvent(data);
        ((ListStoreFilterViewSelectionModel)(getMasterItems().getSelectionModel())).setSelectionAndSuppressEvent(data);
    }

    @Override
    public void deselectItem()
    {
        super.deselectItem();
        getMasterItems().getSelectionModel().deselectAll();
    }

    @Override
    public void maskItemView()
    {
        super.maskItemView();
        getMasterItems().mask(BVC.loading(), ClarityStyle.MASK_NO_SPINNER);
    }

    @Override
    public void unmaskItemView()
    {
        super.unmaskItemView();
        getMasterItems().unmask();
    }

    @Override
    public void cancelEdit()
    {
        super.cancelEdit();
    }

    @SuppressWarnings("unchecked")
    public List<M> getAllItems()
    {
        return getMasterItems().getStore().getModels();
    }

    public static class ListStoreFilterViewSelectionModel<M extends ModelData> extends ListViewSelectionModel<M>
    {
        public void setSelectionAndSuppressEvent(List<M> selection)
        {
            doSelect(selection, false, true);
        }

        public void deselectAllAndSuppressEvent()
        {
            doDeselect(getSelectedItems(), true);
        }
    }
}
