package com.iglooit.core.base.iface.domain;

import com.clarity.commons.iface.domain.ValidationResult;

import java.util.List;

public interface HasValidatingValue<T> extends HasLabelledValue<T>
{
    void handleValidationResults(List<ValidationResult> validationResultList);

    void valueExternallyChangedFrom(T oldLocalValue);
}
