package com.iglooit.core.lib.client;

import com.clarity.commons.iface.type.Tuple2;
import com.clarity.core.base.iface.domain.HasValidatingValue;

import java.util.List;

// interface for mvp, with items such as combo boxes.
public interface HasSelectableValue<T> extends HasValidatingValue<T>
{
    void populateOptions(List<Entry<T>> options);

    void setDefaultOption(int defaultOptionIndex);

    public static class Entry<T> extends Tuple2<T, String>
    {
        public Entry(T dataValue, String description)
        {
            super(dataValue, description);
        }

        public T getDataValue()
        {
            return getFirst();
        }

        public String getDescription()
        {
            return getSecond();
        }

        @Override
        public String toString()
        {
            return getDescription();
        }
    }
}
