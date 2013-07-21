package com.iglooit.core.base.client.widget.combobox;

import com.clarity.core.base.client.widget.ClarityWidgetFlag;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.TextMetrics;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiSelectSearchComboBox<D extends ModelData> extends SearchComboBox<D>
{
    private El ul;
    private El inputLi;
    private List<D> selectedItems;
    private List<D> staticSelectedItems;
    private Map<Element, D> selectedItemsMapping;
    private Map<El, D> selectedItemsElMap;
    private Map<El, D> staticSelectedItemsElMap;
    private TextMetrics metrics;
    private El buttonClear;
    private El currentFocus;

    private SelectedMode selectedMode;

    public static final int DEFAULT_PAGE_SIZE = 100;
    private boolean rendered = false;
    private boolean isResizeParent = true;

    public static final String SELECTED_ELEMENT_CSS_CLASS = "selectedElementCssClass";
    public static final String NOT_ABLE_TO_CHOOSE = "##NOT ABLE TO CHOOSE##";

    public static final EventType MULTI_SELECT_ADD_ITEM_EVENT = new EventType();
    public static final EventType MULTI_SELECT_REMOVE_ITEM_EVENT = new EventType();
    
    public MultiSelectSearchComboBox()
    {
        super();
        selectedItems = new ArrayList<D>();
        staticSelectedItems = new ArrayList<D>();
        selectedItemsMapping = new HashMap<Element, D>();
        selectedItemsElMap = new HashMap<El, D>();
        staticSelectedItemsElMap = new HashMap<El, D>();
        setMinChars(1);
        addStyleName("multi-select-search-combo");
        selectedMode = SelectedMode.MULTIPLE;
    }

    @Override
    protected void onSelect(D model, int index)
    {
        if (selectedMode.equals(SelectedMode.SINGLE))
        {
            getInputEl().setWidth(this.getWidth() - 30);
            super.onSelect(model, index);
        }
        else if (model.toString().equals(ClarityWidgetFlag.NON_SELECTABLE_ITEM_FLAG_MORE))
        {
            onMoreDotSelected();
        }
        else if (model.toString().equals(ClarityWidgetFlag.NON_SELECTABLE_ITEM_FLAG))
        {
            return;
        }
        else
        {
            if (model.toString().equals(BVC.noResultFoundText()))
            {
                setRawValue(getInputEl().getValue());
                collapse();
            }
            else if (model.toString().equals(NOT_ABLE_TO_CHOOSE))
            {
                return;
            }
            else
            {
                for (D d : selectedItems)
                {
                    if (d.get(getDisplayField()).toString().equals(model.get(getDisplayField()).toString()))
                    {
//                    setRawValue("");
                        clearSelections();
                        collapse();
                        autoSizeInput();
                        return;
                    }
                }
                FieldEvent fe = new FieldEvent(this);
                fe.setOldValue(getSelectedItems());
                selectedItems.add(model);
                if (fireEvent(Events.BeforeSelect, fe))
                {
                    addSelectedItem(model);
                    fe.setValue(getSelectedItems());
                    //setRawValue("");
                    clearSelections();
                    collapse();
                    updateEl();
                    autoSizeInput();
                    fireEvent(Events.Select, fe);
//                    fireEvent(Events.Change, fe);

                    FieldEvent fieldEvent = new FieldEvent(this);
                    fieldEvent.setValue(model);
                    fireEvent(MULTI_SELECT_ADD_ITEM_EVENT, fieldEvent);
                }

                manageClearBtn();
            }
        }
    }

    /**
     * to be override
     * */
    protected void onMoreDotSelected()
    {

    }

    private void addSelectedItem(final D model)
    {
        removeEmptyText();
        StringBuilder sb = new StringBuilder();

        String elementClass = " bit-box ";
        if (model.get(SELECTED_ELEMENT_CSS_CLASS) != null)
            elementClass = elementClass + model.get(SELECTED_ELEMENT_CSS_CLASS).toString();

        sb.append("<li class=\"").append(elementClass).append(" \">");
        sb.append(model.get(getDisplayField()).toString());
        sb.append("</li>");
        El myLi = new El(sb.toString());

        El closeButton = new El("<a href=\"#\"/>");
        closeButton.addStyleName("closebutton");
        closeButton.setId(model.get(getDisplayField()).toString());
        myLi.appendChild(closeButton.dom);

        if (this.isRendered())
        {
            ul.insertBefore(myLi.dom, inputLi.dom);
            selectedItemsMapping.put(closeButton.dom, model);
            selectedItemsElMap.put(myLi, model);
        }
    }

    private void addStaticSelectedItem(final D model)
    {
        removeEmptyText();
        StringBuilder sb = new StringBuilder();
        sb.append("<li class=\" bit-box \">");
        sb.append(model.get(getDisplayField()).toString());
        sb.append("</li>");
        El myLi = new El(sb.toString());

        if (this.isRendered())
        {
            ul.insertBefore(myLi.dom, inputLi.dom);
            staticSelectedItemsElMap.put(myLi, model);
        }
    }

    @Override
    protected void onClick(ComponentEvent ce)
    {
        super.onClick(ce);

        clearCurrentFocus();
        if (ce.getTargetEl().getId().equals(buttonClear.getId()))
        {
            if (disabled)
                return;
            removeAllSelectedItems();
        }
        if (ce.getTargetEl().getStyleName().contains("bit-box"))
        {
            if (disabled)
                return;

            clearCurrentFocus();
            setCurrentFocus(ce.getTargetEl());
            focus();
        }
        else if (selectedItemsMapping.get(ce.getTargetEl().dom) != null)
        {
            removeItem(ce, ce.getTargetEl());
        }
        else
        {
            if (!selectedItems.isEmpty() || !staticSelectedItems.isEmpty())
                removeEmptyText();
        }
    }

    private void removeItem(ComponentEvent ce, El removeItemEl)
    {
        List oldValues = getSelectedItems();
        final D removedItem = selectedItemsMapping.get(removeItemEl.dom);
        selectedItems.remove(removedItem);
        selectedItemsMapping.remove(removeItemEl.dom);
        El key = getElFromModelData(removedItem);
        if (key != null)
            selectedItemsElMap.remove(key);

        removeItemEl.getParent().removeFromParent();
        autoSizeInput();
        List newValues = getSelectedItems();

        FieldEvent fe = (ce instanceof FieldEvent) ? (FieldEvent)ce : new FieldEvent(this, ce.getEvent());
        fe.setOldValue(oldValues);
        fe.setValue(newValues);
        fireEvent(Events.Change, fe);

        if (ce instanceof FieldEvent)
        {
            FieldEvent fieldEvent = new FieldEvent(this, ce.getEvent());
            fieldEvent.setValue(removedItem);
            fireEvent(MULTI_SELECT_REMOVE_ITEM_EVENT, fieldEvent);
        }

        ce.stopEvent();
        collapse();

        manageClearBtn();
    }

    /* this will only remove selectable items but will leave all static items still there */
    public void removeAllSelectedItems()
    {
        if (!rendered)
            return;
        if (selectedItems.size() > 0)
        {
            List oldValues = getSelectedItems();
            clearSelections();

            for (El selectItemEl : selectedItemsElMap.keySet())
            {
                selectItemEl.removeFromParent();
            }

            selectedItems.clear();
            selectedItemsMapping.clear();
            selectedItemsElMap.clear();
            updateEl();
            manageClearBtn();

            List newValues = getSelectedItems();
            FieldEvent fe = new FieldEvent(this);
            fe.setOldValue(oldValues);
            fe.setValue(newValues);
            fireEvent(Events.Change, fe);
        }
    }

    public void removeAllStaticSelectedItems()
    {
        if (!rendered)
            return;
        if (staticSelectedItemsElMap.size() > 0)
        {
            for (El selectItemEl : staticSelectedItemsElMap.keySet())
            {
                selectItemEl.removeFromParent();
            }

            staticSelectedItemsElMap.clear();
            updateEl();
        }
    }

    @Override
    protected void onRender(Element parent, int index)
    {
        super.onRender(parent, index);

        El inputEl = getInputEl();
        El inputParent = getInputEl().getParent();

        input.removeStyleName("x-form-text");
        inputParent.removeStyleName("x-form-text");
        inputParent.addStyleName("x-trigger-wrap-focus");
        inputEl.removeFromParent();

        ul = new El("<ul class=\"holder x-form-text\"></ul>");
        inputParent.insertFirst(ul.dom);
        inputLi = ul.createChild("<li class=\"bit-input\"/>");
        inputEl.insertInto(inputLi.dom);

        El buttonWrap = createButtonsWrap();
        buttonWrap.insertInto(inputParent.dom, 1);

        updateEl();

        rendered = true;
        addStaticSelectedItemsToDom();
        addSelectedItemsToDom();

        manageClearBtn();
    }

    @Override
    protected void onKeyUp(FieldEvent fe)
    {
        super.onKeyUp(fe);
        autoSizeInput();
        updateEl();
    }

    @Override
    protected void onKeyDown(FieldEvent fe)
    {
        String oldRawValue = getRawValue();
        super.onKeyDown(fe);
        if (fe.getKeyCode() == KeyCodes.KEY_DELETE && currentFocus != null)
        {
            D model = selectedItemsMapping.get(currentFocus.getChild(0).dom);
            int index = selectedItems.indexOf(model);
            D nextModel = null;
            if (index < selectedItems.size() - 1)
                nextModel = selectedItems.get(index + 1);
            removeItem(fe, currentFocus.getChild(0));
            currentFocus = null;
            if (nextModel != null)
            {
                El nextEl = getElFromModelData(nextModel);
                setCurrentFocus(nextEl);
            }
        }
        else if (oldRawValue.isEmpty() && selectedItems.size() > 0 && fe.getKeyCode() == KeyCodes.KEY_BACKSPACE)
        {
            if (currentFocus != null)
            {
                D model = selectedItemsMapping.get(currentFocus.getChild(0).dom);
                int index = selectedItems.indexOf(model);
                D preModel = null;
                if (index != 0)
                    preModel = selectedItems.get(index - 1);
                removeItem(fe, currentFocus.getChild(0));
                currentFocus = null;
                if (preModel != null)
                {
                    El preEl = getElFromModelData(preModel);
                    setCurrentFocus(preEl);
                }
            }
            else
            {
                El key = getElFromModelData(selectedItems.get(selectedItems.size() - 1));
                if (key != null)
                    removeItem(fe, key.getChild(0));
            }
        }
    }

    @Override
    protected void onResize(int width, int height)
    {
        super.onResize(width, height);
        autoSizeInput();
        updateEl();
    }

    private native LayoutContainer getListLayoutContainer() /*-{
         return this.@com.extjs.gxt.ui.client.widget.form.ComboBox::list;
    }-*/;

    public List<String> getSelectedItems()
    {
        if (selectedMode.equals(SelectedMode.SINGLE))
            return new ArrayList<String>(Arrays.asList(super.getRawValue()));
        else
        {
            List<String> strSelectedItems = new ArrayList<String>();
            for (D d : selectedItems)
            {
                strSelectedItems.add(d.get(getDisplayField()).toString());
            }
            return strSelectedItems;
        }
    }

    // Return a list of object D
    public List<D> getSelectedObjectItems()
    {
        if (selectedMode.equals(SelectedMode.SINGLE))
        {
            selectedItems = super.getSelection();
        }
        return selectedItems;
    }

    // Return a list of object D
    public List<D> getStaticSelectedItems()
    {
        return staticSelectedItems;
    }

    // Return a list of object D
    public List<D> getAppliedObjectItems()
    {
        List<D> mergedList = new ArrayList<D>();
        mergedList.addAll(selectedItems);
        mergedList.addAll(staticSelectedItems);
        return mergedList;
    }

    public void setSelectedItems(List<D> items)
    {
        removeAllSelectedItems();
        selectedItems.clear();
        selectedItems.addAll(items);
        addSelectedItemsToDom();
    }

    public void setStaticSelectedItems(List<D> items)
    {
        removeAllStaticSelectedItems();
        staticSelectedItems.clear();
        staticSelectedItems.addAll(items);
        addStaticSelectedItemsToDom();
    }

    private void addSelectedItemsToDom()
    {
        if (!rendered)
            return;
        if (!selectedItems.isEmpty())
            removeEmptyText();
        for (D d : selectedItems)
        {
            addSelectedItem(d);
        }
//        updateEl();
        autoSizeInput();
    }

    private void addStaticSelectedItemsToDom()
    {
        if (!rendered)
            return;
        if (!staticSelectedItems.isEmpty())
            removeEmptyText();
        for (D d : staticSelectedItems)
        {
            addStaticSelectedItem(d);
        }
//        updateEl();
        autoSizeInput();
    }

    public void forceUpdateEl()
    {
        if (!rendered)
            return;
        el().setHeight(ul.getHeight());
        if (el().getParent() != null && isResizeParent)
            el().getParent().setHeight(ul.getHeight() + 4);
        fireEvent(Events.Resize);
    }

    private void updateEl()
    {
        /* this doesn't make sense, parent height should always be 'auto' and controlled by input box height/width
        * -- use following autoSizeInput to control width/height calculation */

 //        el().setHeight(ul.getHeight());
//        if (el().getParent() != null && isResizeParent)
//            el().getParent().setHeight(ul.getHeight() + 4);
//        fireEvent(Events.Resize);
    }

    private void autoSizeInput()
    {
        if (!this.isRendered())
            return;

        if (metrics == null)
        {
            metrics = TextMetrics.get();
            metrics.bind(getInputEl());
        }

        Element divElement = DOM.createDiv();
        divElement.setInnerText(getRawValue());
        String myValue = divElement.getInnerHTML();
        myValue += "&#160;";
        int width = Math.max(metrics.getWidth(myValue) + 10, 10);
//        width = Math.max(width, metrics.getWidth(getEmptyText()) + 10);
        int parentWidth = inputLi.getParent().getClientWidth();

        Element previousSibling = inputLi.previousSibling();
        if (previousSibling != null)
        {
            /* this can be added up with multiple siblings depend on how many we have, init value = 0 */
            int lastSibListWidth = 0;
            /* this is used for calculation how big input box should be, -18 needed for search button width */

            int childCount = previousSibling.getParentElement().getChildCount();
            for (int i = childCount - 2; i >= 0; i--)
            {
                El previousSiblingEl = inputLi.getParent().getChild(i);
                int preSibWidth = previousSiblingEl.getClientWidth();
                lastSibListWidth += preSibWidth + 12; // 12 is margin of each box + empty space
                int preSibX = previousSiblingEl.getX();
                int inputParentX = inputLi.getParent().getX();

                if (Math.abs(preSibX - inputParentX) < 20)
                {
                    /* just use last sibling width */
                    break;
                }
            }

            int adjustWidth = parentWidth - lastSibListWidth;
            if (adjustWidth < 20 + 10)
                getInputEl().setWidth(parentWidth - 20);
            else
                getInputEl().setWidth(Math.max(width, adjustWidth - 20));
        }
        else
        {
            getInputEl().setWidth(parentWidth - 20);
        }

        int h = Math.max(metrics.getHeight(myValue) + 2, 17);
        getInputEl().setHeight(h);

        if (GXT.isIE)
        {
            getInputEl().dom.getStyle().setTop(0, Style.Unit.PX);
        }
    }

    @Override
    public void onBrowserEvent(Event event)
    {
        int eventType = DOM.eventGetType(event);
        if (disabled && (eventType == Event.ONMOUSEOVER || eventType == Event.ONMOUSEOUT))
        {
            enable();
            super.onBrowserEvent(event);
            disable();
        }
        else
        {
            super.onBrowserEvent(event);
        }
    }

    @Override
    public boolean validate()
    {
        if (!getAllowBlank()
                && ((getSelectedObjectItems() == null) || getSelectedObjectItems().size() <= 0))
        {
            markInvalid(getMessages().getBlankText());
            return false;
        }
        clearInvalid();
        return true;
    }

    @Override
    public void markInvalid(String msg)
    {
        super.markInvalid(msg);
        if (ul != null)
            ul.addStyleName("x-form-invalid");
    }

    @Override
    public void clearInvalid()
    {
        super.clearInvalid();
        if (ul != null)
            ul.removeStyleName("x-form-invalid");
    }

    public SelectedMode getSelectedMode()
    {
        return selectedMode;
    }

    public void setSelectedMode(SelectedMode selectMode)
    {
        this.selectedMode = selectMode;
    }

    public enum SelectedMode
    {
        SINGLE, MULTIPLE;
    }

    protected void applyEmptyText()
    {
        if (selectedItems.isEmpty() && staticSelectedItems.isEmpty())
            super.applyEmptyText();
    }

    @Override
    protected void triggerBlur(ComponentEvent ce)
    {
        if (!input.getValue().trim().equals(""))
        {
            input.setValue("");
            this.applyEmptyText();
            autoSizeInput();
            updateEl();
        }
        super.triggerBlur(ce);
    }

    public void setResizeParent(boolean resizeParent)
    {
        isResizeParent = resizeParent;
    }

    public void removeSelectedItemFromInputField(D model)
    {
        El key = getElFromModelData(model);
        if (key != null)
        {
            selectedItemsElMap.remove(key);
            key.removeFromParent();
        }
    }

    private El getElFromModelData(D model)
    {
        El key = null;
        for (Map.Entry<El, D> entry : selectedItemsElMap.entrySet())
        {
            if (entry.getValue() == model)
            {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    public void setDataStore(ListStore<D> store)
    {
        this.store = store;
    }

    private El createButtonsWrap()
    {
        El buttonWrap = new El(DOM.createDiv());
        buttonWrap.dom.setClassName("x-superboxselect-btns");

        buttonClear = buttonWrap.createChild("");
        buttonClear.dom.setClassName("x-superboxselect-btn-clear");
        addStyleOnOver(buttonClear.dom, "x-superboxselect-btn-over");
        buttonClear.dom.setAttribute("title", "Remove all entires");

        El buttonSearch = buttonWrap.createChild("");
        trigger.insertInto(buttonSearch.dom, 1);
        return buttonWrap;
    }

    private void manageClearBtn()
    {
        String cls = "x-superboxselect-btn-hide";
        if (selectedItems.size() == 0)
            buttonClear.dom.addClassName(cls);
        else
            buttonClear.dom.removeClassName(cls);
    }

    private void clearCurrentFocus()
    {
        if (currentFocus != null)
        {
            currentFocus.dom.removeClassName("x-superboxselect-item-focus");
            currentFocus = null;
        }
    }

    private void setCurrentFocus(El selectedEl)
    {
        currentFocus = selectedEl;
        currentFocus.dom.addClassName("x-superboxselect-item-focus");
    }

    @Override
    protected void onBlur(ComponentEvent ce)
    {
        super.onBlur(ce);
        clearCurrentFocus();
    }
}