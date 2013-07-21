package com.iglooit.core.base.client.composition.presenter;

import com.clarity.core.base.client.composition.display.FormMode;
import com.clarity.core.base.client.composition.display.WidgetContainer;
import com.clarity.core.base.client.composition.presenter.factory.binder.WidgetDerivedBinderFactory;
import com.clarity.core.base.client.mvp.Binder;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.GroupAdapterField;
import com.clarity.core.base.client.mvp.HasChildBindings;
import com.clarity.core.base.client.mvp.MutableBinder;
import com.clarity.core.base.client.widget.form.ClarityAdapterField;
import com.clarity.core.base.iface.domain.Validatable;
import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.core.base.iface.validation.ValidationConstants;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinderContainer implements HandlerRegistration
{
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    private final BinderFactory defaultBinderFactory;
    private final WidgetContainer widgetContainer;
    private final List<Binder> binders;
    private final List<BinderInfo> binderInfoList;

    public BinderContainer(final ClarityField clarityField)
    {
        this(new WidgetContainer()
        {
            public void selectFirstField()
            {
                if (clarityField.getField() instanceof Field)
                    ((Field)clarityField.getField()).focus();
            }

            protected ClarityField doGetField(String propertyName, FormMode formMode)
            {
                return clarityField;
            }
        });
    }

    public BinderContainer(WidgetContainer widgetContainer)
    {
        this(WidgetDerivedBinderFactory.getDefault(), widgetContainer);
    }

    public BinderContainer(BinderFactory defaultBinderFactory, WidgetContainer widgetContainer)
    {
        this.defaultBinderFactory = defaultBinderFactory;
        this.widgetContainer = widgetContainer;
        binders = new ArrayList<Binder>();
        binderInfoList = new ArrayList<BinderInfo>();
    }

    public void add(String propertyName, ValidatableMeta validatableMeta)
    {
        this.add(new BinderInfo(propertyName, validatableMeta));
    }

    public void add(ValidatableMeta validatableMeta, String ... propertyNames)
    {
        for (String propertyName : propertyNames)
            this.add(new BinderInfo(propertyName, validatableMeta));
    }

    public void add(String propertyName, ValidatableMeta validatableMeta, BinderFactory binderFactory)
    {
        this.add(new BinderInfo(propertyName, validatableMeta, binderFactory));
    }

    private void add(BinderInfo binderInfo)
    {
        binderInfoList.add(binderInfo);
    }

    public int findFieldLabelIndexInBinderList(String fieldLabel)
    {
        for (int i = 0; i < binders.size(); i++)
            if (binders.get(i).getFieldLabel().equals(fieldLabel))
                return i;
        return -1;
    }

    public boolean hasModifications()
    {
        for (Binder b : binders)
            if (b.isModified())
                return true;
        return false;
    }

    public void undoModifications()
    {
        for (Binder b : binders)
            b.undoModifications();
    }

    public Binder findBinder(String propertyName)
    {
        for (Binder binder : binders)
        {
            if (binder.getMetaFieldName().equals(propertyName))
            {
                return binder;
            }
        }

        return null;
    }

    public void bind(FormMode formMode)
    {
        unbind();
        for (BinderInfo binderInfo : binderInfoList)
        {
            final String propertyName = binderInfo.getPropertyName();
            ClarityField field = widgetContainer.getField(propertyName, formMode);
            BinderFactory binderFactory = defaultBinderFactory;
            if (binderInfo.getBinderFactory().isSome())
                binderFactory = binderInfo.getBinderFactory().value();

            if (field instanceof ClarityAdapterField)
            {
                if (!((ClarityAdapterField)field).isOneField())
                    binders.add(binderFactory.createBinder(propertyName, binderInfo.getValidatableMeta(),
                        ((ClarityAdapterField)field).getMainField()));
            }
            else if (field instanceof GroupAdapterField)
                binders.addAll(getGroupAdapterFields((GroupAdapterField)field, binderInfo.getValidatableMeta()));
            Binder newBinder = binderFactory.createBinder(propertyName, binderInfo.getValidatableMeta(), field);
            binders.add(newBinder);

            // recursively binds everything
            if (newBinder instanceof HasChildBindings)
            {
                for (BinderContainer childContainer : ((HasChildBindings)newBinder).getChildBinderContainers())
                {
                    childContainer.bind(formMode);
                }
            }
        }
    }

    private Collection<? extends Binder> getGroupAdapterFields(GroupAdapterField groupAdapterField,
                                                               ValidatableMeta validatableMeta)
    {
        BinderFactory binderFactory = defaultBinderFactory;
        List<Binder> binders = new ArrayList<Binder>();
        for (GroupAdapterField.FieldInfo fieldInfo : groupAdapterField.getFieldInfos())
        {
            Binder newBinder = binderFactory.createBinder(fieldInfo.getProperty(),
                validatableMeta,
                fieldInfo.getField());
            binders.add(newBinder);
        }
        return binders;
    }


    public void unbind()
    {
        for (Binder binder : binders)
        {
            if (binder instanceof HasChildBindings)
            {
                for (BinderContainer childContainer : ((HasChildBindings)binder).getChildBinderContainers())
                {
                    childContainer.unbind();
                }
            }
            binder.unbind();
        }
        binders.clear();
    }

    // this iterates through the bound fields in order, finding the first
    // one that will accept focus, then sets it.
    public void selectFirstField()
    {
        widgetContainer.selectFirstField();
    }


    public String getValidationFailEventString(Map<Validatable, List<ValidationResult>> results)
    {
        // sort the list based on the index in the binderList,
        // so validation results appear in the same order as the form
        List<ValidationResult> allResults = new ArrayList<ValidationResult>();
        for (List<ValidationResult> res : results.values())
            allResults.addAll(res);

        Collections.sort(allResults, new Comparator<ValidationResult>()
        {
            public int compare(ValidationResult o1, ValidationResult o2)
            {
                if (o1 == null || o2 == null)
                    return 0;
                String o1FieldLabel = o1.getFieldLabel();
                String o2FieldLabel = o2.getFieldLabel();
                int o1FieldBinderIndex = findFieldLabelIndexInBinderList(o1FieldLabel);
                int o2FieldBinderIndex = findFieldLabelIndexInBinderList(o2FieldLabel);
                return Integer.valueOf(o1FieldBinderIndex).compareTo(o2FieldBinderIndex);
            }
        });

        StringBuilder sb = new StringBuilder();
        sb.append(VC.validationFailedOnServer());
        sb.append("<ul class='error-list'>");
        for (ValidationResult validationResult : allResults)
        {
            if (validationResult.isDisplayInErrorSummary())
            {
                sb.append("<li>");
                sb.append(validationResult.getFieldLabel()).append(": ").append(validationResult.getReason());
                sb.append("</li>");
            }
        }
        sb.append("</ul>");
        return sb.toString();
    }

    public WidgetContainer getWidgetContainer()
    {
        return widgetContainer;
    }

    public boolean forceReValidate()
    {
        boolean isValid = true;
        for (Binder binder : binders)
        {
            if (binder instanceof MutableBinder)
            {
                MutableBinder mutableBinder = (MutableBinder)binder;
                if (!mutableBinder.isValueValid())
                    isValid = false;
            }

            if (binder instanceof HasChildBindings)
            {
                HasChildBindings childBinder = (HasChildBindings)binder;
                for (BinderContainer binding : childBinder.getChildBinderContainers())
                    if (binding.forceReValidate())
                        isValid = false;
            }
        }
        return isValid;
    }

    public Map<String, List<ValidationResult>> getValidationResults()
    {
        Map<String, List<ValidationResult>> validationMap = new HashMap<String, List<ValidationResult>>();
        for (Binder binder : binders)
        {
            if (binder instanceof MutableBinder)
            {
                MutableBinder mutableBinder = (MutableBinder)binder;
                if (!mutableBinder.isValueValid())
                {
                    List<ValidationResult> validationResults = mutableBinder.getValidationResults();
                    for (ValidationResult result : validationResults)
                    {
                        result.setFieldLabel(mutableBinder.getFieldLabel());
                    }
                    validationMap.put(mutableBinder.getMetaFieldName(), validationResults);
                }
            }
        }
        return validationMap;
    }

    public void removeHandler()
    {
        unbind();
    }

    public void clearInvalidSigns()
    {
        for (Binder binder : binders)
        {
            if (binder instanceof MutableBinder)
            {
                MutableBinder mutableBinder = (MutableBinder)binder;
                mutableBinder.handleValidationResults(Collections.<ValidationResult>emptyList());
            }
        }
    }

    public void undoModificationsForAttributes(List<String> attributes)
    {
        if (attributes != null && !attributes.isEmpty())
        {
            for (String attribute : attributes)
            {
                for (Binder binder : binders)
                {
                    if (binder.getMetaFieldName().equals(attribute))
                        binder.undoModifications();
                }
            }
        }
    }

    public void saveValues()
    {
        for (Binder binder : binders)
        {
            if (binder instanceof MutableBinder)
            {
                ((MutableBinder)binder).saveValue();
            }
        }
    }

    // Contenience method to create a binding
    public static BinderContainer createBinding(ClarityField field, String propertyName,
                                                ValidatableMeta validatableMeta, FormMode mode)
    {
        final BinderContainer binder = new BinderContainer(field);
        binder.add(propertyName, validatableMeta);
        binder.bind(mode);
        return binder;

    }
}
