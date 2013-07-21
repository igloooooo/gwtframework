package com.iglooit.core.base.client.widget;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.tips.ToolTip;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;

public class ClarityToolTip extends ToolTip
{
    /**
     * Creates a new clarity tool tip.
     */
    public ClarityToolTip()
    {
        super();
    }

    /**
     * Creates a new clarity tool tip.
     *
     * @param target the target widget
     */
    public ClarityToolTip(Component target)
    {
        super(target);
    }

    /**
     * Creates a new tool clarity tip for the given target.
     *
     * @param target the target widget
     */
    public ClarityToolTip(Component target, ToolTipConfig config)
    {
        super(target, config);
    }

    @Override
    public void initTarget(final Component target)
    {
        this.target = target;
    }
}
