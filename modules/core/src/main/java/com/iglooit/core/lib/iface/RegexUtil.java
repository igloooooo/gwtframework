package com.iglooit.core.lib.iface;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.ArrayList;
import java.util.List;


public class RegexUtil
{
    public static List<String> getTokens(String inputStr, String regex)
    {
        List<String> tokens = new ArrayList<String>();
        RegExp regExp = RegExp.compile(regex, "g");
        MatchResult matcher = regExp.exec(inputStr);
        boolean matchFound = (matcher != null);
        while (matchFound)
        {

            String paramName = matcher.getGroup(0);
            tokens.add(paramName);
            matcher = regExp.exec(inputStr);
            matchFound = (matcher != null);
        }
        return tokens;
    }
}
