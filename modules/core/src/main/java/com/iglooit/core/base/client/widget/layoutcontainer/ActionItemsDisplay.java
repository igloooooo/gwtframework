package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.htmlcontainer.ActionLinkHtml;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class ActionItemsDisplay extends LayoutContainer
{
    private static final RowData ANCHOR_ROWDATA = new RowData(1, -1, new Margins(5, 10, 0, 10));
    private static final RowData DESCRIPTION_ROWDATA = new RowData(1, -1, new Margins(2, 10, 10, 10));

    public ActionItemsDisplay(String title)
    {
        addStyleName("user-actions");
        addStyleName(ClarityStyle.ROUNDED_CORNERS);
        RowLayout rl = new RowLayout(Style.Orientation.VERTICAL);
        setLayout(rl);

        Html panelHeading = new Html(title);
        panelHeading.addStyleName(ClarityStyle.TEXT_TITLE);
        add(panelHeading, ANCHOR_ROWDATA);
    }

    public static Text createAnchorDescription(String desc)
    {
        Text text = new Text();
        text.addStyleName("secondary desc");
        text.setText(desc);
        return text;
    }

    public void add(ActionLinkHtml link, String descriptionText)
    {
        Text desc = createAnchorDescription(descriptionText);
        add(link, desc);
    }

    public void add(ActionLinkHtml linkHtml, Text description)
    {
        add(linkHtml, ANCHOR_ROWDATA);
        add(description, DESCRIPTION_ROWDATA);
    }

}
