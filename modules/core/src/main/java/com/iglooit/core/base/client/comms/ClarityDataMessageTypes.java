package com.iglooit.core.base.client.comms;

public enum ClarityDataMessageTypes
{
    // Internal event types

    REGISTER_PARTICIPANT,

    // From child to parent

    MODULE_READY,
    SET_HEADING,
    UNMASK_ME,
    TEST_MESSAGE,
    REQUEST_COMPLETE_INDICATOR,

    INNER_PROXY_READY,

    REVEAL_PROVIDER_IN_PARENT_EVENT,
    PROVIDER_DISPLAY_IS_READY_EVENT,
    PROVIDER_VIEW_ITEM_EVENT,

    LOAD_ORACLE_FORM,

    LOAD_OBJECT_BY_ID,
    CLOSE_ME,
    UPDATE_VIEW,

    UPDATE_INDICATOR,
    UPDATE_TITLE,
    PROCESS_CREATED,

    UPDATE_SIZE,

    CREATE_FAULT,

    // inbox provider
    FILTER_CONTENT_READY,
    FILTER_PARAMETER,
    FILTER_CONTENT,
    FILTER_ACTION,

    // From parent to child

    SET_INITIAL_STATE,
    REVEAL_DISPLAY,
    GET_STATE_DEEP,

    // launch oracle form from OSV
    LAUNCH_ORACLE_FORM;
}

