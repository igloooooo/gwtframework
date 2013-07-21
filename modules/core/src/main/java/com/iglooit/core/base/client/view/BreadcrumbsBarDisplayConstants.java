package com.iglooit.core.base.client.view;

import com.google.gwt.i18n.client.ConstantsWithLookup;

import java.io.Serializable;

//
// TODO A temporary solution, to be replaced when we have a mechanism that allows customers to add custom
// entry-points.
//
public interface BreadcrumbsBarDisplayConstants extends ConstantsWithLookup, Serializable
{
    String backButtonTooltip();

    String inbox();

    String fault();

    String ticket();

    String order();

    String workorder();
}
