package com.iglooit.core.base.client.widget.combobox;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.NonSerOpt;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.Selectable;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.lib.client.AsyncLoadingList;
import com.clarity.core.lib.client.MetaModelData;
import com.clarity.core.lib.iface.TypeConverter;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidatableComplexComboBox<InternalType, DisplayType extends MetaModelData>
    implements Selectable<InternalType>, ClarityField<InternalType, ComboBox<DisplayType>>
{
    private HandlerManager innerHandlerManager = new HandlerManager(null);

    private NonSerOpt<AsyncLoadingList<InternalType>> loader;
    private ComboBox<DisplayType> comboBox;
    private Listener<ComponentEvent> dynamicComboWidthListener;

    private boolean revalidateAfterLoad = false;

    private Map<InternalType, DisplayType> forwardMap = new HashMap<InternalType, DisplayType>();
    private Map<DisplayType, InternalType> reverseMap = new HashMap<DisplayType, InternalType>();
    private TypeConverter<InternalType, DisplayType> converterToDisplayType;

    public ValidatableComplexComboBox(AsyncLoadingList<InternalType> loader,
                                      TypeConverter<InternalType, DisplayType> converterToDisplayType)
    {
        super();
        this.loader = NonSerOpt.option(loader);
        this.converterToDisplayType = converterToDisplayType;
        init();
    }

    public ValidatableComplexComboBox(List<InternalType> options,
                                      TypeConverter<InternalType, DisplayType> converterToDisplayType)
    {
        super();
        this.loader = NonSerOpt.none();
        this.converterToDisplayType = converterToDisplayType;
        init();
        updateStore(options);
    }

    private void init()
    {
        comboBox = new ComboBox<DisplayType>();
        comboBox.setStore(new ListStore<DisplayType>());
        comboBox.getImages().setInvalid(Resource.ICONS.exclamationRed());
        comboBox.setTypeAhead(true);
        comboBox.setTypeAheadDelay(100);
        comboBox.setForceSelection(true);

        comboBox.setTriggerAction(com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction.ALL);
        comboBox.setLazyRender(false);
        dynamicComboWidthListener = ClarityComboUtil.enableDynamicSelectionWidthCalculation(comboBox);
    }

    public void disableDynamicComboWidthListener()
    {
        if (dynamicComboWidthListener != null)
            ClarityComboUtil.disableDynamicSelectionWidthCalculation(comboBox, dynamicComboWidthListener);
    }

    public ComboBox<DisplayType> getComboBox()
    {
        return getField();
    }

    private void fireValidationChangeEvent()
    {
        ValueChangeEvent.fire(this, getValue());
    }

    public void setTemplate(String html)
    {
        comboBox.setTemplate(html);
    }

    public void setTemplate(XTemplate template)
    {
        comboBox.setTemplate(template);
    }

    public void setDisplayField(String fld)
    {
        comboBox.setDisplayField(fld);
    }

    public void updateStore(List<InternalType> values)
    {
        if (comboBox == null)
            init();
        comboBox.clear();
        comboBox.getStore().removeAll();
        forwardMap.clear();
        reverseMap.clear();
        add(values);
    }

    public boolean contains(InternalType value)
    {
        DisplayType displayType = converterToDisplayType.convert(value);
        return comboBox.getStore().contains(displayType);
    }

    public void add(List<InternalType> values)
    {
//        comboBox.getStore().insert(models, comboBox.getStore().getCount());

        List<DisplayType> displayTypes = new ArrayList<DisplayType>();
        if (values != null)
        {
            for (InternalType value : values)
            {
                DisplayType displayType = converterToDisplayType.convert(value);
                displayTypes.add(displayType);
                forwardMap.put(value, displayType);
                reverseMap.put(displayType, value);
            }
            if (forwardMap.size() != values.size())
                throw new AppX("Non unique forward entries!");
            if (reverseMap.size() != values.size())
                throw new AppX("Non unique reverse entries!");
        }
        comboBox.getStore().insert(displayTypes, comboBox.getStore().getCount());
    }

    public void select(int index)
    {
        if ((index >= 0) && (index < comboBox.getStore().getCount()))
        {
            comboBox.setValue(comboBox.getStore().getAt(index));
        }
    }

    public NonSerOpt<AsyncLoadingList<InternalType>> getLoader()
    {
        return loader;
    }

    public void mask()
    {
        if (comboBox == null)
            return;
        if (comboBox.isRendered() && !comboBox.isMasked())
            comboBox.mask("", ClarityStyle.MASK_FIELD);
    }

    public void loadingMask()
    {
        comboBox.mask("", ClarityStyle.MASK_FIELD);
    }

    public void unMask()
    {
        if (comboBox == null)
            return;
        if (comboBox.isRendered())
            comboBox.unmask();
    }

    public boolean revalidateAfterLoad()
    {
        return revalidateAfterLoad;
    }

    public void setRevalidateAfterLoad(boolean revalidateAfterLoad)
    {
        this.revalidateAfterLoad = revalidateAfterLoad;
    }

    public void setEditable(boolean editable)
    {
        this.comboBox.setEditable(editable);
    }

    // =================================================== //
    // Methods in Selectable
    // =================================================== //
    @Override
    public void select(InternalType value)
    {
        comboBox.clearSelections();
        comboBox.setValue(forwardMap.get(value));
    }

    //Methods in Selectable<T>
    @Override
    public HandlerRegistration addSelectionHandler(final SelectionHandler<InternalType> selectionHandler)
    {
        final SelectionChangedListener<DisplayType> gxtListener
            = new SelectionChangedListener<DisplayType>()
        {
            public void selectionChanged(SelectionChangedEvent<DisplayType> event)
            {
                DisplayType item = event.getSelectedItem();

                if (item == null)
                    selectionHandler.onSelection(new SelectionEvent<InternalType>(null)
                    {
                    });
                else
                {
                    InternalType value = reverseMap.get(item.getMeta());
                    selectionHandler.onSelection(new SelectionEvent<InternalType>(value)
                    {
                    });
                }
            }
        };

        comboBox.addSelectionChangedListener(gxtListener);

        return new HandlerRegistration()
        {
            public void removeHandler()
            {
                comboBox.removeSelectionListener(gxtListener);
            }
        };
    }

    //Methods in Selectable<T>
    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        innerHandlerManager.fireEvent(gwtEvent);
    }

    // =================================================== //
    // Methods in ClarityField
    // =================================================== //

    @Override
    public ComboBox<DisplayType> getField()
    {
        return comboBox;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        if (validationResultList.size() == 0)
        {
            comboBox.clearInvalid();
        }
        else
        {
            String validationShowErrorsMessage = validationResultList.get(0).getReason();
            comboBox.forceInvalid(validationShowErrorsMessage);
        }
    }

    @Override
    public void valueExternallyChangedFrom(InternalType oldLocalValue)
    {
    }

    @Override
    public String getFieldLabel()
    {
        return StringUtil.emptyStringIfNull(comboBox.getFieldLabel());
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        comboBox.setFieldLabel(fieldLabel);
    }

    @Override
    public Option<String> getUsageHint()
    {
        return Option.option(comboBox.getEmptyText());
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        comboBox.setEmptyText(usageHint);
    }

    @Override
    public InternalType getValue()
    {
        DisplayType dt = comboBox.getValue();
        return (dt == null) ? null : reverseMap.get(dt);
    }

    @Override
    public void setValue(InternalType t)
    {
        setValue(t, true);
    }

    @Override
    public void setValue(InternalType internalType, boolean fireChangeEvent)
    {
        comboBox.setFireChangeEventOnSetValue(false);

        boolean set = false;
        // if the value is not null, AND it exists in the store, set. otherwise set null;
        // this will force invalid values that were obtained from the server
        if (internalType != null)
        {
            for (DisplayType val : comboBox.getStore().getModels())
            {
                if (val != null)
                {
                    InternalType value = reverseMap.get(val);
                    if (internalType.equals(value))
                    {
                        comboBox.setValue(val);
                        set = true;
                    }
                }
            }
        }
        if (!set)
        {
            comboBox.setValue(null);
        }
        if (fireChangeEvent)
        {
            fireValidationChangeEvent();
        }

        comboBox.setFireChangeEventOnSetValue(true);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<InternalType> tValueChangeHandler)
    {
        final HandlerRegistration valueChangeReg =
            innerHandlerManager.addHandler(ValueChangeEvent.getType(), tValueChangeHandler);

        final HandlerRegistration selectionChangeReg =
            ValidatableComplexComboBox.this.addSelectionHandler(new SelectionHandler<InternalType>()
            {
                public void onSelection(SelectionEvent<InternalType> tSelectionEvent)
                {
                    fireValidationChangeEvent();
                }
            });

        return new HandlerRegistration()
        {
            public void removeHandler()
            {
                valueChangeReg.removeHandler();
                selectionChangeReg.removeHandler();
            }
        };
    }

    public boolean isValid()
    {
        return comboBox.isValid();
    }

    public void setAllowBlank(boolean allowBlank)
    {
        comboBox.setAllowBlank(allowBlank);
    }
}
