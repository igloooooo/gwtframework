package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;

import java.util.Iterator;

public class DrillDownNavContainer extends LayoutContainer implements Display
{
    private Button backButton;
    private LayoutContainer breadcrumbContainer;

    public DrillDownNavContainer()
    {
        HBoxLayout hBoxLayout = new HBoxLayout();
        hBoxLayout.setHBoxLayoutAlign(HBoxLayout.HBoxLayoutAlign.TOP);
        setLayout(hBoxLayout);
        addStyleName("drill-down-container");
        setBorders(true);
        
        backButton = new Button("");
        backButton.setIcon(Resource.ICONS_SIMPLE.arrowNavLeft());
        //backButton.addStyleName(ClarityStyle.BUTTON_MIN_STYLE);
        add(backButton, new HBoxLayoutData(new Margins(0)));

        breadcrumbContainer = new LayoutContainer(new FlowLayout(0));
        breadcrumbContainer.addStyleName(ClarityStyle.BREADCRUMB_CONTAINER);
        final HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(5));
        layoutData.setFlex(1);
        add(breadcrumbContainer, layoutData);
    }

    public LayoutContainer getBreadcrumbContainer()
    {
        return breadcrumbContainer;
    }

    public void showBreadcrumbs(BreadcrumbTrail<DrillDownNavEntity> breadcrumbTrail)
    {
        show();
        breadcrumbContainer.removeAll();

        Iterator<DrillDownNavEntity> entityIterator = breadcrumbTrail.getBreadcrumbList().iterator();

        while (entityIterator.hasNext())
        {
            DrillDownNavEntity drillDownEntity = entityIterator.next();

            if (drillDownEntity.getAnchorSeparator() != null)
            {
                Html html = new Html(drillDownEntity.getAnchorSeparator().getHTML());
                html.addStyleName(ClarityStyle.FLOAT_LEFT);
                breadcrumbContainer.add(html, new FlowData(0, 5, 0, 0));
            }

            drillDownEntity.getAnchor().addStyleName(ClarityStyle.FLOAT_LEFT);
            breadcrumbContainer.add(drillDownEntity.getAnchor(), new FlowData(0, 5, 0, 0));

            //add a different style to the last breadcrumb
            if (!entityIterator.hasNext())
                drillDownEntity.getAnchor().addStyleName("selected-breadcrumb");
            else
                drillDownEntity.getAnchor().removeStyleName("selected-breadcrumb");
        }

        if (breadcrumbTrail.getBreadcrumbList().size() == 1)
            backButton.hide();
        else
            backButton.show();

        layout(true);
    }

    public void addBackButtonSelectionListener(SelectionListener<ButtonEvent> listener)
    {
        backButton.addSelectionListener(listener);
    }

    @Override
    public String getLabel()
    {
        return "";
    }
}
