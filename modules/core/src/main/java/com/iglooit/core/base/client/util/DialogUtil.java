package com.iglooit.core.base.client.util;

import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

import java.util.List;

public class DialogUtil
{
    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    public static void showError(String msg)
    {
        showError(null, msg);
    }

    public static void showError(String heading, String msg)
    {
         showError(heading, msg, false);
    }

    public static void showError(String heading, String msg, boolean modal)
    {
        MessageBoxHtml msgBox = new MessageBoxHtml(msg, MessageBoxHtml.MessageType.ERROR);
        msgBox.enableAutoClose(false);
        msgBox.enableFadeHighlight(false);

        Dialog dialog = new Dialog();
        dialog.setModal(modal);
        dialog.setLayout(new RowLayout());
        dialog.setResizable(false);
        if (heading != null)
        {
            dialog.setHeading(heading);
        }

        dialog.setButtons(Dialog.OK);
        dialog.setButtonAlign(Style.HorizontalAlignment.RIGHT);
        dialog.setHideOnButtonClick(true);
        dialog.add(msgBox, new RowData(5, 5, new Margins(10, 10, 10, 10)));
        localizeDialogButtons(dialog);
        dialog.show();
    }

    public static void showErrors(String heading, List<String> msgs, boolean modal)
    {
        if (msgs == null || msgs.isEmpty())
            return;
        if (msgs.size() == 1)
        {
            showError(heading, msgs.get(0), modal);
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setModal(modal);
        dialog.setLayout(new RowLayout());
        dialog.setResizable(false);
        if (heading != null)
        {
            dialog.setHeading(heading);
        }

        dialog.setButtons(Dialog.OK);
        dialog.setButtonAlign(Style.HorizontalAlignment.RIGHT);
        dialog.setHideOnButtonClick(true);
        StringBuilder sb = new StringBuilder();
        sb.append(msgs.size()).append(" errors found:")
            .append("<br><ul style='margin-left: 30px'>");
        for (String msg : msgs)
        {
            sb.append("<li type='disc'>")
                .append(msg)
                .append("</li>");
        }
        sb.append("</ul");
        MessageBoxHtml msgBox = new MessageBoxHtml(sb.toString(), MessageBoxHtml.MessageType.ERROR);
        msgBox.enableAutoClose(false);
        msgBox.enableFadeHighlight(false);
        dialog.add(msgBox, new RowData(5, 5, new Margins(10, 10, 10, 10)));
        localizeDialogButtons(dialog);
        dialog.show();
    }

    public static void localizeDialogButtons(Dialog dlg)
    {
        if (dlg == null)
            return;
        Button okBtn = dlg.getButtonById(Dialog.OK);
        if (okBtn != null)
            okBtn.setText(BVC.ok());
        Button cancelBtn = dlg.getButtonById(Dialog.CANCEL);
        if (cancelBtn != null)
            cancelBtn.setText(BVC.cancel());
        Button yesBtn = dlg.getButtonById(Dialog.YES);
        if (yesBtn != null)
            yesBtn.setText(BVC.yes());
        Button closeBtn = dlg.getButtonById(Dialog.CLOSE);
        if (closeBtn != null)
            closeBtn.setText(BVC.close());
        Button noBtn = dlg.getButtonById(Dialog.NO);
        if (noBtn != null)
            noBtn.setText(BVC.no());
    }

    public static void showSuccess(String heading, String msg)
    {
        MessageBoxHtml msgBox = new MessageBoxHtml(msg, MessageBoxHtml.MessageType.SUCCESS);
        msgBox.enableAutoClose(false);
        msgBox.enableFadeHighlight(false);

        Dialog dialog = new Dialog();
        dialog.setLayout(new RowLayout());
        dialog.setModal(true);
        dialog.setResizable(false);
        if (heading != null)
        {
            dialog.setHeading(heading);
        }

        dialog.setButtons(Dialog.OK);
        dialog.setButtonAlign(Style.HorizontalAlignment.RIGHT);
        dialog.setHideOnButtonClick(true);
        dialog.add(msgBox, new RowData(5, 5, new Margins(10, 10, 10, 10)));
        localizeDialogButtons(dialog);
        dialog.show();
    }

    /**
     * its a yes no dialog where, default implementation of no is to hide dialog & yes is left for users to implement
     * @param heading
     * @param msg
     * @return
     */
    public static Dialog getSuccessWithNavigationDialog(String heading, String msg)
    {
        MessageBoxHtml msgBox = new MessageBoxHtml(msg, MessageBoxHtml.MessageType.SUCCESS);
        msgBox.enableAutoClose(false);
        msgBox.enableFadeHighlight(false);

        final Dialog dialog = new Dialog();
        dialog.setLayout(new RowLayout());
        dialog.setModal(true);
        dialog.setWidth(365);
        dialog.setResizable(false);
        if (heading != null)
            dialog.setHeading(heading);

        dialog.setButtons(Dialog.YESNO);
        dialog.getButtonById(Dialog.NO).addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                dialog.hide();
            }
        });
        dialog.setButtonAlign(Style.HorizontalAlignment.RIGHT);
        dialog.setHideOnButtonClick(true);
        dialog.add(msgBox, new RowData(5, 5, new Margins(10, 10, 10, 10)));
        DialogUtil.localizeDialogButtons(dialog);

        return dialog;
    }

    /**
     * its a yes no dialog where, default implementation of no is to hide dialog & yes is left for users to implement
     * @param heading
     * @param msg
     * @return
     */
    public static Dialog getInfoNavigationDialog(String heading, String msg)
    {
        MessageBoxHtml msgBox = new MessageBoxHtml(msg, MessageBoxHtml.MessageType.INFO);
        msgBox.enableAutoClose(false);
        msgBox.enableFadeHighlight(false);

        final Dialog dialog = new Dialog();
        dialog.setLayout(new RowLayout());
        dialog.setModal(true);
        dialog.setWidth(365);
        dialog.setResizable(false);
        if (heading != null)
            dialog.setHeading(heading);

        dialog.setButtons(Dialog.YESNO);
        dialog.getButtonById(Dialog.NO).addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                dialog.hide();
            }
        });
        dialog.setButtonAlign(Style.HorizontalAlignment.RIGHT);
        dialog.setHideOnButtonClick(true);
        dialog.add(msgBox, new RowData(5, 5, new Margins(10, 10, 10, 10)));
        DialogUtil.localizeDialogButtons(dialog);

        return dialog;
    }


}
