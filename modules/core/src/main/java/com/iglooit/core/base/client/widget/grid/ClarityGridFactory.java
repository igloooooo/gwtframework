package com.iglooit.core.base.client.widget.grid;

import com.clarity.core.base.client.model.ClarityGridBaseMd;
import com.clarity.core.base.client.widget.common.ClarityWidgetHelper;
import com.clarity.core.base.client.widget.common.ClarityWidgetOption;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

import java.util.ArrayList;
import java.util.List;

public class ClarityGridFactory
{
    private static final int DEFAULT_COLUMN_WIDTH = 100;
    private static final int DEFAULT_COLUMN_MIN_WIDTH = 50;
    private static final boolean RESIZABLE = true;

    public static <MD extends ClarityGridBaseMd> ClarityBasicGrid<MD> createGrid(final MD md)
    {
        return createGrid(md, new ArrayList<ClarityGridColumnOption>());
    }

    public static <MD extends ClarityGridBaseMd> ClarityBasicGrid<MD> createGrid(
            final MD md, final List<ClarityGridColumnOption> optionList)
    {
        ClarityBasicGrid<MD> grid = new ClarityBasicGrid<MD>(false, false)
        {
            @Override
            public List<ColumnConfig> getColumnConfig()
            {
                List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

                for (String columnStr : md.getColumns())
                {
                    ColumnConfig columnConfig = createColumnConfig(columnStr, columnStr,
                            DEFAULT_COLUMN_WIDTH, RESIZABLE, false);
                    columns.add(columnConfig);

                    ClarityWidgetOption option = md.getColumnWidgetOptionMap().get(columnStr);
                    if (option != null)
                        columnConfig.setRenderer(getMdCellOptionRenderer(option));
                }

                if (optionList != null && optionList.size() > 0)
                {
                    for (ClarityGridColumnOption option : optionList)
                    {
                        columns.add(createOptionColumnConfig(option));
                    }
                }
                return columns;
            }

            @Override
            public String getLabel()
            {
                return "";
            }

            private ColumnConfig createOptionColumnConfig(ClarityGridColumnOption option)
            {
                String headerName = option.getColumnHeader();
                ColumnConfig columnConfig = new ColumnConfig("OPTION_" + headerName,
                        headerName, option.getColumnWidth());

                columnConfig.setMenuDisabled(true);
                columnConfig.setSortable(false);
                columnConfig.setFixed(true);
                columnConfig.setRenderer(getOptionRenderer(option.getWidgetOptionList()));
                return columnConfig;
            }

            private GridCellRenderer getOptionRenderer(final List<ClarityWidgetOption> optionList)
            {
                return new GridCellRenderer<MD>()
                {
                    @Override
                    public Object render(final MD model,
                                         String property, ColumnData config,
                                         final int rowIndex, final int colIndex,
                                         ListStore<MD> store,
                                         final Grid<MD> grid)
                    {
                        ToolBar tb = new ToolBar();
                        tb.setStyleAttribute("background", "none");
                        tb.setStyleAttribute("border-width", "0");
                        tb.setSpacing(2);
                        for (ClarityWidgetOption widgetOption : optionList)
                        {
                            widgetOption.setPreClickBaseListener(new Listener<BaseEvent>()
                            {
                                @Override
                                public void handleEvent(BaseEvent baseEvent)
                                {
                                    grid.getSelectionModel().select(model, false);
                                }
                            });
                            Component widget = ClarityWidgetHelper.getWidgetFromOption(widgetOption);
                            if (widget != null)
                                tb.add(widget);
                        }
                        return tb;
                    }
                };
            }

            private GridCellRenderer getMdCellOptionRenderer(final ClarityWidgetOption option)
            {
                return new GridCellRenderer<MD>()
                {
                    @Override
                    public Object render(final MD model,
                                         String property, ColumnData config,
                                         final int rowIndex, final int colIndex,
                                         ListStore<MD> store,
                                         final Grid<MD> grid)
                    {
                        final Component widget = ClarityWidgetHelper.getWidgetFromOption(option);
                        if (widget instanceof Field)
                        {
                            ((Field)widget).setValue(model.get(property));
                            final Object d = store.getAt(rowIndex).get(property);
                            widget.addListener(Events.Change, new Listener<FieldEvent>()
                            {
                                @Override
                                public void handleEvent(FieldEvent be)
                                {
                                    ClarityWidgetHelper.applyDirtyStyle(widget, !be.getValue().equals(d));
                                }
                            });
                        }
                        return widget;
                    }
                };
            }
        };
        grid.getGrid().setMinColumnWidth(DEFAULT_COLUMN_MIN_WIDTH);
        return grid;
    }

    private static ColumnConfig createColumnConfig(String id, String title,
                                                   int width, boolean resizable, boolean hidden)
    {
        ColumnConfig cc = new ColumnConfig();
        cc.setId(id);
        cc.setHeader(title);
        cc.setWidth(width);
        cc.setResizable(resizable);
        cc.setHidden(hidden);
        return cc;
    }
}
