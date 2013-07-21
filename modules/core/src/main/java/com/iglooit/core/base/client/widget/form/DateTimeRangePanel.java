package com.iglooit.core.base.client.widget.form;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.util.TimeUtil;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.OutputDateFormats;
import com.clarity.core.base.iface.domain.DateTimeRangeInfo;
import com.clarity.core.lib.iface.BssTimeUtil;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 22/06/12 Time: 10:13 AM
 */
public class DateTimeRangePanel extends LayoutContainer
{
    public static final EventType RANGEFORMATCHANGED = new EventType();
    private ToggleButton specificTimeToggle;
    private ToggleButton relativeTimeToggle;

    private CardLayout rangeSelectionCards;
    private HorizontalPanel relativeTimeCard;
    private LayoutContainer specificTimeCard;
    private ToggleButtonGroup tg;
    private LayoutContainer cardContainer;
    private SetValidationHandler specificTimeValidationHandler;
    private SetValidationHandler relativeTimeValidationHandler;
    private boolean isRelativeDirectionReadonly = true;

    public enum RangeFormat
    {
        Relative,
        Specific
    }

    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private DateField fromDateField;
    private SplitTimeField fromTimeField;
    private DateField toDateField;
    private SplitTimeField toTimeField;

    private NumberField relativeTimeField;
    private SimpleComboBox<TimeUtil.TimeUnit> relativeTimeUnitField;
    private SimpleComboBox<DateTimeRangeInfo.RelativeTimeDirection> relativeTimeDirectionField;
    private LabelFormField<DateTimeRangeInfo.RelativeTimeDirection> relativeTimeDirectionReadonly;

    private RangeFormat rangeFormat;

    public DateTimeRangePanel(List<TimeUtil.TimeUnit> relativeTimeUnitList)
    {
        fromDateField = new DateField();
        fromDateField.setFieldLabel(BVC.fromDate());
        fromDateField.getPropertyEditor().setFormat(OutputDateFormats.DATE.toDateTimeFormat());
        fromDateField.getImages().setInvalid(Resource.ICONS.exclamationRed());
        fromTimeField = new SplitTimeField(BVC.fromTime(), SplitTimeField.Format.HHMM);
        toDateField = new DateField();
        toDateField.setFieldLabel(BVC.toDate());
        toDateField.getPropertyEditor().setFormat(OutputDateFormats.DATE.toDateTimeFormat());
        toDateField.getImages().setInvalid(Resource.ICONS.exclamationRed());
        toTimeField = new SplitTimeField(BVC.toTime(), SplitTimeField.Format.HHMM);

        relativeTimeField = new NumberField()
        {
            @Override
            protected void onKeyPress(FieldEvent fe)
            {
                super.onKeyPress(fe);
                // implementation in super.super is to fire an event
//                fireEvent(Events.KeyPress, new FieldEvent(this, fe.getEvent()));
//
//                // no need to test for special keys since they are not raised as keyPressed
//                char key = getChar(fe.getEvent());
//                if (!allowed.contains(key))
//                {
//                    fe.stopEvent();
//                }
            }

            // copied from spinnerfield
            private native char getChar(NativeEvent e) /*-{
              return e.which || e.charCode || e.keyCode || 0;
            }-*/;

        };
        relativeTimeField.setFormat(NumberFormat.getFormat("##0"));
        relativeTimeField.setAllowDecimals(false);
        relativeTimeField.setAllowNegative(false);
        relativeTimeField.setWidth(35);
        relativeTimeField.getImages().setInvalid(Resource.ICONS.blank());
        relativeTimeUnitField = new SimpleComboBox<TimeUtil.TimeUnit>();
        relativeTimeUnitField.setEditable(false);
        relativeTimeUnitField.setWidth(75);
        relativeTimeUnitField.add(relativeTimeUnitList);
        relativeTimeUnitField.setTriggerAction(ComboBox.TriggerAction.ALL);
        relativeTimeDirectionField = new SimpleComboBox<DateTimeRangeInfo.RelativeTimeDirection>();
        relativeTimeDirectionField.setWidth(65);
        relativeTimeDirectionField.setTriggerAction(ComboBox.TriggerAction.ALL);
        relativeTimeDirectionField.setEditable(false);
        relativeTimeDirectionField.add(Arrays.asList(DateTimeRangeInfo.RelativeTimeDirection.values()));
        relativeTimeDirectionReadonly = new LabelFormField<DateTimeRangeInfo.RelativeTimeDirection>();

        Listener<BaseEvent> bubbleListener = new Listener<BaseEvent>()
        {
            @Override
            public void handleEvent(BaseEvent be)
            {
                fireEvent(Events.Change);
            }
        };

        // manually bubble events
        fromDateField.addListener(Events.Change, bubbleListener);
        fromDateField.addListener(Events.Select, bubbleListener);
        fromTimeField.addListener(Events.Change, bubbleListener);
        toDateField.addListener(Events.Change, bubbleListener);
        toDateField.addListener(Events.Select, bubbleListener);
        toTimeField.addListener(Events.Change, bubbleListener);
        relativeTimeField.addListener(Events.Change, bubbleListener);
        relativeTimeUnitField.addListener(Events.Change, bubbleListener);
        relativeTimeUnitField.addListener(Events.Select, bubbleListener);

        relativeTimeValidationHandler = new SetValidationHandler(relativeTimeField);
        specificTimeValidationHandler = new SetValidationHandler(fromDateField, fromTimeField,
            toDateField, toTimeField);

        doOnCreateControls();
    }

    public void doOnCreateControls()
    {
        relativeTimeCard = new HorizontalPanel();
        relativeTimeCard.setSpacing(5);
        if (isRelativeDirectionReadonly)
            relativeTimeCard.add(relativeTimeDirectionReadonly);
        else
            relativeTimeCard.add(relativeTimeDirectionField);

        relativeTimeCard.add(relativeTimeField);
        relativeTimeCard.add(relativeTimeUnitField);
        relativeTimeCard.setAutoHeight(true);

        FormData fd = new FormData("100%");
        FormLayout fl = new FormLayout(FormPanel.LabelAlign.LEFT);
        specificTimeCard = new LayoutContainer(fl);
        // fix to possibly a bug in gxt
        specificTimeCard.setStyleAttribute("clear", "left");

        specificTimeCard.add(fromDateField, fd);
        specificTimeCard.add(fromTimeField, fd);
        specificTimeCard.add(toDateField, fd);
        specificTimeCard.add(toTimeField, fd);
        specificTimeCard.setAutoWidth(true);

        rangeSelectionCards = new CardLayout();

        cardContainer = new LayoutContainer(rangeSelectionCards);
        cardContainer.setWidth("100%");
        cardContainer.add(relativeTimeCard);
        cardContainer.add(specificTimeCard);

        relativeTimeToggle = new ToggleButton(BVC.relativeDateTimeText());
        specificTimeToggle = new ToggleButton(BVC.specificDateTimeText());

        Listener<ButtonEvent> toggleButtonListener = new Listener<ButtonEvent>()
        {
            @Override
            public void handleEvent(ButtonEvent be)
            {
                ToggleButton tb = (ToggleButton)be.getButton();
                boolean newIsRelative = tb == relativeTimeToggle;
                boolean isChanged = newIsRelative != (rangeFormat == RangeFormat.Relative);
                tb.toggle(true);

                if (isChanged)
                {
                    if (newIsRelative)
                        rangeFormat = RangeFormat.Relative;
                    else
                        rangeFormat = RangeFormat.Specific;
                    updateRangeFormatSelectionDisplay();

                    validateValue();
                    fireEvent(RANGEFORMATCHANGED, new BaseEvent(be.getButton()));
                    fireEvent(Events.Change, new BaseEvent(be.getButton()));
                }
            }
        };

        relativeTimeToggle.addListener(Events.OnClick, toggleButtonListener);
        specificTimeToggle.addListener(Events.OnClick, toggleButtonListener);

        tg = new ToggleButtonGroup("dateRangeGroup");
        tg.add(relativeTimeToggle);
        tg.add(specificTimeToggle);

        addListener(RANGEFORMATCHANGED, new Listener<BaseEvent>()
        {
            @Override
            public void handleEvent(BaseEvent be)
            {
                if (be.getSource() == DateTimeRangePanel.this)
                    updateRangeFormatSelectionDisplay();
            }
        });
    }

    private void validateValue()
    {
        if (isDateTimeRangeValid())
            fireEvent(Events.Valid);
        else
            fireEvent(Events.Invalid);
    }

    public DateTimeRangePanel()
    {
        this(Arrays.asList(TimeUtil.TimeUnit.DAYS, TimeUtil.TimeUnit.WEEKS, TimeUtil.TimeUnit.MONTH));
    }

    public void setTimeUnits(List<TimeUtil.TimeUnit> units)
    {
        relativeTimeUnitField.removeAll();
        relativeTimeUnitField.add(units);
    }

    @Override
    protected void onRender(Element parent, int index)
    {
        super.onRender(parent, index);
        if (isRelativeDirectionReadonly == (relativeTimeDirectionReadonly.getParent() != null))
        {
            relativeTimeDirectionField.removeFromParent();
            relativeTimeCard.insert(relativeTimeDirectionReadonly, 0);
        }
        else
        {
            relativeTimeDirectionReadonly.removeFromParent();
            relativeTimeCard.insert(relativeTimeDirectionField, 0);
        }

        RowData rd = new RowData(1, -1);
        add(tg, rd);
        add(cardContainer, rd);

        addStyleName("x-form-field-wrap");
    }

    private void updateRangeFormatSelectionDisplay()
    {
        if (rangeFormat == RangeFormat.Relative)
        {
            relativeTimeToggle.toggle(true);
            rangeSelectionCards.setActiveItem(relativeTimeCard);
            specificTimeToggle.toggle(false);
        }
        else
        {
            relativeTimeToggle.toggle(false);
            specificTimeToggle.toggle(true);
            rangeSelectionCards.setActiveItem(specificTimeCard);
        }
    }

    public DateField getFromDateField()
    {
        return fromDateField;
    }

    public SplitTimeField getFromTimeField()
    {
        return fromTimeField;
    }

    public DateField getToDateField()
    {
        return toDateField;
    }

    public SplitTimeField getToTimeField()
    {
        return toTimeField;
    }

    public NumberField getRelativeTimeField()
    {
        return relativeTimeField;
    }

    public SimpleComboBox<TimeUtil.TimeUnit> getRelativeTimeUnitField()
    {
        return relativeTimeUnitField;
    }

    public TimeUtil.TimeUnit getRelativeTimeUnit()
    {
        return relativeTimeUnitField.getSimpleValue();
    }

    public void setRelativeTimeUnit(TimeUtil.TimeUnit relativeTimeUnitField)
    {
        this.relativeTimeUnitField.setSimpleValue(relativeTimeUnitField);
    }

    public RangeFormat getRangeFormat()
    {
        return rangeFormat;
    }

    public void setRangeFormat(RangeFormat rangeFormat)
    {
        this.rangeFormat = rangeFormat;
        fireEvent(RANGEFORMATCHANGED, new BaseEvent(this));
    }

    public boolean isRelativeDirectionReadonly()
    {
        return isRelativeDirectionReadonly;
    }

    public void setRelativeDirectionReadonly(boolean relativeDirectionReadonly)
    {
        isRelativeDirectionReadonly = relativeDirectionReadonly;
    }

    public boolean isEmpty()
    {
        if (rangeFormat == RangeFormat.Relative)
            return relativeTimeField.getValue() == null;
        else
        {
            return fromDateField.getValue() == null && toDateField.getValue() == null;
        }
    }

    public SimpleComboBox<DateTimeRangeInfo.RelativeTimeDirection> getRelativeTimeDirectionField()
    {
        return relativeTimeDirectionField;
    }

    public LabelFormField<DateTimeRangeInfo.RelativeTimeDirection> getRelativeTimeDirectionReadonly()
    {
        return relativeTimeDirectionReadonly;
    }

    public boolean isDateTimeRangeValid()
    {
        boolean isValid;

        if (rangeFormat == RangeFormat.Relative)
            isValid = relativeTimeValidationHandler.isValid();
        else
            isValid = specificTimeValidationHandler.isValid();

        // Second check to make sure the invariance of from date < to date is observed
        // in the absence of binding and validators provided by a domain entity.
        // In other words, validators in the domain entity can only further limit
        // the range of valid values
        return isValid && internalIsDateTimeRangeValid();
    }

    /**
     * Check to see whether from date is always before to date
     */
    private boolean internalIsDateTimeRangeValid()
    {
        if (rangeFormat == RangeFormat.Relative)
            return relativeTimeField.getValue() == null || relativeTimeField.getValue().intValue() > 0;
        else
        {
            if (fromDateField.getValue() == null || toDateField.getValue() == null)
                return true;

            if (fromDateField.getValue() != null && toDateField.getValue() != null)
            {

                int compareDate = fromDateField.getValue().compareTo(toDateField.getValue());
                if (compareDate < 0)
                    return true;
                else if (compareDate > 0)
                    return false;

                Date fromTime = fromTimeField.getValue();
                Date toTime = toTimeField.getValue();

                if (toTime == null || toTime.equals(BssTimeUtil.getBeginningOfDay(toTime)))
                    return false;

                if (fromTime != null)
                    return fromTime.before(toTime);
                else
                    return true;
            }
            else
                // Only fromDate or toDate has value so it is valid
                return true;
        }
    }

    private final class SetValidationHandler implements Listener<BaseEvent>
    {
        private HashSet<Field> invalidSet = new HashSet<Field>();

        public SetValidationHandler(Field... fields)
        {
            for (Field f : fields)
            {
                f.addListener(Events.Invalid, this);
                f.addListener(Events.Valid, this);
            }
        }

        @Override
        public void handleEvent(BaseEvent be)
        {
            if (be.getType() == Events.Invalid)
            {
                int oldCount = invalidSet.size();
                invalidSet.add((Field)be.getSource());
                if (oldCount != invalidSet.size())
                    fireEvent(Events.Invalid);
            }
            else
            {
                invalidSet.remove(be.getSource());
                if (invalidSet.size() == 0)
                    fireEvent(Events.Valid);
            }
        }

        public boolean isValid()
        {
            return invalidSet.size() == 0;
        }
    }
}
