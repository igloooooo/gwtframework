package com.iglooit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CentralPlaceForDateConstruction extends Check
{
    private static final Set<String> PROHIBITED_CLASS_CONSTRUCTIONS =
        new HashSet<String>(Arrays.asList(
            "Date", "GregorianCalendar",
            "DateTime", "BaseDateTime", "MutableDateTime", "DateMidnight"));
    private static final List<StringPair> PROHIBITED_METHOD_CALLS =
        Arrays.asList(new StringPair("System", "currentTimeMillis"), new StringPair("Calendar", "getInstance"));
    private static final String PROHIBITED_METHODS;

    static
    {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (StringPair stringPair : PROHIBITED_METHOD_CALLS)
        {
            if (first)
                first = false;
            else
                sb.append(", ");
            sb.append(stringPair.getFirst()).append(".").append(stringPair.getSecond());
        }
        PROHIBITED_METHODS = sb.toString();
    }

    public int[] getDefaultTokens()
    {
        return new int[]{TokenTypes.EXPR};
    }

    public void visitToken(DetailAST ast)
    {
        if (ast.getChildCount() == 1 && ast.branchContains(TokenTypes.LITERAL_NEW))
        {
            DetailAST newKeyworkAST = ast.findFirstToken(TokenTypes.LITERAL_NEW);
            if (newKeyworkAST == null)
                return;
            DetailAST createdClassAST = newKeyworkAST.findFirstToken(TokenTypes.IDENT);
            if (createdClassAST == null)
                return;
            if (PROHIBITED_CLASS_CONSTRUCTIONS.contains(createdClassAST.getText()))
            {
                if (createdClassAST.getNextSibling() != null
                    && createdClassAST.getNextSibling().getType() == TokenTypes.ARRAY_DECLARATOR)
                    return;
                log(createdClassAST.getLineNo(), "Creation of these classes is not generally allowed: " +
                    PROHIBITED_CLASS_CONSTRUCTIONS);
            }
        }
        else if (ast.getChildCount() == 1 && ast.branchContains(TokenTypes.METHOD_CALL))
        {
            DetailAST methodCallAST = ast.findFirstToken(TokenTypes.METHOD_CALL);
            if (methodCallAST == null)
                return;
            DetailAST dotAST = methodCallAST.findFirstToken(TokenTypes.DOT);
            if (dotAST == null)
                return;
            DetailAST objNameAST = dotAST.findFirstToken(TokenTypes.IDENT);
            if (objNameAST == null)
                return;
            String objName = objNameAST.getText();
            DetailAST methodNameAST = objNameAST.getNextSibling();
            if (methodNameAST == null)
                return;
            String methodName = methodNameAST.getText();
            if (StringPair.contains(PROHIBITED_METHOD_CALLS, new StringPair(objName, methodName)))
                log(methodNameAST.getLineNo(), "Using these methods is not generally allowed: " + PROHIBITED_METHODS);
        }
    }
}

class StringPair implements Serializable
{
    private String a;
    private String b;

    public StringPair(String a, String b)
    {
        this.a = a;
        this.b = b;
    }

    public String getFirst()
    {
        return a;
    }

    public String getSecond()
    {
        return b;
    }

    @Override
    public boolean equals(Object otherObj)
    {
        if (otherObj instanceof StringPair)
        {
            StringPair other = (StringPair)otherObj;
            return this.a.equals(other.a) && this.b.equals(other.b);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return a.hashCode() * 7 + b.hashCode() * 31;
    }

    public static boolean contains(List<StringPair> stringPairList, StringPair stringPair)
    {
        if (stringPairList == null)
            return false;
        for (StringPair sp : stringPairList)
        {
            if (sp != null && sp.equals(stringPair))
                return true;
        }
        return false;
    }
}
