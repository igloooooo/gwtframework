package com.iglooit.core.base.client.presenter;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.base.client.mvp.DefaultPresenter;
import com.clarity.core.base.client.navigator.INavigator;
import com.clarity.core.base.client.view.BreadcrumbsBarDisplayConstants;
import com.clarity.core.base.client.view.IBreadcrumbsBarDisplay;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.clarity.core.lib.client.StringFormatter;
import com.google.gwt.event.shared.HandlerManager;
import com.google.inject.Inject;

public class BreadcrumbsBarPresenter extends DefaultPresenter<IBreadcrumbsBarDisplay>
    implements IBreadcrumbsBarDisplay.IPresenter
{
    private static final BreadcrumbsBarDisplayConstants BBDC = I18NFactoryProvider.get(BreadcrumbsBarDisplayConstants
        .class);

    private INavigator navigator;
    @Inject
    public BreadcrumbsBarPresenter(IBreadcrumbsBarDisplay display,
                                   CommandServiceClientImpl commandService,
                                   HandlerManager globalEventBus,
                                   INavigator navigator)
    {
        super(display, globalEventBus);
        this.navigator = navigator;

        bind();
    }

    public void bind()
    {
        getDisplay().setPresenter(this);
    }

    /**
     * The current breadcrumb will be displayed as a Label. This method determines the text for the label by looking up
     * BreadcrumbsBarDisplayConstants with the given currentLabelKey, and substituting the given arguments into the
     * result using the same rules as String.format(String format, Object... args).
     */
    public void showBreadcrumbs(String currentLabelKey, String... args)
    {
        String currentLabelUnformatted = BBDC.getString(currentLabelKey);
        String currentLabel = StringFormatter.format(currentLabelUnformatted, args);
        getDisplay().showBreadcrumbs(navigator, currentLabel);
    }

    @Override
    public void onGoBack()
    {
        onGoToBreadcrumb(navigator.getBreadcrumbsSize() - 1);
    }

    @Override
    public void onGoToBreadcrumb(int breadcrumbsIndex)
    {
        navigator.goTo(breadcrumbsIndex);
    }
}
