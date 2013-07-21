package com.iglooit.core.base.client.navigator;

import com.clarity.commons.iface.type.Tuple2;
import com.clarity.core.base.iface.expression.ISimpleExpression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleViewElement
{
    public static final String HELP_NAME = "hn";
    public static final String TABLE_NAME = "tn";
    public static final String SEARCH_MODE = "isSimpleMode";
    public static final String VIEW_ELEMENT_TYPE = "vet";
    public static final String SERVICE_BREACH_NODE = "sbn";
    public static final String SERVICE_BREACH_CHILD_NODE = "sbcn";
    public static final String PERFORMANCE_NODE = "pn";
    public static final String PERFORMANCE_CHILD_NODE = "pcn";
    public static final String SEARCHED_RESULT_NODE = "srn";
    public static final String GEO_DRAWABLE_ENTITY = "gde";
    public static final String QUERY_TREE_TABLE_NAME = "qt";
    private LinkedList<ISimpleExpression> queryTree = new LinkedList<ISimpleExpression>();
    private LinkedHashMap<String, Object> attributes = new LinkedHashMap<String, Object>();

    public SimpleViewElement()
    {

    }

    public SimpleViewElement(String elementName)
    {
        attributes.put(HELP_NAME, elementName);
    }

    public LinkedList<ISimpleExpression> getQueryTree()
    {
        return queryTree;
    }

    public void addQueryTree(ISimpleExpression qt)
    {
        queryTree.add(qt);
    }

    public void setQueryTree(LinkedList<ISimpleExpression> queryTree)
    {
        this.queryTree = queryTree;
    }

    public void addAttribute(String key, Object value)
    {
        attributes.put(key, value);
    }

    public String getHelpName()
    {
        return (String)attributes.get(HELP_NAME);
    }

    public Map<String, Object> getAttributes()
    {
        return attributes;
    }

    public String getTableName()
    {
        return (String)attributes.get(TABLE_NAME);
    }

    public String getSearchMode()
    {
        return attributes.get(SEARCH_MODE) == null ? Boolean.TRUE.toString() : (String)attributes.get(SEARCH_MODE);
    }

    public void setViewElementType(String type)
    {
        attributes.put(VIEW_ELEMENT_TYPE, type);
    }

    public String getViewElementType()
    {
        if (attributes.get(VIEW_ELEMENT_TYPE) != null)
        {
            return (String)attributes.get(VIEW_ELEMENT_TYPE);
        }
        return null;
    }

    public Boolean isPerformanceNode()
    {
        if (attributes.get(VIEW_ELEMENT_TYPE) != null)
        {
            if (attributes.get(VIEW_ELEMENT_TYPE).equals(PERFORMANCE_NODE))
            {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Boolean isSearchedResultViewNode()
    {
        if (attributes.get(VIEW_ELEMENT_TYPE) != null)
        {
            if (attributes.get(VIEW_ELEMENT_TYPE).equals(SEARCHED_RESULT_NODE))
            {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Boolean isGeoDrawableEntityNode()
    {
        if (attributes.get(VIEW_ELEMENT_TYPE) != null)
        {
            if (attributes.get(VIEW_ELEMENT_TYPE).equals(GEO_DRAWABLE_ENTITY))
            {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Boolean isPerformanceChildNode()
    {
        if (attributes.get(VIEW_ELEMENT_TYPE) != null)
        {
            if (attributes.get(VIEW_ELEMENT_TYPE).equals(PERFORMANCE_CHILD_NODE))
            {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Boolean isServiceBreachNode()
    {
        if (attributes.get(VIEW_ELEMENT_TYPE) != null)
        {
            if (attributes.get(VIEW_ELEMENT_TYPE).equals(SERVICE_BREACH_NODE))
            {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Boolean isServiceBreachChildNode()
    {
        if (attributes.get(VIEW_ELEMENT_TYPE) != null)
        {
            if (attributes.get(VIEW_ELEMENT_TYPE).equals(SERVICE_BREACH_CHILD_NODE))
            {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public void addQueryTreeTableName(int index, String tableName)
    {
        attributes.put(QUERY_TREE_TABLE_NAME + index, tableName);
    }

    private String getQueryTreeTableName(int index)
    {
        return (String)attributes.get(QUERY_TREE_TABLE_NAME + index);
    }

    public List<Tuple2<String, ISimpleExpression>> getQueryTreePair()
    {
        List<Tuple2<String, ISimpleExpression>> result = new ArrayList<Tuple2<String, ISimpleExpression>>();
        for (int i = 0; i < queryTree.size(); i++)
        {
            result.add(new Tuple2<String, ISimpleExpression>(getQueryTreeTableName(i), queryTree.get(i)));
        }

        return result;
    }
}
