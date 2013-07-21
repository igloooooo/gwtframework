package com.iglooit.core.base.client.view;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.fx.Fx;
import com.extjs.gxt.ui.client.fx.SingleStyleEffect;

public class FadeHighlightEffect extends SingleStyleEffect
{
    private int[] base = new int[3];
    private int[] delta = new int[3];

    private String styleAttribute;
    private boolean fadeToTransparent;

    public static void highlight(El el, String styleAttribute, String from, String to, int duration)
    {
        highlight(el, styleAttribute, from, to, duration, false);
    }

    public static void highlight(El el, String styleAttribute, String from, String to, int duration,
                                 boolean fadeToTransparent)
    {
        new Fx().run(duration, new FadeHighlightEffect(el, styleAttribute, from, to, fadeToTransparent));
    }

    protected FadeHighlightEffect(El el, String styleAttribute, String from, String to,
                                  boolean fadeToTransparent)
    {
        super(el);
        this.styleAttribute = styleAttribute;
        this.fadeToTransparent = fadeToTransparent;
        for (int i = 0; i < 3; i++)
        {
            int b = Integer.parseInt(from.substring(i * 2, (i + 1) * 2), 16);
            int e = Integer.parseInt(to.substring(i * 2, (i + 1) * 2), 16);
            base[i] = b;
            delta[i] = e - b;
        }
    }

    @Override
    public void onUpdate(double progress)
    {
        StringBuilder sb = new StringBuilder(7).append("#");
        for (int i = 0; i < 3; i++)
        {
            String v = Integer.toHexString((int)(base[i] + delta[i] * progress));
            sb.append((v.length() < 2) ? "0" : "").append(v);
        }
        el.setStyleAttribute(styleAttribute, sb.toString());
    }

    @Override
    public void onComplete()
    {
        if (fadeToTransparent)
            el.setStyleAttribute(styleAttribute, "transparent");
    }
}
