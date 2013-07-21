package com.iglooit.core.base.client.widget.layoutcontainer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.Element;

/**
 * Base object for all panels
 * <p/>
 * lifecycle
 * <p/>
 * on construction
 * + pre :
 * - init any Async Services
 * - create any visual objects
 * + post:
 * <p/>
 * on render
 * +pre : no fields should be null
 * - layout
 * - hook up any callbacks
 * - don't need to delegate this method
 * +post : readyto be initialized with data
 * <p/>
 * on run
 * +pre : rendered + all fields non-null
 * - get initial data(either from args or a service), then populate widgets
 * +post: all widgets rendered, ready for user interaction.
 */
public abstract class GPanel extends LayoutContainer
{
    @Override
    protected final void onRender(Element element, int i)
    {
        super.onRender(element, i);
        doOnRender(element, i);
    }


    /**
     * called when rendering
     */
    public abstract void doOnRender(Element element, int i);

    public abstract String getLabel();

    public void beforeClose(BaseEvent beforeCloseEvent)
    {
        //override this if you want to do any cleaning up before close is called
    }
}
