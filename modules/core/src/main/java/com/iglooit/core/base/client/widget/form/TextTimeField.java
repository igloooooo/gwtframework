package com.iglooit.core.base.client.widget.form;

import com.clarity.commons.iface.util.StringUtil;
import com.clarity.commons.iface.util.TimeUtil;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.combobox.ClarityComboBox;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;

public class TextTimeField
{
    private static final BaseViewConstants BVC = GWT.create(BaseViewConstants.class);

    private NumberField hours;
    private NumberField minutes;
    private NumberField seconds;
    private ClarityComboBox<String> ampmCombo;
    private LayoutContainer layoutContainer;
    private AdapterField adapterField;
    private TimeMode timeMode;

    public void setDefault()
    {
        if (hours.getValue() == null)
            hours.setValue(0);
        if (minutes.getValue() == null)
            minutes.setValue(0);
        if (seconds.getValue() == null)
            seconds.setValue(0);
    }

    public static class ClarityNumberFieldBlankNoteImage extends NumberField
    {
        public ClarityNumberFieldBlankNoteImage()
        {
            super();
            this.getImages().setInvalid(Resource.ICONS.blank());
            setStyleAttribute("position", "relative");
            setPropertyEditorType(Integer.class);
            setMinValue(0);
            setMaxValue(59);
            setMaxLength(2);
            setWidth(25);
            setHeight(22);
            setAllowNegative(false);
            setAllowDecimals(false);
            setAutoValidate(true);
            setAllowBlank(false);
            setValidateOnBlur(true);
        }
    }

    public void setTimeMode(TimeMode mode)
    {
        timeMode = mode;
        reLayoutByMode();
    }

    private void reLayoutByMode()
    {
        LabelField colon1 = new LabelField(":");
        LabelField colon2 = new LabelField(":");

        // hour max value && am/pm combo populate
        if (timeMode == TimeMode.HHMMSS24 || timeMode == TimeMode.HHMM24)
        {
            hours.setMaxValue(23);
        }
        else
        {
            hours.setMaxValue(11);
        }

        int defaultWidth = 80;

        // hh mm ss fields populate
        layoutContainer.removeAll();
        RowData rd = new RowData(-1, -1);
        layoutContainer.add(hours, rd);
        layoutContainer.add(colon1, rd);
        layoutContainer.add(minutes, rd);
        if (timeMode == TimeMode.HHMMSS24 || timeMode == TimeMode.HHMMSS12)
        {
            layoutContainer.add(colon2, rd);
            layoutContainer.add(seconds, rd);
            defaultWidth += 30;
        }

        if (timeMode == TimeMode.HHMMSS12 || timeMode == TimeMode.HHMM12)
        {
            layoutContainer.add(ampmCombo, new RowData(-1, -1, new Margins(0, 0, 0, 5)));
            defaultWidth += 50;
        }

        adapterField.setWidth(defaultWidth);
    }

    public boolean isValueValid()
    {
        long value = getValue();
        boolean hValid = !hours.isVisible() || hours.isValid();
        boolean mValid = !minutes.isVisible() || minutes.isValid();
        boolean sValid = !seconds.isVisible() || seconds.isValid();
        return (hValid && mValid && sValid);
    }

    public TimeMode getTimeMode()
    {
        return timeMode;
    }

    /* format need to be hhmmss
    * and exception need to be handled for bad string */
    public void setValue(String time) throws Exception
    {
        if (StringUtil.isNotBlank(time) && time.length() >= 2)
        {
            String hrStr = time.substring(0, 2);
            hours.setValue(Integer.valueOf(hrStr));
        }
        if (StringUtil.isNotBlank(time) && time.length() >= 4)
        {
            String minStr = time.substring(2, 4);
            minutes.setValue(Integer.valueOf(minStr));
        }
        if (StringUtil.isNotBlank(time) && time.length() >= 6 && seconds.isAttached())
        {
            String secStr = time.substring(4, 6);
            seconds.setValue(Integer.valueOf(secStr));
        }
    }

    public TextTimeField()
    {
        NumberFormat nf = NumberFormat.getFormat("00");
        hours = new ClarityNumberFieldBlankNoteImage();
        hours.setEmptyText("hh");
        hours.setFormat(nf);
        minutes = new ClarityNumberFieldBlankNoteImage();
        minutes.setEmptyText("mm");
        minutes.setFormat(nf);
        seconds = new ClarityNumberFieldBlankNoteImage();
        seconds.setFormat(nf);
        seconds.setEmptyText("ss");

        ampmCombo = new ClarityComboBox<String>();
        ampmCombo.add(BVC.timeAM());
        ampmCombo.add(BVC.timePM());
        ampmCombo.setWidth(50);
        ampmCombo.setForceSelection(true);
        ampmCombo.setEditable(false);
        ampmCombo.setAllowBlank(false);
        ampmCombo.selectFirst();

        layoutContainer = new LayoutContainer(new RowLayout(Style.Orientation.HORIZONTAL));
        adapterField = new AdapterField(layoutContainer);
        adapterField.setHeight(30);

        // default to be hhmmss24
        setTimeMode(TimeMode.HHMMSS24);
    }

    public long getValue()
    {
        if (hours.getValue() == null)
            hours.setValue(0);
        if (minutes.getValue() == null)
            minutes.setValue(0);
        if (seconds.getValue() == null)
            seconds.setValue(0);

        long extraHour = 0L;
        if (timeMode == TimeMode.HHMMSS12 || timeMode == TimeMode.HHMM12)
        {
            if (ampmCombo.getValue().getValue().equalsIgnoreCase(BVC.timePM()))
                extraHour += 12 * TimeUtil.TimeUnit.HOURS.getMilliSeconds();
        }

        return TimeUtil.TimeUnit.HOURS.getMilliSeconds() * hours.getValue().longValue() + extraHour +
                TimeUtil.TimeUnit.MINUTES.getMilliSeconds() * minutes.getValue().longValue() +
                TimeUtil.TimeUnit.SECONDS.getMilliSeconds() * seconds.getValue().longValue();
    }

    public Number getHours()
    {
        return hours.getValue();
    }

    public Number getMinutes()
    {
        return minutes.getValue();
    }

    public Number getSeconds()
    {
        return seconds.getValue();
    }

    public AdapterField getAdapterField()
    {
        return adapterField;
    }

    public void clearData()
    {
        hours.clear();
        minutes.clear();
        seconds.clear();
    }

    public void validate()
    {
        hours.validate();
        minutes.validate();
        seconds.validate();
    }

    public enum TimeMode
    {
        HHMM24,
        HHMMSS24,
        HHMM12,
        HHMMSS12
    }

    public void setMinutesEnabled(boolean enabled)
    {
        minutes.setEnabled(enabled);
    }

    public void setHoursEnabled(boolean enabled)
    {
        hours.setEnabled(enabled);
    }

    public void layout(boolean force)
    {
        if (hours.isVisible())
        {
            hours.setWidth(25);
            hours.setHeight(22);
        }
        if (minutes.isVisible())
        {
            minutes.setWidth(25);
            minutes.setHeight(22);
        }
        if (seconds.isVisible())
        {
            seconds.setWidth(25);
            seconds.setHeight(22);
        }

        layoutContainer.setHeight(22);
        layoutContainer.layout(force);
    }
}
