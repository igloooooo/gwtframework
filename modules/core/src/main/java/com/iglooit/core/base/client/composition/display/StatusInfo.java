package com.iglooit.core.base.client.composition.display;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.base.client.util.DialogUtil;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Accessibility;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.ArrayList;
import java.util.Stack;

public class StatusInfo extends Info
{
    protected static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private static Stack<StatusInfo> infoStack = new Stack<StatusInfo>();
    private static ArrayList<StatusInfo> slots = new ArrayList<StatusInfo>();
    private InfoConfig config;
    private AbstractImagePrototype icon;
    private int level;
    public static final String STYLE_INFO = "status-info-display info-type-info";
    public static final String STYLE_SUCCESS = "status-info-display info-type-success";
    public static final String STYLE_ERROR = "status-info-display info-type-error";
    public static final String STYLE_WARNING = "status-info-display info-type-warning";

    public StatusInfo()
    {
        super();
    }

    public StatusInfo(String style)
    {
        this(style, null);
    }

    public StatusInfo(String style, AbstractImagePrototype icon)
    {
        super();
        addStyleName(style);
        this.icon = icon;
    }

    @Override
    protected Point position()
    {
        Size s = XDOM.getViewportSize();
        int left = s.width / 2 - config.width / 2 - 10 + XDOM.getBodyScrollLeft();
        int top = /*s.height - config.height - 10 - */20 + (level * (config.height + 10)) +
            XDOM.getBodyScrollTop();
        return new Point(left, top);
    }

    public static void display(String title, String text)
    {
        InfoConfig infoCfg = new InfoConfig(title, text);
        infoCfg.display = 3000;
        pop().show(infoCfg);
    }

    public static void displayWithStyle(String title, String text, String style)
    {
        InfoConfig infoCfg = new InfoConfig(title, text);
        infoCfg.display = 3000;
        pop(style).show(infoCfg);
    }

    public static void displayStaticDialog(String title, String text, MessageBoxHtml.MessageType type)
    {
        MessageBoxHtml msgBox = new MessageBoxHtml(text, type);
        msgBox.enableAutoClose(false);
        msgBox.enableFadeHighlight(false);

        Dialog staticInfoDialog = new Dialog();
        staticInfoDialog.setLayout(new RowLayout());
        staticInfoDialog.setButtons(Dialog.OK);
        staticInfoDialog.setModal(true);
        staticInfoDialog.setHideOnButtonClick(true);
        staticInfoDialog.setHeading(title);
        staticInfoDialog.add(msgBox, new RowData(5, 5, new Margins(10, 10, 10, 10)));
        DialogUtil.localizeDialogButtons(staticInfoDialog);

        staticInfoDialog.show();
    }

    public static void displayWithStylePlusIcon(String title, String text,
                                                String style, AbstractImagePrototype icon)
    {
        InfoConfig infoCfg = new InfoConfig(title, text);
        infoCfg.display = 3000;
        pop(style, icon).show(infoCfg);
    }

    private static StatusInfo newStaticInfo(String style)
    {
        StatusInfo info = new StatusInfo(style);
        return info;
    }

    private static StatusInfo pop()
    {
        StatusInfo info = infoStack.size() > 0 ? (StatusInfo)infoStack.pop() : null;
        if (info == null)
        {
            info = new StatusInfo();
        }
        return info;
    }

    private static StatusInfo pop(String style)
    {
        StatusInfo info = infoStack.size() > 0 ? (StatusInfo)infoStack.pop() : null;
//        if (info == null || !info.getStyleName().equals(style))
        if (info == null)
        {
            info = new StatusInfo(style);
        }
        else
        {

        }
        return info;
    }

    private static StatusInfo pop(String style, AbstractImagePrototype icon)
    {
        StatusInfo info = infoStack.size() > 0 ? (StatusInfo)infoStack.pop() : null;
//        if (info == null || !info.getStyleName().equals(style))
        if (info == null)
        {
            info = new StatusInfo(style, icon);
        }
        return info;
    }

    private static void push(StatusInfo info)
    {
        infoStack.push(info);
    }

    public void show(InfoConfig config)
    {
        this.config = config;
        onShowInfo();
    }


    protected void onShowInfo()
    {
        RootPanel.get().add(this);
        el().makePositionable(true);

        setTitle();
        removeAll();
        if (icon != null)
            setIcon(icon);
        setText();

        level = firstAvail();
        slots.add(level, this);

        Point p = position();
        el().setLeftTop(p.x, p.y);
        setWidth(config.width);
        setAutoHeight(true);

        if (GXT.isAriaEnabled())
        {
            Accessibility.setState(getElement(), "aria-live", config.title + " " + config.text);
        }

        afterShow();
    }

    //todo jm->pm -- refactor these out and use the parents if possible
    protected void afterHide()
    {
        RootPanel.get().remove(this);
        slots.set(level, null);
        push(this);
    }

    //todo jm->pm -- refactor these out and use the parents if possible
    protected void afterShow()
    {
        Timer t = new Timer()
        {
            public void run()
            {

                afterHide();
            }
        };
        if (config.display > 0)
            t.schedule(config.display);
    }

    private void setText()
    {
        if (config.text != null)
        {
            if (config.params != null)
            {
                config.text = Format.substitute(config.text, config.params);
            }
//            removeAll();
            addText(config.text);
        }
    }

    private void setTitle()
    {
        if (config.title != null)
        {
            head.setVisible(true);
            if (config.params != null)
            {
                config.title = Format.substitute(config.title, config.params);
            }
            setHeading(config.title);
        }
        else
        {
            head.setVisible(false);
        }
    }

    private static int firstAvail()
    {
        int size = slots.size();
        for (int i = 0; i < size; i++)
        {
            if (slots.get(i) == null)
            {
                return i;
            }
        }
        return size;
    }

    public static void popupMessage(String title, String message, MessageBoxHtml.MessageType messageType)
    {
        InfoConfig infoCfg = new InfoConfig(title, message);
        StatusInfo info;
        switch (messageType)
        {
            case SUCCESS:
                info = new StatusInfo(STYLE_SUCCESS, Resource.ICONS.tick());
                break;
            case ERROR:
                info = new StatusInfo(STYLE_ERROR);
                break;
            case WARNING:
                info = new StatusInfo(STYLE_WARNING);
                break;
            case INFO:
            default:
                info = new StatusInfo(STYLE_INFO);
                break;
        }
        info.show(infoCfg);
    }
}
