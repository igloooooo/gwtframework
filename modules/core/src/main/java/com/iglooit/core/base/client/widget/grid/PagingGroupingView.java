package com.iglooit.core.base.client.widget.grid;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Element;

import java.util.ArrayList;
import java.util.List;

public class PagingGroupingView extends GroupingView
{

    private String checkedStyle = "x-grid3-group-check";
    private String uncheckedStyle = "x-grid3-group-uncheck";

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

    @Override
    public void layout()
    {
        super.layout();
    }

    public void resizeToFit()
    {
        if (cm == null)
            return;
        fitColumns(false, false, -1);
        header.updateTotalWidth(getOffsetWidth(), getTotalWidth());
        if (footer != null)
        {
            footer.updateTotalWidth(getOffsetWidth(), getTotalWidth());
        }
        layout(true);
    }
}
