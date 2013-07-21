package com.iglooit.core.lib.iface;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * UnitUtil is the class of holding reference data/rules/definitions of all units that we need to
 * display on screen dynamically
 * <p/>
 * To minimized the impact of release we are now putting this as java hard code
 * TODO PM: I believe the best way to get this data referenced is to migrate them into DB ref data table
 * <p/>
 * Author: Phobos MENG
 */
public class UnitUtilEntity implements IsSerializable, Serializable
{
    /* fields definitions */
    private String unit;
    /* upper bound of current range end, use current unit def if value is not higher than this bound */
    private Double upperBound;

    /* mask need to be either
    * -- empty
    * -- double value mask that contain 'f'
    * -- time value mask: ms,ss,mm,hh,dd...
    * */
    private String mask;
    /* unit suffix will be used to parse in string.format so %s needed if you want to translate from old to new */
    private String suffix;
    /* a double value that will be divided by to calculate new proper display value */
    private Double simpleDividedBy;

    /* optional, if evaluation rule is empty, use simpleDividedBy */
//    private String evaluationRule;

    public UnitUtilEntity(String unit, Double upperValue, Double simpleDividedBy,
                          String mask, String suffix)
    {
        this.unit = unit;
        this.upperBound = upperValue;
        this.simpleDividedBy = simpleDividedBy;
        this.mask = mask;
        this.suffix = suffix;
    }

    public String getUnit()
    {
        return unit;
    }

    public Double getUpperBound()
    {
        return upperBound;
    }

    public Double getSimpleDividedBy()
    {
        return simpleDividedBy;
    }

    public String getMask()
    {
        return mask;
    }

    public String getSuffix()
    {
        return suffix;
    }
}
