package com.iglooit.core.base.client.view.mxgraph;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.layoutcontainer.ElementAttachingLayoutContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.HTML;

public abstract class CaptionableMXGraphObject extends MXGraphObject implements Captionable
{
    private static final int CAPTION_MIN_LENGTH = 10;

    private String caption;

    protected abstract void setLabel(Object o);

    protected abstract String getLabelStyleName();

    protected abstract MXRectangle getBounds();

    protected abstract MXRectangle getCaptionBounds();

    protected CaptionableMXGraphObject(int x, int y, int width, int height)
    {
        super(x, y, width, height);
    }

    protected CaptionableMXGraphObject()
    {
    }

    public String getCaption()
    {
        return caption;
    }

    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    public void initialiseCaption()
    {
        setStyledTextLabel(getCaption());
    }

    public void shortenCaptionToWidth(double maxCaptionWidth)
    {
        String shortenedCaption = getCaption();
        if (shortenedCaption.length() <= CAPTION_MIN_LENGTH)
            return;

        double captionWidth = getCaptionWidth();
        if (captionWidth <= maxCaptionWidth)
            return;

        int newStringLength = (int)(shortenedCaption.length() * maxCaptionWidth / captionWidth);
        newStringLength = Math.min(Math.max(CAPTION_MIN_LENGTH, newStringLength), shortenedCaption.length());

        shortenedCaption = shortenedCaption.substring(0, newStringLength);
        setStyledTextLabel(shortenedCaption + "...");

    }

    public static String getStyledText(String text, String labelStyleName)
    {
        return "<div class=\"" + labelStyleName + "\">" + text + "</div>";
    }

    private void setStyledTextLabel(String text)
    {
        setLabel(getStyledText(text, getLabelStyleName()));
    }

    public void shortenCaptionToWidthScale(double scaleToCellWidth)
    {
        final double cellWidth = getBounds().getWidth();
        final double maxCaptionWidth = cellWidth * scaleToCellWidth;
        shortenCaptionToWidth(maxCaptionWidth);
    }


    private native void setupTooltip(JavaScriptObject cellJavaScriptObject, CaptionableMXGraphObject cellInstance) /*-{
        cellJavaScriptObject.getTooltip = function () {
            return cellInstance.@com.clarity.core.base.client.view.mxgraph.CaptionableMXGraphObject::initTooltip()();
        };
    }-*/;


    public String initTooltip()
    {
        final String divId = Document.get().createUniqueId();

        DeferredCommand.addCommand(new Command()
        {
            public void execute()
            {
                ElementAttachingLayoutContainer llc = ElementAttachingLayoutContainer.create(divId);
                createTooltipPanel(llc.getPanel());
            }
        });

//        return "<h3>test</h3><div id=\"" + divId + "\">hi</div>";
        return "<div id=\"" + divId + "\"></div>";
    }

    private void createTooltipPanel(LayoutContainer gxtLayoutContainer)
    {
        HTML html = new HTML(getStyledText(getCaption(), ClarityStyle.CIRCUIT_TOOLTIP));
        gxtLayoutContainer.add(html);
//        gxtLayoutContainer.add(new HTML("<b>Caption:</b>" + getCaption()));
//        CircuitViewDisplay circuitViewDisplay = new CircuitViewDisplay();
//        HandlerManager handlerManager = new HandlerManager(null);
//        CommandServiceClientImpl csci = new CommandServiceClientImpl();
//        CircuitViewPresenter pres = new CircuitViewPresenter(circuitViewDisplay, csci, handlerManager);
//        pres.bind();
//        LayoutContainer layoutContainer = new LayoutContainer(new BorderLayout());
//        layoutContainer.setWidth(300);
//        layoutContainer.setHeight(300);
//        layoutContainer.add(pres.getDisplay().asWidget(),
//            new BorderLayoutData(com.extjs.gxt.ui.client.Style.LayoutRegion.CENTER));
//
//        gxtLayoutContainer.add(layoutContainer);
//        handlerManager.fireEvent(new DisplayCircuitEvent("100000089"));
        gxtLayoutContainer.layout(true);
    }

    protected void installTooltipPanel()
    {
        setupTooltip(getBrowserObject(), this);
    }

    public double getCaptionWidth()
    {
        return getCaptionBounds().getWidth();
    }


    public void setSimpleTooltip(String toolTip)
    {
        setSimpleTooltip(getBrowserObject(), toolTip);
    }

    private native void setSimpleTooltip(JavaScriptObject cell, String tooltip)
        /*-{
            cell.getTooltip = function() {
                return tooltip;
            }
        }-*/;
}
