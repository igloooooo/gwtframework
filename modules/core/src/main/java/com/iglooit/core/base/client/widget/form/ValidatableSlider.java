package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SliderEvent;
import com.extjs.gxt.ui.client.util.TextMetrics;
import com.extjs.gxt.ui.client.widget.form.SliderField;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;

import java.util.List;

public class ValidatableSlider extends SliderField implements ClarityField<Integer, SliderField>
{
    private boolean fireChangeEvents = true;
    private HandlerManager innerHandlerManager = new HandlerManager(null);
    private El sliderValue;

    public ValidatableSlider(int minNum, int maxNum, int increment)
    {
        super(new ClaritySlider());
        addStyleName("slider-value-container");
        setStyleAttribute("position", "relative");
        getSlider().setMaxValue(maxNum);
        getSlider().setMinValue(minNum);
        getSlider().setIncrement(increment);
        getSlider().addListener(Events.Change, new Listener<SliderEvent>()
        {
            @Override
            public void handleEvent(SliderEvent be)
            {
                updateSliderValueField();
            }
        });
    }

    /**
     * @return max width of the slider value text
     */
    private int getSliderValueMaxWidth()
    {
        TextMetrics textMetrics = TextMetrics.get();
        textMetrics.bind(sliderValue);
        return textMetrics.getWidth(Integer.toString(getSlider().getMaxValue()));
    }

    /**
     * this method adjust the slider width so that there is enough space in the right side of the slider
     * to display the slider value
     */
    private void adjustSliderWidth()
    {
        int valueWidth = getSliderValueMaxWidth();
        getSlider().setWidth(getWidth() - valueWidth - 5);
    }

    @Override
    protected void onResize(int width, int height)
    {
        super.onResize(width, height);
        if (rendered)
        {
            if (getSlider().isVertical())
            {
                getSlider().setHeight(height);
            }
            else
            {
                adjustSliderWidth();
            }
        }
    }

    @Override
    public SliderField getField()
    {
        return this;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
    }

    @Override
    public void valueExternallyChangedFrom(Integer oldLocalValue)
    {

    }

    @Override
    public String getFieldLabel()
    {
        return super.getFieldLabel();
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        super.setFieldLabel(fieldLabel);
    }

    @Override
    public Option<String> getUsageHint()
    {
        return Option.option(super.getEmptyText());
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        super.setEmptyText(usageHint);
    }

    @Override
    public Integer getValue()
    {
        return getSlider().getValue();
    }

    @Override
    public void setValue(Integer integer)
    {
        if (integer == null)
            super.setValue(0);
        else
            super.setValue(integer);
    }

    @Override
    public void setValue(Integer integer, boolean b)
    {
        fireChangeEvents = b;
        this.setValue(integer);
        fireValidationChangeEvent();
        fireChangeEvents = true;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Integer> tValueChangeHandler)
    {
        final HandlerRegistration valueChangeReg =
            innerHandlerManager.addHandler(ValueChangeEvent.getType(), tValueChangeHandler);
        getSlider().addListener(Events.Change, new Listener<SliderEvent>()
        {
            @Override
            public void handleEvent(SliderEvent sliderEvent)
            {
                fireValidationChangeEvent();
            }
        });
        return valueChangeReg;
    }

    protected void fireValidationChangeEvent()
    {
        if (fireChangeEvents)
        {
            Integer value = getValue();
            ValueChangeEvent.fire(ValidatableSlider.this, value);
        }
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        innerHandlerManager.fireEvent(gwtEvent);
    }

    @Override
    protected void onRender(Element parent, int index)
    {
        super.onRender(parent, index);
        LabelElement inputElement = Document.get().createLabelElement();
        sliderValue = new El((Element)inputElement.cast());
        sliderValue.addStyleName("slider-value");
        sliderValue.setStyleAttribute("position", "absolute");
        sliderValue.setStyleAttribute("top", "2pt");
        sliderValue.setStyleAttribute("right", "0px");
        getElement().appendChild(sliderValue.dom);
    }

    @Override
    protected void afterRender()
    {
        super.afterRender();
        updateSliderValueField();
    }

    private void updateSliderValueField()
    {
        if (rendered)
        {
            sliderValue.setInnerHtml(String.valueOf(this.getValue()));
            sliderValue.setWidth(sliderValue.getTextWidth());
        }
    }
}
