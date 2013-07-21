package com.iglooit.core.base.client.widget.menu;

import com.clarity.core.base.client.view.ClarityStyle;
import com.extjs.gxt.ui.client.widget.Html;

/**
 * A simple widget for a menu item in the navigation bar (NavigationView).
 * <p>
 * A menu item in the navigation bar contains a HTML link, a title and an optional description. A menu item can belong
 * to either a menu item group or the left-side toolbox in the navigation bar.
 * </p>
 */
public class NavigationMenuItem extends Html
{

    /**
     * Constructs a menu item with a given URL and a title. No description is given.
     *
     * @param url
     * @param title
     */
    public NavigationMenuItem(String url, String title)
    {
        super("<a href=\"" + url + ">" + title + "</a>");
        addStyleName(ClarityStyle.TEXT_REGULAR);
    }

    /**
     * Constructs a menu item with a given URL, a title and a description.
     *
     * @param url
     * @param title
     * @param description
     */
    public NavigationMenuItem(String url, String title, String description, int height)
    {
        super("<a href=\"" + url + ">" +
            "<div class=\"link-name\">" + title + "</div>" +
            "<div class=\"link-description secondary\" style=\"height: " + height + "px;\" >" + description +
            "</div></a>");
        addStyleName(ClarityStyle.TEXT_REGULAR);
    }

}
