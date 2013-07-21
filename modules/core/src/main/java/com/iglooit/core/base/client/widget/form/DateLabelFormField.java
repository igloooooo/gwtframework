package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.widget.OutputDateFormats;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

public class DateLabelFormField extends LabelFormField
{
    private DateTimeFormat format;

    public DateLabelFormField()
    {
        throw new UnsupportedOperationException("Please add the format parameter");
    }

    public DateLabelFormField(OutputDateFormats format)
    {
        super();
        if (format == null)
        {
            throw new IllegalArgumentException();
        }

        this.format = format.toDateTimeFormat();
    }

    @Override
    public void setValue(Object value)
    {
        if (value == null)
        {
            setText("-");
        }
        else if (value instanceof Date)
        {
            setText(this.format.format((Date)value));
        }
        else
        {
            throw new IllegalArgumentException("Expected Date, found " + value.getClass().toString());
        }
    }
}
