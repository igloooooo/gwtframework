package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.commons.iface.type.AppX;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.fx.FxConfig;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import java.util.HashMap;
import java.util.Map;

public class SlideableRibbonPanel extends LayoutContainer
{
    private Map<String, ButtonPanel> buttonPanels = new HashMap<String, ButtonPanel>();
    private int buttonWidth, buttonHeight;
    private boolean visible;
    private HandlerRegistration sliderRegistration;
    private ButtonPanel currentPanel;

    private int slideDuration;
    private static final int DEFAULT_SLIDE_DURATION = 200;

    public SlideableRibbonPanel(int buttonWidth, int buttonHeight)
    {
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        slideDuration = DEFAULT_SLIDE_DURATION;

        setHeight(this.buttonHeight);
    }

    public int getSlideDuration()
    {
        return slideDuration;
    }

    public void setSlideDuration(int slideDuration)
    {
        this.slideDuration = slideDuration;
    }

    public int getButtonHeight()
    {
        return buttonHeight;
    }

    public int getButtonWidth()
    {
        return buttonWidth;
    }

    public void addPanel(String name, ButtonPanel panel)
    {
        buttonPanels.put(name, panel);
        add(panel);

        panel.setVisible(false);
    }

    public ButtonPanel getButtonPanel(String name)
    {
        return buttonPanels.get(name);
    }

    public void slideIn(String panelName, ComponentEvent event)
    {

        if (visible) return;
        visible = true;

        ButtonPanel buttonPanel = buttonPanels.get(panelName);
        if (buttonPanel == null)
        {
            throw new AppX("Could not find button panel with name: " + panelName);
        }

        if (currentPanel != null)
        {
            currentPanel.setVisible(false);
        }

        currentPanel = buttonPanel;

        setWidth(buttonWidth * buttonPanel.getItemCount());
        buttonPanel.setVisible(true);

        setStyleAttribute("position", "absolute");
        setPosition(event.<Component>getComponent().getAbsoluteLeft() +
            event.<Component>getComponent().getOffsetWidth(), event.<Component>getComponent().getAbsoluteTop());


        FxConfig config = new FxConfig(slideDuration);
        config.setEffectCompleteListener(new Listener<FxEvent>()
        {
            @Override
            public void handleEvent(FxEvent fxEvent)
            {
                SlideableRibbonPanel.this.layout();

                //Explicitly set margin left to 0 due to GXT oddness with rhandom margin settings
                SlideableRibbonPanel.this.setStyleAttribute("margin-left", "0");

                sliderRegistration = Event.addNativePreviewHandler(new Event.NativePreviewHandler()
                {
                    public void onPreviewNativeEvent(Event.NativePreviewEvent event)
                    {
                        if (Event.as(event.getNativeEvent()).getTypeInt() == Event.ONCLICK)
                        {
                            Element element = (Element)event.getSource();
                            if (element == null || !element.getId().equals(getElement().getId()))
                            {
                                slideOut();
                            }
                        }
                    }
                });
            }
        });
        el().slideIn(Style.Direction.RIGHT, config);
    }

    public void slideOut()
    {
        FxConfig config = new FxConfig(slideDuration);
        config.setEffectCompleteListener(new Listener<FxEvent>()
        {
            @Override
            public void handleEvent(FxEvent fxEvent)
            {
                visible = false;
            }
        });
        el().slideOut(Style.Direction.LEFT, config);
        sliderRegistration.removeHandler();
    }

    public static class ButtonPanel extends LayoutContainer
    {
        private int buttonWidth, buttonHeight;
        private int buttonCount;

        public ButtonPanel(int buttonWidth, int buttonHeight)
        {
            setLayout(new HBoxLayout());
            setHeight(buttonHeight);

            this.buttonWidth = buttonWidth;
            this.buttonHeight = buttonHeight;
        }

        public void addButton(AbstractImagePrototype image, Listener<ComponentEvent> action, String tooltip)
        {
            Button up = new Button();
            up.addListener(Events.OnClick, action);
            up.setToolTip(tooltip);
            up.setText(tooltip);
            up.setIcon(image);
            up.setIconAlign(Style.IconAlign.TOP);
            up.setWidth(buttonWidth);
            up.setHeight(buttonHeight);
            add(up, new TableData(Style.HorizontalAlignment.LEFT, Style.VerticalAlignment.MIDDLE));

            buttonCount++;
            setWidth(buttonCount * buttonWidth);
        }
    }
}


