package com.iglooit.core.base.client.widget.layoutcontainer;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.user.client.ui.Hyperlink;

import java.util.ArrayList;
import java.util.List;


/**
 * Container that lays out a list of hyperlinks that can be used for breadcrumb navigation
 */
public class BreadcrumbContainer extends LayoutContainer
{
    private List<Hyperlink> linkList;

    public BreadcrumbContainer()
    {
        linkList = new ArrayList<Hyperlink>();
        setLayout(new HBoxLayout());
        setStyleName("bc-wrapper small");
    }

    public void addLink(Hyperlink link)
    {
        linkList.add(link);
    }

    /**
     * Creates the markup for the breadcrumb links based on the links added to the list.
     * This must be called after adding all the links in order to render the element
     */
    public void buildContainer()
    {
        Html separator = new Html("&gt;");
        separator.setTagName("span");
        for (Hyperlink hyperlink : linkList)
        {
            add(hyperlink);
            add(separator, new HBoxLayoutData(new Margins(0, 5, 0, 5)));
        }
    }
}
