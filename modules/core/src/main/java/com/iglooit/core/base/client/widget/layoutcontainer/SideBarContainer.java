package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.FormUtils;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.form.AutoMaskButton;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public abstract class SideBarContainer extends ContentPanel
{
    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private ButtonBar bottomActionBar;
    private AutoMaskButton runBtn;
    private Html indicator;
    private static final String INVALID_BTN_STYLE = " button-invalid ";

    protected abstract void onRunBtnClick();

    public SideBarContainer()
    {
        addStyleName("sidebar-panel");
        initBottomButtonBar();

        setScrollMode(Style.Scroll.AUTOY);
        setFrame(true);
        setLayout(new RowLayout());
        addStyleName(ClarityStyle.BOX_SHADOW);

        setBottomComponent(bottomActionBar);
    }

    private void initBottomButtonBar()
    {
        bottomActionBar = new ButtonBar();
        bottomActionBar.addStyleName(ClarityStyle.BUTTONBAR);
        bottomActionBar.setAlignment(Style.HorizontalAlignment.RIGHT);
        bottomActionBar.setSpacing(10);
        bottomActionBar.setMinButtonWidth(50);

        runBtn = new AutoMaskButton(BVC.run());
        runBtn.addStyleName(ClarityStyle.BUTTON_PRIMARY);
        runBtn.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                onRunBtnClick();
            }
        });
        bottomActionBar.add(runBtn);

        AbstractImagePrototype icon = Resource.ICONS.exclamationRed();
        indicator = new Html(icon.getHTML());
        indicator.setToolTip(BVC.btnSearchInvalidMsg(""));
        indicator.setVisible(false);
        bottomActionBar.add(indicator);
    }

    public static LayoutContainer createSection(String title, Component component)
    {
        LayoutContainer sectionContainer = new LayoutContainer(new RowLayout());
        sectionContainer.addStyleName("section-container");

        if (!Util.isEmptyString(title))
        {
            Html titleHtml = new Html(title);
            titleHtml.setStyleName("section-title");
            sectionContainer.add(titleHtml, new RowData(1, -1, new Margins(5, 20, 0, 10)));
        }
        FormLayout formLayout = new FormLayout();
        formLayout.setHideLabels(true);
        LayoutContainer layoutContainer = new LayoutContainer(formLayout);
        layoutContainer.add(component, FormUtils.getDefaultFormData());
        sectionContainer.add(layoutContainer, new RowData(1, -1, new Margins(5, 20, 5, 10)));
        return sectionContainer;
    }

    public void indicateRunnable(boolean isValid)
    {
        indicator.setVisible(!isValid);
        if (isValid)
        {
            runBtn.setStyleAttribute("background-color", "#FFF0F0");
        }
        else
        {
            runBtn.setStyleAttribute("background-color", "none");
        }
    }

    public void maskRunBtn()
    {
        runBtn.processMask();
    }

    public void maskRunBtn(String maskText)
    {
        runBtn.processMask(maskText);
    }

    public void unmaskBtn()
    {
        runBtn.processUnmask();
    }
}
