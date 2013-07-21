package com.iglooit.core.base.client.view;

import com.clarity.core.base.client.navigator.INavigator;
import com.clarity.core.base.client.mvp.Display;

public interface IBreadcrumbsBarDisplay extends Display
{
    void setPresenter(IPresenter presenter);

    void showBreadcrumbs(INavigator navigator, String currentLabel);

    public interface IPresenter
    {
        void onGoBack();

        void onGoToBreadcrumb(int breadcrumbsIndex);
    }
}
