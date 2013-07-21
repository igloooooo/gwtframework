package com.iglooit.core.base.client.navigator;

public class SimpleBreadcrumb
{
    // TODO - find a way to map this to the GWT entry point URL
    private String entryPointId;

    // An i18n property key used to look up the display label. At least, that's the idea. The difficulty is that
    // this requires one entry point to be able to look up a key from a different entry point. This leads to
    // putting all keys in core. But that means core knows all the entry points (read modules). But that's not
    // allowed because customers will ba allowed to add their own modules. Aaaaaargh.
    // So for now, we'll look it up in BreadcrumbsBarDisplayConstants.
    // TODO - Review the above explanation.
    private String displayLabelKey;

    // This represents the action that the entry point would do to redisplay the thing represented by this breadcrumb.
    private PageRequestParams pageRequestParams;

    public SimpleBreadcrumb(String entryPointId, String displayLabelKey, PageRequestParams pageRequestParams)
    {
        this.entryPointId = entryPointId;
        this.displayLabelKey = displayLabelKey;
        this.pageRequestParams = pageRequestParams;
    }

    public String getEntryPointId()
    {
        return entryPointId;
    }

    public String getDisplayLabelKey()
    {
        return displayLabelKey;
    }

    public PageRequestParams getPageRequestParams()
    {
        return pageRequestParams;
    }

    public String toString()
    {
        String s = "";

        s = s.concat("entryPointId = " + entryPointId + ", ");
        s = s.concat("displayLabelKey = " + displayLabelKey + ", ");
        s = s.concat("pageRequestParams = " + pageRequestParams.toString());

        return s;
    }
    
}
