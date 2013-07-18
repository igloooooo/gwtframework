package com.iglooit.commons.iface.util;

import com.clarity.commons.iface.type.AppX;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

public class NumberUtil
{
    private static Date nowDate = TimeUtil.createInaccurateClientDateInvestigateMe();
    private static Random generator = new Random(nowDate.getTime());

    public static Double setDoublePrecision(Double d, Integer precision)
    {
        if (Double.isNaN(d))
        {
            return d;
        }
        BigDecimal bd = new BigDecimal(d).setScale(precision, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

   /**
    * The purpose of this is to fix rounding=0 -> remove .0 part from Double in String
    * Another purpose is to fix java sometimes being stupid parsing 4.5 -> 4.50000000001 or 4.4999999999998
    * */
    public static String getStringFromDouble(Double d, Integer rounding)
    {
        String s = d.toString();
        if (d.isInfinite() || d.isNaN() || !s.contains("."))
            return s;
        Integer indexOfDP = s.indexOf(".");
        if (rounding == null || rounding < 0)
            return s;
        else if (rounding == 0)
            return s.substring(0, indexOfDP);
        else if (s.length() > indexOfDP + rounding + 1)
            return s.substring(0, indexOfDP + rounding + 1);
        else
            return s;
    }

    /**
     * Double.toString() results in the scientific notation when the number is larger than 10^7 or less than 10^-3.
     * This method always returns the plain representation of numbers.
     * */
    public static String toPlainDecimalString(Number num)
    {
        if (num == null)
            return null;
        String numStr = num.toString();
        if (numStr.contains("E") || numStr.contains("e"))
        {
            numStr = (new BigDecimal(numStr)).toPlainString();
            // remove the trailing zeros in the decimal part
            if (numStr.matches(".*\\.\\d*[1-9]0+$"))
            {
                numStr = numStr.replaceAll("0+$", "");
            }
        }
        return numStr;
    }

    public static Double getPercentage(Integer num, Integer denum, Integer precision)
    {
        if (denum == 0)
        {
            throw new AppX("Error, de-numerator is 0");
        }
        Double d = num.doubleValue() * 100.0 / denum.doubleValue();
        BigDecimal bd = new BigDecimal(d).setScale(precision, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static Double getRandomNumber()
    {
        return generator.nextDouble();
    }

    public static Integer zeroValueIfNullI(Integer integer)
    {
        if (integer == null)
            return 0;
        else
            return integer;
    }
}
