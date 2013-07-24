package com.iglooit.core.base.iface.domain;

import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.NoMetaPropertyNameX;
import com.iglooit.commons.iface.type.Option;
import com.google.gwt.event.shared.HandlerRegistration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MetaBasedCombinedDomainEntity implements ValidatableMeta, Serializable
{
    // this is a serializable delegate, unlike the regular DomainEntity transient delegate
    private List<DomainEntity> domainEntityList;
    private Meta nonTransientDelegate;
    private transient Map<String, List<ValidationHandler>> validationHandlers;

    protected MetaBasedCombinedDomainEntity()
    {
    }

    protected MetaBasedCombinedDomainEntity(List<DomainEntity> deList)
    {
        domainEntityList = deList;
        generateDelegateMeta();
    }

    private void generateDelegateMeta()
    {
        if (nonTransientDelegate == null)
            nonTransientDelegate = createMetaDelegate();
        if (domainEntityList == null)
            return;

        for (DomainEntity de : domainEntityList)
        {
            for (String property : de.getMetaDelegate().getPropertyNames())
            {
                nonTransientDelegate.set(property, de.<Object>get(property));
            }
        }
    }

    protected Meta getMetaDelegate()
    {
        if (nonTransientDelegate == null)
            generateDelegateMeta();
        return nonTransientDelegate;
    }

    protected Meta createMetaDelegate()
    {
        return new MapDerivedMeta();
    }

    @Override
    public String getDefaultFieldLabel(String propertyName)
    {
        if (domainEntityList != null)
            for (DomainEntity de : domainEntityList)
            {
                if (de.getPropertyNames().contains(propertyName))
                    return de.getDefaultFieldLabel(propertyName);
            }
        return DomainEntity.getDefaultFieldLabelStatic(propertyName);
    }

    @Override
    public String getDefaultFieldUsageHint(String propertyName)
    {
        if (domainEntityList == null)
            return "";
        for (DomainEntity de : domainEntityList)
        {
            if (de.getPropertyNames().contains(propertyName))
                return de.getDefaultFieldUsageHint(propertyName);
        }
        return "";
    }

    @Override
    public List<Validator> getValidators(Option<String> propertyNameOption)
    {
        List<Validator> vList = new ArrayList<Validator>();
        if (domainEntityList == null)
            return vList;
        for (DomainEntity de : domainEntityList)
        {
            vList.addAll(de.getValidators(propertyNameOption));
        }
        return vList;
    }

    @Override
    public List<ValidationResult> validate(Option<String> metaFieldName)
    {
        List<ValidationResult> vList = new ArrayList<ValidationResult>();
        if (domainEntityList == null)
            return vList;
        for (DomainEntity de : domainEntityList)
        {
            vList.addAll(de.validate(metaFieldName));
        }
        return vList;
    }

    @Override
    public List<ValidationResult> validate(Option<String> metaFieldName, List<Validator> validators)
    {
        List<ValidationResult> vList = new ArrayList<ValidationResult>();
        if (domainEntityList == null)
            return vList;
        for (DomainEntity de : domainEntityList)
        {
            vList.addAll(de.validate(metaFieldName, validators));
        }
        return vList;
    }

    @Override
    public void validateAndNotify(Option<String> metaFieldName)
    {
        if (domainEntityList == null)
            return;
        for (DomainEntity de : domainEntityList)
        {
            de.validateAndNotify(metaFieldName);
        }
    }

    @Override
    public HandlerRegistration registerValidationHandler(ValidationHandler validationHandler, String propertyName)
    {
        if (domainEntityList != null)
            for (DomainEntity de : domainEntityList)
            {
                if (de.getPropertyNames().contains(propertyName))
                    return de.registerValidationHandler(validationHandler, propertyName);
            }
        return new HandlerRegistration()
        {
            public void removeHandler()
            {
            }
        };
    }

    @Override
    public void clearValidationHandlers()
    {
        if (domainEntityList == null)
            return;
        for (DomainEntity de : domainEntityList)
        {
            de.clearValidationHandlers();
        }
    }

    @Override
    public <X> X get(String propertyName)
    {
        if (domainEntityList != null)
            for (DomainEntity de : domainEntityList)
            {
                if (de.getPropertyNames().contains(propertyName))
                    return (X)de.get(propertyName);
            }
        return (X)nonTransientDelegate.get(propertyName);
    }

    @Override
    public String getPropertyTypeName(String propertyName)
    {
        if (domainEntityList != null)
            for (DomainEntity de : domainEntityList)
            {
                if (de.getPropertyNames().contains(propertyName))
                    return de.getPropertyTypeName(propertyName);
            }
        throw new NoMetaPropertyNameX("getPropertyTypeName", propertyName);
    }

    @Override
    public Map<String, Object> getProperties()
    {
        Map<String, Object> propertiesMap = new HashMap<String, Object>();
        if (domainEntityList == null)
            return propertiesMap;
        for (DomainEntity de : domainEntityList)
        {
             propertiesMap.putAll(de.getProperties());
        }
        propertiesMap.putAll(nonTransientDelegate.getProperties());
        return propertiesMap;
    }

    @Override
    public List<String> getPropertyNames()
    {
        List<String> vList = new ArrayList<String>();
        if (domainEntityList == null)
            return vList;
        for (DomainEntity de : domainEntityList)
        {
            vList.addAll(de.getPropertyNames());
        }
        vList.addAll(initExtraProperties());
        return vList;
    }

    protected abstract List<String> initExtraProperties();

    @Override
    public void set(String propertyName, Object value)
    {
        if (domainEntityList != null)
        {
            for (DomainEntity de : domainEntityList)
            {
                if (de.getPropertyNames().contains(propertyName))
                {
                    de.set(propertyName, value);
                    return;
                }
            }
        }
        nonTransientDelegate.set(propertyName, value);
    }

    public <X extends DomainEntity> X getEntity(Class<X> entityClass)
    {
        if (domainEntityList != null)
        {
            for (DomainEntity de : domainEntityList)
            {
                if (de.getClass().equals(entityClass))
                    return (X)de;
            }
        }

        throw new AppX(entityClass.toString() + " is not a part of this combined entity");
    }
}
