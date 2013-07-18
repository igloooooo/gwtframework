package com.iglooit.commons.server.util;

public class OracleUtil
{
    public static final String ANSI_CHARS = getAllNormalANSIChars();
    private static final char[] ANSI_CHARS_ARR = getAllNormalANSIChars().toCharArray();

    private static String getAllNormalANSIChars()
    {
        StringBuilder sb = new StringBuilder();
        for (char c = 'a'; c <= 'z'; c++)
            sb.append(c);
        for (char c = 'A'; c <= 'Z'; c++)
            sb.append(c);
        for (char c = '0'; c <= '9'; c++)
            sb.append(c);
        sb.append("_");
        return sb.toString();
    }

    public static boolean isANSIChar(char ch)
    {
        for (char c : ANSI_CHARS_ARR)
            if (c == ch)
                return true;
        return false;
    }

    public static String modToChars(final String input)
    {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray())
            if (isANSIChar(c))
                sb.append(c);
            else sb.append(ANSI_CHARS_ARR[(Math.abs((int)c)) % ANSI_CHARS_ARR.length]);
        return sb.toString();
    }

    public static String oracleMod(final String input)
    {
        return modToChars(input).replaceAll("[^\\w]", "");
    }
}
