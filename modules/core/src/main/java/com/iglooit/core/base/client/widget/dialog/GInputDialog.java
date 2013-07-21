package com.iglooit.core.base.client.widget.dialog;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;

public class GInputDialog extends Dialog
{
    private GPanel display;

    private HasClickHandlersDialogButtonAdapter okButtonAdapter;
    private HasClickHandlersDialogButtonAdapter cancelButtonAdapter;
    private ToolTipConfig toolTip;

    public GInputDialog(GPanel display)
    {
        this.display = display;

        setLayout(new FitLayout());
        setButtonAlign(Style.HorizontalAlignment.RIGHT);
        setClosable(true);

        //setHeight("auto");
        //setAutoHeight(true);

        setModal(true);
        setHeading(display.getLabel());
        setButtons(Dialog.OKCANCEL);
        //standardise ok text to uppercase
        setTextForOkButton("OK");
        setHideOnButtonClick(true);
        getButtonById(Dialog.OK).addStyleName(ClarityStyle.BUTTON_PRIMARY);

        // There is a rendering issue with RowLayout/ColumnLayout in GXT.
        // www.sencha.com/forum/showthread.php?135571-GXT-2.2.4-rendering-problem-%28Source-and-screens-included%29
        addWindowListener(new WindowListener()
        {
            public void windowShow(WindowEvent we)
            {
                GInputDialog.this.display.layout(true);
            }
        });
    }

    @Override
    protected final void onRender(Element element, int i)
    {
        super.onRender(element, i);

        // setLayout(new FillLayout());

        add(display);
        layout(true);
    }

    protected GPanel getPanel()
    {
        return display;
    }

    public void setButtonBarToolTip(boolean enable, String title, String text)
    {
        if (toolTip == null)
        {
            toolTip = new ToolTipConfig(title, text);
            getButtonBar().setToolTip(toolTip);
        }
        else
        {
            toolTip.setTitle(title);
            toolTip.setText(text);
            getButtonBar().getToolTip().update(toolTip);
        }
        if (enable)
            getButtonBar().getToolTip().enable();
        else
            getButtonBar().getToolTip().disable();
    }

    public void disableOkButton()
    {
        getButtonById(Dialog.OK).disable();
    }

    public void enableOkButton()
    {
        getButtonById(Dialog.OK).enable();
    }

    public void disableCancelButton()
    {
        getButtonById(Dialog.CANCEL).disable();
    }

    public void enableCancelButton()
    {
        getButtonById(Dialog.CANCEL).enable();
    }

    public void setTextForOkButton(String text)
    {
        getButtonById(Dialog.OK).setText(text);
    }

    public void setTextForCancelButton(String text)
    {
        getButtonById(Dialog.CANCEL).setText(text);
    }

    public static class HasClickHandlersDialogButtonAdapter implements HasClickHandlers
    {
        private Button button;
        private HandlerManager handlerManager = new HandlerManager(null);

        public HasClickHandlersDialogButtonAdapter(Button button)
        {
            this.button = button;
        }

        public HandlerRegistration addClickHandler(final ClickHandler clickHandler)
        {
            final SelectionListener<ButtonEvent> selectionListener = new SelectionListener<ButtonEvent>()
            {
                public void componentSelected(ButtonEvent buttonEvent)
                {
                    clickHandler.onClick(new ClickEventWrapper());
                }
            };

            button.addSelectionListener(selectionListener);
            final HandlerRegistration reg = handlerManager.addHandler(ClickEvent.getType(), clickHandler);

            return new HandlerRegistration()
            {
                public void removeHandler()
                {
                    reg.removeHandler();
                    button.removeSelectionListener(selectionListener);
                }
            };
        }

        public void fireEvent(GwtEvent<?> gwtEvent)
        {
            handlerManager.fireEvent(gwtEvent);
        }

        private static class ClickEventWrapper extends ClickEvent
        {

        }
    }

    public HasClickHandlersDialogButtonAdapter getOkButton()
    {
        if (okButtonAdapter == null)
            okButtonAdapter = new HasClickHandlersDialogButtonAdapter(getButtonById(Dialog.OK));
        return okButtonAdapter;
    }

    public HasClickHandlersDialogButtonAdapter getCancelButton()
    {
        if (cancelButtonAdapter == null)
            cancelButtonAdapter = new HasClickHandlersDialogButtonAdapter(getButtonById(Dialog.CANCEL));
        return cancelButtonAdapter;
    }

    public void setCloseButtonListener(final DialogCloseButtonListener listener)
    {
        addWindowListener(new WindowListener()
        {
            public void windowShow(WindowEvent we)
            {
                closeBtn.removeAllListeners();
                closeBtn.addListener(Events.Select, listener);
            }
        });
    }

    public interface DialogCloseButtonCallback
    {
        void dialogCloseConfirmed(boolean flag);
    }

    public abstract static class DialogCloseButtonListener implements Listener<ComponentEvent>
    {
        private final DialogCloseButtonCallback callback;

        public DialogCloseButtonListener(final GInputDialog dialog)
        {
            callback = new DialogCloseButtonCallback()
            {
                public void dialogCloseConfirmed(boolean confirmed)
                {
                    if (confirmed)
                        dialog.hide();
                }
            };
        }

        public void handleEvent(ComponentEvent ce)
        {
            preClose(callback);
        }

        protected abstract void preClose(DialogCloseButtonCallback callback);
    }
}
