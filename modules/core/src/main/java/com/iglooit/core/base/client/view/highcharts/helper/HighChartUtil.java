package com.iglooit.core.base.client.view.highcharts.helper;

import com.clarity.commons.iface.type.AppX;
import com.clarity.core.base.client.view.highcharts.Chart;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to export multiple charts into a single pdf/image or CSV file.
 * It can also be used for single chart exporting, but Chart.java itself already supplied the functionality
 */
public class HighChartUtil
{
    /**
     * all supported exported type except CSV
     */
    public static final List<ExportType> SUPPORTED_EXPORT_TYPE = new ArrayList<ExportType>();
    static
    {
        SUPPORTED_EXPORT_TYPE.add(ExportType.PDF);
        SUPPORTED_EXPORT_TYPE.add(ExportType.PNG);
        SUPPORTED_EXPORT_TYPE.add(ExportType.JPG);
        SUPPORTED_EXPORT_TYPE.add(ExportType.SVG);
    }

    /**
     * A unicode blank
     * Highchart will show a YAxis value of "Y-value" if the YAxix value is set to null or blank String.
     * To prevent the "Y-value" from showing, use this UNICODE_BLANK instead
     */
    public static final String UNICODE_BLANK = "\u2001";

    public static void export(Chart javaChart, JavaScriptObject jsChart, ExportType type)
    {
        checkExportType(type);
        export(javaChart, jsChart, type.getContentType());
    }

    private static native void export(Chart javaChart, JavaScriptObject jsChart, String type)/*-{

        var bottomMargin = jsChart.marginBottom;
        var extraDataTableHeight
            = javaChart.@com.clarity.core.base.client.view.highcharts.Chart::getExtraExportDataHeight()();
        //set the right margin for the exported chart
        var rightMargin = 20 > jsChart.marginRight ? 20 : jsChart.marginRight;

        //if extra table was added to the highchart, we need to adjust the highchart margin,
        //so that the table can be shown when exported
        bottomMargin = bottomMargin + extraDataTableHeight;
        jsChart.exportChart({
						type: type
					},
					{chart:{
					    height:480 + extraDataTableHeight,
					    width:640,
					    marginBottom:bottomMargin,
					    marginRight:rightMargin}}
                );
    }-*/;

    public static void export(JsArray charts, ExportType type, JavaScriptObject extraSVG, int totalColumns)
    {
        checkExportType(type);

        int columns = 1;
        if (totalColumns > 0)
            columns = totalColumns;
        export(charts, type.getContentType(), extraSVG, columns);
    }

    private static native void export(JsArray charts, String type, JavaScriptObject extraSVG, int totalColumns)/*-{
        var exportUrl = @com.clarity.core.base.client.view.highcharts.helper.HighChartUtil::getExportServletURL()();
        $wnd.HCExtend.exportCharts(charts, {type: type, url: exportUrl, enabled: false, extraSVG: extraSVG, totalColumns: totalColumns});
    }-*/;

    public static String getExportServletURL()
    {
        return GWT.getHostPageBaseURL() + "svgexporter/";
    }

    public static void exportToCSV(String csv)
    {

        ClarityFormSubmitter submitter = new ClarityFormSubmitter(getExportServletURL());
        RootPanel.get().add(submitter.getWidget());

        submitter.setParameter(ExportType.CSV.getContentType(), "type");
        submitter.setParameter(csv, "svg");
        submitter.getFormPanel().setEncoding(FormPanel.ENCODING_MULTIPART);
        submitter.submitForm();

        submitter.clearForm();
    }

    private static void checkExportType(ExportType exportType)
    {
        if (!SUPPORTED_EXPORT_TYPE.contains(exportType))
        {
            throw new AppX("Export Type: " + exportType + " not supported!");
        }
    }

    /**
     * most of the time we need to differ chart resize when resizing window or pressing Ctrl +/- key to zoom in/out
     * or clicking the maximize/restore button of the window
     * otherwise the chart will not resize probably
     * @param chart the chart object
     * @param chartContainer the container that chart will be resize to fit in
     * @param immediateResize whether to resize the chart immediately on window resize event.
     *                        set to false to improve performance when have multi charts in one screen
     */
    public static void deferChartResizeOnContainerResize(final Chart chart,
                                                         final LayoutContainer chartContainer,
                                                         final boolean immediateResize)
    {
        if (chart == null || chartContainer == null)
            throw new IllegalArgumentException("chart and chartContainer should not be null");

        final Timer timer = new Timer()
        {
            @Override
            public void run()
            {
                if (chart != null && chart.isRendered())
                {
                    resizeChart(chartContainer, chart);
                }

            }
        };

        chartContainer.addListener(Events.Resize, new Listener<BaseEvent>()
        {
            @Override
            public void handleEvent(BaseEvent be)
            {
                if (immediateResize && chart != null && chart.isRendered())
                {
                    resizeChart(chartContainer, chart);
                }
                timer.schedule(500);
            }
        });
    }

    public static void resizeChart(LayoutContainer chartContainer, Chart chart)
    {
        if (chartContainer.getParent() instanceof ContentPanel)
        {
            ContentPanel panel = (ContentPanel)chartContainer.getParent();
            if (!panel.isCollapsed())
            {
                chart.setSize(chartContainer.getWidth(), chartContainer.getHeight());
                chart.layout(true);
            }
        }
        else
        {
            chart.setSize(chartContainer.getWidth(), chartContainer.getHeight());
            chart.layout(true);
        }
    }
}
