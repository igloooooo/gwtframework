package com.iglooit.core.base.client.view.resource.images;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

@SuppressWarnings("deprecation")
public interface Images extends ImageBundle
{
    @Resource("logo-small.gif")
    AbstractImagePrototype logoSmall();

    @Resource("logo-medium.gif")
    AbstractImagePrototype logoMedium();

    @Resource("loader.gif")
    AbstractImagePrototype loader();
}
