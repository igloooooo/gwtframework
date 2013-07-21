package com.iglooit.core.base.client.widget;

import com.clarity.commons.iface.type.Tuple2;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;

public class CustomLoadingMask
{
    public enum RelativePos
    {
        CENTER, TOP_CENTER
    };

    private Element parentElement;
    private HTML maskHtml;
    private RelativePos positionOfMask = RelativePos.CENTER;

    public CustomLoadingMask()
    {
    }

    public CustomLoadingMask(RelativePos positionOfMask)
    {
        this.positionOfMask = positionOfMask;
    }

    public void showMask(Element parentElement)
    {
        if (maskHtml == null)
        {
            this.parentElement = parentElement;
            Tuple2<Integer, Integer> relativePos = getRelativePosition();

            maskHtml = new HTML("<div style='z-index: 999; display: block; left: " + relativePos.getFirst() +
                "px; top: " + relativePos.getSecond() + "px;' class='ext-el-mask-msg'><div>Loading...</div></div>");

            parentElement.appendChild(maskHtml.getElement());
        }
    }

    public void showMask(Element parentElement, String content)
    {
        if (maskHtml == null)
        {
            this.parentElement = parentElement;
            Tuple2<Integer, Integer> relativePos = getRelativePosition();

            maskHtml = new HTML("<div style='z-index: 999; display: block; left: " + relativePos.getFirst() +
              "px; top: " + relativePos.getSecond() + "px;' class='ext-el-mask-msg'><div>" + content + "</div></div>");

            parentElement.appendChild(maskHtml.getElement());
        }
    }


    public void hideMask()
    {
        if (maskHtml != null)
        {
            parentElement.removeChild(maskHtml.getElement());
            maskHtml = null;
        }
    }

    private Tuple2<Integer, Integer> getRelativePosition()
    {
        int x = 0;
        int y = 0;

        switch (positionOfMask)
        {
            case CENTER:
                x = parentElement.getOffsetWidth() / 2;
                y = parentElement.getOffsetHeight() / 2;
                break;

            case TOP_CENTER:
                x = parentElement.getOffsetWidth() / 2;
                y = 0;
                break;

            default:
                break;
        }

        return new Tuple2<Integer, Integer>(x, y);

    }


}
