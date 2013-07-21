package com.iglooit.core.base.client.composition.presenter;

import com.clarity.core.base.client.composition.display.FormMode;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.mvp.Presenter;
import com.clarity.core.base.client.widget.layoutcontainer.FormDisplay;

public abstract class FormPresenter implements Presenter
{
    private final FormDisplay display;
    private BinderContainer binderContainer;
    private FormMode formMode;

    public FormPresenter(FormDisplay display, FormMode formMode)
    {
        this.display = display;
        this.formMode = formMode;
        display.setup(formMode);
    }

    protected abstract BinderContainer createBinderContainer();

    public void  bindFields()
    {
        if (binderContainer != null)
            binderContainer.unbind();
        binderContainer = createBinderContainer();
        binderContainer.bind(formMode);
    }

    public void clearInvalidSigns()
    {
        if (binderContainer != null)
        {
            binderContainer.clearInvalidSigns();
        }
    }

    public BinderContainer getBinderContainer()
    {
        return binderContainer;
    }

    public Display getDisplay()
    {
        return display;
    }

    public void setFormMode(FormMode formMode)
    {
        this.formMode = formMode;
        display.setup(formMode);
        bindFields();
    }

    public void selectFirstField()
    {
        display.getWidgetContainer().selectFirstField();
    }

    public boolean hasModifications()
    {
        if (binderContainer != null)
            return binderContainer.hasModifications();
        return false;
    }

    public FormMode getFormMode()
    {
        return this.formMode;
    }

    public boolean isEditingMode()
    {
        return formMode.equals(FormMode.UPDATE) || formMode.equals(FormMode.CREATE);
    }

    public void undoModifications()
    {
        if (binderContainer != null)
            binderContainer.undoModifications();
    }
}
