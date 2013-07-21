package com.iglooit.core.base.client.view.mxgraph;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.HashMap;

public abstract class Style
{
    protected HashMap<String, Object> getStyleHash()
    {
        return styleHash;
    }

    private HashMap<String, Object> styleHash = new HashMap<String, Object>();
    private String styleName;

    protected enum CloneType
    {
        DEFAULT, OTHER_STYLE
    }


    public void addElement(String name, int value)
    {
        addElement(name, Integer.toString(value));
    }

    public void addElement(String name, String element)
    {
        styleHash.put(name, element);
    }

    public String getElement(String name)
    {
        return (String)styleHash.get(name);
    }

    public void addElement(String name, boolean element)
    {
        styleHash.put(name, Boolean.toString(element));

        //addParameterToStyleB(getStyleObject(), name, element);
    }

    public JavaScriptObject getStyleObject()
    {
        return styleObject;
    }

    public void setStyleObject(JavaScriptObject styleObject)
    {
        this.styleObject = styleObject;
    }

    private JavaScriptObject styleObject;

    public void processStyleJS(Graph graph)
    {
        this.styleObject = addStyle(graph.getGraphObject());
        for (String key : styleHash.keySet())
        {
            if (key.substring(0, 2).equals("mx"))
                addParameterToStyleWithEval(getStyleObject(), key, (String)styleHash.get(key));
            else if (styleHash.get(key) instanceof String)
                addParameterToStyleS(getStyleObject(), key, styleHash.get(key));
        }
    }


    protected native JavaScriptObject addStyle(JavaScriptObject graph) /*-{
         return new Object();
    }-*/;

    private native void addParameterToStyleS(JavaScriptObject style, String constant, Object svalue) /*-{
        style[constant] = svalue;
    }-*/;

    private native void addParameterToStyleB(JavaScriptObject style, String constant, boolean value) /*-{
        style[constant] = value;
    }-*/;

    private native void addParameterToStyleWithEval(JavaScriptObject style, String constant, String value) /*-{
        var evalledResult = "";
        eval("evalledResult = $wnd." + value);
        style[constant] = evalledResult;
    }-*/;

    protected Style(String styleName)
    {
        this.styleName = styleName;
    }

    public String getStyleName()
    {
        return styleName;
    }
}
