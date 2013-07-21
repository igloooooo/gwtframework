package com.iglooit.core.base.client.widget;

import com.clarity.commons.iface.type.AppX;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A grid selection model and component plugin for grids with grouping. To use, add the column config to the column
 * model using {@link #getColumn()} and set the grid view to {@link @getGroupingView()}. You must add this object to the
 * grid's plugin. When the group checkbox is clicked, only the rows in the grid view will be selected/deselected which
 * means if the group spans more than one page, only the rows visible in the page will be selected/deselected. The
 * showCheckBox parameter in the constructor is the flag to check if the checkbox is shown in the grid and group
 * header..
 * <p/>
 * <p/>
 * The following flags can be set to change the behavior/look of the grid enableClickAnywhereSelect : set to true if you
 * want the checkbox to be checked/unchecked when the mouse is clicked anywhere in the row showSelectedCount        :
 * set to true if you want to see the number of selected items in the grid header preserveSelections       : set to true
 * if you want to preserve the selections for a paged grid. By default this is set to true if the grid loader is an
 * instance of PagingLoader and false if it's not. showCheckAll             : set to true to show the select all
 * checkbox in the grid header. The current behavior of Select All checkbox will only select all the records in the page
 * and NOT the whole data set for paged grids showGroupCount           : set to true to show the number of records in a
 * group showGroupCountTotal      : set to true to show the number of records shown in the page and the actual total.
 * This info will only be shown if the groupCountMap<Group Id/Name, total count> has been provided.
 *
 * @param <MD> the model data type
 */
public class GroupColumnSelectionModel<MD extends ModelData> extends CheckBoxSelectionModel<MD>
{
    private String checkedStyle = "x-grid3-group-check";
    private String uncheckedStyle = "x-grid3-group-uncheck";
    private String hideSelectAllCheckStyle = "hide-select-all-check";

    private boolean enableClickAnywhereSelect = true;
    private boolean showCheckBox = false;
    private boolean showSelectedCount = false;
    private boolean preserveSelections = false;
    private boolean showCheckAll = false;
    private boolean showGroupCount = false;
    private boolean showGroupCountTotal = false;
    private List<MD> preservedSelections = new ArrayList<MD>();
    private Map<Object, Integer> groupCountMap;
    private boolean restoringSelections = false;

    private Grid<MD> grid;
    private GroupingStore<MD> groupStore;

    public GroupColumnSelectionModel(boolean showCheckBox)
    {
        this.showCheckBox = showCheckBox;
    }

    public List<MD> getPreservedSelections()
    {
        return preservedSelections;
    }

    public void setPreservedSelections(List<MD> preservedSelections)
    {
        this.preservedSelections = preservedSelections;
    }

    @Override
    public void init(Component component)
    {
        super.init(component);
        this.grid = (Grid<MD>)component;

        if (!(grid.getStore() instanceof GroupingStore))
        {
            throw new AppX("This plugin can only be used by grids with grouping");
        }
        GridView view = grid.getView();
        if (!(view instanceof CustomGroupingView))
        {
            throw new AppX("This plugin requires the grid must use CustomGroupingView");
        }
        initGroupStore();
        initGroupColumnHeader();
    }

    private void initGroupStore()
    {
        groupStore = (GroupingStore)grid.getStore();
        preserveSelections = groupStore.getLoader() != null && groupStore.getLoader() instanceof PagingLoader;
        if (groupStore.getLoader() != null && preserveSelections)
        {
            groupStore.getLoader().addListener(Loader.Load, new Listener<BaseEvent>()
            {
                @Override
                public void handleEvent(BaseEvent be)
                {
                    restoreSelections();
                }
            });
        }
    }

    private void initGroupColumnHeader()
    {
        ColumnConfig c = super.getColumn();
        if (showCheckBox && !showCheckAll)
            c.setColumnStyleName(hideSelectAllCheckStyle);
    }

    public GroupingView getGroupingView()
    {
        GroupingView view = new CustomGroupingView();
        view.setShowGroupedColumn(false);
        view.setStartCollapsed(true);
        view.setForceFit(true);
        return view;
    }

    private void restoreSelections()
    {
        restoringSelections = true;
        if (preservedSelections != null)
            grid.getSelectionModel().select(preservedSelections, false);
        restoringSelections = false;
    }

    private void preserveSelection(MD item, boolean selected)
    {
        if (restoringSelections)
            return;
        if (selected)
        {
            if (!preservedSelections.contains(item))
                preservedSelections.add(item);
        }
        else
        {
            preservedSelections.remove(item);
        }
    }

    public List<MD> getAllPreservedItems()
    {
        if (preserveSelections)
            return preservedSelections;
        else
            return super.getSelectedItems();
    }

    public void clearAllPreservedItems()
    {
        boolean requiresChange = preservedSelections != null && preservedSelections.size() > 0;
        // trick any listeners to receive a selection changed event
        if (requiresChange)
        {
            preservedSelections.clear();
            if (getSelectedItems().size() > 0)
                deselectAll();
            else
                fireEvent(Events.SelectionChange,
                    new SelectionChangedEvent<MD>(this, preservedSelections));
        }
    }

    private El findCheck(Element group)
    {
        return El.fly(group).selectNode(".x-grid3-group-checker").firstChild();
    }

    private void setGroupChecked(Element group, boolean checked)
    {
        if (showCheckBox)
        {
            findCheck(group).replaceStyleName(checked ? uncheckedStyle : checkedStyle,
                checked ? checkedStyle : uncheckedStyle);
        }
    }

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
        for (int i = 0; i < groups.getLength(); i++)
        {
            com.google.gwt.dom.client.Element group = groups.getItem(i).getFirstChildElement();
            setGroupChecked((Element)group, true);
        }
    }

    @Override
    protected void doDeselect(List<MD> models, boolean supressEvent)
    {
        super.doDeselect(models, supressEvent);
        if (showCheckBox)
        {
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

        if (showSelectedCount)
        {
            ((CustomGroupingView)grid.getView()).updateSelectedCount();
        }
    }

    @Override
    protected void doSelect(List<MD> models, boolean keepExisting, boolean supressEvent)
    {
        super.doSelect(models, keepExisting, supressEvent);
        if (showCheckBox)
        {
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

        if (showSelectedCount)
        {
            ((CustomGroupingView)grid.getView()).updateSelectedCount();
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

    public Map<Object, Integer> getGroupCountMap()
    {
        return groupCountMap;
    }

    public void setGroupCountMap(Map<Object, Integer> groupCountMap)
    {
        this.groupCountMap = groupCountMap;
        showGroupCountTotal = groupCountMap != null && !groupCountMap.isEmpty();
    }

    public boolean isEnableClickAnywhereSelect()
    {
        return enableClickAnywhereSelect;
    }

    /**
     * Enables additive selection of items by clicking anywhere in a row. By default, only clicking on the checkbox
     * would add to the current selection, this allows the user to more easily add to the current selection. Note: this
     * is only available when the check column flag is true and selection mode is set to multi.
     */
    public void setEnableClickAnywhereSelect(boolean enableClickAnywhereSelect)
    {
        this.enableClickAnywhereSelect = enableClickAnywhereSelect;
    }

    public boolean isPreserveSelections()
    {
        return preserveSelections;
    }

    public void setPreserveSelections(boolean preserveSelections)
    {
        this.preserveSelections = preserveSelections;
    }

    public boolean isShowCheckAll()
    {
        return showCheckAll;
    }

    public void setShowCheckAll(boolean showCheckAll)
    {
        this.showCheckAll = showCheckAll;
    }

    public boolean isShowSelectedCount()
    {
        return showSelectedCount;
    }

    public void setShowSelectedCount(boolean showSelectedCount)
    {
        this.showSelectedCount = showSelectedCount;
    }

    public boolean isShowGroupCount()
    {
        return showGroupCount;
    }

    public void setShowGroupCount(boolean showGroupCount)
    {
        this.showGroupCount = showGroupCount;
    }

    public GridGroupRenderer getGridGroupRenderer()
    {
        return new GridGroupRenderer()
        {
            public String render(GroupColumnData data)
            {
                String groupHeader = grid.getColumnModel().getColumnById(data.field).getHeader();
                String cntStr = "";
                if (showGroupCountTotal)
                {
                    Integer groupCount = groupCountMap.get(data.group);
                    cntStr = groupCount != null && (data.models.size() != groupCount)
                        ? " (" + data.models.size() + "of " + groupCount + ")" : "";
                }
                else if (showGroupCount)
                {
                    cntStr = " (" + data.models.size() + ")";
                }
                return
                    (showCheckBox ? "<div class='x-grid3-group-checker'><div class='" +
                        uncheckedStyle + "'> </div></div>&nbsp;" : "") +
                        groupHeader + ": " + data.group + cntStr;
            }
        };
    }

    private final class CustomGroupingView extends GroupingView
    {
        private String label;

        public CustomGroupingView()
        {
            super();
            setGroupRenderer(getGridGroupRenderer());
        }


        public void updateSelectedCount()
        {
            if (!grid.isHideHeaders())
            {
                int count = preserveSelections ? preservedSelections.size() : getSelectedItems().size();
                int headerIndex = showCheckBox ? 2 : 1;
                if (label == null)
                {
                    label = getHeader().getHead(headerIndex).el().childElement("span").getInnerHTML();
                }
                getHeader().getHead(headerIndex).el().childElement("span")
                    .setInnerHTML(label + " (" + count + " selected)");
            }
        }

        @Override
        protected void onMouseDown(GridEvent<ModelData> ge)
        {
            El hd = ge.getTarget(".x-grid-group-hd", 10);
            El target = ge.getTargetEl();
            if (hd != null && target.hasStyleName(uncheckedStyle) || target.hasStyleName(checkedStyle))
            {
                boolean checked = !ge.getTargetEl().hasStyleName(uncheckedStyle);
                checked = !checked;
                if (checked)
                {
                    ge.getTargetEl().replaceStyleName(uncheckedStyle, checkedStyle);
                }
                else
                {
                    ge.getTargetEl().replaceStyleName(checkedStyle, uncheckedStyle);
                }

                Element group = (Element)findGroup(ge.getTarget());
                if (group != null)
                {
                    NodeList<Element> rows = El.fly(group).select(".x-grid3-row");
                    List<ModelData> temp = new ArrayList<ModelData>();
                    for (int i = 0; i < rows.getLength(); i++)
                    {
                        Element r = rows.getItem(i);
                        int idx = findRowIndex(r);
                        ModelData m = grid.getStore().getAt(idx);
                        temp.add(m);
                    }
                    if (checked)
                    {
                        grid.getSelectionModel().select(temp, true);
                    }
                    else
                    {
                        grid.getSelectionModel().deselect(temp);
                    }
                }
                return;
            }
            super.onMouseDown(ge);
        }
    }

}
