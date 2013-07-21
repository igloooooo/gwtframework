package com.iglooit.core.lib.iface;


import com.clarity.commons.iface.type.Tuple2;
import com.clarity.commons.iface.util.NumberUtil;
import com.clarity.commons.iface.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit Util class is to help client to manage/organize the unit displaying on screen by using unitUtilEntity
 * -- should include all logic that translate unit around within same type (dynamic unit value/unit calculation)
 * <p/>
 * Author: Phobos MENG
 */
public class UnitUtil
{
    /* algorithms that we support */
    public static final String ALGORITHM_DEFAULT = "DEFAULT";
    public static final String ALGORITHM_DATA = "DATA";
    public static final String ALGORITHM_TIME = "TIME";
    public static final String ALGORITHM_DO_NOTHING = "DO_NOTHING";

    /* units that we support */
    public static final String UNIT_PERCENTAGE = "%";
    /* for those units we cannot find from our unit definition map */
    public static final String UNIT_DEFAULT = "DEFAULT";
    /* data related, bits, bytes, bps, Bps... */
    public static final String UNIT_BIT = "bits";
    public static final String UNIT_BYTE = "bytes";
    public static final String UNIT_BIT_SHORT = "b";
    public static final String UNIT_BYTE_SHORT = "B";
    public static final String UNIT_BIT_PER_SEC = "bps";
    public static final String UNIT_BYTE_PER_SEC = "Bps";
    /* time related units */
    public static final String UNIT_MILLISECOND = "ms";
    public static final String UNIT_SECOND = "s";


    private static Map<String, String> unitAlgorithmMap = new HashMap<String, String>()
    {
        {
            /* default */
            put(UNIT_DEFAULT, ALGORITHM_DEFAULT);
            /* data */
            put(UNIT_BIT, ALGORITHM_DATA);
            put(UNIT_BYTE, ALGORITHM_DATA);
            put(UNIT_BIT_SHORT, ALGORITHM_DATA);
            put(UNIT_BYTE_SHORT, ALGORITHM_DATA);
            put(UNIT_BIT_PER_SEC, ALGORITHM_DATA);
            put(UNIT_BYTE_PER_SEC, ALGORITHM_DATA);
            /* time */
            put(UNIT_MILLISECOND, ALGORITHM_TIME);
            put(UNIT_SECOND, ALGORITHM_TIME);
            /* no nothing */
            put(UNIT_PERCENTAGE, ALGORITHM_DO_NOTHING);
        }
    };


    private static Map<String, List<UnitUtilEntity>> algorithmDefMap = new HashMap<String, List<UnitUtilEntity>>()
    {
        {
            /**
             * NOTICE HERE:
             * 1. only %s supported in suffix mask
             * 2. only time unit should use mask field with ms,s,m,h,d only
             * */
            put(ALGORITHM_DEFAULT, generateDefaultAlgorithm());
            put(ALGORITHM_DATA, generateDataAlgorithm());
            put(ALGORITHM_TIME, generateTimeAlgorithm());
        }
    };

    /**
     * For undefined unit name or no unit name specified
     * -- thousand, million, billion, trillion...
     *
     * @return
     */
    private static List<UnitUtilEntity> generateDefaultAlgorithm()
    {
        List<UnitUtilEntity> countUnitList = new ArrayList<UnitUtilEntity>();
        countUnitList.add(new UnitUtilEntity(ALGORITHM_DEFAULT, 1000D, 1D, "", "%s"));
        countUnitList.add(new UnitUtilEntity(ALGORITHM_DEFAULT, 1000D * 1000, 1000D, "", "K%s"));
        countUnitList.add(new UnitUtilEntity(ALGORITHM_DEFAULT, 1000D * 1000 * 1000, 1000D * 1000, "", "M%s"));
        countUnitList.add(new UnitUtilEntity(ALGORITHM_DEFAULT, 1000D * 1000 * 1000 * 1000,
            1000D * 1000 * 1000, "", "B%s"));
        countUnitList.add(new UnitUtilEntity(ALGORITHM_DEFAULT, 1000D * 1000 * 1000 * 1000 * 1000,
            1000D * 1000 * 1000 * 1000, "", "T%s"));
        countUnitList.add(new UnitUtilEntity(ALGORITHM_DEFAULT, Double.POSITIVE_INFINITY,
            1000D * 1000 * 1000 * 1000, "", "T%s"));
        return countUnitList;
    }

    /**
     * For those bit/byte/bps/Bps units
     * <p/>
     * e.g. data speed
     * --data transfer speed - based on byte per second = Bps
     * -- KBps, MBps, GBps, TBps...
     *
     * @return
     */
    private static List<UnitUtilEntity> generateDataAlgorithm()
    {
        List<UnitUtilEntity> dataUnitList = new ArrayList<UnitUtilEntity>();
        dataUnitList.add(new UnitUtilEntity(ALGORITHM_DATA, 1024D, 1D, "", "%s"));
        dataUnitList.add(new UnitUtilEntity(ALGORITHM_DATA, 1024D * 1024, 1024D, "", "K%s"));
        dataUnitList.add(new UnitUtilEntity(ALGORITHM_DATA, 1024D * 1024 * 1024, 1024D * 1024, "", "M%s"));
        dataUnitList.add(new UnitUtilEntity(ALGORITHM_DATA, 1024D * 1024 * 1024 * 1024,
            1024D * 1024 * 1024, "", "G%s"));
        dataUnitList.add(new UnitUtilEntity(ALGORITHM_DATA, 1024D * 1024 * 1024 * 1024 * 1024,
            1024D * 1024 * 1024 * 1024, "", "T%s"));
        dataUnitList.add(new UnitUtilEntity(ALGORITHM_DATA, Double.POSITIVE_INFINITY,
            1024D * 1024 * 1024 * 1024, "", "T%s"));
        return dataUnitList;
    }

    /**
     * For time related units
     * -- ms, s, m, h, day...
     *
     * @return
     */
    private static List<UnitUtilEntity> generateTimeAlgorithm()
    {
        List<UnitUtilEntity> timeUnitList = new ArrayList<UnitUtilEntity>();
        timeUnitList.add(new UnitUtilEntity(ALGORITHM_TIME, 1000D, 1D, "ms", ""));
        timeUnitList.add(new UnitUtilEntity(ALGORITHM_TIME, 1000D * 60, 1000D, "s", ""));
        timeUnitList.add(new UnitUtilEntity(ALGORITHM_TIME, 1000D * 60 * 60, 1000D * 60, "m:s", ""));
        timeUnitList.add(new UnitUtilEntity(ALGORITHM_TIME, 1000D * 60 * 60 * 24,
            1000D * 60 * 60, "h:m", ""));
        timeUnitList.add(new UnitUtilEntity(ALGORITHM_TIME, 1000D * 60 * 60 * 24 * 31,
            1000D * 60 * 60 * 24, "d:h", ""));
        timeUnitList.add(new UnitUtilEntity(ALGORITHM_TIME, Double.POSITIVE_INFINITY,
            1000D * 60 * 60 * 24, "d", ""));
        return timeUnitList;
    }


    /**
     * Need to multiply by the number if we found parts in mask (not main)
     */
    private static Map<String, Double> timeMaskCalculationMap = new HashMap<String, Double>()
    {
        {
            put("ms", 1000D);
            put("s", 60D);
            put("m", 60D);
            put("h", 24D);
        }
    };


    /*********************************************************************/
    /************************** methods **********************************/
    /*********************************************************************/

    /**
     * Take a value and unit, return translated value and unit with default rounding
     *
     * @param value
     * @param unit
     * @return
     */
    public static Tuple2<Double, String> translateValueAndUnit(Double value, String unit)
    {
        return translateValueAndUnitWithRounding(value, unit, 2);
    }

    /**
     * Take a value and unit, return translated value and unit
     *
     * @param valueIn
     * @param unitIn
     * @return
     */
    public static Tuple2<Double, String> translateValueAndUnitWithRounding(Double valueIn,
                                                                           String unitIn,
                                                                           Integer roundingIn)
    {
        if (valueIn == null || valueIn.isInfinite() || valueIn.isNaN())
            return new Tuple2<Double, String>(valueIn, unitIn);

        Integer rounding = roundingIn;
        if (roundingIn == null)
        {
            rounding = 2;
        }

        Double sign = valueIn >= 0 ? 1D : -1D;
        Double value = Math.abs(valueIn);
        String unit = unitIn == null ? "" : unitIn;
        // look through unit maps see if an algorithm can be found, otherwise use default
        String algorithm = ALGORITHM_DEFAULT;
        if (StringUtil.isNotBlank(unit) && StringUtil.isNotBlank(unitAlgorithmMap.get(unit)))
            algorithm = unitAlgorithmMap.get(unit);

        List<UnitUtilEntity> unitDefs = algorithmDefMap.get(algorithm);
        // do nothing if unit def is null = do nothing algorithm
        if (unitDefs == null || unitDefs.isEmpty())
            return new Tuple2<Double, String>(NumberUtil.setDoublePrecision(value, rounding) * sign, unit);

        // for time algorithm, we will use ms as base so if value came in as second we need to * 1000
        if (algorithm.equals(ALGORITHM_TIME) && unit.equals(UNIT_SECOND))
        {
            if (value == 0)
                return new Tuple2<Double, String>(0D, UNIT_SECOND);
            value = value * 1000;
            unit = UNIT_MILLISECOND;
        }

        /* loop through sorted unit defs find the proper range and calculate the translated value and mask of unit*/
        for (UnitUtilEntity unitDef : unitDefs)
        {
            if (!unitDef.getUpperBound().equals(Double.POSITIVE_INFINITY) && value >= unitDef.getUpperBound())
                continue;

            Double dividedBy = unitDef.getSimpleDividedBy();
            String newValue = applyMaskToValue(value / dividedBy, unitDef.getMask(), rounding);
            // gwt does not support string.format so we need to work out a better way
//            String newUnit = String.format(unitDef.getSuffix(), unit);
            String newUnit = unitDef.getSuffix().replace("%s", unit);
            // see new value is double or not
            try
            {
                // check 'd' to avoid bad parsing for time unit
                if (newValue.contains("d"))
                    throw new Exception("time unit found.");
                Double newValudD = Double.valueOf(newValue);
                return new Tuple2<Double, String>(NumberUtil.setDoublePrecision(newValudD, rounding) * sign,
                    (sign == -1 && !StringUtil.isBlank(newUnit) && algorithm == ALGORITHM_TIME ? "-" : "") + newUnit);
            }
            catch (Exception e)
            {
                // value is not a double, must be a date, pass string value as unit back and double value as value back
                Double valueD = NumberUtil.setDoublePrecision(value / dividedBy, rounding);
                return new Tuple2<Double, String>(valueD * sign,
                    (sign == -1 && !StringUtil.isBlank(newValue) && algorithm == ALGORITHM_TIME ? "-" : "") + newValue);
            }
        }
        return new Tuple2<Double, String>(value * sign, unit);
    }

    private static String applyMaskToValue(Double value, String mask, Integer rounding)
    {
        if (StringUtil.isBlank(mask))
        {
            return value.toString();
        }
        else if (mask.contains("f"))
        {
            // since gwt does not support string.format we need to find an alternative here
            return value.toString();
//            return String.format(mask, value);
        }
        else
        {
            // date mask need to be considered separately, return value in the proper mask as unit
            // e.g. return 32m12s ...
            return applyDateMaskToValue(value, mask, rounding);
        }
    }

    /**
     * can only handle 2 parts mask for now, need to use ':' as splitter
     *
     * @param value
     * @param mask
     * @return
     */
    private static String applyDateMaskToValue(Double value, String mask, Integer rounding)
    {
        String[] valurParts = value.toString().split("\\.");
        String[] maskParts = mask.split(":");

        if (valurParts.length == 0 || maskParts.length == 0)
            return value.toString();
        String mainValue = valurParts[0];
        String mainMask = maskParts[0];
        String mainFinal = mainValue + maskParts[0];
        if (valurParts.length == 1)
        {
            return mainFinal;
        }
        else
        {
            if (maskParts.length == 1)
            {
                Double refinedValue = NumberUtil.setDoublePrecision(value, rounding);
                String refinedValueStr = NumberUtil.getStringFromDouble(refinedValue, rounding);
                return refinedValueStr + mainMask;
            }
            String subValue = valurParts[1];
            String subMask = maskParts[1];
            // calculate the value for sub mask
            Double subValueD = Double.valueOf("0." + subValue);
            Double multiplier = timeMaskCalculationMap.get(subMask);
            Double subValueReal = NumberUtil.setDoublePrecision(subValueD * multiplier, 0);
            // sub value need to be integer without dot
            Integer subValueInt = subValueReal.intValue();
            String subFinal = subValueInt.toString() + subMask;
            return mainFinal + " " + subFinal;
        }
    }


    /**
     * The expression can have a value place holder 'x' and will be replace with the value from param
     * -- if x not found, just use pure expression
     *
     * @param expression
     * @param value
     * @return
     */
    /*public static Double evaluateExpression(String expression, Double value)
    {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String finalExp = expression.replace("x", value.toString());
        try
        {
            return (Double)engine.eval(finalExp);
        }
        catch (ScriptException e)
        {
            e.printStackTrace();
            return Double.NaN;
        }
    }*/

    /**
     * Take a value and unit, return basic value and unit
     *
     * @param valueIn
     * @param unitIn
     * @param basicUnit
     * @param rounding
     * @return
     */
    public static Tuple2<Double, String> getBasicValue(Double valueIn,
                                                       String unitIn,
                                                       String basicUnit,
                                                       Integer rounding)
    {
        Double value = valueIn;
        String unit = basicUnit;
        // look through unit maps see if an algorithm can be found, otherwise use default
        String algorithm = ALGORITHM_DEFAULT;
        if (StringUtil.isNotBlank(unit) && StringUtil.isNotBlank(unitAlgorithmMap.get(unit)))
            algorithm = unitAlgorithmMap.get(unit);

        List<UnitUtilEntity> unitDefs = algorithmDefMap.get(algorithm);
        // do nothing if unit def is null = do nothing algorithm
        if (unitDefs == null || unitDefs.isEmpty())
            return new Tuple2<Double, String>(NumberUtil.setDoublePrecision(value, rounding), unit);

        /* loop through sorted unit defs find the proper range*/
        for (UnitUtilEntity unitDef : unitDefs)
        {
            String newUnit = unitDef.getSuffix().replace("%s", unit);
            if (algorithm.equals(ALGORITHM_TIME))
                newUnit = unitDef.getMask();
            if (!newUnit.equals(unitIn))
                continue;

            Double dividedBy = unitDef.getSimpleDividedBy();
            Double newValue = value * dividedBy;

            // for time algorithm, we will use ms as base so if value came in as second we need to divided by 1000
            if (algorithm.equals(ALGORITHM_TIME) && basicUnit.equals(UNIT_SECOND))
            {
                newValue = newValue / 1000;
                unit = UNIT_SECOND;
            }
            return new Tuple2<Double, String>(NumberUtil.setDoublePrecision(newValue, rounding), unit);
        }
        return new Tuple2<Double, String>(NumberUtil.setDoublePrecision(value, rounding), unit);
    }

    /**
     * Take a value and unit, return translated value and unit
     *
     * Note: for unit m:s, the returning of this method XX:YY does not mean XX minutes YY seconds, but XX.YY minutes.
     * Same rule applies to other similar unit that contains :
     *
     * @param valueIn
     * @param unitIn
     * @param unitOut
     * @return
     */
    public static Tuple2<Double, String> formatValue(Double valueIn,
                                                     String unitIn,
                                                     String unitOut,
                                                     Integer rounding)
    {
        String unit = StringUtil.emptyStringIfNull(unitIn);
        if (valueIn.isInfinite() || valueIn.isNaN())
            return new Tuple2<Double, String>(valueIn, unit);

        // look through unit maps see if an algorithm can be found, otherwise use default
        String algorithm = ALGORITHM_DEFAULT;
        if (StringUtil.isNotBlank(unit) && StringUtil.isNotBlank(unitAlgorithmMap.get(unit)))
            algorithm = unitAlgorithmMap.get(unit);

        List<UnitUtilEntity> unitDefs = algorithmDefMap.get(algorithm);
        // do nothing if unit def is null = do nothing algorithm
        if (unitDefs == null || unitDefs.isEmpty())
            return new Tuple2<Double, String>(NumberUtil.setDoublePrecision(valueIn, rounding), unit);

        Double sign = valueIn >= 0 ? 1D : -1D;
        Double value = Math.abs(valueIn);

        // for time algorithm, we will use ms as base so if value came in as second we need to * 1000
        if (algorithm.equals(ALGORITHM_TIME) && unit.equals(UNIT_SECOND))
        {
            value = value * 1000;
            unit = UNIT_MILLISECOND;
        }

        /* loop through sorted unit defs find the proper range */
        for (UnitUtilEntity unitDef : unitDefs)
        {
            if (unitOut == null)
            {
                if (!unitDef.getUpperBound().equals(Double.POSITIVE_INFINITY) && value > unitDef.getUpperBound())
                    continue;
            }
            else
            {
                String tempUnit = unitDef.getSuffix().replace("%s", unit);
                if (algorithm.equals(ALGORITHM_TIME))
                    tempUnit = unitDef.getMask();
                if (!tempUnit.equals(unitOut))
                    continue;
            }

            Double dividedBy = unitDef.getSimpleDividedBy();
            Double newValue = value / dividedBy;
            String newUnit = unitDef.getSuffix().replace("%s", unit);
            if (algorithm.equals(ALGORITHM_TIME))
            {
                newUnit = unitDef.getMask();
            }

            Double newValueRounded = NumberUtil.setDoublePrecision(newValue, rounding);
            return new Tuple2<Double, String>(newValueRounded * sign, newUnit);

        }
        return new Tuple2<Double, String>(NumberUtil.setDoublePrecision(value * sign, rounding), unit);
    }
}
