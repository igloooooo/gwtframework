package com.iglooit.core.base.client.model;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;


public class MenuTreeModel<DTO> extends BaseTreeModel
{
    public static final String LABEL = "label";
    public static final String STYLE = "Style";
    public static final String MENU_WIDGET = "menu_widget";
    public static final String MENU_WIDGET_WIDTH = "widget_width";
    public static final String MENU_ICON = "menu_icon";
    public static final String DTO = "DTO";

    public MenuTreeModel(String label)
    {
        this(label, (String)null);
    }

    public MenuTreeModel(String label, DTO data)
    {
        this(label, (String)null);
        setData(data);
    }

    public MenuTreeModel(String label, String style)
    {
        this(label, style, null);
    }

    public MenuTreeModel(String label, String style, Widget widget)
    {
        this(label, style, widget, null);
    }

    public MenuTreeModel(String label, String style, Widget widget, AbstractImagePrototype icon)
    {
        super();
        setLabel(label);
        setStyle(style);
        setMenuWidget(widget);
        setMenuIcon(icon);
    }

    public void setLabel(String label)
    {
        set(LABEL, label);
    }

    public String getLabel()
    {
        return get(LABEL);
    }

    public void setStyle(String style)
    {
        set(STYLE, style);
    }

    public String getStyle()
    {
        return get(STYLE);
    }

    public void setMenuWidget(Widget w)
    {
        set(MENU_WIDGET, w);
    }

    public Widget getMenuWidget()
    {
        return (Widget)get(MENU_WIDGET);
    }

    public void setMenuIcon(AbstractImagePrototype icon)
    {
        set(MENU_ICON, icon);
    }

    public AbstractImagePrototype getMenuIcon()
    {
        return get(MENU_ICON);
    }

    public void setMenuWidgetWidth(Integer width)
    {
        set(MENU_WIDGET_WIDTH, width);
    }

    public Integer getMenuWidgetWidth()
    {
        return get(MENU_WIDGET_WIDTH);
    }

    public DTO getData()
    {
        return (DTO)get(DTO);
    }

    public void setData(DTO data)
    {
        set(DTO, data);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuTreeModel that = (MenuTreeModel)o;
        final DTO dto = getData();
        if (dto != null ? !dto.equals(that.getData()) : that.getData() != null) return false;
        final String label = getLabel();
        if (label != null ? !label.equals(that.getLabel()) : that.getLabel() != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        final DTO dto = getData();
        int result = dto != null ? dto.hashCode() : 0;
        final String label = getLabel();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}
