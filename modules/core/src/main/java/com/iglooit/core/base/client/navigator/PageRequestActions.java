package com.iglooit.core.base.client.navigator;

/**
 * This class exists to help us spot potential conflicts of action names.
 */
public class PageRequestActions
{
    // Action names
    public static final String FAULT_DISPLAY = "FaultDisplay";
    public static final String FAULT_EDIT = "FaultEdit";
    public static final String TICKET_DISPLAY = "TicketDisplay";
    public static final String ORDER_DISPLAY = "OrderDisplay";
    public static final String WORK_ORDER_DISPLAY = "WODisplay";
    public static final String WORK_FLOW_DISPLAY = "SODisplay";
    public static final String WORK_FLOW_TASK_DISPLAY = "TaskDisplay";
    public static final String CREATE_WORK_ORDER = "CreateWO";
    public static final String CREATE_WORK_FLOW = "CreateWF";
    public static final String NAVIGATE_TO_ACTIVE_WO = "NavActiveWO";
    // inbox filter
    public static final String FAULT_INBOX_FILTER_ASSIGN_TO_MY_WG = "FaultFilterWG";
    public static final String FAULT_INBOX_FILTER_SAVED_SEARCH = "FaultFilterSS";
    public static final String FAULT_INBOX_FILTER_SEARCH = "FaultFilterS";
    public static final String TICKET_INBOX_FILTER_ASSIGN_TO_MY_WG = "TicketFilterWG";
    public static final String TICKET_INBOX_FILTER_ASSIGN_TO_ME = "TicketFilterME";
    public static final String TICKET_INBOX_FILTER_SEARCH = "TicketFilterS";
    public static final String ACTIVITY_INBOX_FILTER_ASSIGN_TO_ME = "ActivityFilterME";
    public static final String ACTIVITY_INBOX_FILTER_ASSIGN_TO_MY_WG = "ActivityFilterMyWG";
    public static final String ACTIVITY_INBOX_FILTER_SEARCH = "ActivityFilterS";
    public static final String ORDER_INBOX_FILTER_ASSIGN_TO_ME = "OrderFilterME";
    public static final String ORDER_INBOX_FILTER_ASSIGN_TO_MY_WG = "OrderFilterWG";
    public static final String ORDER_INBOX_FILTER_SEARCH = "OrderFilterS";
}
