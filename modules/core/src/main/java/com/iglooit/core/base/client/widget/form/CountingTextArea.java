package com.iglooit.core.base.client.widget.form;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;

public class CountingTextArea extends TextArea
{
    private Html countHtml;
    private HtmlContainer countingContainer;
    private Timer messageLengthTimer;
    private static final String TEXT_REMAINING = "Text Remaining: ";
    private static final String TEXT_SELECTOR = "text-selector";
    private static final String NUMBER_SELECTOR = "number-selector";

    //
    public CountingTextArea()
    {
        setAutoHeight(true);
        addListener(Events.Render, new Listener<ComponentEvent>()
        {
            @Override
            public void handleEvent(ComponentEvent componentEvent)
            {
                createCountingContainer();
                Element p = el().getParent().dom;
                countingContainer.render(p);
            }
        });
        messageLengthTimer = new Timer()
        {
            @Override
            public void run()
            {
                int messageSize = getValue() == null
                    ? 0 : getValue().length();
                int textRemainingSize = getMaxLength() - messageSize;
                updateCount(textRemainingSize < 0 ? 0 : textRemainingSize);
            }
        };
    }

    public void startTimer()
    {
        messageLengthTimer.scheduleRepeating(100);
    }

    public void stopTimer()
    {                             
        messageLengthTimer.cancel();
    }

    public void updateCount(int count)
    {
        countHtml.setHtml(Integer.toString(count));
        if (getMaxLength() == count)
        {
            if (countingContainer.el().isConnected())
            {
                El elem = el().findParent(".x-form-element", 3);
                elem.removeChild(countingContainer.getElement());
                adjustHeight();
            }
        }
        else
        {
            if (!countingContainer.el().isConnected())
            {
                El elem = el().findParent(".x-form-element", 3);
                elem.appendChild(countingContainer.getElement());
                adjustHeight();
            }
            countingContainer.show();
        }
    }

    private void adjustHeight()
    {
        int totalHeight = el().findParent(".x-form-label-left", 4).getHeight();
        if (countingContainer.el().isConnected())
            setHeight(totalHeight - countingContainer.getHeight());
        else
            setHeight(totalHeight);
    }

    private void createCountingContainer()
    {
        countingContainer = new HtmlContainer("<span class=" + TEXT_SELECTOR + "></span>" +
            "<span class=" + NUMBER_SELECTOR + "></span>");
        Html html = new Html();
        html.setTagName("span");
        html.setHtml(TEXT_REMAINING);
        countHtml = new Html();
        countHtml.setTagName("span");
        countingContainer.add(html, "." + TEXT_SELECTOR);
        countingContainer.add(countHtml, "." + NUMBER_SELECTOR);
    }
}
