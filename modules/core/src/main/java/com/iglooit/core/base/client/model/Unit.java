package com.iglooit.core.base.client.model;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.lib.iface.UnitUtil;

import java.util.Arrays;
import java.util.List;

public class Unit
{
    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    public enum UnitEnum
    {

        PERCENTAGE(UnitUtil.UNIT_PERCENTAGE, BVC.percentage()),
        BIT(UnitUtil.UNIT_BIT, BVC.bits()),
        BYTE(UnitUtil.UNIT_BYTE, BVC.bytes()),
        BIT_SHORT(UnitUtil.UNIT_BIT_SHORT, BVC.bits()),
        BYTE_SHORT(UnitUtil.UNIT_BYTE_SHORT, BVC.bytes()),
        BIT_PER_SECOND(UnitUtil.UNIT_BIT_PER_SEC, BVC.bitsPerSecond()),
        BYTE_PER_SECOND(UnitUtil.UNIT_BYTE_PER_SEC, BVC.bytesPerSecond()),
        MILLISECOND(UnitUtil.UNIT_MILLISECOND, BVC.millisecond()),
        SECOND(UnitUtil.UNIT_SECOND, BVC.second()),
        CUSTOM("", BVC.customUnit());

        private String symbol;
        private String localisedDescription;

        UnitEnum(String symbol, String localisedDescription)
        {
            this.symbol = symbol;
            this.localisedDescription = localisedDescription;
        }

        public String getSymbol()
        {
            return symbol;
        }

        public String getLocalisedDescription()
        {
            return localisedDescription;
        }

        public String getDisplayText()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(localisedDescription);
            if (StringUtil.isNotEmpty(symbol))
                sb.append(" (").append(symbol).append(") ");

            return sb.toString();
        }

        public static List<UnitEnum> getPreDefinedUnits()
        {
            return Arrays.asList(
                PERCENTAGE,
                BIT, BIT_SHORT, BYTE, BYTE_SHORT, BIT_PER_SECOND, BYTE_PER_SECOND,
                MILLISECOND, SECOND,
                CUSTOM);
        }

        @Override
        public String toString()
        {
            return getDisplayText();
        }

        public static UnitEnum getUnitEnum(String symbol)
        {
            for (UnitEnum item : getPreDefinedUnits())
            {
                if (item.getSymbol().equals(symbol))
                    return item;
            }

            return CUSTOM;
        }
    }
}