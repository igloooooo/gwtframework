package com.iglooit.core.base.client.view;

import com.clarity.core.base.client.view.resource.Resource;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.tips.QuickTip;

/**
 * General utility class for forms UI related functions
 */
public class FormUtils
{
    public static final String DEFAULT_FORM_DATA_WIDTH = "93%";
    public static final String REQUIRED_LABEL_SEPARATOR = "<span class='required'>*</span>";
    public static final String DEFAULT_LABEL_SEPARATOR = ":";

    private static final String HELP_LABEL_SEPARATOR_PREFIX = "<span class='usage-hint cursor-help' ";
    private static final String HINT_PLACEHOLDER = "HINT_PLACEHOLDER";
    public static final String HELP_LABEL_SEPARATOR = HELP_LABEL_SEPARATOR_PREFIX + "qtip='" + HINT_PLACEHOLDER +
        "'>" + Resource.ICONS_SIMPLE.question().getHTML() + "</span>";

    /**
     * Styles a field as mandatory by appending an '*' character
     * @param field
     */
    public static void addMandatoryStyle(Field field)
    {
        if (field.getLabelSeparator() == null)
            field.setLabelSeparator(DEFAULT_LABEL_SEPARATOR);

        String previousLabelSeparator = field.getLabelSeparator();
        String newLabelSeparator;
        if (previousLabelSeparator.contains(REQUIRED_LABEL_SEPARATOR))
            newLabelSeparator = previousLabelSeparator;
        else
            newLabelSeparator = REQUIRED_LABEL_SEPARATOR + previousLabelSeparator;

        field.setLabelSeparator(newLabelSeparator);
    }

    /**
     * This method must be called to enable field usage hints to display correctly.
     * @param parentComponent This is the parent component or container that the field is added to (and not the field
     * itself) 
     */
    public static void enableUsageHints(Component parentComponent)
    {
        new QuickTip(parentComponent);
    }

    /**
     * Appends a help icon and styled usage hint tooltip to the right of a field label
     * Important: To view usage hints correctly, you must enable the parent container of the field
     * (and not the field itself) to support tooltips using FormUtils.enableUsageHints(parentContainer)
     * @param field
     * @param hint
     */
    public static void addUsageHint(Field field, String hint)
    {
        if (field.getLabelSeparator() == null)
            field.setLabelSeparator(DEFAULT_LABEL_SEPARATOR);

        String previousLabelSeparator = field.getLabelSeparator();
        String newLabelSeparator;

        if (previousLabelSeparator.contains(HELP_LABEL_SEPARATOR_PREFIX))
            newLabelSeparator = previousLabelSeparator;
        else
            newLabelSeparator = previousLabelSeparator + HELP_LABEL_SEPARATOR.replaceFirst(HINT_PLACEHOLDER, hint);

        field.setLabelSeparator(newLabelSeparator);
    }

    /**
     * return a html snippet containing a help icon and styled usage hint tooltip to the right of a field label
     * Important: To view usage hints correctly, you must enable the parent container of the field
     * (and not the field itself) to support tooltips using FormUtils.enableUsageHints(parentContainer)
     * @param hint
     * @return hint html
     */
    public static String getUserHint(String hint)
    {
        return HELP_LABEL_SEPARATOR.replaceFirst(HINT_PLACEHOLDER, hint);
    }

    public static FormData getDefaultFormData()
    {
        return new FormData(DEFAULT_FORM_DATA_WIDTH);
    }
}
