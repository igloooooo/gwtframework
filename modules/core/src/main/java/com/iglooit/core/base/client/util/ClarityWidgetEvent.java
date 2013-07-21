package com.iglooit.core.base.client.util;

import com.extjs.gxt.ui.client.event.EventType;

/**
 * All event number must be not as 2 to the power x, which are default gxt events. e.g. 1,2,4,8,16,32...
 *
 * We here start from 301 onwards, and +2 for next to make it a odd number
 */
public class ClarityWidgetEvent
{
    /* for combo box ...More item selection event */
    private static final Integer MORE_SELECTION_EVENT_INT = 301;
    public static final EventType MORE_SELECTION_EVENT_TYPE = new EventType(MORE_SELECTION_EVENT_INT);
}
