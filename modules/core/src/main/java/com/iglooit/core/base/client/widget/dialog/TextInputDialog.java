package com.iglooit.core.base.client.widget.dialog;

import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public abstract class TextInputDialog implements Display
{
    private String title;
    private TextField<String> field;
    private GInputDialog inputDialog;

    public TextInputDialog(final String title, final String fieldName)
    {
        this.title = title;
        field = new TextField<String>();
        inputDialog = new GInputDialog(new GPanel()
        {
            @Override
            public void doOnRender(Element element, int i)
            {
                setLayout(new VBoxLayout());
                FormPanel fp = new FormPanel();
                fp.setHeaderVisible(false);
                field.setFieldLabel(fieldName);
                fp.add(field);
                add(fp);
                layout();
            }

            @Override
            public String getLabel()
            {
                return title;
            }
        });

        inputDialog.getOkButton().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent clickEvent)
            {
                onOk(field.getValue());
            }
        });

        inputDialog.show();
    }

    public Widget asWidget()
    {
        return inputDialog;
    }

    public String getLabel()
    {
        return title;
    }

    protected abstract void onOk(String field);
}
