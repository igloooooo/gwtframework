package com.iglooit.core.base.client.navigator;

import com.clarity.core.base.client.comms.UniversalCommsService;
import com.clarity.core.lib.client.UniversalHandlerManager;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This implementation of INavigator stores breadcrumbs in the URL fragment identifier.
 */
public final class NavigatorWithBreadcrumbsInUrl implements INavigator
{
    private IBreadcrumbs breadcrumbs;

    private ValueChangeHandler<String> defaultHandler;

    // Do not replace this list with a map - there can be more than one handler for a given actionName.
    private List<ActionHandler> actionHandlers = new ArrayList<ActionHandler>();

    @Inject
    public NavigatorWithBreadcrumbsInUrl(IBreadcrumbs breadcrumbs)
    {
        this.breadcrumbs = breadcrumbs;
        History.addValueChangeHandler(this);
    }

    @Override
    public void addToBrowserHistory(PageRequestParams pageRequestParams)
    {
        History.newItem(toFragmentIdentifierWithBreadcrumbs(pageRequestParams), false);
    }

    /**
     * Directs the browser to a new URL which it builds by getting the current URL and modifying its
     * fragment identifier by moving its current page request parameters into the "breadcrumb" portion and inserting
     * the given page request parameters into the "current" portion.
     * <p/>
     * For example, if the current URL is this...
     * <pre>
     * "http://abc.com:8080/faults.jsp?codeserver=10.10.0.5&xyz#subset=today"
     * </pre>
     * ...then calling this method with pageRequestParams [{"ac", "FaultDisplay"}, {"id", 1234}],
     * will direct the browser to this...
     * <pre>
     * "http://abc.com:8080/faults.jsp?codeserver=10.10.0.5&xyz#ac=FaultDisplay;id=1234$subset=today"
     * </pre>
     *
     * @param pageRequestParams For example, containing [{"ac", "FaultDisplay"}, {"id", 1234}].
     */
    @Override
    public void goTo(PageRequestParams pageRequestParams)
    {
        History.newItem(toFragmentIdentifierWithBreadcrumbs(pageRequestParams), true);
    }

    /**
     * Directs the browser to a new URL which it builds by getting the current URL and then:
     * <ul>
     * <li>it replaces the URL's path (for example, "faults.jsp") with the given path</li>
     * <li>it removes the URL's query parameters except "codeserver"</li>
     * <li>it modifies the URL's fragment identifier by moving its current page request parameters into the "breadcrumb"
     * portion and inserting the given page request parameters into the "current" portion</li>
     * </ul>
     * For example, if the current URL is this...
     * <pre>
     * "http://abc.com:8080/faults.jsp?codeserver=10.10.0.5&xyz#subset=today"
     * </pre>
     * ...then calling this method with relativePath of "faultinstance.jsp" and pageRequestParams of
     * [{"ac", "FaultDisplay"}, {"id", 1234}], will direct the browser to this...
     * <pre>
     * "http://abc.com:8080/faultinstance.jsp?codeserver=10.10.0.5#ac=FaultDisplay;id=1234$subset=today"
     * </pre>
     *
     * @param relativePath      A path relative to the web app context. For a GWT page this will be an entry point.
     *                          For example, "faultinstance.jsp".
     * @param pageRequestParams For example, containing [{"ac", "FaultDisplay"}, {"id", 1234}].
     */
    @Override
    public void goTo(String relativePath, PageRequestParams pageRequestParams)
    {
        String path = calculatePath(relativePath);
        String currentPath = Window.Location.getPath();

        // Remove leading slash if there is one.

        if (currentPath.startsWith("/"))
        {
            currentPath = currentPath.substring(1);
        }

        // If staying in same entry point

        if (path.equals(currentPath))
        {
            goTo(pageRequestParams);
        }

        // Else, send the window to the new entry point

        else
        {
            Window.Location.assign(calculateUrl(path, null, pageRequestParams, true));
        }
    }

    /**
     * @param relativePath
     * @param urlParams         For example, urlBuilder.setParameter("task", "visualise");
     * @param pageRequestParams
     */
    @Override
    public void goTo(String relativePath, Map<String, String> urlParams, PageRequestParams pageRequestParams)
    {
        String path = calculatePath(relativePath);
        String currentPath = Window.Location.getPath();

        // Remove leading slash if there is one.

        if (currentPath.startsWith("/"))
        {
            currentPath = currentPath.substring(1);
        }

        // If staying in same entry point

        if (path.equals(currentPath))
        {
            goTo(pageRequestParams);
        }

        // Else, send the window to the new entry point

        else
        {
            Window.Location.assign(calculateUrl(path, urlParams, pageRequestParams, true));
        }
    }

    @Override
    public void goTo(int breadcrumbsIndex)
    {
        // Truncate the breadcrumbs (removes the chosen breadcrumb and its successors).

        SimpleBreadcrumb breadcrumb = breadcrumbs.truncate(breadcrumbsIndex);

        // Go to wherever the breadcrumb is pointing!

        String path = EntryPoints.getPath(breadcrumb.getEntryPointId());
        goTo(path, breadcrumb.getPageRequestParams());
    }

    /**
     * Opens a new window with a given absolute or relative URL.
     * <p/>
     *
     * @param url
     */
    @Override
    public void openUrl(String url)
    {
        Window.open(url, "_blank", "");
    }

    /**
     * To open a new window with a given relative URL
     * it will figure out the absolute URL for both dev environment and server
     * it does not support PageRequestParams
     *
     * @param relativePath
     */
    @Override
    public void openNew(String relativePath, String feature)
    {
        Window.open(calculateUrl(calculatePath(relativePath), null, null, true), "_blank", feature);
    }


    /**
     * Opens a new window with a new URL which it builds by getting the current URL and then:
     * <ul>
     * <li>it replaces the URL's path (for example, "faults.jsp") with the given path</li>
     * <li>it removes the URL's query parameters except "codeserver"</li>
     * <li>it modifies the URL's fragment identifier by moving its current page request parameters into the "breadcrumb"
     * portion and inserting the given page request parameters into the "current" portion</li>
     * </ul>
     * For example, if the current URL is this...
     * <pre>
     * "http://abc.com:8080/faults.jsp?codeserver=10.10.0.5&xyz#subset=today"
     * </pre>
     * ...then calling this method with relativePath of "faultinstance.jsp" and pageRequestParams of
     * [{"ac", "FaultDisplay"}, {"id", 1234}], will direct the browser to this...
     * <pre>
     * "http://abc.com:8080/faultinstance.jsp?codeserver=10.10.0.5#ac=FaultDisplay;id=1234$subset=today"
     * </pre>
     *
     * @param relativePath      A path relative to the web app context. For a GWT page this will be an entry point.
     *                          For example, "faultinstance.jsp".
     * @param pageRequestParams For example, containing [{"ac", "FaultDisplay"}, {"id", 1234}].
     */
    @Override
    public void openNew(String relativePath, PageRequestParams pageRequestParams)
    {
        Window.open(calculateUrl(calculatePath(relativePath), null, pageRequestParams, false), "_blank", "");
    }

    /**
     * Opens a new window with a new URL which it builds by getting the current URL and then:
     * <ul>
     * <li>it replaces the URL's path (for example, "faults.jsp") with the given path</li>
     * <li>it removes the URL's query parameters except "codeserver"</li>
     * <li>it modifies the URL's fragment identifier by moving its current page request parameters into the "breadcrumb"
     * portion and inserting the given page request parameters into the "current" portion</li>
     * </ul>
     * For example, if the current URL is this:
     * <pre>
     * "http://abc.com:8080/faults.jsp?codeserver=10.10.0.5&xyz#subset=today"
     * </pre>
     * then calling this method with relativePath of "faultinstance.jsp" and pageRequestParams of
     * [{"ac", "FaultDisplay"}, {"id", 1234}], will direct the browser to this:
     * <pre>
     * "http://abc.com:8080/faultinstance.jsp?codeserver=10.10.0.5#ac=FaultDisplay;id=1234$subset=today"
     * </pre>
     *
     * @param relativePath      A path relative to the web app context. For a GWT page this will be an entry point.
     *                          For example, "faultinstance.jsp".
     * @param pageRequestParams For example, containing [{"ac", "FaultDisplay"}, {"id", 1234}].
     * @param feature           See features of
     *                          <a href='http://developer.mozilla.org/en/docs/DOM:window.open'>window.open</a>.
     */
    @Override
    public void openNew(String relativePath, PageRequestParams pageRequestParams, String feature)
    {
        Window.open(calculateUrl(calculatePath(relativePath), null, pageRequestParams, false), "_blank", feature);
    }

    @Override
    public boolean openNewAndCacheHash(String relativePath, PageRequestParams pageRequestParams)
    {
        // todo: not support yet, look at simple navigator for implementation
        throw new UnsupportedOperationException();
    }

    public Frame createFrame(String iFrameId, String relativePath, PageRequestParams pageRequestParams)
    {
        // TODO - explain why we're adding these urlParams. It's to do with UniversalCommsService.
        Map<String, String> urlParams = new HashMap();
        urlParams.put(UniversalCommsService.QUERY_PARAM_PARTICIPANT_ID, iFrameId);

        String url = calculateUrl(calculatePath(relativePath), urlParams, pageRequestParams, true);
        return new Frame(url);
    }

    public HtmlContainer createHtmlContainer(String iFrameId, String relativePath, PageRequestParams pageRequestParams)
    {
        // TODO - explain why we're adding these urlParams. It's to do with UniversalCommsService.
        Map<String, String> urlParams = new HashMap();
        urlParams.put(UniversalCommsService.QUERY_PARAM_PARTICIPANT_ID, iFrameId);

        String url = calculateUrl(calculatePath(relativePath), urlParams, pageRequestParams, true);
        HtmlContainer htmlContainer = new HtmlContainer();
        htmlContainer.setUrl(url);
        return htmlContainer;
    }

    /**
     * Avoid using this method. In general, use a goTo or openNew* method instead.
     */
    @Override
    public String toUrl(PageRequestParams pageRequestParams)
    {
        String currentPath = Window.Location.getPath();

        // Remove leading slash if there is one.
        if (currentPath.startsWith("/"))
        {
            currentPath = currentPath.substring(1);
        }

        return calculateUrl(currentPath, null, pageRequestParams, false);
    }

    /**
     * Avoid using this method. In general, use a goTo or openNew* method instead.
     */
    @Override
    public String toUrl(String relativePath, PageRequestParams pageRequestParams)
    {
        String path = calculatePath(relativePath);
        return calculateUrl(path, null, pageRequestParams, false);
    }

    /**
     * Avoid using this method. In general, use a goTo or openNew* method instead.
     */
    @Override
    public String toUrl(String relativePath, PageRequestParams pageRequestParams, Map<String, String> urlParams)
    {
        String path = calculatePath(relativePath);
        return calculateUrl(path, urlParams, pageRequestParams, false);
    }

    private String calculatePath(String relativePath)
    {
        String hostPageBaseUrl = GWT.getHostPageBaseURL();
        int i = hostPageBaseUrl.indexOf("//");
        int j = hostPageBaseUrl.indexOf("/", i + 2);
        String webAppContext = hostPageBaseUrl.substring(j + 1);

        return webAppContext + relativePath;
    }

    private String calculateUrl(String path, Map<String, String> urlParams, PageRequestParams pageRequestParams,
                                boolean includeBreadcrumbs)
    {
        UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
        urlBuilder.setPath(path);

        if (urlParams != null)
        {
            for (Map.Entry<String, String> paramKeyValue : urlParams.entrySet())
            {
                urlBuilder.setParameter(paramKeyValue.getKey(), paramKeyValue.getValue());
            }
        }

        // Remove the current query parameters (except leave GWT codeserver param if present - it's for DevMode)

        Map<String, List<String>> parameterMap = Window.Location.getParameterMap();

        for (String key : parameterMap.keySet())
        {
            if (!key.equals(QUERY_PARAM_GWT_CODESVR))
            {
                urlBuilder.removeParameter(key);
            }
        }
        if (pageRequestParams != null)
        {
            // Remove fragment identifier (AKA hash).

            urlBuilder.setHash(null);

            // Add the given query parameters, and the breadcrumbs, as a fragment identifier.

            if (includeBreadcrumbs)
            {
                String fragmentIdentifier = toFragmentIdentifierWithBreadcrumbs(pageRequestParams);
                urlBuilder.setHash(fragmentIdentifier);
            }
            else
            {
                String fragmentIdentifier = pageRequestParams.toFragmentIdentifier();
                urlBuilder.setHash(fragmentIdentifier);
            }
        }

        return urlBuilder.buildString();
    }

    public UniversalHandlerManager getUniversalEventBus()
    {
        throw new UnsupportedOperationException();
    }

    public void setUniversalEventBus(UniversalHandlerManager universalEventBus)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public PageRequestParams getActionPortionOfPageRequest()
    {
        String historyToken = History.getToken();
        int separatorIndex = historyToken.indexOf(BreadcrumbsInUrlConstants.BREADCRUMBS_SEPARATOR);

        if (separatorIndex < 0)
        {
            return new PageRequestParams(historyToken);
        }
        else
        {
            return new PageRequestParams(historyToken.substring(0, separatorIndex));
        }
    }

    @Override
    public String getBreadcrumbsPortionOfPageRequest()
    {
        String historyToken = History.getToken();
        int separatorIndex = historyToken.indexOf(BreadcrumbsInUrlConstants.BREADCRUMBS_SEPARATOR);

        if (separatorIndex < 0)
        {
            return "";
        }
        else
        {
            return historyToken.substring(separatorIndex + 1);
        }
    }

    @Override
    public void back()
    {
        History.back();
    }

    @Override
    public void fireCurrentHistoryState()
    {
        History.fireCurrentHistoryState();
    }

    @Override
    public void forward()
    {
        History.forward();
    }

    @Override
    public String getHistoryToken()
    {
        return History.getToken();
    }

    @Override
    public void setDefaultActionHandler(ValueChangeHandler<String> handler)
    {
        defaultHandler = handler;
    }

    @Override
    public void addActionHandler(String actionName, ValueChangeHandler<String> handler)
    {
        actionHandlers.add(new ActionHandler(actionName, handler));
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event)
    {
        // TODO - Maybe we should be passing it the breadcrumbsString. Is this the only caller?
        breadcrumbs.fromFragmentIdentifier(getBreadcrumbsPortionOfPageRequest());

        String historyToken = History.getToken();

        if (historyToken.length() == 0)
        {
            if (defaultHandler != null)
            {
                defaultHandler.onValueChange(event);
            }
            return;
        }

        // Parse out the action portion of the URL.

        String actionPortion = "";
        int i = historyToken.indexOf(BreadcrumbsInUrlConstants.BREADCRUMBS_SEPARATOR);

        if (i < 0)
        {
            actionPortion = historyToken;
        }
        else
        {
            actionPortion = historyToken.substring(0, i);
        }

        PageRequestParams token = new PageRequestParams(actionPortion);

        // Invoke onValueChange(event) on each handler of the action.

        String actionName = token.getAction();

        // TODO - Can we simplify this by mapping each actionName to one and only one ActionHandler? If yes,
        // then we could simplify the below - if actionName is empty or has no mapping then use defaultActionHandler.
        // OR - if actionName is not empty but there's no corresponding handler then go to error page.

        if (actionName == null)
        {
            if (defaultHandler != null)
            {
                defaultHandler.onValueChange(event);
            }
        }
        else
        {
            for (ActionHandler actionHandler : actionHandlers)
            {
                if (actionHandler.getActionName().equals(actionName))
                {
                    actionHandler.getHandler().onValueChange(event);
                }
            }
        }
    }

    @Override
    public void addToBreadcrumbs(SimpleBreadcrumb breadcrumb)
    {
        breadcrumbs.add(breadcrumb);
    }

    @Override
    public void clearBreadcrumbs()
    {
        breadcrumbs.clear();
    }

    @Override
    public int getBreadcrumbsSize()
    {
        return breadcrumbs.size();
    }

    @Override
    public Iterator<SimpleBreadcrumb> getBreadcrumbsIterator()
    {
        return breadcrumbs.iterator();
    }

    //    @Override
    public SimpleBreadcrumb truncateBreadcrumbs(int breadcrumbsIndex)
    {
        return breadcrumbs.truncate(breadcrumbsIndex);
    }

    private String toFragmentIdentifierWithBreadcrumbs(PageRequestParams pageRequestParams)
    {
        // Build a fragment identifier that holds the action token followed by the breadcrumbs

        String fragmentIdentifier = pageRequestParams.toFragmentIdentifier();

        String breadcrumbsString = breadcrumbs.toFragmentIdentifier();

        if (!breadcrumbsString.isEmpty())
        {
            fragmentIdentifier += BreadcrumbsInUrlConstants.BREADCRUMBS_SEPARATOR + breadcrumbs.toFragmentIdentifier();
        }

        return fragmentIdentifier;
    }

    private static final class ActionHandler
    {
        private String actionName;
        private ValueChangeHandler<String> handler;

        private ActionHandler(String actionName, ValueChangeHandler<String> handler)
        {
            this.actionName = actionName;
            this.handler = handler;
        }

        public String getActionName()
        {
            return actionName;
        }

        public ValueChangeHandler<String> getHandler()
        {
            return handler;
        }
    }

}
