package com.iglooit.core.base.client.mvp;

import com.clarity.commons.iface.domain.ValidationResult;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Fred Date: 27/06/12 Time: 12:12 AM
 */
public interface ValidationResultHandler
{
    void handleValidationResults(List<ValidationResult> validationResultList);
}

