package com.iglooit.commons.iface.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringUtil
{

    public static final Map<String, String> URL_RESERVED_CHARS;
    public static final Map<String, String> URL_UNSAFE_CHARS;

    static
    {
        URL_RESERVED_CHARS = new HashMap<String, String>();
        URL_RESERVED_CHARS.put("$", "%24");
        URL_RESERVED_CHARS.put("&", "%26");
        URL_RESERVED_CHARS.put("+", "%2B");
        URL_RESERVED_CHARS.put(",", "%2C");
        URL_RESERVED_CHARS.put("/", "%2F");
        URL_RESERVED_CHARS.put(":", "%3A");
        URL_RESERVED_CHARS.put(";", "%3B");
        URL_RESERVED_CHARS.put("=", "%3D");
        URL_RESERVED_CHARS.put("?", "%3F");
        URL_RESERVED_CHARS.put("@", "%40");

        URL_UNSAFE_CHARS = new HashMap<String, String>();
        URL_UNSAFE_CHARS.put(" ", "%20");
        URL_UNSAFE_CHARS.put("\"", "%22");
        URL_UNSAFE_CHARS.put("<", "%3C");
        URL_UNSAFE_CHARS.put(">", "%3E");
        URL_UNSAFE_CHARS.put("#", "%23");
        // comment out the following line because after replace space with %20
        // % will be in the way !
        // what we actually need to do is parse the string
        // then give an order of replacing !
        // URL_UNSAFE_CHARS.put("%", "%25");
        URL_UNSAFE_CHARS.put("{", "%7B");
        URL_UNSAFE_CHARS.put("}", "%7D");
        URL_UNSAFE_CHARS.put("|", "%7C");
        URL_UNSAFE_CHARS.put("\\", "%5C");
        URL_UNSAFE_CHARS.put("^", "%5E");
        URL_UNSAFE_CHARS.put("~", "%7E");
        URL_UNSAFE_CHARS.put("[", "%5B");
        URL_UNSAFE_CHARS.put("]", "%5D");
        URL_UNSAFE_CHARS.put("`", "%60");
    }

    public static String emptyStringIfNull(String str)
    {
        return str == null ? "" : str;
    }

    public static String emptyStringIfNull(Object obj)
    {
        return obj == null ? "" : obj.toString();
    }

    public static boolean isEmpty(String str)
    {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str)
    {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotBlank(String str)
    {
        return !isBlank(str);
    }

    public static String trim(String str)
    {
        return str == null ? null : str.trim();
    }

    public static String join(String separator, List<String> strings)
    {
        String actualSeperator = separator;
        if (strings == null)
            return "";
        if (separator == null)
            actualSeperator = "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); i++)
        {
            sb.append(strings.get(i));
            if (i < strings.size() - 1)
                sb.append(actualSeperator);
        }
        return sb.toString();
    }

    public static String join(String joinBy, String... strings)
    {
        return join(joinBy, strings != null ? Arrays.asList(strings) : null);
    }

    public static List<String> separate(String str, String separator)
    {
        String[] strArr = str.split(separator);
        return new ArrayList<String>(Arrays.asList(strArr));
    }

    /**
     * returns a left padded string.
     * <p/>
     * its important to note that the size of the result is the set in the second parameter. i.e.
     * if you have a str with length 3 and resulting string size of 4, then only one padding character
     * is added to the left.
     */
    public static String leftPad(String str, int resultingStringSize, char padChar)
    {
        if (str == null)
        {
            return "";
        }
        int pads = resultingStringSize - str.length();
        if (pads <= 0)
        {
            return str; // returns original String when possible
        }
        return padding(pads, padChar).concat(str);
    }

    public static String padding(int repeat, char padChar)
    {
        if (repeat < 0)
            return "";

        final char[] buf = new char[repeat];
        for (int i = 0; i < buf.length; i++)
        {
            buf[i] = padChar;
        }
        return new String(buf);
    }

    public static String getNotNullString(String s)
    {
        return s != null ? s : "";
    }

    public static String getNullFromEmptyString(String s)
    {
        return isEmpty(s) ? null : s;
    }

    public static boolean stringsEqualOrBothNull(String s1, String s2)
    {
        return s1 == null ? s2 == null : s1.equals(s2);
    }

    public static boolean stringsEqualIgnoreCaseOrBothNull(String s1, String s2)
    {
        return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
    }

    public static boolean notNullEqual(String s1, String s2)
    {
        return s1 != null && s1.equals(s2);
    }

    public static boolean isNumeric(String str)
    {
        if (str == null) return false;
        final int sz = str.length();
        for (int i = 0; i < sz; i++)
        {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }

    public static String ellipsis(String str, int maxLength)
    {
        if (str != null && str.length() > maxLength)
        {
            return str.substring(0, maxLength - 3) + "...";
        }
        return str;
    }

    /*
    This function simulates the same functionality that NVL provides in PL/SQL.
    the function lets you substitute a value when a null value is encountered.
    If the first parameter is null/empty the second parameter will be used as the
    substitute for the first parameter.
     */

    public static String oracleNvl(String s1, String s2)
    {
        if (isEmpty(s1))
            return s2;
        else
            return s1;
    }

    public static List<String> getDistinctList(List<String> stringList)
    {
        Set<String> distinctSet = new HashSet<String>(stringList);
        return new ArrayList<String>(distinctSet);
    }

    public static List<String> getIntersectList(List<String> listA,
                                                List<String> listB)
    {
        Set<String> setA = new HashSet<String>(listA);
        Set<String> setB = new HashSet<String>(listB);
        List<String> result = new ArrayList<String>(listB.size());

        for (String b : setB)
        {
            if (setA.contains(b))
                result.add(b);
        }
        return result;
    }

    public static List<String> getAOuterB(Collection<String> listA, Collection<String> listB)
    {
        Set<String> setA = new HashSet<String>(listA);
        Set<String> setB = new HashSet<String>(listB);
        List<String> result = new ArrayList<String>();

        for (String a : setA)
        {
            if (!setB.contains(a))
                result.add(a);
        }
        return result;
    }

    public static String getNextAvailable(String base, Collection<String> list)
    {
        for (Integer i = 1; i < 9999; i++)
        {
            if (list.contains(base + i.toString()))
                continue;
            return base + i.toString();
        }
        return null;
    }

    public static int compareIgnoreCase(String s1, String s2)
    {
        if (s1 != null && s2 != null)
        {
            return s1.compareToIgnoreCase(s2);
        }

        // The following puts null string before non-null string

        else if (s1 == null && s2 != null)
        {
            return -1;
        }
        else if (s1 != null && s2 == null)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public static String getStringUSInsteadOfSpace(String input)
    {
        return input.replaceAll(" ", "_");
    }

    public static String getStringSpaceInsteadOfUS(String input)
    {
        return input.replaceAll("_", " ");
    }

    /*This method convert input string to non comel case & then replaces token by whitepace.
    * So for string like Ticket_Details, it will return Ticket details on execution
    * convertToNonCamelStd("Ticket_Details", "_"); */
    public static String convertToNonCamelStd(String input, String token)
    {
        return convertToNonCamelCase(input, token).replaceAll(token, " ");
    }

    public static String convertToNonCamelCase(String input, String token)
    {
        StringBuilder result = new StringBuilder();
        String[] inputArr = input.split(token);
        boolean first = true;
        for (String key : inputArr)
        {
            if (first)
            {
                if (key.length() > 1)
                {
                    result.append(key.substring(0, 1).toUpperCase());
                    result.append(key.substring(1).toLowerCase());
                }
                else
                {
                    result.append(key.toLowerCase());
                }
                first = false;
            }
            else
            {
                result.append(key.toLowerCase());
            }

            result.append(token);
        }
        if (result.indexOf(token) != -1)
            return result.toString().substring(0, result.lastIndexOf(token));
        else
            return result.toString();
    }

    public static String covertToCamelCase(String input, String token)
    {
        StringBuilder result = new StringBuilder();
        String[] inputArr = input.split(token);
        for (String key : inputArr)
        {
            if (key.length() > 1)
            {
                result.append(key.substring(0, 1).toUpperCase());
                result.append(key.substring(1).toLowerCase());
            }
            else
            {
                result.append(key.toUpperCase());
            }
            result.append(token);
        }
        if (result.indexOf(token) != -1)
            return result.toString().substring(0, result.lastIndexOf(token));
        else
            return result.toString();
    }

    public static String getStringBetweenMark(String base, String mark1, String mark2)
    {
        int index1 = base.indexOf(mark1);
        int index2 = base.lastIndexOf(mark2);
        if (index1 == index2)
            return "";
        String subStr = base.substring(Math.min(index1, index2) + 1, Math.max(index1, index2));
        return subStr.trim();
    }

    /*
   * Only accept integer as sequence count
   * */

    public static String getNextIntIncrement(String prevCount)
    {
        try
        {
            Integer prevInt = Integer.valueOf(prevCount);
            Integer nextInt = prevInt + 1;
            return nextInt.toString();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static int indexOf(String base, String target, int occurrence)
    {
        int index = -1;
        int occ = occurrence;
        while (occ > 0)
        {
            index = base.indexOf(target, index + 1);
            if (index == -1)
                return -1;
            occ--;
        }
        return index;
    }

    public static int lastIndexOf(String base, String target, int occurrence)
    {
        int index = base.length() - 1;
        int occ = occurrence;
        while (occ > 0)
        {
            index = base.lastIndexOf(target, index - 1);
            if (index == -1)
                return -1;
            occ--;
        }
        return index;
    }

    public static String getUTF8String(String rawStr)
    {
        try
        {
            int strLen = rawStr.length();
            byte[] bArray = new byte[strLen];
            for (int i = 0; i < strLen; i++)
            {
                char c = rawStr.charAt(i);
                byte b = (byte)c;
                // System.out.println("b = " + b);
                bArray[i] = b;
            }
            return new String(bArray, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return "Error Encoding UTF-8.";
        }
    }

    public static void deleteCommaAtTheEnd(StringBuilder builder)
    {
        if (builder.lastIndexOf(",") == builder.length() - 1)
        {
            builder.deleteCharAt(builder.length() - 1);
        }
    }

    public static String escapeUrlString(String rawString)
    {

        String ret = rawString;

        for (Map.Entry<String, String> entry : URL_UNSAFE_CHARS.entrySet())
        {
            ret = ret.replace(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : URL_RESERVED_CHARS.entrySet())
        {
            ret = ret.replace(entry.getKey(), entry.getValue());
        }

        return ret;
    }

    public static int countOccurrence(String base, String tag)
    {
        return base.split(tag, -1).length - 1;
    }

    public static void insertBefore(StringBuilder base, String extra, String matchStr)
    {
        int idx = base.indexOf(matchStr);
        base.insert(idx, extra);
    }

    public static void insertAfter(StringBuilder base, String extra, String matchStr)
    {
        int idx = base.indexOf(matchStr);
        base.insert(idx + matchStr.length(), extra);
    }
}
