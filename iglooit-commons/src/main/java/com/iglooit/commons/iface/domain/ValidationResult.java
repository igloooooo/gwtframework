package com.iglooit.commons.iface.domain;

import java.io.Serializable;
import java.util.List;

public class ValidationResult implements Serializable
{
    private List<String> tags;
    private String reason;
    private String fieldLabel;
    private boolean displayInErrorSummary;

    public ValidationResult()
    {
        // for gwt serialization, shouldn't be called
    }

    public ValidationResult(List<String> tags, String reason)
    {
        this(tags, reason, true);
    }

    public ValidationResult(List<String> tags, String reason, boolean displayInErrorSummary)
    {
        this.tags = tags;
        this.reason = reason;
        this.displayInErrorSummary = displayInErrorSummary;
    }

    public String getReason()
    {
        return reason;
    }

    public boolean matches(String tagRegexp)
    {
        for (String tag : tags)
            if (tag.matches(tagRegexp))
                return true;
        return false;
    }

    public List<String> getTags()
    {
        return tags;
    }

    public String getFieldLabel()
    {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel)
    {
        this.fieldLabel = fieldLabel;
    }

    public boolean isDisplayInErrorSummary()
    {
        return displayInErrorSummary;
    }

    public void setDisplayInErrorSummary(boolean displayInErrorSummary)
    {
        this.displayInErrorSummary = displayInErrorSummary;
    }
}
