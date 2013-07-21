package com.iglooit.core.base.client.util;

import com.google.gwt.user.client.Window;

public class NavigationUtil
{
    private static final String DEFAULT_DEV_PORT = ":8888";
    private static final String DEFAULT_DEV_CODE_SERVER_PARAM = "?gwt.codesvr=";
    private static final String DEFAULT_DEV_CODE_SERVER_PORT = ":9997";

    public static String buildRedirectURL(String url)
    {
        // The best guess for code server is the server in which the application is running.
        // When remote-debugging client side code, the developer will need to modify the url manually.
        // TODO HA: There might be a better way to figure out the code server parameter.
        // TODO HA: See InboxMainPresenter.addSharedEventBusHandlers()
        String hostName = Window.Location.getHostName();
        if (getCurrentLocation().contains(DEFAULT_DEV_PORT))
            return url + DEFAULT_DEV_CODE_SERVER_PARAM + hostName + DEFAULT_DEV_CODE_SERVER_PORT;
        else
            return url;
    }

    public static native String getCurrentLocation()/*-{
      return $wnd.location.toString();
    }-*/;

    //redirect the browser to the given url
    public static native void redirect(String url)/*-{
      $wnd.location = url;
    }-*/;

    public static void reloadPage()
    {
        Window.Location.reload();
    }

    public static native void reloadChildWindows()/*-{
      $wnd.reloadChildWindows();
    }-*/;

    public static void redirectToUrl(String url)
    {
        Window.Location.assign(url);
    }
}
