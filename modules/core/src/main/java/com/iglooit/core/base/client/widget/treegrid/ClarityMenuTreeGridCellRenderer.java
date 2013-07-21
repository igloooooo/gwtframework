package com.iglooit.core.base.client.widget.treegrid;

import com.clarity.core.base.client.model.MenuTreeModel;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.treegrid.WidgetTreeGridCellRenderer;
import com.google.gwt.user.client.ui.Widget;

/**
 * Custom renderer for the ClarityMenuTreeGrid
 */
public class ClarityMenuTreeGridCellRenderer extends WidgetTreeGridCellRenderer<BaseTreeModel>
{

    private static final int DEFAULT_WIDGET_WIDTH_PERCENT = 50;

    @Override
    public Widget getWidget(BaseTreeModel model,
                            String property,
                            ColumnData config,
                            int rowIndex,
                            int colIndex,
                            ListStore<BaseTreeModel> store,
                            Grid<BaseTreeModel> grid)
    {

        LayoutContainer lc = new LayoutContainer();
        lc.setStyleAttribute("overflow", "hidden");

        int cellWidth = grid.getColumnModel().getColumn(0).getWidth() - 40;
        int widgetWidth = model.get(MenuTreeModel.MENU_WIDGET_WIDTH) == null
            ? DEFAULT_WIDGET_WIDTH_PERCENT : (Integer)model.get(MenuTreeModel.MENU_WIDGET_WIDTH);

        lc.setWidth(cellWidth);
        lc.addStyleName((String)model.get(MenuTreeModel.STYLE));
        Label l = new Label((String)model.get(MenuTreeModel.LABEL));
        l.addStyleName((String)model.get(MenuTreeModel.STYLE));
        LayoutContainer labelContainer = new LayoutContainer();
        labelContainer.setWidth(cellWidth - widgetWidth);
        labelContainer.add(l);
        labelContainer.setStyleAttribute("float", "left");
        labelContainer.add(l);
        lc.add(labelContainer);
        Widget w = model.get(MenuTreeModel.MENU_WIDGET);
        if (w != null)
        {
            LayoutContainer widgetContainer = new LayoutContainer();
            widgetContainer.setWidth(widgetWidth);
            widgetContainer.add(w);
            widgetContainer.setStyleAttribute("float", "right");
            lc.add(widgetContainer);
        }
        return lc;
    }

    /*
   @Override
   public Widget getWidget(BaseTreeModel model,
                           String property,
                           ColumnData config,
                           int rowIndex,
                           int colIndex,
                           ListStore<BaseTreeModel> store,
                           Grid<BaseTreeModel> grid) {
        LayoutContainer lc = new LayoutContainer();
        HBoxLayout layout = new HBoxLayout();
        lc.setLayout(layout);
        lc.setWidth(150);
        lc.addStyleName((String)model.get(MenuTreeModel.STYLE));
        Label l = new Label((String)model.get(MenuTreeModel.LABEL));
        l.addStyleName((String)model.get(MenuTreeModel.STYLE));
        lc.add(l, new HBoxLayoutData(new Margins(0,5,0,0)));
        Widget w = model.get(MenuTreeModel.MENU_WIDGET);
        if (w != null)
        {
            lc.add(w);
        }
        return lc;
    }
    */
}
