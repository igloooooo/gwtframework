package com.iglooit.core.base.client.widget.plugin;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.google.gwt.user.client.Element;

/**
 * A column config and component plugin that adds the ability for each row to be
 * expanded, showing custom content that spans all the rows columns.
 * with extended control over expanding rows
 * <p/>
 * you can add a custom renderer for the expanded part
 * thanks to siberian
 * http://www.extjs.com/forum/showthread.php?p=424228#post424228
 *
 * @author anonym
 */
public class ExtendedRowExpander extends RowExpander
{

    private GridCellRenderer<ModelData> expandRenderer;
    private java.util.Map<El, Component> layouts = new java.util.HashMap();

    /**
     * Creates a new extended row expander.
     */
    public ExtendedRowExpander()
    {
        super();
    }

    /**
     * Creates a new extended row expander with the given template.
     *
     * @param template the template
     */
    public ExtendedRowExpander(XTemplate template)
    {
        super(template);
    }

    /**
     * expand all rows
     */
    public void expandAllRows()
    {
        for (int rowIndex = 0; rowIndex < grid.getStore().getCount(); rowIndex++)
        {
            this.expandRow(rowIndex);
        }
    }

    /**
     * collapse all rows
     */
    public void collapseAllRows()
    {
        for (int rowIndex = 0; rowIndex < grid.getStore().getCount(); rowIndex++)
        {
            this.collapseRow(rowIndex);
        }
    }

    /**
     * toggle all rows
     *
     * @param expand true to expand, false to collapse rows
     */
    public void toggleAllRows(boolean expand)
    {
        if (expand)
        {
            expandAllRows();
        }
        else
        {
            collapseAllRows();
        }
    }

    /**
     * expand row
     *
     * @param rowIndex index of the row
     */
    public void expandRow(int rowIndex)
    {
        this.expandRow(getRowAsEl(rowIndex));
    }

    /**
     * collapse row
     *
     * @param rowIndex index of the row
     */
    public void collapseRow(int rowIndex)
    {
        this.collapseRow(getRowAsEl(rowIndex));
    }

    /**
     * toggle row
     *
     * @param rowIndex index of the row
     */
    public void toggleRow(int rowIndex)
    {
        this.toggleRow(getRowAsEl(rowIndex));
    }

    /**
     * toggle row
     *
     * @param rowIndex index of the row
     * @param expand   true to expand, false to collapse rows
     */
    public void toggleRow(int rowIndex, boolean expand)
    {
        if (expand)
        {
            expandRow(rowIndex);
        }
        else
        {
            collapseRow(rowIndex);
        }
    }

    /**
     * get the row as El
     *
     * @param rowIndex
     * @return El row
     */
    protected El getRowAsEl(int rowIndex)
    {
        return El.fly(grid.getView().getRow(rowIndex));
    }

    /**
     * Returns the rowexpanders cell renderer.
     *
     * @return the renderer
     */
    public GridCellRenderer<ModelData> getExpandRenderer()
    {
        return expandRenderer;
    }

    /**
     * Sets the rowexpanders cell renderer (pre-render).
     *
     * @param expandRenderer the cell renderer
     */
    @SuppressWarnings("unchecked")
    public void setExpandRenderer(GridCellRenderer expandRenderer)
    {
        this.expandRenderer = expandRenderer;
    }

    @Override
    protected boolean beforeExpand(ModelData model, Element body, El row, int rowIndex)
    {
        if (expandRenderer == null)
        {
            return super.beforeExpand(model, body, row, rowIndex);
        }
        else
        {
            if (fireEvent(Events.BeforeExpand))
            {
                return makePanel(model, body, row, rowIndex);
            }
            return false;
        }
    }

    @Override
    protected void collapseRow(El row)
    {
        if (fireEvent(Events.BeforeCollapse))
        {
            row.replaceStyleName("x-grid3-row-expanded", "x-grid3-row-collapsed");
            // Detach so we don't bleed memory
            if (layouts.get(row) != null)
            {
                ComponentHelper.doDetach(layouts.get(row));
            }
            layouts.remove(row);
            fireEvent(Events.Collapse);
        }
    }

    protected boolean makePanel(ModelData model, Element body, El row, int rowIndex)
    {
        Object detail = expandRenderer.render(model, null, null, rowIndex, 0, ((Grid)grid).getStore(), (Grid)grid);

        if (detail instanceof Component)
        {
            layouts.put(row, (Component)detail);
            body.setInnerHTML("");
            layouts.get(row).render(body);
            ComponentHelper.doAttach(layouts.get(row));
        }
        else if (detail != null)
        {
            layouts.put(row, null);
            body.setInnerHTML(detail.toString());
        }
        else
        {
            layouts.put(row, null);
            body.setInnerHTML("");
        }
        return true;
    }
}  
