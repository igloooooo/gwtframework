package com.iglooit.core.base.client.view.jgauges;

import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.clarity.commons.iface.util.NumberUtil;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;

public class JGaugesTestDisplay extends GPanel
{
    private Button valueBtn;
    private Button ticksChangeBtn;
    private Button minusWidthBtn;
    private Button rangeChangeBtn;
    private Button getValueBtn;
    private JGauges testGauge1;
    private JGauges testGauge2;
    private JGauges testGauge3;
    private JGauges testGauge4;
    private TextField<Double> valueField;

    public JGaugesTestDisplay()
    {
        setLayout(new RowLayout());
        Label label = new Label("Hi, I am BQM!");
        add(label);

        valueBtn = new Button("Set Value");
        add(valueBtn);
        addValueBtnClickListener();

        ButtonBar sizeBtnBar = new ButtonBar();
        ticksChangeBtn = new Button("change ticks");
        rangeChangeBtn = new Button("change range");
        minusWidthBtn = new Button("width-");
        getValueBtn = new Button("get value");
        add(sizeBtnBar, new RowData(-1, -1, new Margins(5, 5, 25, 5)));
        sizeBtnBar.add(ticksChangeBtn);
//        sizeBtnBar.add(minusWidthBtn);
        sizeBtnBar.add(rangeChangeBtn);
        sizeBtnBar.add(getValueBtn);
        addSizeBtnClickListeners();


        testGauge1 = new JGauges("gauge1");
        add(testGauge1);

        testGauge2 = new JGauges("gauge2");
        testGauge2.getGaugeOption().setMode(JGaugesOption.GaugeMode.MODE1);
        add(testGauge2);

        testGauge3 = new JGauges("gauge3");
        testGauge3.getGaugeOption().setMode(JGaugesOption.GaugeMode.MODE3);
        add(testGauge3);

        testGauge4 = new JGauges("gauge4");
        testGauge4.getGaugeOption().setMode(JGaugesOption.GaugeMode.MODE2);
        add(testGauge4);

        valueField = new TextField();
        valueField.setValue(0.0);
        add(valueField, new RowData(-1, -1));
    }

    private void addSizeBtnClickListeners()
    {
        ticksChangeBtn.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                Double d = NumberUtil.getRandomNumber();
                JGaugesTicksOption tickOption = new JGaugesTicksOption();
                tickOption.setMin(d * 100 - 100);
                tickOption.setMax(d * 100 + 100);
                tickOption.setCount(((Double)(d * 100)).intValue() % 8 + 2);
                JGaugesHelper.updateGaugeTicks(testGauge1, tickOption);
            }
        });

        rangeChangeBtn.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                Double d = NumberUtil.getRandomNumber() * 100;
                Integer intValue = d.intValue();
                JGaugesRangeOption rangeOption = new JGaugesRangeOption();
                rangeOption.setEndAngle(intValue + 20);
                rangeOption.setStartAngle(intValue - 20);
                JGaugesHelper.updateGaugeRange(testGauge1, rangeOption);
            }
        });

        getValueBtn.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                Double d = testGauge1.getValue();
                valueField.setValue(d);
            }
        });
    }

    private void addValueBtnClickListener()
    {
        valueBtn.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                Double d = NumberUtil.getRandomNumber();
                testGauge1.setValue(d * 100);
                testGauge2.setValue(d * 100);
                testGauge3.setValue(d * 100);
                testGauge4.setValue(d * 100);
            }
        });
    }

    @Override
    public void doOnRender(Element element, int i)
    {
        layout(true);
    }

    @Override
    public String getLabel()
    {
        return "BQM";
    }
}
