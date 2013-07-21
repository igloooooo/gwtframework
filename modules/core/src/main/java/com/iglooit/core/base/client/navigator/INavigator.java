package com.iglooit.core.base.client.navigator;

import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Frame;

import java.util.Iterator;
import java.util.Map;

public interface INavigator extends ValueChangeHandler<String>
{
    String QUERY_PARAM_GWT_CODESVR = "gwt.codesvr";

    void addToBreadcrumbs(SimpleBreadcrumb breadcrumb);

    void clearBreadcrumbs();

    void addToBrowserHistory(PageRequestParams pageRequestParams);

    void goTo(PageRequestParams pageRequestParams);

    void goTo(String path, PageRequestParams pageRequestParams);

    void goTo(String relativePath, Map<String, String> urlParams, PageRequestParams pageRequestParams);

    void goTo(int breadcrumbsIndex);

    void openUrl(String url);

    void openNew(String relativePath, String feature);

    void openNew(String relativePath, PageRequestParams pageRequestParams);

    void openNew(String relativePath, PageRequestParams pageRequestParams, String feature);

    /**
     * return false if a hash is found in previous opened tab map
     *
     * @param relativePath
     * @param pageRequestParams
     * @return
     */
    boolean openNewAndCacheHash(String relativePath, PageRequestParams pageRequestParams);

    Frame createFrame(String iFrameId, String relativePath, PageRequestParams pageRequestParams);

    /**
     * Useful for static pages only, ie. it won't run JavaScript esp. GWT).
     */
    HtmlContainer createHtmlContainer(String iFrameId, String relativePath, PageRequestParams pageRequestParams);

    /**
     * Avoid using this method. In general, use a goTo or openNew* method instead.
     */
    String toUrl(PageRequestParams pageRequestParams);

    /**
     * Avoid using this method. In general, use a goTo or openNew* method instead.
     */
    String toUrl(String relativePath, PageRequestParams pageRequestParams);

    /**
     * Avoid using this method. In general, use a goTo or openNew* method instead
     */
    String toUrl(String url, PageRequestParams pageRequestParams, Map<String, String> urlParamsByName);

    void back();

    void fireCurrentHistoryState();

    void forward();

    String getHistoryToken();

    void addActionHandler(String actionName, ValueChangeHandler<String> handler);

    void setDefaultActionHandler(ValueChangeHandler<String> handler);

    @Override
    void onValueChange(ValueChangeEvent<String> event);

    PageRequestParams getActionPortionOfPageRequest();

    // Not all implementations will support this method
    String getBreadcrumbsPortionOfPageRequest();

    int getBreadcrumbsSize();

    Iterator<SimpleBreadcrumb> getBreadcrumbsIterator();
}
