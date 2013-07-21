package com.iglooit.core.base.client.navigator;

public final class BreadcrumbsInUrlConstants
{
    public static final String BREADCRUMBS_SEPARATOR = PageRequestParams.RESERVED_FOR_BREADCRUMBS_SEPARATOR;

    public static final String ACTION_PARAMS_SEPARATOR = PageRequestParams.PARAMS_SEPARATOR;
    public static final String ACTION_PARAM_NAME_VALUE_SEPARATOR = PageRequestParams.NAME_VALUE_SEPARATOR;

    // These are used often in actions (and therefore in breadcrumbs)

    public static final String PARAM_NAME_ACTION = PageRequestParamNames.ACTION;
    public static final String PARAM_NAME_ID = PageRequestParamNames.ID;

    // These are not used in actions but are mandatory in breadcrumbs

    public static final String PARAM_NAME_ENTRY_POINT = "ep";
    public static final String PARAM_NAME_BREADCRUMB_LABEL_KEY = "bc";
}
