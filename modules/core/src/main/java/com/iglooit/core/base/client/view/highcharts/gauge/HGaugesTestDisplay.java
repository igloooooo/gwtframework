package com.iglooit.core.base.client.view.highcharts.gauge;

import com.clarity.core.base.client.view.highcharts.config.LinearGradientColor;
import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.clarity.commons.iface.util.NumberUtil;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.google.gwt.user.client.Element;

import java.util.List;

public class HGaugesTestDisplay extends GPanel
{
    private HGauges testGauge1;
    private Button valueBtn;
    private Button mode1Btn;
    private Button mode2Btn;

    public HGaugesTestDisplay()
    {
        ButtonBar bb = new ButtonBar();
        valueBtn = new Button("set value");
        mode1Btn = new Button("mode 1");
        mode2Btn = new Button("mode 2");
        bb.add(valueBtn);
        bb.add(mode1Btn);
        bb.add(mode2Btn);
        add(bb);

        testGauge1 = new HGauges();
        add(testGauge1);

        addBtnClickListeners();
    }

    private void addBtnClickListeners()
    {
        valueBtn.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                Double d = NumberUtil.getRandomNumber();
                testGauge1.setGaugeValue(d * 200);
            }
        });

        mode1Btn.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                testGauge1.getGaugeOption().setMinMaxInterval(0.0, 100.0, 20.0);
                testGauge1.getGaugeOption().setSize(200, 200);

                List<HGaugesRangeOption> ranges = testGauge1.getGaugeOption().getRanges();
                ranges.clear();
                HGaugesRangeOption defRange = new HGaugesRangeOption(0.0, 100.0, "yellow");
                ranges.add(defRange);
                testGauge1.getGaugeOption().setRangeOptions(ranges);

                testGauge1.redrawGauge();
            }
        });
        
        mode2Btn.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                testGauge1.getGaugeOption().setMinMaxInterval(0.0, 200.0, 50.0);
                testGauge1.getGaugeOption().setSize(100, 100);

                List<HGaugesRangeOption> ranges = testGauge1.getGaugeOption().getRanges();
                HGaugesRangeOption rangeEx1 = new HGaugesRangeOption(100.0, 200.0, "yellow",
                    new LinearGradientColor(0.0, 0.0, 0.0, 200.0, "yellow", "red"));
                ranges.add(rangeEx1);
                testGauge1.getGaugeOption().setRangeOptions(ranges);

                testGauge1.redrawGauge();
            }
        });
    }

    @Override
    public void doOnRender(Element element, int i)
    {

    }

    @Override
    public String getLabel()
    {
        return null;
    }
}
