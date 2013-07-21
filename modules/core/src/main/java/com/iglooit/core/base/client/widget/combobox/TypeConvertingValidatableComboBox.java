package com.iglooit.core.base.client.widget.combobox;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.NonSerOpt;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.Selectable;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.lib.client.AsyncLoadingList;
import com.clarity.core.lib.iface.TypeConverter;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
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

public class TypeConvertingValidatableComboBox<InternalType, DisplayType> implements
        Selectable<InternalType>, ClarityField<InternalType,
        SimpleComboBox<DisplayType>>
{
    private static final BaseViewConstants BASEVC = I18NFactoryProvider.get(BaseViewConstants.class);
    private Map<InternalType, DisplayType> forwardMap = new HashMap();
    private Map<DisplayType, InternalType> reverseMap = new HashMap();

    private NonSerOpt<AsyncLoadingList<InternalType>> loader;
    private SimpleComboBox<DisplayType> comboBox;
    private TypeConverter<InternalType, DisplayType> converterToDisplayType;
    private boolean revalidateAfterLoad = false;

    private List<InternalType> permanentList;
    private Listener<ComponentEvent> dynamicComboWidthListener;

    public TypeConvertingValidatableComboBox(AsyncLoadingList<InternalType> loader,
                                             TypeConverter<InternalType, DisplayType> converterToDisplayType)
    {
        super();
        this.converterToDisplayType = converterToDisplayType;
        this.loader = NonSerOpt.option(loader);
        init();
    }

    public TypeConvertingValidatableComboBox(List<InternalType> options,
                                             TypeConverter<InternalType, DisplayType> converterToDisplayType)
    {
        super();
        this.converterToDisplayType = converterToDisplayType;
        this.loader = NonSerOpt.none();
        init();
        updateStore(options);
    }

    private void init()
    {
        comboBox = new SimpleComboBox<DisplayType>()
        {
            @Override
            public boolean validate()
            {
                return false;
            }

            @Override
            public void add(List<DisplayType> values)
            {
                super.add(values);
            }
        };
        comboBox.getImages().setInvalid(Resource.ICONS.exclamationRed());
        comboBox.setTypeAhead(true);
        comboBox.setTypeAheadDelay(100);
        comboBox.setForceSelection(true);
        comboBox.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<DisplayType>>()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent<SimpleComboValue<DisplayType>> ce)
            {
                if (ce.getSelectedItem() == null)
                {
                    setValue(null);
                }
                else
                {
                    InternalType internalType = reverseMap.get(ce.getSelectedItem().getValue());
                    setValue(internalType);
                }
            }
        });

        comboBox.setTriggerAction(com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction.ALL);
        comboBox.setLazyRender(false);
        comboBox.setTemplate(
                "<tpl for=\".\"><div class=x-combo-list-item><span>{value:defaultValue(\"&nbsp\")}</span></div></tpl>");
        dynamicComboWidthListener = ClarityComboUtil.enableDynamicSelectionWidthCalculation(comboBox);
    }

    public void disableDynamicComboWidthListener()
    {
        if (dynamicComboWidthListener != null)
            ClarityComboUtil.disableDynamicSelectionWidthCalculation(comboBox, dynamicComboWidthListener);
    }

    public void resetState()
    {
    }

    public void setPermanentList(List<InternalType> permanentList)
    {
        this.permanentList = permanentList;
    }

    public List<InternalType> getPermanentList()
    {
        return permanentList;
    }

    public void setEditable(boolean editable)
    {
        comboBox.setEditable(editable);
    }

    public void select(int index)
    {
        if ((index >= 0) && (index < comboBox.getView().getStore().getCount()))
        {
            comboBox.setValue(comboBox.getView().getStore().getAt(index));
        }
    }

    public HandlerRegistration addSelectionHandler(final SelectionHandler<InternalType> selectionHandler)
    {
        final SelectionChangedListener<SimpleComboValue<DisplayType>> gxtListener =
                new SelectionChangedListener<SimpleComboValue<DisplayType>>()
                {
                    public void selectionChanged(
                            SelectionChangedEvent<SimpleComboValue<DisplayType>> event)
                    {
                        SimpleComboValue<DisplayType> item = event.getSelectedItem();

                        if (item == null)
                            selectionHandler.onSelection(new SelectionEvent<InternalType>(null)
                            {
                            });
                        else
                        {
                            InternalType value = reverseMap.get(item.getValue());
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

    protected void add(List<InternalType> values)
    {
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
        }
        comboBox.add(displayTypes);
    }

    public void updateStore(List<InternalType> values)
    {
        comboBox.removeAll();
        forwardMap.clear();
        reverseMap.clear();
        add(values);
    }

    public List<InternalType> getStore()
    {
        return new ArrayList(forwardMap.keySet());
    }

    public void select(InternalType value)
    {
        comboBox.clearSelections();
        comboBox.setSimpleValue(forwardMap.get(value));
    }

    private HandlerManager innerHandlerManager = new HandlerManager(null);

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

    public void valueExternallyChangedFrom(InternalType oldLocalValue)
    {

    }

    public String getFieldLabel()
    {
        return StringUtil.emptyStringIfNull(comboBox.getFieldLabel());
    }

    public void setFieldLabel(String fieldLabel)
    {
        comboBox.setFieldLabel(fieldLabel);
    }

    public Option<String> getUsageHint()
    {
        return Option.option(comboBox.getEmptyText());
    }

    public void setUsageHint(String usageHint)
    {
        comboBox.setEmptyText(usageHint);
    }

    public InternalType getValue()
    {
        return (comboBox.getSimpleValue() != null && !"".equals(comboBox.getSimpleValue()))
                ? reverseMap.get(comboBox.getSimpleValue()) : null;
    }

    public void setValue(InternalType internalType)
    {
        setValue(internalType, true);
    }

    public void setValue(InternalType internalType, boolean fireChangeEvent)
    {
        comboBox.setFireChangeEventOnSetValue(false);

        boolean set = false;
        // if the value is not null, AND it exists in the store, set. otherwise set null;
        // this will force invalid values that were obtained from the server
        if (internalType != null)
            for (SimpleComboValue<DisplayType> val : comboBox.getStore().getModels())
            {
                if (val != null)
                {
                    InternalType value = reverseMap.get(val.getValue());
                    if (internalType.equals(value))
                    {
                        comboBox.setValue(val);
                        set = true;
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

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<InternalType> tValueChangeHandler)
    {
        final HandlerRegistration valueChangeReg =
                innerHandlerManager.addHandler(ValueChangeEvent.getType(), tValueChangeHandler);

        final HandlerRegistration selectionChangeReg =
                TypeConvertingValidatableComboBox.this.addSelectionHandler(new SelectionHandler<InternalType>()
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

    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        innerHandlerManager.fireEvent(gwtEvent);
    }

    private void fireValidationChangeEvent()
    {
        /*
       *  do not cache old value here, it should be in MutableBinder.java
       * */
        InternalType value = getValue();
        ValueChangeEvent.fire(this, value);
    }

    public SimpleComboBox<DisplayType> getField()
    {
        return comboBox;
    }

    public NonSerOpt<AsyncLoadingList<InternalType>> getLoader()
    {
        return loader;
    }

    public void setLoader(AsyncLoadingList<InternalType> loader)
    {
        this.loader = NonSerOpt.some(loader);
    }

    public void removeAll()
    {
        comboBox.removeAll();
    }

    public boolean revalidateAfterLoad()
    {
        return revalidateAfterLoad;
    }

    public void setRevalidateAfterLoad(boolean revalidateAfterLoad)
    {
        this.revalidateAfterLoad = revalidateAfterLoad;
    }

    public void setWidth(int width)
    {
        comboBox.setWidth(width);
    }

    //todo ef: fix?
    //what if I just want to mask it without any message or spinner????
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

    public SimpleComboBox<DisplayType> getComboBox()
    {
        return getField();
    }

    public void loadUnbound()
    {
        if (loader.isNone())
            throw new AppX("Cannot force a non existant loader");
        loader.value().loadOnce(new AsyncLoadingList.ListLoadCallback<InternalType>()
        {
            public void setList(List<InternalType> internalTypes)
            {
                updateStore(internalTypes);
            }
        });
    }

    public void setValueAsFirstItem()
    {
        if (comboBox.getStore().getCount() > 0)
            comboBox.setSimpleValue(comboBox.getStore().getAt(0).getValue());
    }

    public static final TypeConverter<String, String> getDefaultTypeConverterForAll()
    {
        return new TypeConverter<String, String>()
        {
            public String convert(String s)
            {
                if ("All".equalsIgnoreCase(s))
                    return BASEVC.selectAll();
                else
                    return s;
            }
        };
    }

    public void setEmptyText(String emptyText)
    {
        comboBox.setEmptyText(emptyText);
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

}
