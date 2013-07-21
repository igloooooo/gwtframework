package com.iglooit.core.base.client.widget;

/**
 * Secure interface for sub-classes of com.extjs.gxt.ui.client.widget.Component
 */
public interface SecuredWidget
{
    /**
     * Same as com.extjs.gxt.ui.client.widget.Component.addStyleName
     */
    void addStyleName(String style);

    /**
     * Same as com.extjs.gxt.ui.client.widget.Component.removeStyleName
     */
    void removeStyleName(String style);

    /**
     * Same as com.extjs.gxt.ui.client.widget.Component.removeStyleName
     */
    void setEnabled(boolean enabled);

    /**
     * This should call the setEnabled method of the super class of the component
     */
    void superSetEnabled(boolean enabled);

    /**
     * This should call the isRendered method of the super class of the component
     */
    boolean superIsRendered();

}
