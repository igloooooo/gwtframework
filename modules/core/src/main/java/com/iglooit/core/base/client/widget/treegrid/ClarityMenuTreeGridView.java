package com.iglooit.core.base.client.widget.treegrid;

import com.clarity.core.base.client.model.MenuTreeModel;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridView;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ClarityMenuTreeGridView extends TreeGridView
{

    public ClarityMenuTreeGridView()
    {
        setRowHeight(26);
        initViewConfig();
    }

    private void initViewConfig()
    {
        viewConfig = new GridViewConfig()
        {
            @Override
            public String getRowStyle(ModelData model, int rowIndex, ListStore<ModelData> ds)
            {
                String style = model.get(MenuTreeModel.STYLE);
                if (style != null)
                {
                    return style;
                }
                return super.getRowStyle(model, rowIndex, ds);
            }
        };

    }

    @Override
    public boolean isSelectableTarget(ModelData model,
                                      Element target)
    {
        return ((BaseTreeModel)model).getChildCount() == 0;
    }

    @Override
    public String getWidgetTemplate(ModelData m, String id, String text, AbstractImagePrototype icon,
                                    boolean checkable, TreePanel.Joint joint, int level)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<div unselectable=\"on\" id=\"");
        sb.append(id);
        sb.append("\" class=\"x-tree3-node\">");
        // jumping content when inserting in column with cell widget the column
        // extra width fixes
        sb.append("<div unselectable=\"on\" class=\"x-tree3-el\" style=\"width: 1000px;height: auto;\">");

        sb.append("<table cellpadding=0 cellspacing=0><tr><td>");

        String h = "";
        switch (joint)
        {
            case COLLAPSED:
                h = tree.getStyle().getJointCollapsedIcon().getHTML();
                break;
            case EXPANDED:
                h = tree.getStyle().getJointExpandedIcon().getHTML();
                break;
            default:
                AbstractImagePrototype defaultIcon = (AbstractImagePrototype)m.get(MenuTreeModel.MENU_ICON);
                if (defaultIcon != null)
                {
                    h = defaultIcon.getHTML();
                }
                else
                {
                    h = "<img src=\"" + GXT.BLANK_IMAGE_URL + "\" style='width: 16px'>";
                }
        }

        sb.append("</td><td><img src=\"");
        sb.append(GXT.BLANK_IMAGE_URL);
        sb.append("\" style=\"height: 18px; width: ");
        sb.append(level * 18);
        sb.append("px;\" /></td><td  class='x-tree3-el-jnt'>");
        sb.append(h);
        if (checkable)
        {
            sb.append(GXT.IMAGES.unchecked().getHTML());
        }
        else
        {
            sb.append("<span></span>");
        }
        sb.append("</td><td>");
        if (icon != null)
        {
            sb.append(icon.getHTML());
        }
        else
        {
            sb.append("<span></span>");
        }
        sb.append("</td><td>");
        sb.append("<span unselectable=\"on\" class=\"x-tree3-node-text\">");
        sb.append(text);
        sb.append("</span>");
        sb.append("</td></tr></table>");

        sb.append("</div>");
        sb.append("</div>");

        return sb.toString();
    }

}
