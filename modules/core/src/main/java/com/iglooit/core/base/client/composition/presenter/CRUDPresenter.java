package com.iglooit.core.base.client.composition.presenter;

import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.mvp.Presenter;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.clarity.core.base.client.widget.layoutcontainer.CRUDDisplay;
import com.clarity.core.base.iface.domain.DomainEntity;
import com.clarity.core.base.iface.domain.Validatable;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.google.gwt.event.shared.HandlerManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CRUDPresenter implements Presenter
{
    private HandlerManager globalEventBus;
    private HandlerManager screenEventBus;

    private CRUDDisplay crudDisplay;
    private FormPresenter formPresenter;

    public CRUDPresenter(HandlerManager globalEventBus,
                         CRUDDisplay crudDisplay,
                         FormPresenter formPresenter)
    {
        this.crudDisplay = crudDisplay;
        this.formPresenter = formPresenter;
        this.globalEventBus = globalEventBus;
        this.screenEventBus = new HandlerManager(null);
    }

    public void setFormPresenter(FormPresenter formPresenter)
    {
        this.formPresenter = formPresenter;
    }

    public void bind()
    {

    }

    public void unbind()
    {

    }

    public Display getDisplay()
    {
        return crudDisplay;
    }

    public static String getConfirmFieldPropertyName(String propertyName)
    {
        return propertyName + ",confirm";
    }

    public static String getOriginalPropertyNameFromConfirm(String confirmString)
    {
        return confirmString.split("\\,")[0];
    }

    public HandlerManager getGlobalEventBus()
    {
        return globalEventBus;
    }

    public HandlerManager getScreenEventBus()
    {
        return screenEventBus;
    }

    public CRUDDisplay getCrudDisplay()
    {
        return crudDisplay;
    }

    public FormPresenter getFormPresenter()
    {
        return formPresenter;
    }

    public void mask(String s)
    {
        crudDisplay.mask(s);
    }

    public boolean validateAndNotify(DomainEntity meta)
    {
        if (meta == null)
            return false;
        List<ValidationResult> results = meta.validate(Option.<String>none());
        if (results.size() != 0)
        {
            Map<Validatable, List<ValidationResult>> resultMap = new
                HashMap<Validatable, List<ValidationResult>>();
            resultMap.put(meta, results);
            meta.notifyValidationHandlers(results, Option.<String>none());
            crudDisplay.updateStatusDescription(
                getFormPresenter().getBinderContainer().getValidationFailEventString(resultMap),
                MessageBoxHtml.MessageType.ERROR);
            return true;
        }
        else
        {
            crudDisplay.clearStatusDescription();
        }
        return false;

    }

}
