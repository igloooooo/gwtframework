package com.iglooit.core.base.client.view.resource;

import com.clarity.core.base.client.view.resource.icons.Icons;
import com.clarity.core.base.client.view.resource.iconssimple.IconsSimple;
import com.clarity.core.base.client.view.resource.images.Images;
import com.google.gwt.core.client.GWT;

public class Resource
{
    public static final Icons ICONS = (Icons)GWT.create(Icons.class);
    public static final IconsSimple ICONS_SIMPLE = (IconsSimple)GWT.create(IconsSimple.class);
    public static final Images IMAGES = (Images)GWT.create(Images.class);
}
