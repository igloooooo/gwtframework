package com.iglooit.core.base.iface.domain;

import com.iglooit.commons.iface.domain.I18NFactoryProvider;
import com.iglooit.core.base.client.view.BaseViewConstants;

public enum IglooitEntryPoint
{
    EXAMPLE("inboxpoc.jsp", "Inbox", "Manage assigned tasks")
        {
            @Override
            public String getDisplayName()
            {
                return BC.inboxPocNavTitle();
            }
        },
    ADMIN("admin.jsp", "Administration", "Manage the system")
        {
            @Override
            public String getDisplayName()
            {
                return BC.adminNavTitle();
            }
        },
    HELP("help.jsp", "Help", "Help")
        {
            @Override
            public String getDisplayName()
            {
                return BC.helpNavTitle();
            }
        };
    private static final BaseViewConstants BC = I18NFactoryProvider.get(BaseViewConstants.class);
    private String jsp;
    private String name;
    private String description;
    private boolean showModuleNavigation;
    private boolean enableModuleNavigation;

    /**
     * each IglooitEntryPoint entity should match the upper(name) in module.properties file
     * since it is used in Help module
     * eg.   TICKETMANAGER("ticketinstance.jsp", "Clarity Inbox", "")
     * TICKETMANAGER = upper(ticketmanager)
     * in module.properties ticketmanager=classpath:spring/applicationContext-ticketmanager.xml
     *
     * @param jsp
     * @param name
     * @param description
     */
    IglooitEntryPoint(String jsp, String name, String description)
    {
        this(jsp, name, description, true);
    }

    IglooitEntryPoint(String jsp, String name, String description, boolean showModuleNavigation)
    {
        this(jsp, name, description, showModuleNavigation, true);
    }

    IglooitEntryPoint(String jsp, String name, String description,
                      boolean showModuleNavigation, boolean enableModuleNavigation)
    {
        this.jsp = jsp;
        this.name = name;
        this.description = description;
        this.showModuleNavigation = showModuleNavigation;
        this.enableModuleNavigation = enableModuleNavigation;
    }

    //    public String getHtmlLink(boolean enabled, boolean isSelected)
//    {
//        return "<a href=\"#\" title=\"" + description + "\" onclick=\"" +
//            (enabled && !isSelected ? "window.open('" + jsp + "','" +
//                name.replaceAll(" ", "_") + "','').focus();return false;" : "return false;") + "\"" +
//            (!enabled ? "class='disabled'" : "") + ">" + getDisplayName() + "</a>" +
//            (isSelected ? "<div class='tip-arrow-border'></div><div class='tip-arrow'></div>" : "");
//
//    }
//
    public String getDescription()
    {
        return description;
    }

    public String getJsp()
    {
        return jsp;
    }

    public String getName()
    {
        return name;
    }

    public abstract String getDisplayName();

    public boolean isHelp()
    {
        return this == HELP;
    }

    public boolean isShowModuleNavigation()
    {
        return showModuleNavigation;
    }

    public boolean isEnableModuleNavigation()
    {
        return enableModuleNavigation;
    }
}


