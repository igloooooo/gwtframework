package com.iglooit.core.base.client.view.sparkline;

import com.clarity.core.base.client.view.sparkline.option.SparkLineOption;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;

import java.util.List;

/*
* For SparkLine JS usage, please refer to:
* http://omnipotent.net/jquery.sparkline/
* */
public class SparkLineHelper
{
    public static void showSparkline(String key,
                                     List<Double> vals)
    {
        JsArrayNumber valsJs = getJSNumberArray(vals);
        showSparklineJs(key, valsJs);
    }

    public static void showSparklineWithOption(String key,
                                               List<Double> vals,
                                               SparkLineOption option)
    {
        JsArrayNumber valsJs = getJSNumberArray(vals);
        JavaScriptObject optionJs = Util.getJsObject(option);
        showSparklineWithOptionJs(key, valsJs, optionJs);
    }

    public static JsArrayNumber getJSNumberArray(List<Double> dList)
    {
        JsArrayNumber arr = JsArrayNumber.createArray().cast();

        for (double data : dList)
        {
            arr.push(data);
        }

        return arr;
    }

    private static native void showSparklineJs(String key, JsArrayNumber vals) /*-{
        $wnd.$('.'+key).sparkline(vals, {width:"70px", lineColor:"black"});
    }-*/;

    private static native void showSparklineWithOptionJs(String key,
                                                      JsArrayNumber vals,
                                                      JavaScriptObject option) /*-{
        $wnd.$('.'+key).sparkline(vals, option);
    }-*/;
}
