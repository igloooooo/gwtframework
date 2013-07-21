package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.DisplayField;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;

public class TitledDisplayField implements DisplayField
{
    public Label getTitle()
    {
        return title;
    }

    private final Label title;
    private final Label value;

    public TitledDisplayField(String stitle)
    {
        title = new Label(stitle);
        value = new Label();
    }

    public String getValue()
    {
        return value.getText();
    }

    public void setValue(String s)
    {
        value.setText(s);
    }

    public Label getField()
    {
        return value;
    }

    public void setValue(String s, boolean b)
    {
        value.setText(s);
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> stringValueChangeHandler)
    {
        return null;
    }

    public void fireEvent(GwtEvent<?> gwtEvent)
    {

    }
}
