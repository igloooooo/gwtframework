package com.iglooit.core.base.iface.domain;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.base.client.view.BaseViewConstants;

public enum ClarityEntryPoint
{
    //has to be in this order as per Duncan's comment in TFT-551
    INBOXPOC("inboxpoc.jsp", "Inbox", "Manage assigned tasks")
        {
            @Override
            public String getDisplayName()
            {
                return BC.inboxPocNavTitle();
            }
        },
    INBOX("inbox.jsp", "Clarity Inbox", "A central Inbox for Clarity activities, tasks and tickets")
        {
            @Override
            public String getDisplayName()
            {
                return BC.inboxNavTitle();
            }
        },
    FORMS("forms.jsp", "Forms", "Manage inventory")
        {
            @Override
            public String getDisplayName()
            {
                return BC.formsNavTitle();
            }
        },
    VISUALISATION("visualisation.jsp", "Visualisation", "Browse inventory by visualisation")
        {
            @Override
            public String getDisplayName()
            {
                return BC.visualisationNavTitle();
            }
        },
    ALARMS("alarms.jsp", "Alarms", "Manage Alarms")
        {
            @Override
            public String getDisplayName()
            {
                return BC.alarmsNavTitle();
            }
        },
    FSM("fsm.jsp", "Field Service Manager", "Manage human resources and field work")
        {
            @Override
            public String getDisplayName()
            {
                return BC.fsmNavTitle();
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
    SCHEDULER("scheduler.jsp", "Scheduler", "Schedule tasks")
        {
            @Override
            public String getDisplayName()
            {
                return BC.schedulerNavTitle();
            }
        },
    DISCOVERY("discovery.jsp", "Discovery", "Discover discrepancies between inventory and the network")
        {
            @Override
            public String getDisplayName()
            {
                return BC.discoveryNavTitle();
            }
        },
    HELP("help.jsp", "Help", "User guides")
        {
            @Override
            public String getDisplayName()
            {
                return BC.helpNavTitle();
            }
        },
    ACCOUNT_MANAGER("account.jsp", "Account Manager", "Entry point for new orders")
        {
            @Override
            public String getDisplayName()
            {
                return getDescription();
            }
        },
    CUSTOMER_MANAGEMENT("customermanager.jsp", "Customer Manager", "Customer Manager", true, false)
        {
            @Override
            public String getDisplayName()
            {
                return getDescription();
            }
        },
    HEALTH("health.jsp", "Clarity Health", "Clarity Health")
        {
            @Override
            public String getDisplayName()
            {
                return getDescription();
            }
        },
    PERFORMANCE("performance.jsp", "Performance Manager", "Performance Manager")
        {
            @Override
            public String getDisplayName()
            {
                return getDescription();
            }
        },
    BQM("bqm.jsp", "Business Quality Manager", "Business Quality Manager")
        {
            @Override
            public String getDisplayName()
            {
                return getDescription();
            }
        },
    NETWORK_CONFIGURATION("networkconfig.jsp", "Network Configuration", "Network Configuration CLI")
        {
            @Override
            public String getDisplayName()
            {
                return BC.networkConfigurationNavTitle();
            }
        },
    REPORT_CONNECT("reportconnect.jsp", "Report Connect", "Report Connect", true, false)
        {
            @Override
            public String getDisplayName()
            {
                return BC.reportConnect();
            }
        },
    BQM_DIAGNOSE("bqmdiag.jsp", "BQM Diagnose", "BQM Diagnose")
        {
            @Override
            public String getDisplayName()
            {
                return BC.performanceDiagnose();
            }
        },
    WORKORDER("workorder.jsp", "Work Order", "Work Order", false)
        {
            @Override
            public String getDisplayName()
            {
                return BC.workOrder();
            }
        },
    CALENDAR_EVENT("calendar.jsp", "Calendar", "Calendar", false)
        {
            @Override
            public String getDisplayName()
            {
                return BC.calendarEventNavTitle();
            }
        },
    FAULTMANAGER("faultinstance.jsp", "Fault Manager", "Fault Manager")
        {
            @Override
            public String getDisplayName()
            {
                return BC.faultmanagerNavTitle();
            }
        },
    MARKETPLACE("unifiedcatalog.jsp", "Unified Catalog", "Unified Catalog")
        {
            @Override
            public String getDisplayName()
            {
                return (BC.unifiedCatalogNavTitle());
            }
        },
    ORDERMANAGER("ordermanager.jsp", "Order Manager", "Order Manager", true, false)
        {
            @Override
            public String getDisplayName()
            {
                return (BC.orderManagerNavTitle());
            }
        },
    TICKETMANAGER("ticketmanager.jsp", "Ticket Manager", "Ticket Manager", true, false)
        {
            @Override
            public String getDisplayName()
            {
                return (BC.ticketManagerNavTitle());
            }
        },
    TESTMANAGER("testmanager.jsp", "Test Manager", "Test Manager")
        {
            @Override
            public String getDisplayName()
            {
                return BC.testManagerNavTitle();
            }
        };

    private String jsp;
    private String name;
    private String description;
    private boolean showModuleNavigation;
    private boolean enableModuleNavigation;
    private static final BaseViewConstants BC = I18NFactoryProvider.get(BaseViewConstants.class);

    /**
     * each ClarityEntryPoint entity should match the upper(name) in module.properties file
     * since it is used in Help module
     * eg.   TICKETMANAGER("ticketinstance.jsp", "Clarity Inbox", "")
     * TICKETMANAGER = upper(ticketmanager)
     * in module.properties ticketmanager=classpath:spring/applicationContext-ticketmanager.xml
     *
     * @param jsp
     * @param name
     * @param description
     */
    ClarityEntryPoint(String jsp, String name, String description)
    {
        this(jsp, name, description, true);
    }

    ClarityEntryPoint(String jsp, String name, String description, boolean showModuleNavigation)
    {
        this(jsp, name, description, showModuleNavigation, true);
    }

    ClarityEntryPoint(String jsp, String name, String description,
                      boolean showModuleNavigation, boolean enableModuleNavigation)
    {
        this.jsp = jsp;
        this.name = name;
        this.description = description;
        this.showModuleNavigation = showModuleNavigation;
        this.enableModuleNavigation = enableModuleNavigation;
    }

    public String getHtmlLink(boolean enabled, boolean isSelected)
    {
        return "<a href=\"#\" title=\"" + description + "\" onclick=\"" +
            (enabled && !isSelected ? "window.open('" + jsp + "','" +
                name.replaceAll(" ", "_") + "','').focus();return false;" : "return false;") + "\"" +
            (!enabled ? "class='disabled'" : "") + ">" + getDisplayName() + "</a>" +
            (isSelected ? "<div class='tip-arrow-border'></div><div class='tip-arrow'></div>" : "");

    }

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


