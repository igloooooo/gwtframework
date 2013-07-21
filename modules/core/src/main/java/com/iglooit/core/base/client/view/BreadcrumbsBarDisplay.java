package com.iglooit.core.base.client.view;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.base.client.navigator.BreadcrumbsInUrlConstants;
import com.clarity.core.base.client.navigator.INavigator;
import com.clarity.core.base.client.navigator.SimpleBreadcrumb;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.lib.client.StringFormatter;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import java.util.Iterator;

public class BreadcrumbsBarDisplay extends LayoutContainer implements IBreadcrumbsBarDisplay
{
    private static final AbstractImagePrototype BREADCRUMB_SEPARATOR_IMAGE = Resource.ICONS_SIMPLE.arrowPathRight();

    private static final BreadcrumbsBarDisplayConstants BBDC =
        I18NFactoryProvider.get(BreadcrumbsBarDisplayConstants.class);

    private IPresenter presenter;
    private Button backButton;
    private LayoutContainer breadcrumbContainer;
    private boolean showAsTwoLines = true;

    @Inject
    public BreadcrumbsBarDisplay()
    {
        this(true);
    }

    /**
     * Use this constructor to configure whether the breadcrumb bar should display breadcrumbs over a single line or
     * two lines (default)
     *
     * @param showAsTwoLines
     */
    public BreadcrumbsBarDisplay(boolean showAsTwoLines)
    {
        this.showAsTwoLines = showAsTwoLines;
        addStyleName("breadcrumb-bar");
        if (showAsTwoLines)
            addStyleName("two-line");

        HBoxLayout hBoxLayout = new HBoxLayout();
        hBoxLayout.setHBoxLayoutAlign(HBoxLayout.HBoxLayoutAlign.TOP);
        hBoxLayout.setPadding(new Padding(2));
        setLayout(hBoxLayout);
        setBorders(true);

        // Add the Back Button
        // ?? create and add Back button only when launched from Inbox. When this is launched from another module
        // e.g. alarms, then this would be shown in a new browser tab/window and thus doesn't need a 'Back' button
        // TODO: add conditional check here to see if we've come from the inbox

        backButton = new Button();
        backButton.setIcon(Resource.ICONS_SIMPLE.arrowNavLeftWhite());
        backButton.addStyleName(ClarityStyle.BUTTON_MIN_STYLE);
        backButton.addStyleName(ClarityStyle.BUTTON_CSS);
        backButton.addStyleName("button-back");
        backButton.setToolTip(BBDC.backButtonTooltip());
        backButton.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent ce)
            {
                presenter.onGoBack();
            }
        });

        HBoxLayoutData hBoxData = new HBoxLayoutData(new Margins(-1, 0, 0, 5));
        if (showAsTwoLines)
            hBoxData.setMargins(new Margins(5, 0, 0, 5));
        add(backButton, hBoxData);

        // Add a container for the breadcrumbs - we'll add the actual breadcrumbs later 

        breadcrumbContainer = new LayoutContainer(new FlowLayout(0));
        breadcrumbContainer.addStyleName(ClarityStyle.BREADCRUMB_CONTAINER);

        add(breadcrumbContainer, new HBoxLayoutData(new Margins(0, 0, 0, 5)));
    }

    public void setPresenter(IPresenter presenter)
    {
        this.presenter = presenter;
    }

    public void showBreadcrumbs(INavigator navigator, String currentLabel)
    {
        breadcrumbContainer.removeAll();

        if (navigator.getBreadcrumbsSize() > 0)
            backButton.show();
        else
            backButton.hide();

        Iterator<SimpleBreadcrumb> breadcrumbsIterator = navigator.getBreadcrumbsIterator();
        int i = -1;

        while (breadcrumbsIterator.hasNext())
        {
            i++;
            final SimpleBreadcrumb breadcrumb = breadcrumbsIterator.next();
            final int breadcrumbIndex = i;

            Anchor anchor = new Anchor(getLabel(breadcrumb));
            anchor.addStyleName(ClarityStyle.FLOAT_LEFT);
            anchor.addClickHandler(new ClickHandler()
            {
                @Override
                public void onClick(ClickEvent clickEvent)
                {
                    presenter.onGoToBreadcrumb(breadcrumbIndex);
                }
            });

            if (showAsTwoLines)
            {
                HtmlContainer htmlContainer = new HtmlContainer(
                    "<div class='float-left link'></div>" +
                        "<div class='float-left separator'>&gt;</div>");
                htmlContainer.addStyleName(ClarityStyle.FLOAT_LEFT);
                htmlContainer.addStyleName(ClarityStyle.TEXT_NO_WRAP);
                htmlContainer.add(anchor, ".link");

                breadcrumbContainer.add(htmlContainer, new FlowData(0, 5, 0, 0));
            }
            else
            {
                breadcrumbContainer.add(anchor, new FlowData(0, 5, 0, 0));

                Html separator = new Html(BREADCRUMB_SEPARATOR_IMAGE.getHTML());
                separator.addStyleName(ClarityStyle.FLOAT_LEFT);
                breadcrumbContainer.add(separator, new FlowData(5, 5, 0, 0));
            }
        }

        Text selectedAnchor = new Text(currentLabel);
        selectedAnchor.addStyleName(ClarityStyle.FLOAT_LEFT);
        selectedAnchor.addStyleName(ClarityStyle.TEXT_TITLE);
        selectedAnchor.addStyleName("selected-breadcrumb");

        breadcrumbContainer.add(selectedAnchor, new FlowData(0, 5, 0, 0));

        layout(true);
        show();
    }

    private String getLabel(SimpleBreadcrumb breadcrumb)
    {
        String displayLabelKey = breadcrumb.getDisplayLabelKey();
        String itemId = breadcrumb.getPageRequestParams().getStringParam(BreadcrumbsInUrlConstants.PARAM_NAME_ID, null);
        String label = StringFormatter.format(BBDC.getString(displayLabelKey), itemId);
        return label;
    }

    @Override
    public String getLabel()
    {
        return "";
    }

    @Override
    public Widget asWidget()
    {
        return this;
    }
}
