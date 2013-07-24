package com.iglooit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.HashSet;
import java.util.Set;

public class RequestHandlersMustBeThreadSafe extends Check
{
    private static final Set<String> REQUEST_HANDLER_CLASS_NAMES = new HashSet<String>()
    {
        {
            add("RequestHandler");
            add("AlarmRequestHandler");
            add("WorkflowDomainEntityRequestHandler");
            add("VersionableWorkflowRequestHandler");
            add("MetaConvertingRequestHandler");
            add("UserRolePasswordUpdateRequestHandlerBase");
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
        final DetailAST className = ast.findFirstToken(TokenTypes.IDENT);

        //Find class extension (if any)
        DetailAST ext = ast.findFirstToken(TokenTypes.EXTENDS_CLAUSE);

        if (ext != null)
        {
            String classExtended = null;
            DetailAST dot = ext.findFirstToken(TokenTypes.DOT);
            if (dot != null)
                classExtended = dot.findFirstToken(TokenTypes.IDENT).getText();
            else
                classExtended = ext.findFirstToken(TokenTypes.IDENT).getText();

            if (!REQUEST_HANDLER_CLASS_NAMES.contains(classExtended))
                return;

            DetailAST classModifiersAst = ast.findFirstToken(TokenTypes.MODIFIERS);
            if (parentRequestHandlerIsNotMappedInCheck(className.getText(), classModifiersAst))
                log(ast.getLine(), "Requests that are inherited from should be added to the " +
                    "source code of the RequestHandlersMustBeThreadSafe check");

            //Get the class object..
            DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);

            if (objBlock.getChildCount(TokenTypes.VARIABLE_DEF) == 0)
                //Return, no variables == no non static variables..
                return;

            DetailAST field = objBlock.findFirstToken(TokenTypes.VARIABLE_DEF);
            while (field != null)
            {
                if (field.getType() == TokenTypes.VARIABLE_DEF)
                {
                    DetailAST modAst = field.findFirstToken(TokenTypes.MODIFIERS);
                    if (hasNoModifiers(modAst)
                        || (isNotFinal(modAst) && isNotAnnotatedWithAtResource(modAst)))
                    {
                        log(objBlock.getLineNo(),
                            "This class is not thread safe. Member variables should either be " +
                                "final, or injected via spring. It is recommended to use local variables " +
                                "method parameters for data that changes per request.");
                    }
                }
                field = field.getNextSibling();
            }
        }
    }

    private boolean parentRequestHandlerIsNotMappedInCheck(String className, DetailAST classModifiersAst)
    {
        return classModifiersAst.findFirstToken(TokenTypes.ABSTRACT) != null
            && !REQUEST_HANDLER_CLASS_NAMES.contains(className);
    }

    private boolean isNotAnnotatedWithAtResource(DetailAST modAst)
    {
        boolean result = true;
        DetailAST annotation = modAst.findFirstToken(TokenTypes.ANNOTATION);

        while (annotation != null)
        {
            //this isn't the best way to match on a resource...
            //remove these once all the code is converted to use final
            if (annotation.toStringTree().contains("Resource")
                || annotation.toStringTree().contains("PersistenceContext")
                )
                return false;

            annotation = annotation.getNextSibling();
        }
        return result;
    }

    private boolean isNotFinal(DetailAST modAst)
    {
        return (!modAst.branchContains(TokenTypes.FINAL));
    }

    private boolean hasNoModifiers(DetailAST modAst)
    {
        return modAst == null;
    }
}

