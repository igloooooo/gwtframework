package com.iglooit.core.base.client.navigator;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO This is temporary, until we figure out a solution that allows customer to add their own modules/entry-points.
 */
public final class EntryPoints
{
    public static final String ID_INBOX = "inbox";
    public static final String ID_FAULT_INSTANCE = "fault";
    public static final String ID_TICKET_INSTANCE = "ticket";
    public static final String ID_PERFORMANCE_DIAGNOSE_INSTANCE = "bqmdiag";
    public static final String ID_VISUALIZATION_INSTANCE = "vis";
    public static final String ID_WORK_ORDER_INSTANCE = "workorder";
    public static final String ID_ORDER_INSTANCE = "order";
    public static final String ID_GENERIC_CALENDAR = "genericcalendar";
    
    private static Map<String, String> pathsByEntryPointId;

    static
    {
        pathsByEntryPointId = new HashMap<String, String>();

        pathsByEntryPointId.put(ID_INBOX, "inbox.jsp");
        pathsByEntryPointId.put(ID_TICKET_INSTANCE, "ticketinstance.jsp");
        pathsByEntryPointId.put(ID_FAULT_INSTANCE, "faultinstance.jsp");
        pathsByEntryPointId.put(ID_PERFORMANCE_DIAGNOSE_INSTANCE, "bqmdiag.jsp");
        pathsByEntryPointId.put(ID_VISUALIZATION_INSTANCE, "visualisation.jsp");
        pathsByEntryPointId.put(ID_WORK_ORDER_INSTANCE, "workorderinstance.jsp");
        pathsByEntryPointId.put(ID_ORDER_INSTANCE, "orderinstance.jsp");
        pathsByEntryPointId.put(ID_GENERIC_CALENDAR, "genericcalendar.jsp");
    }

    public static String getPath(String entryPointId)
    {
        for (Map.Entry<String, String> entry : pathsByEntryPointId.entrySet())
        {
            if (entry.getKey().equals(entryPointId))
            {
                return entry.getValue();
            }
        }

        return null;
    }
}
