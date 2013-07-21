package com.iglooit.core.base.client.view;

import com.clarity.core.lib.client.MetaModelData;
import com.clarity.core.security.client.ClarityRoleHelper;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.gwt.dom.client.Element;

public class PlainListStoreFilterView<M extends MetaModelData, T extends Enum<T>> extends ListStoreFilterView
{
    private ListView<M> listView;

    public PlainListStoreFilterView(String heading, ClarityRoleHelper<T> clarityRoleHelper)
    {
        super(heading, clarityRoleHelper);
    }

    public ListView<M> setupMasterViewItems()
    {
        ListView<M> masterItems = new ListView<M>();
        masterItems.setSelectionModel(new ListStoreFilterViewSelectionModel<M>());
        masterItems.setStore(new ListStore<M>());
        masterItems.setBorders(false);

        return masterItems;
    }

    @Override
    public void setViewItemsWidget()
    {
        ListView<M> masterViewItems = setupMasterViewItems();
        super.setViewItemsWidget(setupMasterViewItems());
        this.listView = (ListView)masterViewItems;
        add(masterViewItems);
        layout(true);
    }

    @Override
    public ListView getMasterItems()
    {
        return listView;
    }

    @Override
    public void cancelEdit()
    {
        super.cancelEdit();
        ModelData selectedItem = listView.getSelectionModel()
            .getSelectedItem();
        ListStore listStore = listView.getStore();

        if (selectedItem != null)
        {
            listView.getElement(listStore.indexOf(selectedItem))
                .removeClassName("x-view-item-sel");
            listView.getElement(listStore.indexOf(selectedItem))
                .removeClassName("x-view-highlightrow");
        }
        else
        {
            for (Element element : listView.getElements())
            {
                element.removeClassName("x-view-highlightrow");
            }
        }
        listView.getSelectionModel().deselectAll();
    }
}
