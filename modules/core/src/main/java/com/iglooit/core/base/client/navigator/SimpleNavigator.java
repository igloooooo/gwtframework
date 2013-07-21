package com.iglooit.core.base.client.navigator;

import com.clarity.core.base.client.comms.UniversalCommsService;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This implementation of INavigator handles url changes & does not honor breadcrumbs. Ideally
 * NavigatorWithBreadcrumbsInUrl should be subclass of SimpleNavigator but stupid GIN complains on subclassing.
 */
public class SimpleNavigator implements INavigator
{
    private ValueChangeHandler<String> defaultActionHandler;
    private List<ActionHandler> actionHandlers = new ArrayList<ActionHandler>();
    private Map<Integer, JavaScriptObject> windowMap = new HashMap<Integer, JavaScriptObject>();

    public SimpleNavigator()
    {
        History.addValueChangeHandler(this);
    }

    @Override
    public void addActionHandler(String actionName, ValueChangeHandler<String> handler)
    {
        actionHandlers.add(new ActionHandler(actionName, handler));
    }

    @Override
    public void addToBreadcrumbs(SimpleBreadcrumb breadcrumb)
    {
        throw new UnsupportedOperationException("Breadcrumbs are not supported in SimpleNavigator");
    }

    @Override
    public void clearBreadcrumbs()
    {
        throw new UnsupportedOperationException("Breadcrumbs are not supported in SimpleNavigator");
    }

    @Override
    public void addToBrowserHistory(PageRequestParams pageRequestParams)
    {
        throw new UnsupportedOperationException("Breadcrumbs are not supported in SimpleNavigator");
    }

    /**
     * Directs the browser to a new URL which it builds by getting the current URL and replacing its fragment
     * identifier with one that represents the given page request parameters.
     * <p/>
     * For example, if the current URL is this...
     * <pre>
     * "http://abc.com:8080/faults.jsp?codeserver=10.10.0.5&xyz#subset=today"
     * </pre>
     * ...then calling this method with pageRequestParams [{"ac", "FaultDisplay"}, {"id", 1234}],
     * will direct the browser to this...
     * <pre>
     * "http://abc.com:8080/faults.jsp?codeserver=10.10.0.5&xyz#ac=FaultDisplay;id=1234"
     * </pre>
     *
     * @param pageRequestParams For example, containing [{"ac", "FaultDisplay"}, {"id", 1234}].
     */
    @Override
    public void goTo(PageRequestParams pageRequestParams)
    {
        History.newItem(pageRequestParams.toFragmentIdentifier(), true);
    }

    /**
     * Directs the browser to a new URL which it builds by getting the current URL and then:
     * <ul>
     * <li>it replaces the URL's path (for example, "faults.jsp") with the given path</li>
     * <li>it removes the URL's query parameters except "codeserver"</li>
     * <li>it replaces the URL's fragment identifier with one that represents the given page request parameters</li>
     * </ul>
     * For example, if the current URL is this...
     * <pre>
     * "http://abc.com:8080/faults.jsp?codeserver=10.10.0.5&xyz#subset=today"
     * </pre>
     * ...then calling this method with relativePath of "faultinstance.jsp" and pageRequestParams of
     * [{"ac", "FaultDisplay"}, {"id", 1234}], will direct the browser to this...
     * <pre>
     * "http://abc.com:8080/faultinstance.jsp?codeserver=10.10.0.5#ac=FaultDisplay;id=1234"
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
            Window.Location.assign(calculateUrl(path, null, pageRequestParams));
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
            Window.Location.assign(calculateUrl(path, urlParams, pageRequestParams));
        }
    }

    @Override
    public void goTo(int breadcrumbsIndex)
    {
        throw new UnsupportedOperationException("Breadcrumbs are not supported in SimpleNavigator");
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
     * it will figure out the absolute URL for both dev environment and real environment
     * it does not support PageRequestParams
     *
     * @param relativePath
     */
    @Override
    public void openNew(String relativePath, String feature)
    {
        Window.open(calculateUrl(calculatePath(relativePath), null, null), "_blank", feature);
    }

    /**
     * Opens a new window with a new URL which it builds by getting the current URL and then:
     * <ul>
     * <li>it replaces the URL's path (for example, "faults.jsp") with the given path</li>
     * <li>it removes the URL's query parameters except "codeserver"</li>
     * <li>it replaces the URL's fragment identifier with one that represents the given page request parameters</li>
     * </ul>
     * For example, if the current URL is this...
     * <pre>
     * "http://abc.com:8080/faults.jsp?codeserver=10.10.0.5&xyz#subset=today"
     * </pre>
     * ...then calling this method with relativePath of "faultinstance.jsp" and pageRequestParams of
     * [{"ac", "FaultDisplay"}, {"id", 1234}], will direct the browser to this...
     * <pre>
     * "http://abc.com:8080/faultinstance.jsp?codeserver=10.10.0.5#ac=FaultDisplay;id=1234"
     * </pre>
     *
     * @param relativePath      A path relative to the web app context. For a GWT page this will be an entry point.
     *                          For example, "faultinstance.jsp".
     * @param pageRequestParams For example, containing [{"ac", "FaultDisplay"}, {"id", 1234}].
     */
    @Override
    public void openNew(String relativePath, PageRequestParams pageRequestParams)
    {
        Window.open(calculateUrl(calculatePath(relativePath), null, pageRequestParams), "_blank", "");
    }

    /**
     * Opens a new window with a new URL which it builds by getting the current URL and then:
     * <ul>
     * <li>it replaces the URL's path (for example, "faults.jsp") with the given path</li>
     * <li>it removes the URL's query parameters except "codeserver"</li>
     * <li>it replaces the URL's fragment identifier with one that represents the given page request parameters</li>
     * </ul>
     * For example, if the current URL is this...
     * <pre>
     * "http://abc.com:8080/faults.jsp?codeserver=10.10.0.5&xyz#subset=today"
     * </pre>
     * ...then calling this method with relativePath of "faultinstance.jsp" and pageRequestParams of
     * [{"ac", "FaultDisplay"}, {"id", 1234}], will direct the browser to this...
     * <pre>
     * "http://abc.com:8080/faultinstance.jsp?codeserver=10.10.0.5#ac=FaultDisplay;id=1234"
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
        Window.open(calculateUrl(calculatePath(relativePath), null, pageRequestParams), "_blank", feature);
    }

    /**
     * This function will return:
     * -- true if open new tab
     * -- false if an existing tab found
     *
     * @param relativePath
     * @param pageRequestParams
     * @return
     */
    @Override
    public boolean openNewAndCacheHash(String relativePath, PageRequestParams pageRequestParams)
    {
        String url = calculateUrl(calculatePath(relativePath), null, pageRequestParams);
        int urlHash = url.hashCode();
        if (windowMap.containsKey(urlHash))
        {
            try
            {
                boolean isSuccess = changeWindowFavIconJs(windowMap.get(urlHash));
                if (isSuccess)
                    return false; // indicate open tab
            }
            catch (JavaScriptException e)
            {
                JavaScriptObject windowObject = openNewJs(url);
                windowMap.put(urlHash, windowObject);
                return true;
            }
        }
        JavaScriptObject windowObject = openNewJs(url);
        windowMap.put(urlHash, windowObject);
        return true;
    }

    private native JavaScriptObject openNewJs(String url)/*-{
        return $wnd.open(url, '_blank');
    }-*/;

    public Frame createFrame(String iFrameId, String relativePath, PageRequestParams pageRequestParams)
    {
        Map<String, String> urlParams = new HashMap();
        urlParams.put(UniversalCommsService.QUERY_PARAM_PARTICIPANT_ID, iFrameId);

        String url = calculateUrl(calculatePath(relativePath), urlParams, pageRequestParams);
        return new Frame(url);
    }

    public HtmlContainer createHtmlContainer(String iFrameId, String relativePath, PageRequestParams pageRequestParams)
    {
        Map<String, String> urlParams = new HashMap();
        urlParams.put(UniversalCommsService.QUERY_PARAM_PARTICIPANT_ID, iFrameId);

        String url = calculateUrl(calculatePath(relativePath), urlParams, pageRequestParams);
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

        return calculateUrl(currentPath, null, pageRequestParams);
    }

    /**
     * Avoid using this method. In general, use a goTo or openNew* method instead.
     */
    @Override
    public String toUrl(String relativePath, PageRequestParams pageRequestParams)
    {
        String path = calculatePath(relativePath);
        return calculateUrl(path, null, pageRequestParams);
    }

    /**
     * Avoid using this method. In general, use a goTo or openNew* method instead.
     */
    @Override
    public String toUrl(String relativePath, PageRequestParams pageRequestParams, Map<String, String> urlParamMap)
    {
        String path = calculatePath(relativePath);

        return calculateUrl(path, urlParamMap, pageRequestParams);
    }

    private String calculatePath(String relativePath)
    {
        String hostPageBaseUrl = GWT.getHostPageBaseURL();
        int i = hostPageBaseUrl.indexOf("//");
        int j = hostPageBaseUrl.indexOf("/", i + 2);
        String webAppContext = hostPageBaseUrl.substring(j + 1);

        return webAppContext + relativePath;
    }

    private String calculateUrl(String path, Map<String, String> urlParams, PageRequestParams pageRequestParams)
    {
        UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
        urlBuilder.setPath(path);

        // Remove the current query parameters (except leave GWT codeserver param if present - it's for DevMode)

        if (urlParams != null)
        {
            for (Map.Entry<String, String> paramKeyValue : urlParams.entrySet())
            {
                urlBuilder.setParameter(paramKeyValue.getKey(), paramKeyValue.getValue());
            }
        }

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

            // Add the given parameters, as a fragment identifier.

            String fragmentIdentifier = pageRequestParams.toFragmentIdentifier();
            urlBuilder.setHash(fragmentIdentifier);
        }

        return urlBuilder.buildString();
    }

    /*
    * test only
    * */
    private native void focusWindowJs(JavaScriptObject window)/*-{
            window.alert('test');
        }-*/;

    private native boolean changeWindowFavIconJs(JavaScriptObject window)/*-{
        if (window == null)
            return false;
        if (window.closed)
            return false;

        // window.testFunction('blah test');
        window.addFavIcon();
        return true;
    }-*/;

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
        defaultActionHandler = handler;
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event)
    {
        String historyToken = getHistoryToken();

        if (historyToken.length() == 0)
        {
            if (defaultActionHandler != null)
            {
                defaultActionHandler.onValueChange(event);
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
            if (defaultActionHandler != null)
            {
                defaultActionHandler.onValueChange(event);
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
    public int getBreadcrumbsSize()
    {
        return 0;
    }

    @Override
    public Iterator<SimpleBreadcrumb> getBreadcrumbsIterator()
    {
        throw new UnsupportedOperationException("Breadcrumbs are not supported in SimpleNavigator");
    }

    protected static final class ActionHandler
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