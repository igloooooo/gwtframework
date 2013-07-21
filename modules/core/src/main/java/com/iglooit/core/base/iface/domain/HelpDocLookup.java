package com.iglooit.core.base.iface.domain;

import java.util.HashMap;
import java.util.Map;

public class HelpDocLookup
{
    private static final Map<ClarityEntryPoint, String> HELP_DOC_MAP = new HashMap();

    static
    {

        HELP_DOC_MAP.put(ClarityEntryPoint.ADMIN, "help/admin.pdf");
        HELP_DOC_MAP.put(ClarityEntryPoint.SCHEDULER, "help/scheduler.pdf");
        HELP_DOC_MAP.put(ClarityEntryPoint.REPORT_CONNECT, "help/report_connect.pdf");

        HELP_DOC_MAP.put(ClarityEntryPoint.MARKETPLACE, "help/unified_catalog.pdf");
        HELP_DOC_MAP.put(ClarityEntryPoint.ORDERMANAGER, "help/order_manager.pdf");
        //TODO: put the right URL for customer Manager User Guide
        HELP_DOC_MAP.put(ClarityEntryPoint.CUSTOMER_MANAGEMENT, "help/order_manager.pdf");

        HELP_DOC_MAP.put(ClarityEntryPoint.HEALTH, "help/self_monitoring.pdf");
        HELP_DOC_MAP.put(ClarityEntryPoint.ALARMS, "help/alarms.pdf");
        HELP_DOC_MAP.put(ClarityEntryPoint.PERFORMANCE, "help/performance.pdf");
        HELP_DOC_MAP.put(ClarityEntryPoint.FAULTMANAGER, "help/fault_manager.pdf");
        HELP_DOC_MAP.put(ClarityEntryPoint.TICKETMANAGER, "help/ticket_manager.pdf");

        HELP_DOC_MAP.put(ClarityEntryPoint.DISCOVERY, "help/discovery.pdf");
        HELP_DOC_MAP.put(ClarityEntryPoint.NETWORK_CONFIGURATION, "help/network_configuration.pdf");
        HELP_DOC_MAP.put(ClarityEntryPoint.VISUALISATION, "help/visualisation.pdf");
        HELP_DOC_MAP.put(ClarityEntryPoint.CALENDAR_EVENT, "help/calendar_event.pdf");

    }

    public static String getDocUrl(ClarityEntryPoint entryPoint)
    {
        if (HELP_DOC_MAP.containsKey(entryPoint))
        {
            return HELP_DOC_MAP.get(entryPoint);
        }
        else
        {
            return entryPoint.name().toLowerCase() + ".pdf";
        }
    }
}
