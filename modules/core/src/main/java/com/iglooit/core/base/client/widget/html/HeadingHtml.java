package com.iglooit.core.base.client.widget.html;

import com.clarity.core.base.client.view.ClarityStyle;
import com.extjs.gxt.ui.client.widget.Html;

public class HeadingHtml extends Html
{
    public HeadingHtml(String html, HeadingType type)
    {
        super(html);
        setTagName(type.value());
        addStyleName(ClarityStyle.TEXT_TITLE);
        if (type == HeadingType.APP_PAGE)
            addStyleName(ClarityStyle.APP_PAGE);
    }

    public static enum HeadingType
    {
        MAIN("h1"),
        APP_PAGE("h2"),
        PAGE("h2"),
        PAGE_SECTION("h3"),
        PAGE_SUBSECTION("h4"),
        PAGE_SUBSECTION_SECTION("h5"),
        PAGE_SUBSECTION_SECTION2("h6");

        private final String value;

        private HeadingType(String value)
        {
            this.value = value;
        }

        public String value()
        {
            return value;
        }
    }
}
