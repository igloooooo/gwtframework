package com.iglooit.core.base.server.util;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Collection;

/**
 * An extension to Apache's ReflectionToStringBuilder which allows recursively build string presentation for an object.
 *
 * @author Michael Truong
 */
public class RecursiveReflectionToStringBuilder extends ReflectionToStringBuilder
{

    /**
     * @param object
     */
    public RecursiveReflectionToStringBuilder(Object object)
    {
        super(object, new RecursiveToStringStyle());
    }

    /**
     * @param object
     * @param style
     */
    public RecursiveReflectionToStringBuilder(Object object, ToStringStyle style)
    {
        super(object, new RecursiveToStringStyle(style));
    }

    private static class RecursiveToStringStyle extends ToStringStyle
    {
        private static final long serialVersionUID = 1L;
        private ToStringStyle actualToStringStyle;

        RecursiveToStringStyle()
        {
            this.actualToStringStyle = getDefaultStyle();
        }

        RecursiveToStringStyle(ToStringStyle actualToStringStyle)
        {
            this.actualToStringStyle = actualToStringStyle;
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, Object value)
        {
            String valueClassName = value.getClass().getName();
            if (valueClassName.startsWith("java.lang."))
            {
                buffer.append(value);
            }
            else
            {
                buffer.append(ReflectionToStringBuilder.reflectionToString(value, actualToStringStyle));
            }
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName,
                                    @SuppressWarnings("rawtypes") Collection collection)
        {
            for (Object object : collection)
            {
                append(buffer, fieldName, object, Boolean.TRUE);
            }
        }
    }
}
