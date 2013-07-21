package com.iglooit.core.base.client.widget.layoutcontainer;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;

public final class ElementAttachingLayoutContainer extends HTMLPanel
{
    private final LayoutContainer gxtLayoutContainer;

    public static ElementAttachingLayoutContainer create(String divId)
    {
        String innerDivId = Document.get().createUniqueId();
        ElementAttachingLayoutContainer llc = new ElementAttachingLayoutContainer(innerDivId);
        Element outerDiv = Document.get().getElementById(divId);
        outerDiv.appendChild(llc.getElement());
        llc.onAttach();
        llc.add(llc.gxtLayoutContainer, innerDivId);
        return llc;
    }

    public ElementAttachingLayoutContainer(String innerDivId)
    {
        super("<div id=\"" + innerDivId + "\"> </div>");
        gxtLayoutContainer = new LayoutContainer(new FlowLayout());
        gxtLayoutContainer.setAutoHeight(true);
        gxtLayoutContainer.setAutoWidth(true);
//            gxtLayoutContainer.add(new HTML("lol"));
    }

    public LayoutContainer getPanel()
    {
        return gxtLayoutContainer;
    }
}
