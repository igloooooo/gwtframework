package com.iglooit.core.base.client.widget.grid;

import com.extjs.gxt.ui.client.widget.grid.GridView;

public class NonEmptyGridView extends GridView
{
    @Override
    protected void applyEmptyText()
    {
        if (emptyText == null)
        {
            emptyText = "&nbsp;";
        }
        if (!hasRows())
        {
            mainBody.setInnerHtml("<div class='x-grid-empty non-empty-grid'>" + emptyText + "</div>");
        }
        syncHScroll();
    }
}
