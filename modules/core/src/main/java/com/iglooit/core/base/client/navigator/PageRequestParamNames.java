package com.iglooit.core.base.client.navigator;

/**
 * This class exists to help us spot potential conflicts of parameter names before they end up in the same
 * PageRequestParams.
 * If using breadcrumbs (see IBreadcrumbs), a parameter name has to be unique within a breadcrumb.
 */
public class PageRequestParamNames
{
    // General

    public static final String ACTION = "ac";
    public static final String ID = "id";
    public static final String DISPLAY_NAME = "dn";
    public static final String ITEM = "it";
    public static final String VIEW = "vw";
    public static final String PAGE = "pg";
    public static final String LOAD_WITHOUT_UPDATING = "lwu";
    // Shorten this name?
    public static final String OBJECT_TYPE = "objectType";
    // Inbox
    public static final String INBOX_PROVIDER_GROUP_PUBLIC_ID = "gr";
    public static final String INBOX_PROVIDER_PUBLIC_ID = "pr";
    //Inbox Provider: Tickets Assigned to me
    public static final String TME_WORK_GROUPS = "tmewg";
    public static final String TME_STATUSES = "tmest";
    public static final String TME_PRIORITIES = "tmepy";
    public static final String TME_PAGE = "tmepg";
    public static final String TME_ITEM = "tmeit";
    //Inbox Provider: Tickets Assigned to my work groups
    public static final String TMWG_WORK_GROUPS = "tmwgwg";
    public static final String TMWG_STATUSES = "tmwgst";
    public static final String TMWG_PRIORITIES = "tmwgpy";
    public static final String TMWG_PAGE = "tmwgpg";
    public static final String TMWG_ITEM = "tmwgit";

    // Inbox Provider: Faults Assigned to Me
    public static final String FM_PAGE = "fmpg";
    public static final String FM_ITEM = "fmit";

    // Inbox Provider: Faults Assigned to My WorkGroup
    public static final String FW_WORK_GROUPS = "fwwg";
    public static final String FW_STATUSES = "fwst";
    public static final String FW_PRIORITIES = "fwpy";
    public static final String FW_INCL_CHILD = "fwic";
    public static final String FW_PAGE = "fwpg";
    public static final String FW_ITEM = "fwit";

    // Inbox Provider: Faults Search
    public static final String FS_FILTER_TYPE = "fsfl";
    public static final String FS_FAULT_ID = "fsfi";
    public static final String FS_DESCRIPTION = "fsds";
    public static final String FS_INCL_KNOWLEDGE_BASE = "fsik";
    public static final String FS_FAULT_TYPES = "fsft";
    public static final String FS_PRIORITIES = "fspy";
    public static final String FS_WORK_GROUPS = "fswg";
    public static final String FS_STATUSES = "fsst";
    public static final String FS_ADVANCED_QUERY = "fsaq";
    public static final String FS_RELATED_ITEM_TYPE = "fsrt";
    public static final String FS_RELATED_ITEM_IDS = "fsri";
    public static final String FS_PAGE = "fspg";
    public static final String FS_ITEM = "fsit";

    // Inbox Provider: Faults Saved Search
    public static final String FQ_VIEW_DEF_ID = "fqvd";
    public static final String FQ_FILTER_TYPE = "fqfl";
    public static final String FQ_FAULT_ID = "fqfi";
    public static final String FQ_DESCRIPTION = "fqds";
    public static final String FQ_INCL_KNOWLEDGE_BASE = "fqik";
    public static final String FQ_FAULT_TYPES = "fqft";
    public static final String FQ_PRIORITIES = "fqpy";
    public static final String FQ_WORK_GROUPS = "fqwg";
    public static final String FQ_STATUSES = "fqst";
    public static final String FQ_ADVANCED_QUERY = "fqaq";
    public static final String FQ_RELATED_ITEM_TYPE = "fqrt";
    public static final String FQ_RELATED_ITEM_IDS = "fqri";
    public static final String FQ_PAGE = "fqpg";
    public static final String FQ_ITEM = "fqit";
    public static final String FQ_IS_EDITING = "fqed";

    // Inbox Provider: Tickets Assigned to Me
    public static final String TM_PAGE = "tmpg";
    public static final String TM_ITEM = "tmit";

    // Inbox Provider: Tickets Assigned to My WorkGroup
    public static final String TW_PAGE = "twpg";
    public static final String TW_ITEM = "twit";

    // Inbox Provider: Tickets Search
    public static final String TS_PAGE = "tspg";
    public static final String TS_ITEM = "tsit";
    public static final String TS_FILTER = "tsft";

    // Inbox Provider: Work Flows Assigned to Me
    public static final String WFM_PAGE = "wfmpg";
    public static final String WFM_ITEM = "wfmit";

    // Inbox Provider: Activity Assigned to My WG
    public static final String AMWG_PAGE = "amwgpg";
    public static final String AMWG_ITEM = "amwgit";
    public static final String AMWG_WORK_GROUPS = "amwgwg";
    public static final String AMWG_STATUSES = "amwgst";
    public static final String AMWG_NAMES = "amwgn";

    // Inbox Provider: Work Flows Search
    public static final String WFS_PAGE = "wsfpg";
    public static final String WFS_ITEM = "wfsit";
    public static final String WFS_FILTER = "wfsft";

    // Inbox Provider: Work Orders Assigned to Me
    public static final String WM_PAGE = "wmpg";
    public static final String WM_ITEM = "wmit";

    // Inbox Provider: Work Orders Assigned to My WorkGroup
    public static final String WW_PAGE = "wwpg";
    public static final String WW_ITEM = "wwit";

    // Inbox Provider: Work Orders Search
    public static final String WS_PAGE = "wspg";
    public static final String WS_ITEM = "wsit";

    //************************************ Order Manager *****************************************************

    // Inbox Provider: Orders Manager Assigned to Me
    public static final String OM_WORK_GROUPS = "omwg";
    public static final String OM_STATES = "omst";
    public static final String OM_TYPES = "omty";
    public static final String OM_PAGE = "ompg";
    public static final String OM_ITEM = "omit";

    // Inbox Provider: Orders Manager Assigned to My WorkGroup
    public static final String OW_WORK_GROUPS = "owwg";
    public static final String OW_STATES = "owst";
    public static final String OW_TYPES = "owty";
    public static final String OW_PAGE = "owpg";
    public static final String OW_ITEM = "owit";

    // Inbox Provider: Orders manager Search
    public static final String OS_PAGE = "ospg";
    public static final String OS_ITEM = "osit";

    // Fault Instance
    public static final String COMMENTS_FILTER = "cmfi";
    public static final String RELATED_ITEMS_FILTER = "rifi";
    public static final String RELATED_ITEMS_PAGE = "ripg";
    public static final String RELATED_ITEM = "riit";
    public static final String HISTORY_FILTER = "hsfi";
}
