package com.iglooit.core.base.iface.domain;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.AppX;

import java.util.List;
import java.util.Map;

public class ValidationX extends AppX
{
    private Map<Validatable, List<ValidationResult>> validationResults;

    public ValidationX()
    {
    }

    public ValidationX(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ValidationX(String message, Map<Validatable, List<ValidationResult>> validationResults)
    {
        super(message);
        this.validationResults = validationResults;
    }

    public Map<Validatable, List<ValidationResult>> getValidationResults()
    {
        return validationResults;
    }

    public void setValidationResults(Map<Validatable, List<ValidationResult>> validationResults)
    {
        this.validationResults = validationResults;
    }
}
