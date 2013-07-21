package com.iglooit.core.base.iface.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NavigationBarItem implements Serializable
{

    /**
     * Predefined module categories such as OSS, IM or MP. There is one special category, TOOLBOX,
     * which is used as the left-side menu in the navigation bar.
     */
    public enum NavigationModuleCategory
    {
        TOOLBOX("Toolbox"),

        /**
         * Column preference for 'OSS' category
         */
        OSS("OSS"),

        /**
         * Column preference for 'Infrastructure Management' category
         */
        IM("IM"),

        /**
         * Column preference for 'Market Place' category
         */
        MP("MP");

        private String defaultTitle;

        private NavigationModuleCategory(String defaultTitle)
        {
            this.defaultTitle = defaultTitle;
        }

        public int getColumnPreference()
        {
            return this.ordinal();
        }

        public String getDefaultTitle()
        {
            return this.defaultTitle;
        }

        /**
         * Returns all values except for the TOOLBOX value.
         *
         * @return
         */
        public static List<NavigationModuleCategory> getNonToolboxValues()
        {
            NavigationModuleCategory[] values = values();
            List<NavigationModuleCategory> categories = new ArrayList<NavigationModuleCategory>(values.length);
            for (NavigationModuleCategory category : values)
            {
                if (category != TOOLBOX)
                {
                    categories.add(category);
                }
            }
            return categories;
        }
    }

    private int columnPreference;
    private String title;
    private String description;
    private String moduleURL;
    private int orderPriority;

    public NavigationBarItem()
    {
    }

    /**
     * @param title
     * @param description
     * @param moduleURL
     * @param columnPreference Category of the nav-bar item; can be either a number or a predefined constant
     *                         <code>CATEGORY_*</code>.
     * @param orderPriority
     */
    public NavigationBarItem(String title, String description, String moduleURL, int columnPreference,
                             int orderPriority)
    {
        this.title = title;
        this.description = description;
        this.moduleURL = moduleURL;
        this.columnPreference = columnPreference;
        this.orderPriority = orderPriority;
    }

    public int getColumnPreference()
    {
        return columnPreference;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getModuleURL()
    {
        return moduleURL;
    }

    public int getOrderPriority()
    {
        return orderPriority;
    }
}
