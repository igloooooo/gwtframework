package com.iglooit.core.base.client.widget;

/**
 * This class define all our customized widget flag that used in:
 * 1. widget js template
 * 2. java logic control around widget
 */
public interface ClarityWidgetFlag
{
    /*
    * Selection widgets:
    * - combo box -
    * */
    String NON_SELECTABLE_ITEM_FLAG = "clarityWidgetNonSelectable";
    /* this can be selectable in some cases */
    String NON_SELECTABLE_ITEM_FLAG_MORE = "clarityWidgetNonSelectableMore";

}
