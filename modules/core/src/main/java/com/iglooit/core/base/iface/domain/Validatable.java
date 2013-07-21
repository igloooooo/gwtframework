package com.iglooit.core.base.iface.domain;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.type.Option;
import com.google.gwt.event.shared.HandlerRegistration;

import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlType(name = "Validatable")
public interface Validatable
{
    List<Validator> getValidators(Option<String> propertyNameOption);

    List<ValidationResult> validate(Option<String> metaFieldName);

    List<ValidationResult> validate(Option<String> metaFieldName, List<Validator> validators);

    void validateAndNotify(Option<String> metaFieldName);

    HandlerRegistration registerValidationHandler(ValidationHandler validationHandler, String propertyName);

    void clearValidationHandlers();
}
