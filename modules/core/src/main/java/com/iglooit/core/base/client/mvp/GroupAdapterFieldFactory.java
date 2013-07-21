package com.iglooit.core.base.client.mvp;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapterFieldFactory
{
    private List<TwoFieldInfo> twoFieldInfoList = new ArrayList<TwoFieldInfo>();

    public GroupAdapterFieldFactory(TwoFieldInfo... twoFieldInfos)
    {
        for (TwoFieldInfo twoFieldInfo : twoFieldInfos)
        {
            twoFieldInfoList.add(twoFieldInfo); 
        }
    }

    public GroupAdapterField getViewOnlyField()
    {
        List<GroupAdapterField.FieldInfo> fieldInfos = new ArrayList<GroupAdapterField.FieldInfo>();
        for (TwoFieldInfo twoFieldInfo : twoFieldInfoList)
        {
            GroupAdapterField.FieldInfo fieldInfo =
                new GroupAdapterField.FieldInfo(twoFieldInfo.getProperty(),
                    twoFieldInfo.getImmutableField(), twoFieldInfo.getProportion());
            fieldInfos.add(fieldInfo);
        }
        return new GroupAdapterField(fieldInfos);
    }

    public GroupAdapterField getEditField()
    {
        List<GroupAdapterField.FieldInfo> fieldInfos = new ArrayList<GroupAdapterField.FieldInfo>();
        for (TwoFieldInfo twoFieldInfo : twoFieldInfoList)
        {
            GroupAdapterField.FieldInfo fieldInfo =
                new GroupAdapterField.FieldInfo(twoFieldInfo.getProperty(),
                    twoFieldInfo.getMutableField(), twoFieldInfo.getProportion());
            fieldInfos.add(fieldInfo);
        }
        return new GroupAdapterField(fieldInfos);
    }

    public static final class TwoFieldInfo
    {
        private String property;
        private ClarityField mutableField;
        private ClarityField immutableField;
        private double proportion;

        public TwoFieldInfo(String property, ClarityField mutableField,
                            ClarityField immutableField, double proportion)
        {
            this.property = property;
            this.mutableField = mutableField;
            this.immutableField = immutableField;
            this.proportion = proportion;
        }

        public String getProperty()
        {
            return property;
        }

        public ClarityField getMutableField()
        {
            return mutableField;
        }

        public ClarityField getImmutableField()
        {
            return immutableField;
        }

        public double getProportion()
        {
            return proportion;
        }
    }
}
