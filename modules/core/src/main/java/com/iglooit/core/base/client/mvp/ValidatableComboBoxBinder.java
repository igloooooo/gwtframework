package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.combobox.TypeConvertingValidatableComboBox;
import com.clarity.core.lib.client.AsyncLoadingList;
import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.Collections;
import java.util.List;

public class ValidatableComboBoxBinder<D> extends ClarityFieldBinder<D>
{
    //    private ClarityFieldBinder<D> widgetBinder;
    private HandlerRegistration loadingListRegistration;

    public ValidatableComboBoxBinder(final TypeConvertingValidatableComboBox<D, ?> comboBox,
                                     final ValidatableMeta validatableMeta,
                                     final String metaFieldName)
    {
        super(comboBox, validatableMeta, metaFieldName);
//        widgetBinder = new ClarityFieldBinder<D>(
//            comboBox, comboBox.getField(), validatableMeta, metaFieldName);

        if (comboBox.getLoader().isSome())
            // populate comboBox with valid values
            loadingListRegistration =
                comboBox.getLoader().value().addListHandler(
                    new AsyncLoadingList.BeforeLoading<D>()
                    {
                        @Override
                        public void beforeLoading()
                        {
                            comboBox.mask();
                        }
                    },
                    new AsyncLoadingList.ListUpdatedHandler<D>()
                    {
                        public void handle(List<D> updatedList)
                        {
                            D oldValue = comboBox.getValue();

                            comboBox.updateStore(updatedList);

                            comboBox.unMask();

                            if (!isModified())
                                restoreOriginalValue();
                            else
                                comboBox.setValue(oldValue, false);

                            if (comboBox.revalidateAfterLoad())
                                validatableMeta.validateAndNotify(Option.some(metaFieldName));
                            else
                                comboBox.handleValidationResults(Collections.<ValidationResult>emptyList());
                        }
                    });

        clearInvalidFieldSigns();
    }

    private void clearInvalidFieldSigns()
    {
        handleValidationResults(Collections.<ValidationResult>emptyList());
    }

    public void unbind()
    {
        super.unbind();
        if (loadingListRegistration != null)
            loadingListRegistration.removeHandler();
    }
}
