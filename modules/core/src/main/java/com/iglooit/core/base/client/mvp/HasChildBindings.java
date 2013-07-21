package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.composition.presenter.BinderContainer;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 22/06/12
 * Time: 5:24 PM
 */
public interface HasChildBindings
{
    Collection<BinderContainer> getChildBinderContainers();
}
