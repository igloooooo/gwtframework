package com.iglooit.core.base.client.widget.layoutcontainer;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.Header;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;


/**
 * CollapsibleLayoutContainer is a component container that has a collapsible header and a body container
 * It provides built-in expandable and collapsible behavior, along with a variety
 * of pre-built tool buttons that can be wired up to provide other customized behavior.
 */

public class CollapsibleLayoutContainer extends LayoutContainer
{
    private El body;
    private InputElement checkbox;
    private String checkboxName;
    private boolean checkboxToggle;
    private ToolButton collapseBtn;
    private boolean collapsed;
    private boolean collapsible;
    private String text;
    private Header header;
    private boolean titleCollapse;

    public CollapsibleLayoutContainer()
    {
        baseStyle = "collapsible-layout-container";
        header = new Header();
        header.addStyleName("container-header");
        ComponentHelper.setParent(this, header);
        enableLayout = true;
        getFocusSupport().setIgnore(false);
    }

    /**
     * Collapses the layoutContainer.
     */
    public void collapse()
    {
        if (rendered)
        {
            if (collapsible && !collapsed)
            {
                if (fireEvent(Events.BeforeCollapse))
                {
                    onCollapse();
                }
            }
        }
        else
        {
            collapsed = true;
        }
    }

    /**
     * Expands the layoutContainer.
     */
    public void expand()
    {
        if (rendered)
        {
            if (collapsible && collapsed)
            {
                if (fireEvent(Events.BeforeExpand))
                {
                    onExpand();
                }
            }
        }
        else
        {
            collapsed = false;
        }
    }

    /**
     * Returns the checkbox name.
     *
     * @return the checkbox name
     */
    public String getCheckboxName()
    {
        return checkboxName;
    }

    /**
     * Returns the panel Header.
     *
     * @return the header
     */
    public Header getHeader()
    {
        return header;
    }

    /**
     * Returns the panel's heading.
     *
     * @return the header text
     */
    public String getHeaderText()
    {
        return text;
    }

    @Override
    public El getLayoutTarget()
    {
        return body;
    }

    @Override
    public boolean insert(Component item, int index)
    {
        return super.insert(item, index);
    }

    /**
     * Returns true if checkbox toggle is enabled.
     *
     * @return the checkbox toggle state
     */
    public boolean isCheckboxToggle()
    {
        return checkboxToggle;
    }

    /**
     * Returns true if the layoutContainer is collapsible.
     *
     * @return true if callapsible
     */
    public boolean isCollapsible()
    {
        return collapsible;
    }

    /**
     * Returns <code>true</code> if the panel is expanded.
     *
     * @return the expand state
     */
    public boolean isExpanded()
    {
        return !collapsed;
    }

    @Override
    public void onComponentEvent(ComponentEvent ce)
    {
        super.onComponentEvent(ce);
        if (ce.getEventTypeInt() == Event.ONCLICK)
        {
            onClick(ce);
        }
    }

    /**
     * The name to assign to the LayoutContainer's checkbox if
     * {@link #setCheckboxToggle(boolean)} = true.
     *
     * @param checkboxName the name
     */
    public void setCheckboxName(String checkboxName)
    {
        this.checkboxName = checkboxName;
    }

    /**
     * True to render a checkbox into the layoutContainer frame just in front of the
     * div (defaults to false, pre-render). The layoutContainer will be expanded or
     * collapsed when the checkbox is toggled.
     *
     * @param checkboxToggle true for checkbox toggle
     */
    public void setCheckboxToggle(boolean checkboxToggle)
    {
        this.checkboxToggle = checkboxToggle;
        this.collapsible = true;
    }

    /**
     * Sets whether the layoutContainer is collapsible (defaults to false, pre-render).
     *
     * @param collapsible true for collapse
     */
    public void setCollapsible(boolean collapsible)
    {
        this.collapsible = collapsible;
    }

    /**
     * True to allow expanding and collapsing the panel (when {@link #collapsible}
     * = true) by clicking anywhere in the header bar, false to allow it only by
     * clicking to tool button (defaults to false, pre-render).
     *
     * @param titleCollapse the titleCollapse to set
     */
    public void setTitleCollapse(boolean titleCollapse)
    {
        assertPreRender();
        this.titleCollapse = titleCollapse;
    }

    /**
     * Sets the panel's expand state.
     *
     * @param expand <code>true<code> true to expand
     */
    public void setExpanded(boolean expand)
    {
        if (expand)
        {
            expand();
        }
        else
        {
            collapse();
        }
    }

    /**
     * Sets the panel heading.
     *
     * @param text the heading text
     */
    public void setHeading(String text)
    {
        this.text = text;
        if (rendered)
        {
            header.setText(text);
        }
    }

    /**
     * Sets the alignment of the items. (defaults to RIGHT, pre-render).
     *
     * @param alignment the alignment to set
     */
    public void setAlignment(final Style.HorizontalAlignment alignment)
    {
        assertPreRender();
        addListener(Events.Render, new Listener<BaseEvent>()
        {
            @Override
            public void handleEvent(final BaseEvent be)
            {
                final HorizontalPanel panel = getWidgetPanel(getHeader());

                switch (alignment)
                {
                    case LEFT:
                        panel.setStyleAttribute("float", "left");
                        break;
                    case RIGHT:
                        panel.setStyleAttribute("float", "right");
                        break;
                    default:
                        panel.setStyleAttribute("float", "left");
                        break;
                }
            }
        });
    }


    @Override
    protected ComponentEvent createComponentEvent(Event event)
    {
        return new BoxComponentEvent(this, event);
    }

    @Override
    protected void doAttachChildren()
    {
        super.doAttachChildren();
        ComponentHelper.doAttach(collapseBtn);
    }

    @Override
    protected void doDetachChildren()
    {
        super.doDetachChildren();
        ComponentHelper.doDetach(collapseBtn);
    }

    @Override
    protected void notifyHide()
    {
        if (!collapsed)
        {
            super.notifyHide();
        }
    }

    @Override
    protected void notifyShow()
    {
        if (!collapsed)
        {
            super.notifyShow();
        }
    }

    protected void onClick(ComponentEvent ce)
    {
        if (checkboxToggle && ce.getTarget() == (Element)checkbox.cast()
            || collapsible && titleCollapse && header != null && ce.within(header.getElement()))
        {
            setExpanded(!isExpanded());
        }
    }

    protected void onCollapse()
    {
        collapsed = true;
        if (checkboxToggle && checkbox != null)
        {
            checkbox.setChecked(false);
        }
        body.setVisible(false);
        addStyleName("x-panel-collapsed");

        for (Component c : getItems())
        {
            if (!isComponentHidden(c) && c.isRendered())
            {
                doNotify(c, false);
            }
        }

        updateIconTitles();

        BoxComponentEvent fe = new BoxComponentEvent(this);
        fireEvent(Events.Collapse, fe);
    }

    @Override
    protected void onDisable()
    {
        super.onDisable();
        if (collapseBtn != null)
        {
            collapseBtn.disable();
        }
        else if (checkbox != null)
        {
            checkbox.setDisabled(true);
        }
    }

    @Override
    protected void onEnable()
    {
        super.onEnable();
        if (collapseBtn != null)
        {
            collapseBtn.enable();
        }
        else if (checkbox != null)
        {
            checkbox.setDisabled(false);
        }
    }

    protected void onExpand()
    {
        collapsed = false;
        if (checkboxToggle && checkbox != null)
        {
            checkbox.setChecked(true);
        }
        body.setVisible(true);
        removeStyleName("x-panel-collapsed");

        for (Component c : getItems())
        {
            if (!isComponentHidden(c) && c.isRendered())
            {
                doNotify(c, true);
            }
        }

        updateIconTitles();

        BoxComponentEvent fe = new BoxComponentEvent(this);
        fireEvent(Events.Expand, fe);
    }

    @Override
    protected void onFocus(ComponentEvent ce)
    {
        super.onFocus(ce);
        if (GXT.isFocusManagerEnabled())
        {
            if (checkboxToggle && checkbox != null)
            {
                checkbox.focus();
            }
            else if (collapseBtn != null)
            {
                collapseBtn.focus();
            }
        }
    }

    @Override
    protected void onRender(Element parent, int pos)
    {
        setElement(DOM.createDiv(), parent, pos);
        header.render(this.getElement());

        if (checkboxToggle && collapsible)
        {
            checkbox = DOM.createInputCheck().cast();
            sinkEvents(Event.ONCLICK);
            if (checkboxName != null)
            {
                checkbox.setAttribute("name", checkboxName);
                checkbox.setAttribute("name", checkboxName);
            }
            header.el().appendChild((Element)checkbox.cast());
            checkbox.setDefaultChecked(!collapsed);
            checkbox.setChecked(!collapsed);
            if (GXT.isAriaEnabled())
            {
                checkbox.setTitle("Expand " + text);
            }
        }
        else if (!checkboxToggle && collapsible)
        {
            collapseBtn = new ToolButton("x-tool-toggle");
            collapseBtn.addListener(Events.Select, new Listener<ComponentEvent>()
            {
                public void handleEvent(ComponentEvent be)
                {
                    setExpanded(!isExpanded());
                }
            });
            collapseBtn.getAriaSupport().setRole("checkbox");
            if (GXT.isAriaEnabled())
            {
                collapseBtn.setTitle("Expand " + text);
            }
        }

        if (collapseBtn != null)
        {
            header.addTool(collapseBtn);
        }
        getElement().appendChild(header.getElement());

        body = el().appendChild(DOM.createDiv());


        if (text != null)
        {
            setHeading(text);
        }

        if (collapsed)
        {
            onCollapse();
        }

        updateIconTitles();

        if (GXT.isFocusManagerEnabled() && !getFocusSupport().isIgnore())
        {
            el().setTabIndex(0);
            el().setElementAttribute("hideFocus", "true");
            sinkEvents(Event.FOCUSEVENTS);
        }
        if (titleCollapse)
        {
            sinkEvents(Event.ONCLICK);
        }
    }

    @Override
    protected void onResize(int width, int height)
    {
        super.onResize(width, height);
        Size frameSize = el().getFrameSize();

        if (isAutoWidth())
        {
            getLayoutTarget().setWidth("auto");
        }
        else if (width != -1)
        {
            getLayoutTarget().setWidth(width - frameSize.width, true);
        }
        if (isAutoHeight())
        {
            getLayoutTarget().setHeight("auto");
        }
        else if (height != -1)
        {
            getLayoutTarget().setHeight(height - frameSize.height - header.el().getHeight()
                - (GXT.isIE ? header.el().getMargins("b") : 0), true);
        }
    }

    protected void updateIconTitles()
    {
        if (GXT.isAriaEnabled())
        {
            String txt = "Expand " + text;
            if (checkbox != null)
            {
                checkbox.setTitle(txt);
            }
            if (collapseBtn != null)
            {
                collapseBtn.setTitle(txt);
                collapseBtn.getAriaSupport().setState("aria-checked", !collapsed ? "true" : "false");
            }
        }
    }

    private native void doNotify(Component c, boolean show) /*-{
      if(show){
        c.@com.extjs.gxt.ui.client.widget.Component::notifyShow()()
      } else {
        c.@com.extjs.gxt.ui.client.widget.Component::notifyHide()();
      }
    }-*/;

    private native boolean isComponentHidden(Component c) /*-{
      return c.@com.extjs.gxt.ui.client.widget.Component::hidden;
    }-*/;

    private native HorizontalPanel getWidgetPanel(Component header)/*-{
            return header.@com.extjs.gxt.ui.client.widget.Header::widgetPanel;
    }-*/;

}

