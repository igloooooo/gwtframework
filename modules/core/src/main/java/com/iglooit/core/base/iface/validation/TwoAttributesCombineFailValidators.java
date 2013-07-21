package com.iglooit.core.base.iface.validation;

public class TwoAttributesCombineFailValidators
{
    private TwoAttributesCombineFail firstValidator;
    private TwoAttributesCombineFail secondValidator;

    public TwoAttributesCombineFailValidators(String firstProperty, String secondProperty,
                                              Object firstValue, Object secondValue,
                                              String message)
    {
        firstValidator = new TwoAttributesCombineFail(firstProperty, secondProperty,
            firstValue, secondValue, message);
        secondValidator = new TwoAttributesCombineFail(secondProperty, firstProperty,
            secondValue, firstValue, message);
    }

    public TwoAttributesCombineFail getFirstValidator()
    {
        return firstValidator;
    }

    public TwoAttributesCombineFail getSecondValidator()
    {
        return secondValidator;
    }
}
