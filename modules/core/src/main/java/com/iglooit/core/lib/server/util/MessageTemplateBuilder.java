package com.iglooit.core.lib.server.util;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Tuple2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 26/09/12
 * Time: 2:55 PM
 */
public class MessageTemplateBuilder
{
    private static final Pattern PATTERN = Pattern.compile("(^|[^\\$]+)(\\$\\$)*(\\$\\{[^}]+\\})");
    public interface FindValueDelegate
    {
        Tuple2<String, Boolean> findValue(String keyword);
    }

    private String template;
    private FindValueDelegate findValueDelegate;

    public MessageTemplateBuilder(FindValueDelegate findValueDelegate)
    {
        this.findValueDelegate = findValueDelegate;
    }

    public MessageTemplateBuilder(String template, FindValueDelegate findValueDelegate)
    {
        this.template = template;
        this.findValueDelegate = findValueDelegate;
    }

    public String getTemplate()
    {
        return template;
    }

    public void setTemplate(String template)
    {
        this.template = template;
    }

    public FindValueDelegate getFindValueDelegate()
    {
        return findValueDelegate;
    }

    public void setFindValueDelegate(FindValueDelegate findValueDelegate)
    {
        this.findValueDelegate = findValueDelegate;
    }

    public String toString()
    {
        Matcher m = PATTERN.matcher(template);

        StringBuffer result = new StringBuffer();
        while (m.find())
        {
            String placeHolder = m.group(3);
            if (placeHolder == null)
                continue;
            String keyword = placeHolder.substring(2, placeHolder.length() - 1);
            Tuple2<String, Boolean> valueResult = findValueDelegate.findValue(keyword);
            if (!valueResult.getSecond())
                throw new AppX(String.format("keyword '%s' not found ", keyword));

            m.appendReplacement(result, m.group(1) + valueResult.getFirst());
        }
        m.appendTail(result);

        return result.toString();
    }

}
