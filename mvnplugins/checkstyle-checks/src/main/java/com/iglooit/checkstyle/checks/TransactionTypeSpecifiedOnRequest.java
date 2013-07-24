package com.iglooit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.HashSet;
import java.util.Set;

public class TransactionTypeSpecifiedOnRequest extends Check
{
    private static final Set<String> REQUEST_CLASS_NAMES = new HashSet<String>()
    {
        {
            add("Request");
            add("DomainEntityRequest");
            add("StringArrayRequest");
        }
    };


    @Override
    public int[] getDefaultTokens()
    {
        return new int[]{TokenTypes.CLASS_DEF};
    }

    @Override
    public void visitToken(DetailAST ast)
    {
        //Find class extension (if any)
        DetailAST ext = ast.findFirstToken(TokenTypes.EXTENDS_CLAUSE);

        final DetailAST className = ast.findFirstToken(TokenTypes.IDENT);

        if (ext != null)
        {
            String classExtended = null;
            DetailAST dot = ext.findFirstToken(TokenTypes.DOT);
            if (dot != null)
                classExtended = dot.findFirstToken(TokenTypes.IDENT).getText();
            else
                classExtended = ext.findFirstToken(TokenTypes.IDENT).getText();

            if (!REQUEST_CLASS_NAMES.contains(classExtended))
                return;

            DetailAST classModifiersAst = ast.findFirstToken(TokenTypes.MODIFIERS);
            if (parentRequestIsNotMappedInCheck(className.getText(), classModifiersAst))
                log(ast.getLine(), "Requests that are inherited from should be added to the " +
                    "source code of the TransactionTypeSpecifiedOnRequest check");

            if (isNotAnnotatedWithRequestType(ast))
            {
                log(ast.getLine(), "Requests must be annotated with either @ReadOnlyRequest or @ReadWriteRequest");
            }
        }
    }

    private boolean parentRequestIsNotMappedInCheck(String className, DetailAST classModifiersAst)
    {
        return (classModifiersAst.findFirstToken(TokenTypes.ABSTRACT) != null
            && !REQUEST_CLASS_NAMES.contains(className));
    }

    private boolean isNotAnnotatedWithRequestType(DetailAST modAst)
    {
        boolean result = true;
        DetailAST annotation = modAst.findFirstToken(TokenTypes.ANNOTATION);

        while (annotation != null)
        {
            if (annotation.toStringTree().contains("ReadWriteRequest")
                || annotation.toStringTree().contains("ReadOnlyRequest")
                )
                return false;

            annotation = annotation.getNextSibling();
        }
        return result;
    }
}
