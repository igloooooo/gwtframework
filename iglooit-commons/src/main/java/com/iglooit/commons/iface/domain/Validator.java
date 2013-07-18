package com.iglooit.commons.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class Validator
{
    private List<String> tags;

    protected Validator(String... tags)
    {
        this.tags = Arrays.asList(tags);
    }

    protected Validator(Collection<String> tags)
    {
        this.tags = new ArrayList<String>(tags);
    }

    public abstract List<ValidationResult> validate(Meta instance);

    public List<String> getTags()
    {
        return tags;
    }

    protected static List<String> getTagsListForValidators(Validator... validators)
    {
        List<String> list = new ArrayList<String>();
        for (Validator validator : validators)
        {
            list.addAll(validator.getTags());
        }
        return list;
    }
}
