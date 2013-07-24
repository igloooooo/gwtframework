package com.iglooit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.HashSet;

public class GwtSupportedJavaClassesCheck extends Check
{
    // For GWT 2.3, the list is obtained from:
    // https://developers.google.com/web-toolkit/doc/2.3/RefJreEmulation
    private static final HashSet<String> supportedJavaClassesSet = new HashSet<String>() {{
        add("java.lang.annotation.Annotation");
        add("java.lang.annotation.AnnotationFormatError");
        add("java.lang.annotation.AnnotationTypeMismatchException");
        add("java.lang.annotation.Documented");
        add("java.lang.annotation.ElementType");
        add("java.lang.annotation.IncompleteAnnotationException");
        add("java.lang.annotation.Inherited");
        add("java.lang.annotation.Retention");
        add("java.lang.annotation.RetentionPolicy");
        add("java.lang.annotation.Target");
        add("java.math.BigDecimal");
        add("java.math.BigInteger");
        add("java.math.MathContext");
        add("java.math.RoundingMode");
        add("java.io.FilterOutputStream");
        add("java.io.IOException");
        add("java.io.OutputStream");
        add("java.io.PrintStream");
        add("java.io.Serializable");
        add("java.io.UnsupportedEncodingException");
        add("java.sql.Date");
        add("java.sql.Time");
        add("java.sql.Timestamp");
        add("java.util.AbstractCollection");
        add("java.util.AbstractList");
        add("java.util.AbstractMap");
        add("java.util.AbstractQueue");
        add("java.util.AbstractSequentialList");
        add("java.util.AbstractSet");
        add("java.util.ArrayList");
        add("java.util.Arrays");
        add("java.util.Collection");
        add("java.util.Collections");
        add("java.util.Comparator");
        add("java.util.ConcurrentModificationException");
        add("java.util.Date");
        add("java.util.EmptyStackException");
        add("java.util.EnumMap");
        add("java.util.EnumSet");
        add("java.util.Enumeration");
        add("java.util.EventListener");
        add("java.util.EventObject");
        add("java.util.HashMap");
        add("java.util.HashSet");
        add("java.util.IdentityHashMap");
        add("java.util.Iterator");
        add("java.util.LinkedHashMap");
        add("java.util.LinkedHashSet");
        add("java.util.LinkedList");
        add("java.util.List");
        add("java.util.ListIterator");
        add("java.util.Map");
        add("java.util.Map.Entry");
        add("java.util.MissingResourceException");
        add("java.util.NoSuchElementException");
        add("java.util.PriorityQueue");
        add("java.util.Queue");
        add("java.util.Random");
        add("java.util.RandomAccess");
        add("java.util.Set");
        add("java.util.SortedMap");
        add("java.util.SortedSet");
        add("java.util.Stack");
        add("java.util.TooManyListenersException");
        add("java.util.TreeMap");
        add("java.util.TreeSet");
        add("java.util.Vector");
        add("java.util.logging.Formatter");
        add("java.util.logging.Handler");
        add("java.util.logging.Level");
        add("java.util.logging.LogManager");
        add("java.util.logging.LogRecord");
        add("java.util.logging.Logger");
    }};

    private boolean isClientCode = true;

    @Override
    public int[] getDefaultTokens()
    {
        return new int[]{TokenTypes.IMPORT, TokenTypes.PACKAGE_DEF};
    }

    @Override
    public void visitToken(DetailAST ast)
    {
        if (ast.getType() == TokenTypes.PACKAGE_DEF)
        {
            final String line = getLines()[ast.getLineNo() - 1];
            if (line.contains(".client.") || line.contains(".iface."))
            {
                isClientCode = true;
            }
            else
            {
                isClientCode = false;
            }
            return;
        }

        if (!isClientCode) return;
        super.visitToken(ast);

        if (ast.getType() == TokenTypes.IMPORT)
        {
            final String line = getLines()[ast.getLineNo() - 1];
            if (line.contains(" java."))
            {
                String importClass = new String(line);
                importClass = importClass.replaceFirst("^\\s*import\\s+", "");
                importClass = importClass.replaceFirst("\\s*;.*$", "");
                if (!supportedJavaClassesSet.contains(importClass))
                {
                    log(ast.getLineNo(), importClass + " is NOT supported by GWT.");
                }
            }
        }
    }
}
