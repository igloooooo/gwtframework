package com.iglooit.core.base.client.widget.combobox;

import com.clarity.core.lib.client.AsyncLoadingList;
import com.clarity.core.lib.iface.TypeConverter;

import java.util.List;

public class ValidatableComboBox<T> extends TypeConvertingValidatableComboBox<T, T>
{
    public ValidatableComboBox(AsyncLoadingList<T> loader)
    {
        super(loader, new IdentityTypeConverter<T>());
    }

    public ValidatableComboBox(List<T> options, TypeConverter<T, T> converter)
    {
        super(options, converter);
    }

    public ValidatableComboBox(List<T> options)
    {
        super(options, new IdentityTypeConverter<T>());
    }

    private static final class IdentityTypeConverter<D> implements TypeConverter<D, D>
    {
        public D convert(D d)
        {
            return d;
        }
    }

    public void removeListFromStore(List<T> duplicateValueList)
    {
        updateStoreAsPermanentStore();
        deleteDuplicateList(duplicateValueList);
    }

    private void deleteDuplicateList(List<T> duplicateValueList)
    {
        for (T internalType : duplicateValueList)
        {
            for (int i = 0; i < getComboBox().getStore().getCount(); i++)
            {
                if (getComboBox().getStore().getAt(i).getValue().equals(internalType))
                {
                    getComboBox().getStore().remove(getComboBox().getStore().getAt(i));
                    break;
                }
            }
        }
    }

    private void updateStoreAsPermanentStore()
    {
        getComboBox().getStore().removeAll();
        updateStore(getPermanentList());
    }

    public void clear()
    {
        getComboBox().clear();
    }

    public void clearInvalid()
    {
        getField().clearInvalid();
        getComboBox().clearInvalid();
    }

    public void clearSelection()
    {
        getComboBox().clearSelections();
    }

    public void setSimpleValue(T value)
    {
        getComboBox().setSimpleValue(value);
    }

    public void setForceSelection(boolean force)
    {
        getComboBox().setForceSelection(force);
    }
}
