package com.iglooit.core.base.client.navigator;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This implementation of IBreadcrumbs suits an INavigator that stores breadcrumbs in the URL's fragment
 * identifier (AKA the hash tag).
 */
public final class BreadcrumbsInUrl implements IBreadcrumbs
{
    private List<SimpleBreadcrumb> breadcrumbs = new ArrayList<SimpleBreadcrumb>();

    @Inject
    public BreadcrumbsInUrl()
    {
    }

    @Override
    public void add(SimpleBreadcrumb newBreadcrumb)
    {
        breadcrumbs.add(newBreadcrumb);
    }

    @Override
    public int size()
    {
        return breadcrumbs.size();
    }

    @Override
    public Iterator<SimpleBreadcrumb> iterator()
    {
        // Should probably return something immutable?
        return breadcrumbs.listIterator();
    }

    @Override
    public SimpleBreadcrumb truncate(int breadcrumbIndex)
    {
        SimpleBreadcrumb requestedBreadcrumb = breadcrumbs.get(breadcrumbIndex);

        List<SimpleBreadcrumb> newBreadcrumbs = new ArrayList<SimpleBreadcrumb>();

        for (int i = 0; i < breadcrumbIndex; i++)
        {
            newBreadcrumbs.add(breadcrumbs.get(i));
        }

        breadcrumbs = newBreadcrumbs;

        return requestedBreadcrumb;
    }

    @Override
    public void clear()
    {
        // How to clear them from the history token???
        breadcrumbs.clear();
    }

    /**
     * Example result: "ep=fi;bc=fault-label;ac=FaultDisplay;id=12321;tab=links$ep=ib;bc=inbox".
     *
     * @return
     */
    @Override
    public String toFragmentIdentifier()
    {
        StringBuilder s = new StringBuilder("");

        // Put breadcrumbs in the URL in reverse age order ie. newest first, oldest last.

        for (int i = breadcrumbs.size() - 1; i >= 0; i--)
        {
            s.append(formatForUrl(breadcrumbs.get(i)));
            if (i > 0)
            {
                s.append(BreadcrumbsInUrlConstants.BREADCRUMBS_SEPARATOR);
            }
        }

        return s.toString();
    }

    @Override
    public void fromFragmentIdentifier(String fragmentIdentifier)
    {
        breadcrumbs = parseBreadcrumbs(fragmentIdentifier);
    }

    /**
     * Example result: "ep="fi";bc="fault-label";ac="FaultDisplay";id="12321";tab="links" .
     *
     * @param breadcrumb
     * @return
     */
    private String formatForUrl(SimpleBreadcrumb breadcrumb)
    {
        StringBuilder s = new StringBuilder("");
        boolean first = true;
        PageRequestParams pageRequestParams = breadcrumb.getPageRequestParams();

        // First, the entry point id

        s.append(buildNameValuePairForFragmentIdentifier(first, BreadcrumbsInUrlConstants.PARAM_NAME_ENTRY_POINT,
            breadcrumb.getEntryPointId()));
        first = false;

        // Second, the label key

        s.append(buildNameValuePairForFragmentIdentifier(first,
            BreadcrumbsInUrlConstants.PARAM_NAME_BREADCRUMB_LABEL_KEY, breadcrumb.getDisplayLabelKey()));

        // Third, the action (if there is one)

        for (Map.Entry<String, String> paramNameAndValue : pageRequestParams.getParamValuesByParamName().entrySet())
        {
            if (paramNameAndValue.getKey().equals(BreadcrumbsInUrlConstants.PARAM_NAME_ACTION))
            {
                s.append(buildNameValuePairForFragmentIdentifier(first, paramNameAndValue.getKey(),
                    paramNameAndValue.getValue()));
                break;
            }
        }

        // Lastly, all the rest of the action's parameters

        for (Map.Entry<String, String> paramNameAndValue : pageRequestParams.getParamValuesByParamName().entrySet())
        {
            if (!paramNameAndValue.getKey().equals(BreadcrumbsInUrlConstants.PARAM_NAME_ACTION))
            {
                s.append(buildNameValuePairForFragmentIdentifier(first, paramNameAndValue.getKey(),
                    paramNameAndValue.getValue()));
            }
        }

        return s.toString();
    }

    private String buildNameValuePairForFragmentIdentifier(boolean first, String name, String value)
    {
        StringBuilder s = new StringBuilder("");
        if (!first)
        {
            s.append(BreadcrumbsInUrlConstants.ACTION_PARAMS_SEPARATOR);
        }
        // The name does not need encoding - that's PageRequestParams's responsibility.
        s.append(name);
        s.append(BreadcrumbsInUrlConstants.ACTION_PARAM_NAME_VALUE_SEPARATOR);
        // The value does not need encoding - that's PageRequestParams's responsibility.
        s.append(value);
        return s.toString();
    }

    private List<SimpleBreadcrumb> parseBreadcrumbs(String breadcrumbsString)
    {
        List<SimpleBreadcrumb> breadcrumbs = new ArrayList<SimpleBreadcrumb>();

        if (breadcrumbsString != null && breadcrumbsString.length() > 0)
        {
            // Split the string into individual breadcrumb strings.

            String[] breadcrumbStrings = breadcrumbsString.split("\\" + BreadcrumbsInUrlConstants
                .BREADCRUMBS_SEPARATOR, -1);

            // IBreadcrumbs in the URL are in reverse age order ie. newest first, oldest last.

            for (int b = breadcrumbStrings.length - 1; b >= 0; b--)
            {
                // Parse the breadcrumb string into an entry point id, display label key and action parameters

                String entryPointId = null;
                String displayLabelKey = null;
                PageRequestParams pageRequestParams = new PageRequestParams();

                String[] parameterStrings =
                    breadcrumbStrings[b].split("\\" + BreadcrumbsInUrlConstants.ACTION_PARAMS_SEPARATOR, -1);

                for (int p = 0; p < parameterStrings.length; p++)
                {
                    String[] nameValuePair =
                        parameterStrings[p].split("\\" + BreadcrumbsInUrlConstants.ACTION_PARAM_NAME_VALUE_SEPARATOR,
                            -1);

                    if (nameValuePair.length != 2)
                    {
                        // The breadcrumb is corrupted. No choice but to ignore it.
                        continue;
                    }

                    if (nameValuePair[0].equals(BreadcrumbsInUrlConstants.PARAM_NAME_ENTRY_POINT))
                    {
//                        entryPointId = URL.decodeQueryString(nameValuePair[1]);
                        entryPointId = nameValuePair[1];
                    }
                    else if (nameValuePair[0].equals(BreadcrumbsInUrlConstants.PARAM_NAME_BREADCRUMB_LABEL_KEY))
                    {
//                        displayLabelKey = URL.decodeQueryString(nameValuePair[1]);
                        displayLabelKey = nameValuePair[1];
                    }
                    else
                    {
//                        actionParamValuesByParamName.put(nameValuePair[0], URL.decodeQueryString(nameValuePair[1]));
                        pageRequestParams.addParamAsIs(nameValuePair[0], nameValuePair[1]);
                    }
                }

                // Put it all into a SimpleBreadcrumb and add it to the list.

                SimpleBreadcrumb breadcrumb = new SimpleBreadcrumb(entryPointId, displayLabelKey, pageRequestParams);
                breadcrumbs.add(breadcrumb);
            }

        }

        return breadcrumbs;
    }

}
