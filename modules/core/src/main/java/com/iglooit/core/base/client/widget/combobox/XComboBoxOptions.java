package com.iglooit.core.base.client.widget.combobox;

/**
 * Holds the options that guide construction of an XComboBox.
 */
public class XComboBoxOptions
{
    private boolean showApplyBtn = false;
    private boolean showSelectAllBtn = true;
    private boolean usePhobosSpecialTemplate = false;

    public XComboBoxOptions()
    {
    }

    public boolean isShowApplyBtn()
    {
        return showApplyBtn;
    }

    public void setShowApplyBtn(boolean showApplyBtn)
    {
        this.showApplyBtn = showApplyBtn;
    }

    public boolean isShowSelectAllBtn()
    {
        return showSelectAllBtn;
    }

    public void setShowSelectAllBtn(boolean showSelectAllBtn)
    {
        this.showSelectAllBtn = showSelectAllBtn;
    }

    public boolean isUsePhobosSpecialTemplate()
    {
        return usePhobosSpecialTemplate;
    }

    /*
    * Phobos special template can provide you with
    * 1. self defined combo template based on a model data
    * 2. grouping title that do not associate with a checkbox and not selectable
    *
    * To use it, you have to
    * 1. define a XComboBox<ModelDataComboValue<D extends ModelData>>
    * 2. set self defined template that use x-phobos-item for checkbox input
    *
    * see example as: BQMGaugeMonitorDisplay.java and PerfDimensionFilterModelData.java
    * */
    public void setUsePhobosSpecialTemplate(boolean usePhobosSpecialTemplate)
    {
        this.usePhobosSpecialTemplate = usePhobosSpecialTemplate;
    }
}
