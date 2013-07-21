package com.iglooit.core.expr.iface.domain;

public enum RuleType
{
    VALIDATION, ACCESS, MODIFICATION, RATING;


    @Override
    public String toString()
    {
        switch (this)
        {
            case VALIDATION:
                return "General Validation";
            case ACCESS:
                return "Read Access";
            case RATING:
                return "Rating";
            case MODIFICATION:
                return "Modify Access";
            default:
                return "Unknown";
        }
    }

}
