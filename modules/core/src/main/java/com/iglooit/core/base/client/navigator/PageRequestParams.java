package com.iglooit.core.base.client.navigator;

import com.clarity.commons.iface.type.UUID;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.util.ClarityStringUtil;
import com.clarity.core.base.iface.expression.ISimpleExpression;
import com.clarity.core.base.iface.expression.SimpleBinaryComparison;
import com.clarity.core.base.iface.expression.SimpleBinaryComparisonOperators;
import com.clarity.core.base.iface.expression.SimpleLogicalOperation;
import com.clarity.core.base.iface.expression.SimpleLogicalOperationOperators;
import com.clarity.core.base.iface.expression.SimpleUnaryComparison;
import com.clarity.core.base.iface.expression.SimpleUnaryComparisonOperators;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PageRequestParams represents an action to execute within a GWT entry point.
 * It holds the action name and its parameters (as name-value pairs) and it translates
 * from, and to, a string to use in the fragment identifier of a page's URL.
 * <p/>
 * For example, given a URL from an entry point called "vehicles"...<br/>
 * - "http://myhost/vehicles.jsp#ac=DisplayCar;id=12321;views=[interior,side]"<br/>
 * <p/>
 * ...its fragment identifier is...<br/>
 * - "ac=DisplayCar;id=12321;views=[interior,side]"<br/>
 * <p/>
 * ...and its corresponding PageRequestParams would be... <br/>
 * - paramValuesByParamName: [ {"ac", "DisplayCar"}, {"id", "12321"}, {"views", "[interior,side]"} ]<br/>
 * <p/>
 * You can create this PageRequestParams like this: <br/>
 * PageRequestParams pageRequestParams = new PageRequestParams();<br/>
 * pageRequestParams.setAction("DisplayCar");<br/>
 * pageRequestParams.addParam("id", new Long(12321));<br/>
 * pageRequestParams.addParams("views", Arrays.asList("interior", "side"));<br/>
 * <p/>
 * You can read the values from the PageRequestParams like this:<br/>
 * pageRequestParams.getAction();<br/>
 * pageRequestParams.getLongParam("id");<br/>
 * pageRequestParams.getListParam("views");<br/>
 * <p/>
 * It would be nice if we could retain the original type of each parameter, but we don't because we would have to
 * include the type info in the URL which would make the URL less humanly-readable.
 * An example might be "ac=*SDisplayCar;id=*L12321;views=*LS[interior,side]".
 */
public final class PageRequestParams
{
    // Only for a dev/testing experiment to evaluate pros and cons of prefix notation.
    static final boolean PREFIX_NOTATION = false;
    static final String PARAMS_SEPARATOR = ";";
    static final String NAME_VALUE_SEPARATOR = "=";
    static final String VALUE_ELEMENTS_START = "[";
    static final String VALUE_ELEMENTS_SEPARATOR = ",";
    static final String VALUE_VIEW_DEF_ELEMENTS_SEPARATOR = "|";
    static final String VALUE_VIEW_ELE_ELEMENTS_SEPARATOR = "^";
    static final String VALUE_ELEMENTS_END = "]";
    static final String RESERVED_FOR_BREADCRUMBS_SEPARATOR = "$";
    static final String KEYWORDS_PREFIX = "*";
    static final String KEYWORD_NULL = KEYWORDS_PREFIX + "n";
    static final String STRING_FOR_TRUE = "y";
    static final String STRING_FOR_FALSE = "n";
    // We use LinkedHashMap, not just Map, to ensure the sequence of entries is preserved.
    // Regardless of the value's object type, we store it as a String in a format that can go to and from a URL
    // without loss. However, the format does not include type info, so it is up to the caller to keep track of type.
    // The reason we leave out type info is to keep the URL more readable by humans.
    private LinkedHashMap<String, String> paramValuesByParamName = new LinkedHashMap();
    // We use Strategy Pattern to allow unit tests to provide an encoder and inclusive splitter that work server-side.
    private Encoder encoder = new DefaultEncoder();
    private StringHandler stringHandler = new DefaultStringHandler();

    public PageRequestParams()
    {
    }

    /**
     * @param fragmentIdentifier Eg. "ac=DisplayCar;id=12321;views=[interior,side]".<br/>
     *                           Null value is output as "*n", eg. "views=*n".<br/>
     *                           Value that starts with "*" is output with extra "*" prepended, eg. "note=**wow".<br/>
     *                           List values are output in square brackets, eg. "views=[interior,side]".<br/>
     *                           List with one value, empty string, is output like this: "views=[]".<br/>
     *                           List with one value, null, is output like this: "views=[*n]".<br/>
     *                           List with no values is output like this: "views=".<br/>
     *                           String value that has square brackets is fine, eg. "note=[rubbish]".
     */
    public PageRequestParams(String fragmentIdentifier)
    {
        if (StringUtil.isNotBlank(fragmentIdentifier))
        {
            // From the fragment identifier, extract the name/value pairs

            String[] actionChunks = fragmentIdentifier.split(PARAMS_SEPARATOR, -1);

            for (int p = 0; p < actionChunks.length; p++)
            {
                String[] nameValuePair = actionChunks[p].split(NAME_VALUE_SEPARATOR, 2);

                if (nameValuePair.length == 1 && StringUtil.isEmpty(nameValuePair[0]))
                {
                    // Empty parameter - ignore it.
                }
                else if (nameValuePair.length == 2)
                {
                    if (StringUtil.isNotEmpty(nameValuePair[0]))
                    {
                        put(nameValuePair[0], nameValuePair[1]);
                    }
                    else
                    {
                        GWT.log(">>> ERROR: Bad argument: Expected name, found empty name in fragmentIdentifier \"" +
                            fragmentIdentifier + "\".");
                    }
                }
                else
                {
                    GWT.log(">>> ERROR: Bad argument: Expected nameValuePair.length = 2, found " +
                        nameValuePair.length + ". Name is \"" + nameValuePair[0] + "\" in fragmentIdentifier \"" +
                        fragmentIdentifier + "\".");
                }
            }
        }
    }

    public String toString()
    {
        return toFragmentIdentifier();
    }

    /**
     * @return eg. "DisplayCar".
     */
    public String getAction()
    {
        return get(encoder.encode(PageRequestParamNames.ACTION));
    }

    /**
     * @param actionName eg. "DisplayCar".
     */
    public void setAction(String actionName)
    {
        put(encoder.encode(PageRequestParamNames.ACTION), actionName);
    }

    public void addParam(String name, Object value)
    {
        if (name == null || name.trim().isEmpty())
        {
            GWT.log("ERROR", new IllegalArgumentException("Illegal 'name' argument. Expected non-empty string, " +
                "found \"" + name + "\"."));
            return;
        }
        else if (name.equals(PageRequestParamNames.ACTION))
        {
            GWT.log("ERROR", new IllegalArgumentException("Illegal 'name' argument used. \"" +
                PageRequestParamNames.ACTION + "\" is reserved. To add an action, use setAction(String)."));
            return;
        }

        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            GWT.log("ERROR", new IllegalArgumentException("Asked to add duplicate parameter to PageRequestParams. " +
                "Name = \"" + name + "\", value = \"" + value + "\". " +
                "Existing value = \"" + get(encodedName) + "\"."));
            return;
        }

        // Regardless of the value's object type, we will store it in a format that can go to and from a URL
        // without loss.

        if (value == null)
        {
            put(encodedName, encoder.encode(KEYWORD_NULL));
        }
        else if (value instanceof String)
        {
            String v = (String)value;
            if (v.startsWith(KEYWORDS_PREFIX))
            {
                put(encodedName, encoder.encode(KEYWORDS_PREFIX + v));
            }
            else
            {
                put(encodedName, encoder.encode(v));
            }
        }
        else if (value instanceof Boolean)
        {
            if (value.equals(Boolean.TRUE))
            {
                put(encodedName, STRING_FOR_TRUE);
            }
            else
            {
                put(encodedName, STRING_FOR_FALSE);
            }
        }
        else if (value instanceof Integer || value instanceof Long)
        {
            put(encodedName, value.toString());
        }
        else if (value instanceof Double)
        {
            put(encodedName, encoder.encode(value.toString()));
        }
//        else if (value instanceof JSONObject)
//        {
//            JSONObject v = (JSONObject)value;
//            put(encodedName, encoder.encode(v.toString()));
//        }
        else if (value instanceof List)
        {
            put(encodedName, convert(name, (List)value));
        }
        else if (value instanceof ISimpleExpression)
        {
            put(encodedName, convert((ISimpleExpression)value));
        }
        else if (value instanceof SimpleViewDef)
        {
            addSimpleViewDefParam(encodedName, (SimpleViewDef)value);
        }
        else if (value instanceof SimpleViewElement)
        {
            addSimpleViewElement(encodedName, (SimpleViewElement)value);
        }
        else if (value instanceof UUID)
        {
            put(encodedName, value.toString());
        }
        else
        {
            GWT.log(">>> ERROR: Asked to add parameter with name = \"" + name + "\". Expected value to be a " +
                "String, Integer, Long, Boolean, or List<String>. Found " + value.getClass().getName() + ".");
            return;
        }
    }

    /**
     * For use by infrastructure classes only that need to add params, exactly as they find them, to the URL. They will
     * read from the URL.
     *
     * @param name
     * @param value
     */
    void addParamAsIs(String name, String value)
    {
        put(name, value);
    }

    private String convert(String name, List value)
    {
        StringBuilder sb = new StringBuilder();

        if (value.size() != 0)
        {
            sb.append(VALUE_ELEMENTS_START);
            boolean firstElement = true;

            for (Object element : value)
            {
                if (!firstElement)
                {
                    sb.append(VALUE_ELEMENTS_SEPARATOR);
                }

                if (element == null)
                {
                    sb.append(encoder.encode(KEYWORD_NULL));
                }
                else
                {
                    // TODO - Assert that all elements of the list are of the same type.

                    if (element instanceof String)
                    {
                        String elementStr = (String)element;
                        if (elementStr.startsWith(KEYWORDS_PREFIX))
                        {
                            sb.append(encoder.encode(KEYWORDS_PREFIX + elementStr));
                        }
                        else
                        {
                            sb.append(encoder.encode(elementStr));
                        }
                    }
                    else if (element instanceof Boolean)
                    {
                        if (element.equals(Boolean.TRUE))
                        {
                            sb.append(STRING_FOR_TRUE);
                        }
                        else
                        {
                            sb.append(STRING_FOR_FALSE);
                        }
                    }
                    else if (element instanceof Integer || element instanceof Long)
                    {
                        sb.append(element.toString());
                    }
                    else if (element instanceof Double)
                    {
                        sb.append(encoder.encode(element.toString()));
                    }
                    else if (element instanceof UUID)
                    {
                        sb.append(encoder.encode(element.toString()));
                    }
                    else
                    {
                        GWT.log(">>> ERROR: Asked to add parameter with name = \"" + name + "\". " +
                            "Expected value to be a list of String, Integer, Long, or Boolean. " +
                            "Found " + element.getClass().getName() + ".");
                        return null;
                    }
                }

                firstElement = false;
            }

            sb.append(VALUE_ELEMENTS_END);
        }

        return sb.toString();
    }

    private void addSimpleViewDefParam(String name, SimpleViewDef value)
    {
        put(name, convert(value));
    }

    private void addSimpleViewElement(String name, SimpleViewElement value)
    {
        put(name, convert(value));
    }

    private String convert(SimpleViewDef simpleViewDef)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(VALUE_ELEMENTS_START);
        boolean first = true;
        // add attributes
        for (Map.Entry<String, Object> entry : simpleViewDef.getAttributes().entrySet())
        {
            if (!first)
            {
                sb.append(VALUE_VIEW_DEF_ELEMENTS_SEPARATOR);
            }
            sb.append(entry.getKey())
                .append(NAME_VALUE_SEPARATOR)
                .append(entry.getValue());
            first = false;
        }
        // add simpleViewElement
        first = true;
        if (simpleViewDef.getViewElementList().size() > 0)
        {
            sb.append(VALUE_VIEW_DEF_ELEMENTS_SEPARATOR);
        }
        for (SimpleViewElement element : simpleViewDef.getViewElementList())
        {
            if (!first)
            {
                sb.append(VALUE_VIEW_DEF_ELEMENTS_SEPARATOR);
            }
            sb.append(convert(element));
            first = false;
        }
        sb.append(VALUE_ELEMENTS_END);
        return sb.toString();
    }

    private String convert(SimpleViewElement simpleViewElement)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(VALUE_ELEMENTS_START);
        boolean first = true;
        // add attributes
        for (Map.Entry<String, Object> entry : simpleViewElement.getAttributes().entrySet())
        {
            if (!first)
            {
                sb.append(VALUE_VIEW_ELE_ELEMENTS_SEPARATOR);
            }
            sb.append(entry.getKey())
                .append(NAME_VALUE_SEPARATOR)
                .append(entry.getValue());
            first = false;
        }
        // add query tree
        if (simpleViewElement.getQueryTree() != null)
        {
            for (ISimpleExpression se : simpleViewElement.getQueryTree())
            {
                sb.append(VALUE_VIEW_ELE_ELEMENTS_SEPARATOR);
                sb.append(convert(se));
            }
        }
        sb.append(VALUE_ELEMENTS_END);
        return sb.toString();
    }

    private String convert(ISimpleExpression expression)
    {
        if (expression instanceof SimpleLogicalOperation)
        {
            return convertLogicalOperation((SimpleLogicalOperation)expression);
        }
        else if (expression instanceof SimpleBinaryComparison)
        {
            return convertBinaryComparison((SimpleBinaryComparison)expression);
        }
        else if (expression instanceof SimpleUnaryComparison)
        {
            return convertUnaryComparison((SimpleUnaryComparison)expression);
        }
        else
        {
            throw new IllegalArgumentException(expression.toString());
        }
    }

    private String convertLogicalOperation(SimpleLogicalOperation operation)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(VALUE_ELEMENTS_START);
        if (PREFIX_NOTATION)
        {
            sb.append(convert(operation.getOperator()));
            for (ISimpleExpression operand : operation.getOperands())
            {
                sb.append(VALUE_ELEMENTS_SEPARATOR);
                sb.append(convert(operand));
            }
        }
        else
        {
            int operandCount = 0;
            for (ISimpleExpression operand : operation.getOperands())
            {
                operandCount++;
                if (operandCount != 1)
                {
                    sb.append(VALUE_ELEMENTS_SEPARATOR);
                }
                sb.append(convert(operand));
                if (operandCount == 1)
                {
                    sb.append(VALUE_ELEMENTS_SEPARATOR);
                    sb.append(convert(operation.getOperator()));
                }
            }
        }
        sb.append(VALUE_ELEMENTS_END);

        return sb.toString();
    }

    private String convertBinaryComparison(SimpleBinaryComparison comparison)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(VALUE_ELEMENTS_START);
        if (PREFIX_NOTATION)
        {
            sb.append(convert(comparison.getOperator()));
            sb.append(VALUE_ELEMENTS_SEPARATOR);
            sb.append(encoder.encode(comparison.getFieldName()));
            sb.append(VALUE_ELEMENTS_SEPARATOR);
            sb.append(encoder.encode(comparison.getValue()));
        }
        else
        {
            sb.append(encoder.encode(comparison.getFieldName()));
            sb.append(VALUE_ELEMENTS_SEPARATOR);
            sb.append(convert(comparison.getOperator()));
            sb.append(VALUE_ELEMENTS_SEPARATOR);
            sb.append(encoder.encode(comparison.getValue()));
        }
        sb.append(VALUE_ELEMENTS_END);

        return sb.toString();
    }

    private String convertUnaryComparison(SimpleUnaryComparison comparison)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(VALUE_ELEMENTS_START);
        if (PREFIX_NOTATION)
        {
            sb.append(convert(comparison.getOperator()));
            sb.append(VALUE_ELEMENTS_SEPARATOR);
            sb.append(encoder.encode(comparison.getFieldName()));
        }
        else
        {
            sb.append(encoder.encode(comparison.getFieldName()));
            sb.append(VALUE_ELEMENTS_SEPARATOR);
            sb.append(convert(comparison.getOperator()));
        }
        sb.append(VALUE_ELEMENTS_END);

        return sb.toString();
    }

    public void addParams(PageRequestParams pageRequestParams)
    {
        Map<String, String> valuesByName = pageRequestParams.getParamValuesByParamName();
        paramValuesByParamName.putAll(valuesByName);
    }

    public void removeParam(String name)
    {
        String encodedParamName = encoder.encode(name);
        remove(encodedParamName);
    }

    public boolean containsParam(String name)
    {
        String encodedParamName = encoder.encode(name);
        return contains(encodedParamName);
    }

    public String getStringParam(String name, String defaultValue)
    {
        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            String value = get(encodedName);
            String s = encoder.decode(value);

            if (s.equals(KEYWORD_NULL))
            {
                return null;
            }
            else if (s.startsWith(KEYWORDS_PREFIX + KEYWORDS_PREFIX))
            {
                return s.substring(1);
            }
            else
            {
                return s;
            }
        }
        else
        {
            return defaultValue;
        }
    }

    public Boolean getBooleanParam(String name, Boolean defaultValue)
    {
        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            String value = get(encodedName);
            String s = encoder.decode(value);

            if (s.equals(KEYWORD_NULL))
            {
                return null;
            }
            else if (s.equals(STRING_FOR_TRUE))
            {
                return Boolean.TRUE;
            }
            else if (s.equals(STRING_FOR_FALSE))
            {
                return Boolean.FALSE;
            }
            else
            {
                return defaultValue;
            }
        }
        else
        {
            return defaultValue;
        }
    }

    public Double getDoubleParam(String name, Double defaultValue)
    {
        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            String value = get(encodedName);
            String s = encoder.decode(value);

            if (s.equals(KEYWORD_NULL))
            {
                return null;
            }
            else
            {
                try
                {
                    Double d = Double.valueOf(encoder.decode(value));
                    return d;
                }
                catch (NumberFormatException e)
                {
                    // Probably due to user fiddling with the URL. Handle gracefully.
                    GWT.log("PageRequestParams.getDoubleParam(\"" + encodedName + "\")", e);
                    return defaultValue;
                }
            }
        }
        else
        {
            return defaultValue;
        }
    }

    public Integer getIntegerParam(String name, Integer defaultValue)
    {
        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            String value = get(encodedName);
            String s = encoder.decode(value);

            if (s.equals(KEYWORD_NULL))
            {
                return null;
            }
            else
            {
                try
                {
                    Integer i = Integer.valueOf(value);
                    return i;
                }
                catch (NumberFormatException e)
                {
                    // Probably due to user fiddling with the URL. Handle gracefully.
                    GWT.log("PageRequestParams.getIntegerParam(\"" + name + "\", " + defaultValue + ")", e);
                    return defaultValue;
                }
            }
        }
        else
        {
            return defaultValue;
        }
    }

    /**
     * @param name
     * @param defaultValue
     * @return
     */
    public int getIntParam(String name, int defaultValue)
    {
        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            String value = get(encodedName);

            try
            {
                int i = Integer.parseInt(value);
                return i;
            }
            catch (NumberFormatException e)
            {
                // Probably due to user fiddling with the URL. Handle gracefully.
                GWT.log("PageRequestParams.getIntParam(\"" + name + "\", " + defaultValue + ")", e);
                return defaultValue;
            }
        }
        else
        {
            return defaultValue;
        }
    }

    /**
     * @param name
     * @param defaultValue
     * @return
     * @throws NumberFormatException
     */
    public int getIntParamStrict(String name, int defaultValue) throws NumberFormatException
    {
        if (contains(name))
        {
            // DO NOT catch NumberFormatException, because this method is "Strict".
            String value = get(name);
            int i = Integer.parseInt(value);
            return i;
        }
        else
        {
            return defaultValue;
        }
    }

//    public JSONObject getJSONObjectParam(String name, JSONObject defaultValue)
//    {
//        String encodedName = encoder.encode(name);
//
//        if (contains(encodedName))
//        {
//            String value = get(encodedName);
//            String s = encoder.decode(value);
//
//            if (s.equals(KEYWORD_NULL))
//            {
//                return null;
//            }
//            else if (s.equals(STRING_FOR_TRUE))
//            {
//                return Boolean.TRUE;
//            }
//            else if (s.equals(STRING_FOR_FALSE))
//            {
//                return Boolean.FALSE;
//            }
//            else
//            {
//                return new JSONObject();
//            }
//        }
//        else
//        {
//            return defaultValue;
//        }
//    }

    public Long getLongParam(String name, Long defaultValue)
    {
        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            String value = get(encodedName);
            String s = encoder.decode(value);

            if (s.equals(KEYWORD_NULL))
            {
                return null;
            }
            else
            {
                try
                {
                    Long l = Long.valueOf(value);
                    return l;
                }
                catch (NumberFormatException e)
                {
                    // Probably due to user fiddling with the URL. Handle gracefully.
                    GWT.log("PageRequestParams.getLongParam(\"" + encodedName + "\")", e);
                    return defaultValue;
                }
            }
        }
        else
        {
            return defaultValue;
        }
    }

    public UUID getUUIDParam(String name, UUID defaultValue)
    {
        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            String value = get(encodedName);
            String s = encoder.decode(value);

            if (s.equals(KEYWORD_NULL))
            {
                return null;
            }
            else
            {
                try
                {
                    UUID uuid = UUID.fromString(value);
                    return uuid;
                }
                catch (Exception e)
                {
                    // Probably due to user fiddling with the URL. Handle gracefully.
                    GWT.log("PageRequestParams.getUUIDParam(\"" + encodedName + "\")", e);
                    return defaultValue;
                }
            }
        }
        else
        {
            return defaultValue;
        }

    }

    public List<UUID> getListUUIDParam(String name, List<UUID> defaultValue)
    {
        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            String value = get(encodedName);
            String s = encoder.decode(value);

            if (s.equals(KEYWORD_NULL))
            {
                return null;
            }
            else if (s.trim().length() == 0)
            {
                return new ArrayList();
            }
            else
            {
                if (value.startsWith(VALUE_ELEMENTS_START) && value.endsWith(VALUE_ELEMENTS_END))
                {
                    List<UUID> uuidList = new ArrayList();

                    String elementsStr =
                        value.substring(VALUE_ELEMENTS_START.length(), value.length() - VALUE_ELEMENTS_END.length());
                    String[] elements = elementsStr.split(VALUE_ELEMENTS_SEPARATOR, -1);

                    for (String element : elements)
                    {
                        String s0 = encoder.decode(element);
                        if (s0.equals(KEYWORD_NULL))
                        {
                            uuidList.add(null);
                        }
                        else
                        {
                            try
                            {
                                UUID v = UUID.fromString(element);
                                uuidList.add(v);
                            }
                            catch (NumberFormatException e)
                            {
                                // Probably due to user fiddling with the URL. Handle gracefully.
                                GWT.log("PageRequestParams.getListUUIDParam(\"" + encodedName + "\")", e);
                                uuidList.add(null);
                            }
                        }
                    }

                    return uuidList;
                }
                else
                {
                    return defaultValue;
                }
            }
        }
        else
        {
            return defaultValue;
        }
    }

    public List<String> getListParam(String name, List<String> defaultValue)
    {
        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            String value = get(encodedName);
            String s = encoder.decode(value);

            if (s.equals(KEYWORD_NULL))
            {
                return null;
            }
            else if (s.trim().length() == 0)
            {
                return new ArrayList();
            }
            else
            {
                if (value.startsWith(VALUE_ELEMENTS_START) && value.endsWith(VALUE_ELEMENTS_END))
                {
                    List<String> l = new ArrayList();

                    String elementsStr =
                        value.substring(VALUE_ELEMENTS_START.length(), value.length() - VALUE_ELEMENTS_END.length());
                    String[] elements = elementsStr.split(VALUE_ELEMENTS_SEPARATOR, -1);

                    for (String element : elements)
                    {
                        String s0 = encoder.decode(element);
                        if (s0.equals(KEYWORD_NULL))
                        {
                            l.add((String)null);
                        }
                        else if (s0.startsWith(KEYWORDS_PREFIX + KEYWORDS_PREFIX))
                        {
                            l.add(s0.substring(1));
                        }
                        else
                        {
                            l.add(s0);
                        }
                    }

                    return l;
                }
                else
                {
                    return defaultValue;
                }
            }
        }
        else
        {
            return defaultValue;
        }
    }

    public ISimpleExpression getSimpleExpressionParam(String name, ISimpleExpression defaultValue)
    {
        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            String s = encoder.decode(get(encodedName));

            if (s.equals(KEYWORD_NULL))
            {
                return null;
            }
            else if (s.trim().length() == 0)
            {
                // There is no such thing as an empty ISimpleExpression, so return null.
                return null;
            }
            else
            {
                if (s.startsWith(VALUE_ELEMENTS_START) && s.endsWith(VALUE_ELEMENTS_END))
                {
                    try
                    {
                        return parseExpression(s);
                    }
                    catch (ParseException e)
                    {
                        return defaultValue;
                    }
                }
                else
                {
                    return defaultValue;
                }
            }
        }
        else
        {
            return defaultValue;
        }
    }

    ISimpleExpression parseExpression(String s) throws ParseException
    {
        // Because GWT doesn't support StringTokenizer, we'll provide our own so we can emulate this:
        // StringTokenizer st = new StringTokenizer(s, "[,]", true);

        MyStringTokenizer tokenizer = new MyStringTokenizer(s, "[\\[\\,\\]]", true);
        return parseExpression(tokenizer);
    }

    // This is a simple recursive descent parser.
    private ISimpleExpression parseExpression(MyStringTokenizer tokenizer) throws ParseException
    {
        ISimpleExpression expression = null;
        List<Object> elements = new ArrayList<Object>();
        int elementNum = 0;

        String tok;

        while ((tok = getNextToken(tokenizer)) != null)
        {
            if (tok.equals("["))
            {
                expression = parseExpression(tokenizer);
                elements.add(expression);
            }
            else if (tok.equals("]"))
            {
                if (elements.size() < elementNum)
                {
                    elements.add("");
                }
                return toExpression(elements);
            }
            else if (tok.equals(","))
            {
                if (elements.size() < elementNum)
                {
                    elements.add("");
                }
                elementNum++;
            }
            else
            {
                elements.add(tok);
            }
        }

        return expression;
    }

    private SimpleViewElement parseSimpleViewElement(String content) throws ParseException
    {
        SimpleViewElement simpleViewElement = new SimpleViewElement();
        if (content == null)
        {
            return null;
        }
        String elementString = content.substring(VALUE_ELEMENTS_START.length(),
            content.length() - VALUE_ELEMENTS_END.length());
        String[] elements = elementString.split("\\" + VALUE_VIEW_ELE_ELEMENTS_SEPARATOR, -1);

        for (String element : elements)
        {
            if (element.startsWith(VALUE_ELEMENTS_START) && element.endsWith(VALUE_ELEMENTS_END))
            {
                // query tree
                simpleViewElement.addQueryTree(parseExpression(element));
            }
            else
            {
                // attributes
                String[] pair = element.split(NAME_VALUE_SEPARATOR);
                if (pair != null)
                {
                    if (pair.length == 2)
                    {
                        // transfer a value to a specific type
                        Object value = convertValue(pair[1]);
                        simpleViewElement.addAttribute(pair[0], value);

                    }
                }
            }
        }
        return simpleViewElement;
    }

    private Object convertValue(String valueStr)
    {
        try
        {
            return Long.valueOf(valueStr);

        }
        catch (NumberFormatException e)
        {
            try
            {
                return Double.valueOf(valueStr);
            }
            catch (NumberFormatException e1)
            {
                return valueStr;
            }
        }
    }

    public SimpleViewDef getSimpleViewDefParam(String name, SimpleViewDef defaultValue)
    {
        String encodedName = encoder.encode(name);
        if (contains(encodedName))
        {
            String s = encoder.decode(get(encodedName));

            if (s.equals(KEYWORD_NULL))
            {
                return null;
            }
            else if (s.trim().length() == 0)
            {
                return new SimpleViewDef(name);
            }
            else
            {
                if (s.startsWith(VALUE_ELEMENTS_START) && s.endsWith(VALUE_ELEMENTS_END))
                {
                    SimpleViewDef svd = new SimpleViewDef();

                    String elementsStr =
                        s.substring(VALUE_ELEMENTS_START.length(), s.length() - VALUE_ELEMENTS_END.length());
                    String[] elements = stringHandler.split(elementsStr, "\\" + VALUE_VIEW_DEF_ELEMENTS_SEPARATOR);
                    for (String element : elements)
                    {
                        if (element.equals("|"))
                            continue;
                        if (element.startsWith(VALUE_ELEMENTS_START) && element.endsWith(VALUE_ELEMENTS_END))
                        {
                            SimpleViewElement sve = null;
                            try
                            {
                                // parse simple view element
                                sve = parseSimpleViewElement(element);
                            }
                            catch (ParseException e)
                            {
                                return defaultValue;
                            }
                            svd.addSimpleViewElement(sve);
                        }
                        else
                        {
                            // parse attributes
                            String[] pair = element.split(NAME_VALUE_SEPARATOR);
                            if (pair != null)
                            {
                                if (pair.length == 2)
                                {
                                    svd.addAttribute(pair[0], pair[1]);
                                }
                            }
                        }
                    }
                    return svd;
                }
                else
                {
                    return defaultValue;
                }
            }
        }
        else
        {
            return defaultValue;
        }
    }

    private String getNextToken(MyStringTokenizer st)
    {
        while (st.hasMoreTokens())
        {
            String tok = st.nextToken();
            if (tok.length() > 0)
            {
                return tok;
            }
        }
        return null;
    }

    private ISimpleExpression toExpression(List<Object> elements) throws ParseException
    {
        if (elements.size() > 1)
        {
            if (elements.get(1) instanceof String)
            {
                String operatorStr = (String)elements.get(1);

                SimpleUnaryComparisonOperators unaryOperator = parseUnaryOperator(operatorStr);

                if (unaryOperator != null)
                {
                    return parseUnary(unaryOperator, elements);
                }
                else
                {
                    SimpleBinaryComparisonOperators binaryOperator = parseBinaryOperator(operatorStr);

                    if (binaryOperator != null)
                    {
                        return parseBinary(binaryOperator, elements);
                    }
                    else
                    {
                        SimpleLogicalOperationOperators logicalOperator = parseLogicalOperator(operatorStr);

                        if (logicalOperator != null)
                        {
                            return parseLogical(logicalOperator, elements);
                        }
                        else
                        {
                            throw new ParseException("Expected a known operator, found " + operatorStr + ".");
                        }
                    }
                }
            }
            else
            {
                // Corrupted expression
                throw new ParseException("Expected operator to be a simple String, found " +
                    elements.get(1).getClass().getName());
            }
        }
        else
        {
            throw new ParseException("Expected expression containing at least 2 comma-separated elements, " +
                "found \"" + elements.get(0) + "\".");
        }
    }

    private SimpleUnaryComparison parseUnary(SimpleUnaryComparisonOperators unaryOperator,
                                             List<Object> elements) throws ParseException
    {
        // Expect element[1] is the operator. The operands is element[0].

        if (elements.size() != 2)
        {
            throw new ParseException("Expected 2 elements, found " + Integer.toString(elements.size()));
        }

        if (!(elements.get(0) instanceof String))
        {
            throw new ParseException("Expected String, found " + elements.get(0).getClass().getName());
        }

        String fieldName = encoder.decode((String)elements.get(0));
        return new SimpleUnaryComparison(fieldName, unaryOperator);
    }

    private SimpleBinaryComparison parseBinary(SimpleBinaryComparisonOperators binaryOperator,
                                               List<Object> elements) throws ParseException
    {
        // Expect element[1] is the operator. The operands are element[0] and element[2].

        if (elements.size() != 3)
        {
            throw new ParseException("Expected 3 elements, found " + Integer.toString(elements.size()));
        }

        if (!(elements.get(0) instanceof String))
        {
            throw new ParseException("Expected String, found " + elements.get(0).getClass().getName());
        }

        if (!(elements.get(2) instanceof String))
        {
            throw new ParseException("Expected String, found " + elements.get(2).getClass().getName());
        }

        String fieldName = encoder.decode((String)elements.get(0));
        // there is a bug about character '%', sometime there is one and it can't be parsed correctly
        String value = (String)elements.get(2);
        if (!value.equals("%"))
        {
            value = encoder.decode(value);
        }
        return new SimpleBinaryComparison(fieldName, binaryOperator, value);
    }

    private SimpleLogicalOperation parseLogical(SimpleLogicalOperationOperators logicalOperator,
                                                List<Object> elements) throws ParseException
    {
        // Expect element[1] is the operator. The operands are element[0] and element[2..n].

        if (elements.size() < 2)
        {
            // Corrupted expression - a logical operation must have an operator and at least 2 operands.
            throw new ParseException("Expected 2 elements, found " + Integer.toString(elements.size()));
        }

        List<ISimpleExpression> operands = new ArrayList<ISimpleExpression>();
        operands.add((ISimpleExpression)elements.get(0));
        for (int i = 2; i < elements.size(); i++)
        {
            operands.add((ISimpleExpression)elements.get(i));
        }
        return new SimpleLogicalOperation(logicalOperator, operands);
    }

    public List<Long> getListLongParam(String name, List<Long> defaultValue)
    {
        String encodedName = encoder.encode(name);

        if (contains(encodedName))
        {
            String value = get(encodedName);
            String s = encoder.decode(value);

            if (s.equals(KEYWORD_NULL))
            {
                return null;
            }
            else if (s.trim().length() == 0)
            {
                return new ArrayList();
            }
            else
            {
                if (value.startsWith(VALUE_ELEMENTS_START) && value.endsWith(VALUE_ELEMENTS_END))
                {
                    List<Long> l = new ArrayList();

                    String elementsStr =
                        value.substring(VALUE_ELEMENTS_START.length(), value.length() - VALUE_ELEMENTS_END.length());
                    String[] elements = elementsStr.split(VALUE_ELEMENTS_SEPARATOR, -1);

                    for (String element : elements)
                    {
                        String s0 = encoder.decode(element);
                        if (s0.equals(KEYWORD_NULL))
                        {
                            l.add(null);
                        }
                        else
                        {
                            try
                            {
                                Long v = Long.valueOf(element);
                                l.add(v);
                            }
                            catch (NumberFormatException e)
                            {
                                // Probably due to user fiddling with the URL. Handle gracefully.
                                GWT.log("PageRequestParams.getListLongParam(\"" + encodedName + "\")", e);
                                l.add(null);
                            }
                        }
                    }

                    return l;
                }
                else
                {
                    return defaultValue;
                }
            }
        }
        else
        {
            return defaultValue;
        }
    }

    public boolean isEmpty()
    {
        return paramValuesByParamName.isEmpty();
    }

    /**
     * @return The action token as a URL fragment identifier, eg. "ac=DisplayCar;id=12321;tab=specs".
     */
    public String toFragmentIdentifier()
    {
        StringBuilder fragmentIdentifier = new StringBuilder("");
        boolean firstParam = true;

        for (Map.Entry<String, String> actionParam : paramValuesByParamName.entrySet())
        {
            if (actionParam.getKey() == null || actionParam.getKey().trim().isEmpty())
            {
                GWT.log(">>> ERROR: Illegal argument paramValuesByParamName. " +
                    "Expected all keys to be non-empty string, found \"" + paramValuesByParamName + "\".");
            }
            else
            {
                if (!firstParam)
                {
                    fragmentIdentifier.append(PARAMS_SEPARATOR);
                }
                fragmentIdentifier.append(actionParam.getKey());
                fragmentIdentifier.append(NAME_VALUE_SEPARATOR);
                fragmentIdentifier.append(actionParam.getValue());
                firstParam = false;
            }
        }

        return fragmentIdentifier.toString();
    }

    private String convert(SimpleBinaryComparisonOperators operator)
    {
        if (operator == SimpleBinaryComparisonOperators.EQ)
        {
            return "EQ";
        }
        else if (operator == SimpleBinaryComparisonOperators.NE)
        {
            return "NE";
        }
        else if (operator == SimpleBinaryComparisonOperators.GE)
        {
            return "GE";
        }
        else if (operator == SimpleBinaryComparisonOperators.LE)
        {
            return "LE";
        }
        else if (operator == SimpleBinaryComparisonOperators.LIKE)
        {
            return "L";
        }
        else if (operator == SimpleBinaryComparisonOperators.NOT_LIKE)
        {
            return "NL";
        }
        else
        {
            throw new IllegalArgumentException(operator.name());
        }
    }

    private SimpleBinaryComparisonOperators parseBinaryOperator(String s)
    {
        if (s.equals("EQ"))
        {
            return SimpleBinaryComparisonOperators.EQ;
        }
        else if (s.equals("NE"))
        {
            return SimpleBinaryComparisonOperators.NE;
        }
        else if (s.equals("GE"))
        {
            return SimpleBinaryComparisonOperators.GE;
        }
        else if (s.equals("LE"))
        {
            return SimpleBinaryComparisonOperators.LE;
        }
        else if (s.equals("L"))
        {
            return SimpleBinaryComparisonOperators.LIKE;
        }
        else if (s.equals("NL"))
        {
            return SimpleBinaryComparisonOperators.NOT_LIKE;
        }
        else
        {
            return null;
        }
    }

    private String convert(SimpleUnaryComparisonOperators operator)
    {
        if (operator == SimpleUnaryComparisonOperators.IS_NULL)
        {
            return "N";
        }
        else if (operator == SimpleUnaryComparisonOperators.IS_NOT_NULL)
        {
            return "NN";
        }
        else
        {
            throw new IllegalArgumentException(operator.name());
        }
    }

    private SimpleUnaryComparisonOperators parseUnaryOperator(String s)
    {
        if (s.equals("N"))
        {
            return SimpleUnaryComparisonOperators.IS_NULL;
        }
        else if (s.equals("NN"))
        {
            return SimpleUnaryComparisonOperators.IS_NOT_NULL;
        }
        else
        {
            return null;
        }
    }

    private String convert(SimpleLogicalOperationOperators operator)
    {
        if (operator == SimpleLogicalOperationOperators.AND)
        {
            return "AND";
        }
        else if (operator == SimpleLogicalOperationOperators.OR)
        {
            return "OR";
        }
        else
        {
            throw new IllegalArgumentException(operator.name());
        }
    }

    private SimpleLogicalOperationOperators parseLogicalOperator(String s)
    {
        if (s.equals("AND"))
        {
            return SimpleLogicalOperationOperators.AND;
        }
        else if (s.equals("OR"))
        {
            return SimpleLogicalOperationOperators.OR;
        }
        else
        {
            return null;
        }
    }

    /**
     * @return Eg. [ {"ac", "DisplayCar"}, {"id", "12321"}, {"views", {"interior", "side"} ].
     */
    Map<String, String> getParamValuesByParamName()
    {
        // TODO Should probably return something immutable instead?
        return paramValuesByParamName;
    }

    /**
     * @return Eg. {"ac", "id", "views"}.
     */
    public Set<String> getParamNames()
    {
        Set<String> keySet = new HashSet<String>();
        for (String key : paramValuesByParamName.keySet())
            keySet.add(encoder.decode(key));
        return keySet;
    }

    public boolean isPrefixNotation()
    {
        return PREFIX_NOTATION;
    }

    private void put(String encodedParamName, String encodedParamValue)
    {
        paramValuesByParamName.put(encodedParamName, encodedParamValue);
    }

    private boolean contains(String encodedParamName)
    {
        return paramValuesByParamName.containsKey(encodedParamName);
    }

    private String get(String encodedParamName)
    {
        return paramValuesByParamName.get(encodedParamName);
    }

    private void remove(String encodedParamName)
    {
        paramValuesByParamName.remove(encodedParamName);
    }

    /**
     * We provide this method because GWT doesn't support
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/lang/String.html#format(java.lang.String,
     * java.lang.Object...)">String.format(java.lang.String, java.lang.Object...)</a>.
     * <p/>
     * Same arguments and behaviour as
     * <a href="http://docs.oracle.com/javase/6/docs/api/java/lang/String.html#format(java.lang.String,
     * java.lang.Object...)">String.format(java.lang.String, java.lang.Object...)</a>
     * EXCEPT we support only one type of format specifier in the format string: "<code>%s</code>".
     */
    private String format(final String format, final String... args)
    {
        String[] split = format.split("%s");
        final StringBuilder msg = new StringBuilder();

        for (int pos = 0; pos < split.length - 1; pos += 1)
        {
            msg.append(split[pos]);
            msg.append(args[pos]);
        }

        msg.append(split[split.length - 1]);
        return msg.toString();
    }

    /**
     * Typically used by unit tests to give me an encoder that will work outside the browser, eg. one based on
     * java.net.URLEncoder; whereas my DefaultEncoder uses com.google.gwt.http.client.URL which works only in a browser.
     *
     * @param encoder
     */
    void setEncoder(Encoder encoder)
    {
        this.encoder = encoder;
    }

    /**
     * Typically used by unit tests to give me a stringHandler that will work outside the browser, eg. one based on
     * java.util.StringTokenizer; whereas my DefaultEncoder uses ClarityStringUtil which works only in a browser.
     *
     * @param stringHandler
     */
    void setStringHandler(StringHandler stringHandler)
    {
        this.stringHandler = stringHandler;
    }

    interface Encoder
    {
        String encode(String s);

        String decode(String s);
    }

    interface StringHandler
    {
        /**
         * Unlike a Java split, returns the delimiters as tokens amongst the other tokens.
         */
        String[] split(String str, String delimiters);

        String[] match(String str, String regex);
    }

    private static class ParseException extends Exception
    {
        public ParseException()
        {
            super();
        }

        public ParseException(String message)
        {
            super(message);
        }
    }

    /**
     * PageRequestParams's default encoder uses GWT's URL encoder which works fine in any browser but not server-side.
     * To run PageRequestParams server-side, eg. in unit tests; you'll have to provide a different encoder.
     */
    private static class DefaultEncoder implements Encoder
    {
        public String encode(String s)
        {
            return URL.encodeQueryString(s);
        }

        public String decode(String s)
        {
            return URL.decodeQueryString(s);
        }
    }

    /**
     * PageRequestParams's default StringHandler uses ClarityStringUtil to split the string, which works fine in a
     * browser but not server-side.
     * To run PageRequestParams server-side, eg. in unit tests; you'll have to provide a different StringHandler.
     */
    private static class DefaultStringHandler implements StringHandler
    {
        public String[] split(String str, String delimiters)
        {
            // Put the delimiters in a group so that JavaScript will return the delimiters in the token (just like
            // Java's StringTokenizer can, but StringTokenizer isn't supported in GWT).
            String regex = "(" + delimiters + ")";

            String[] tokens = ClarityStringUtil.split(str, regex);

            // Compensate for a flaw in JavaScript's String split: if first char matches delimiter then it
            // returns empty string in first token - we don't want that first token.

            if (tokens.length > 0 && tokens[0].equals("") && match(str.substring(0, 1), regex).length > 0)
            {
                String[] tokens1 = new String[tokens.length - 1];
                System.arraycopy(tokens, 1, tokens1, 0, tokens1.length);
                tokens = tokens1;
            }

            // Compensate for a flaw in JavaScript's String split: if last char matches delimiter then it
            // returns empty string in last token - we don't want that last token.

            if (tokens.length > 0)
            {
                if (tokens[tokens.length - 1].equals("") && match(str.substring(str.length() - 1), regex).length > 0)
                {
                    String[] tokens1 = new String[tokens.length - 1];
                    System.arraycopy(tokens, 0, tokens1, 0, tokens1.length);
                    tokens = tokens1;
                }
            }

            return tokens;
        }

        public String[] match(String str, String regex)
        {
            return ClarityStringUtil.match(str, regex);
        }
    }

    /**
     * We provide this string tokenizer because GWT doesn't support StringTokenizer. We can't just use String.split()
     * because, unlike StringTokenizer, it cannot include the delimiters in the returned list of tokens.
     * <p/>
     * TODO - Once this is considered robust we could move it out of here and into our utilities library.
     */
    private class MyStringTokenizer
    {
        private String[] tokens;
        private int t = -1;

        /**
         * Same arguments and behaviour as
         * <a href="http://docs.oracle.com/javase/6/docs/api/java/util/StringTokenizer.html#StringTokenizer(
         * java.lang.String, java.lang.String, boolean)">StringTokenizer(java.lang.String, java.lang.String,
         * boolean)</a>.
         */
        public MyStringTokenizer(String str, String delimiters, boolean returnDelimiters)
        {
            if (returnDelimiters)
            {
                tokens = stringHandler.split(str, delimiters);
            }
            else
            {
                tokens = str.split(delimiters);
            }
        }

        public boolean hasMoreTokens()
        {
            return t < tokens.length - 1;
        }

        public String nextToken()
        {
            return tokens[++t];
        }

    }
}
