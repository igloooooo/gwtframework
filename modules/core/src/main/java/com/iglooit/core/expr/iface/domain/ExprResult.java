package com.iglooit.core.expr.iface.domain;

import java.util.*;

public class ExprResult
{
    private Object result;
    private Map<String, Object> context = new HashMap<String, Object>();

    /**
     * Only expected for use where ExprDef::canHaveChildren is false -- therefore a leaf node.
     *
     * @param result
     */
    public ExprResult(Object result)
    {
        this.result = result;
    }

    public ExprResult(Object result, ExprResult... childResults)
    {
        this.result = result;
        //merge the maps of all the children results
        // - note that if there is a collision, and the collision occurs with a collection then the
        // two collections are merged. This supports the case where for example you have multiple
        // sources of validation error messages
        for (ExprResult childResult : childResults)
        {
            for (Map.Entry<String, Object> entry : childResult.context.entrySet())
            {
                final String key = entry.getKey();
                final Object contextValue = context.get(key);
                final Object entryValue = entry.getValue();

                if (contextValue == null)
                    context.put(key, entryValue);
                else if (contextValue instanceof List)
                {
                    List contextValueList = (List)contextValue;
                    List entryValueList = (List)entryValue;
                    List resultList = new ArrayList(contextValueList.size() + entryValueList.size());
                    resultList.addAll(contextValueList);
                    resultList.addAll(entryValueList);
                    context.put(key, resultList);
                }
                else if (contextValue instanceof Set)
                {
                    Set contextValueSet = (Set)contextValue;
                    Set entryValueSet = (Set)entryValue;
                    Set resultSet = new HashSet(contextValueSet.size() + entryValueSet.size());
                    resultSet.addAll(contextValueSet);
                    resultSet.addAll(entryValueSet);
                    context.put(key, resultSet);
                }
                else if (contextValue instanceof Map)
                {
                    Map contextValueMap = (Map)contextValue;
                    Map entryValueMap = (Map)entryValue;
                    Map resultMap = new HashMap(contextValueMap.size() + entryValueMap.size());
                    resultMap.putAll(contextValueMap);
                    resultMap.putAll(entryValueMap);
                    context.put(key, resultMap);
                }
                else
                    context.put(key, entryValue);
            }
        }
    }

    public ExprResult(Object result, Map<String, Object> context)
    {
        this.result = result;
        this.context = context;
    }

    public Object getResult()
    {
        return result;
    }

    public int contextSize()
    {
        return context.size();
    }

    public Object getContextValue(String key)
    {
        return context.get(key);
    }

    public Map<String, Object> getContext()
    {
        return context;
    }
}