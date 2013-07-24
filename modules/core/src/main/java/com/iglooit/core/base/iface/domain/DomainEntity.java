package com.iglooit.core.base.iface.domain;

import com.iglooit.commons.iface.annotation.NoMetaAccess;
import com.iglooit.commons.iface.domain.I18NFactoryProvider;
import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.DelegatingMetaEntity;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.Option;
import com.iglooit.commons.iface.util.StringUtil;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class DomainEntity extends DelegatingMetaEntity implements ValidatableMeta
{
    protected static final FieldNameConstants FNC = I18NFactoryProvider.get(FieldNameConstants.class);

    @NoMetaAccess
    private transient List<Validator> validators;
    @NoMetaAccess
    private transient Map<String, List<ValidationHandler>> validationHandlers;

    public String getDefaultFieldLabel(String propertyName)
    {
        return DomainEntity.getDefaultFieldLabelStatic(propertyName);
    }

    public static String getDefaultFieldLabelStatic(String propertyName)
    {
        String localPropertyName = propertyName;
        String[] metaSplittedStr = localPropertyName.split(Meta.FIELD_META_SPLIT);

        if (metaSplittedStr != null && metaSplittedStr.length > 0)
        {
            localPropertyName = metaSplittedStr[0];
        }

        String[] propNameParts = localPropertyName.split("\\-");
        if (propNameParts.length > 0)
            return propNameParts[propNameParts.length - 1];
        return localPropertyName;
    }

    public String getDefaultPropertyNameToEdit()
    {
        return getPropertyNames().iterator().next();
    }

    private static String singleFieldNameFromMetaPropertyName(String propertyValue)
    {
        String[] s = propertyValue.split("\\-");
        return s[s.length - 1];
    }

    public static String getFieldNameFromMetaPropertyName(String propertyValue)
    {
        StringBuilder sb = new StringBuilder();
        if (propertyValue.contains(Meta.FIELD_META_SPLIT))
        {
            String[] entities = propertyValue.split(Meta.FIELD_META_SPLIT_REGEX);
            List<String> str = new ArrayList<String>();
            for (String entity : entities)
                str.add(singleFieldNameFromMetaPropertyName(entity));
            return StringUtil.join(".", str);
        }
        else
        {
            return singleFieldNameFromMetaPropertyName(propertyValue);
        }
    }

    public static String getClassNameFromMetaPropertyName(String propertyValue)
    {
        if (propertyValue.contains(Meta.FIELD_META_SPLIT))
        {
            String[] entities = propertyValue.split(Meta.FIELD_META_SPLIT_REGEX);
            return getClassNameFromMetaPropertyName(entities[entities.length - 1]);
        }
        else
        {
            String field = getFieldNameFromMetaPropertyName(propertyValue);
            return propertyValue.substring(0, propertyValue.length() - field.length() - 1).replaceAll("\\-", ".");
        }
    }

    public String getDefaultFieldUsageHint(String propertyName)
    {
        return "";
    }

    public abstract List<Validator> doGetValidators();

    private List<Validator> getValidators()
    {
        if (validators == null)
            validators = doGetValidators();
        return validators;
    }

    public List<Validator> getValidators(Option<String> propertyNameOption)
    {
        List<Validator> validators = new ArrayList<Validator>();
        for (Validator validator : getValidators())
            if (propertyNameOption.isNone() || validator.getTags().contains(propertyNameOption.value()))
                validators.add(validator);
        return validators;
    }

    public List<ValidationResult> validate(Option<String> metaFieldName, List<Validator> validators)
    {
        List<ValidationResult> validationResults = new ArrayList<ValidationResult>();
        for (Validator validator : validators)
            if (metaFieldName.isNone() || validator.getTags().contains(metaFieldName.value()))
                validationResults.addAll(validator.validate(this));
        return validationResults;
    }

    public List<ValidationResult> validate(Option<String> metaFieldName)
    {
        return validate(metaFieldName, getValidators());
    }

    public void validateAndNotify(Option<String> metaFieldName)
    {
        notifyValidationHandlers(validate(metaFieldName), metaFieldName);
    }

    public void validateAndNotify(Option<String> metaFieldName, List<Validator> validators)
    {
        notifyValidationHandlers(validate(metaFieldName, validators), metaFieldName);
    }

    public void notifyValidationHandlers(List<ValidationResult> validationResults, Option<String> metaFieldName)
    {
        Map<String, List<ValidationResult>> results = new HashMap<String, List<ValidationResult>>();

        for (ValidationResult validationResult : validationResults)
        {
            for (String tag : validationResult.getTags())
            {
                if (!results.containsKey(tag))
                    results.put(tag, new ArrayList<ValidationResult>());
                results.get(tag).add(validationResult);
            }
        }

        if (validationHandlers != null)
        {
            Set<String> relevantValidationHandlerTags;
            //JM: Commented the validation results only being shown for the field validation was asked for,
            //this allows validation to fail on a related field, not the one that was changed.

//            if (metaFieldName.isSome())
//                relevantValidationHandlerTags = new TreeSet<String>(Arrays.asList(metaFieldName.value()));
//            else
            //end of comment

            relevantValidationHandlerTags = validationHandlers.keySet();

            for (String tag : relevantValidationHandlerTags)
            {
                List<ValidationHandler> handlers = validationHandlers.get(tag);

                // handlers may be null when one attribute affects another attribute
                // and try to notify another attribute handler, which hasn't been bind yet
                if (handlers == null)
                    return;
                //throw new AppX("Calling specific validation for unbound propertyName: " + tag);

                for (ValidationHandler handler : handlers)
                {
                    if (results.containsKey(tag))
                        handler.handleValidationResults(results.get(tag));
                    else if (metaFieldName.isNone() || (metaFieldName.isSome() && metaFieldName.value().equals(tag)))
                        handler.handleValidationResults(Collections.<ValidationResult>emptyList());
                    // else not the tag being validated against, don't handle 
                }
            }
        }
    }

    public boolean isValidEntity()
    {
        return validate(Option.<String>none()).size() == 0;
    }

    public HandlerRegistration registerValidationHandler(final ValidationHandler validationHandler,
                                                         final String tagOfInterest)
    {
        if (validationHandlers == null)
            validationHandlers = new HashMap<String, List<ValidationHandler>>();
        if (!validationHandlers.containsKey(tagOfInterest))
            validationHandlers.put(tagOfInterest, new ArrayList<ValidationHandler>());
        validationHandlers.get(tagOfInterest).add(validationHandler);
        return new HandlerRegistration()
        {
            public void removeHandler()
            {
                if (validationHandlers != null)
                    if (validationHandlers.containsKey(tagOfInterest))
                        validationHandlers.get(tagOfInterest).remove(validationHandler);
            }
        };
    }

    public void clearValidationHandlers()
    {
        if (validationHandlers != null)
            validationHandlers.clear();
        validationHandlers = null;
    }
}
