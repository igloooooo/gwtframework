package com.iglooit.core.base.client.widget.grid;

import com.clarity.core.base.client.widget.OutputDateFormats;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

import java.util.Date;

public class DateColumnConfig extends ColumnConfig
{

    /**
     * Creates a new column config. Uses the default format.
     */
    public DateColumnConfig()
    {
        throw new UnsupportedOperationException("Add a format parameter.");
    }

    /**
     * Creates a new column config.
     *
     * @param format the format to use
     */
    public DateColumnConfig(OutputDateFormats format)
    {
        this((String)null, (String)null, 0, format, (OutputDateFormats)null);
    }

    /**
     * Creates a new column config.
     *
     * @param id    the column id
     * @param width the column width
     */
    public DateColumnConfig(String id, int width)
    {
        throw new UnsupportedOperationException("Add a format parameter.");
    }

    /**
     * Creates a new column config.
     *
     * @param id     the column id
     * @param width  the column width
     * @param format the format to use
     */
    public DateColumnConfig(String id, int width, OutputDateFormats format)
    {
        this(id, (String)null, width, format, (OutputDateFormats)null);
    }

    /**
     * Creates a new column config.
     *
     * @param id    the column id
     * @param name  the column name
     * @param width the column width
     */
    public DateColumnConfig(String id, String name, int width)
    {
        throw new UnsupportedOperationException("Add a format parameter.");
    }

    /**
     * Creates a new column config.
     *
     * @param id     the column id
     * @param name   the column name
     * @param width  the column width
     * @param format the format to use
     */
    public DateColumnConfig(String id, String name, int width, final OutputDateFormats format)
    {
        this(id, name, width, format, (OutputDateFormats)null);
    }

    /**
     * Creates a new column config.
     *
     * @param id            the column id
     * @param name          the column name
     * @param width         the column width
     * @param format        the date format to use
     * @param toolTipFormat the date format to use for the tooltip
     */
    public DateColumnConfig(String id, String name, int width, final OutputDateFormats format,
                            final OutputDateFormats toolTipFormat)
    {
        super(id, name, width);

        if (format.isSmartFormat() || toolTipFormat != null)
        {
            this.setRenderer(new GridCellRenderer()
            {
                @Override
                public Object render(ModelData model, String property, ColumnData config,
                                     int rowIndex, int colIndex, ListStore listStore, Grid grid)
                {
                    Date value = (Date)model.get(property);
                    if (value != null)
                    {
                        Html dateHtml = new Html(format.format(value));
                        if (toolTipFormat != null)
                        {
                            dateHtml.setToolTip(toolTipFormat.format(value));
                        }
                        return dateHtml;
                    }
                    return null;
                }
            });
        }
        else
        {
            this.setDateTimeFormat(format.toDateTimeFormat());
        }
    }

}
