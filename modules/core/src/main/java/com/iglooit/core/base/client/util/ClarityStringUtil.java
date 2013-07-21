package com.iglooit.core.base.client.util;

import com.google.gwt.core.client.JsArrayString;

public class ClarityStringUtil
{
    public static final String[] split(String string, String regex)
    {
        return convertToStringArray(jsSplit(string, regex));
    }

    public static final String[] split(String string, String regex, int limit)
    {
        return convertToStringArray(jsSplit(string, regex, limit));
    }

    public static final String[] match(String string, String regex)
    {
        return convertToStringArray(jsMatch(string, regex));
    }

    public static final native JsArrayString jsSplit(String string, String regex) /*-{
        return string.split(new RegExp(regex));
    }-*/;

    public static final native JsArrayString jsSplit(String string, String regex, int limit) /*-{
        return string.split(new RegExp(regex), limit);
    }-*/;

    public static final native JsArrayString jsMatch(String string, String regex) /*-{
        return string.match(new RegExp(regex));
    }-*/;

    private static String[] convertToStringArray(JsArrayString jsArrayString)
    {
        if (jsArrayString == null)
        {
            return new String[]{};
        }
        else
        {
            int len = jsArrayString.length();
            String[] result = new String[len];
            for (int i = 0; i < len; i++)
            {
                result[i] = jsArrayString.get(i);
            }
            return result;
        }
    }
}
