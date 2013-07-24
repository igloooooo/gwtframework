package com.iglooit.core.base.iface.domain;

import com.iglooit.commons.iface.domain.ValidationResult;

import java.util.List;

public interface ValidationHandler
{
    void handleValidationResults(List<ValidationResult> resultList);
}
