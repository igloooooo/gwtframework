package com.iglooit.core.base.client.mvp;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.widget.combobox.ValidatableComplexComboBox;
import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.core.lib.client.AsyncLoadingList;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.Collections;
import java.util.List;

public class ValidatableComplexComboBoxBinder<T extends ModelData> extends ClarityFieldBinder<T>
{
    private HandlerRegistration loadingListRegistration;
    private ValidatableComplexComboBox complexComboBox;

    public ValidatableComplexComboBoxBinder(final ValidatableComplexComboBox<T, ?> comboBox,
                                            final ValidatableMeta validatableMeta,
                                            final String metaFieldName)
    {
        super(comboBox, validatableMeta, metaFieldName);
        this.complexComboBox = comboBox;
        if (comboBox.getLoader().isSome())
            // populate comboBox with valid values
            loadingListRegistration =
                comboBox.getLoader().value().addListHandler(
                    new AsyncLoadingList.BeforeLoading<T>()
                    {
                        @Override
                        public void beforeLoading()
                        {
                            comboBox.mask();
                        }
                    },
                    new AsyncLoadingList.ListUpdatedHandler<T>()
                    {
                        public void handle(List<T> updatedList)
                        {
                            T oldValue = comboBox.getValue();

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
                    }
                );

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

    @Override
    public boolean isValueValid()
    {
        return super.isValueValid() && complexComboBox.isValid();
    }
}
