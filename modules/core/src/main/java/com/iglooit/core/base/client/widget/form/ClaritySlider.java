package com.iglooit.core.base.client.widget.form;

import com.extjs.gxt.ui.client.widget.Slider;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class ClaritySlider extends Slider
{
    private boolean isShowCurrentValue = false;
    private boolean isShowMinMaxLabel = false;

    public interface TipRenderer
    {
        String format(Slider slider, int value);
    }

    private TipRenderer tipRenderer;

    public void setTipRenderer(TipRenderer renderer)
    {
        tipRenderer = renderer;
    }

    protected String onFormatValue(int value)
    {
        if (tipRenderer != null)
            return tipRenderer.format(this, value);
        else
            return super.onFormatValue(value);
    }

    @Override
    protected void onRender(Element target, int index)
    {
        Element parent = DOM.createDiv();
        if (el() == null)
        {
            setElement(parent, target, index);
            parent = getElement();
        }
        else
        {
            target.appendChild(parent);
        }
        Element divSliderBar = DOM.createDiv();
        if (isShowMinMaxLabel)
        {
            Element leftLabel = DOM.createLabel();
            Element rightLabel = DOM.createLabel();

            leftLabel.setInnerHTML("Min");
            rightLabel.setInnerHTML("Max");
            leftLabel.setAttribute("style", "float:left; width:20px;margin-top:2px");
            rightLabel.setAttribute("style", "float:right;width:20px;margin-top:2px");
            parent.appendChild(leftLabel);
            parent.appendChild(rightLabel);
            divSliderBar.setAttribute("style", "margin-top:2px;clear:both");
            parent.appendChild(divSliderBar);

        }
        else
        {
            parent.appendChild(divSliderBar);
        }

        super.onRender(divSliderBar, index);
        Element valueBar = DOM.createDiv();
        if (isShowCurrentValue)
        {
            valueBar.setClassName("clarity-slider-current-value");
            divSliderBar.appendChild(valueBar);
            valueBar.setInnerHTML("Current Value : " + onFormatValue(1));
        }
        // remove the width attribute for slider
        el().child(".x-slider-inner").setStyleAttribute("width", "100%");

    }

    @Override
    protected void moveThumb(int v)
    {
        super.moveThumb(v);
        if (isShowCurrentValue)
        {
            el().child(".clarity-slider-current-value").setInnerHtml("Current Value : " + onFormatValue(getValue()));
        }
    }

    public boolean isShowCurrentValue()
    {
        return isShowCurrentValue;
    }

    public void setShowCurrentValue(boolean showCurrentValue)
    {
        isShowCurrentValue = showCurrentValue;
    }

    public boolean isShowMinMaxLabel()
    {
        return isShowMinMaxLabel;
    }

    public void setShowMinMaxLabel(boolean showMinMaxLabel)
    {
        isShowMinMaxLabel = showMinMaxLabel;
    }
}
