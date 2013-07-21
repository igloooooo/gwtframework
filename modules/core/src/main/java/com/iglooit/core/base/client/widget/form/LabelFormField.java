package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.ClarityFieldBinder;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

import java.util.List;

public class LabelFormField<D> extends LabelField implements ClarityField<D, LabelFormField>
{
    private static final BaseViewConstants BASEVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private String emptyText;
    private boolean showEmptyText;
    private static String defaultEmptyText;

    private static final String EMPTY_STYLE = "field-empty";

    static
    {
        if (BASEVC == null)
        {
            throw new AppX("Could not find BaseViewConstants");
        }
        defaultEmptyText = BASEVC.labelFieldEmptyDefaultText();
    }

    public LabelFormField()
    {
        this(true);
    }

    public LabelFormField(boolean showEmptyText)
    {
        this.showEmptyText = showEmptyText;
        emptyText = defaultEmptyText;
        setLabelSeparator(ClarityFieldBinder.DEFAULT_LABEL_SEPARATOR);
    }

    @Override
    public void setText(String text)
    {
        super.setText(text);
        if (StringUtil.isEmpty(text) && showEmptyText)
        {
            if (rendered)
            {
                el().update(emptyText);
            }
            addStyleName(EMPTY_STYLE);
        }
        else if (showEmptyText)
        {
            removeStyleName(EMPTY_STYLE);
        }

    }

    @Override
    protected void onRender(Element parent, int index)
    {
        setElement(DOM.createDiv(), parent, index);

        originalValue = getText();
        setText(getText());
    }

    public void setValue(D d, boolean b)
    {
        super.setValue(d);
    }

    @Override
    public void setValue(Object value)
    {
        if (getPropertyEditor() != null && value != null)
            super.setValue(getPropertyEditor().getStringValue(value));
        else
            super.setValue(value);
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<D> dValueChangeHandler)
    {
        throw new AppX("LabelFormField should not be used with mutable binders");
    }

    public D getValue()
    {
        return (D)super.getValue();
    }

    public LabelFormField getField()
    {
        return this;
    }

    public void handleValidationResults(List<ValidationResult> validationResultList)
    {

    }

    public void valueExternallyChangedFrom(D oldLocalValue)
    {

    }

    public Option<String> getUsageHint()
    {
        return Option.none();
    }

    public void setUsageHint(String usageHint)
    {
    }

    public String getEmptyText()
    {
        return emptyText;
    }

    public void setEmptyText(String emptyText)
    {
        this.emptyText = emptyText;
    }

    public boolean isShowEmptyText()
    {
        return showEmptyText;
    }

    public void setShowEmptyText(boolean showEmptyText)
    {
        this.showEmptyText = showEmptyText;
    }
}
