package com.iglooit.checkstyle.checks;

/**
 * Created with IntelliJ IDEA.
 * User: gmcmahon
 * Date: 8/6/12
 * Time: 8:33 AM
 * To change this template use File | Settings | File Templates.
 */
import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.HashMap;
import java.util.Map;

public class ClarityInvalidMethodNameCheck extends Check
{
    int count;

    private static final Map<String, String> ILLEGAL_METHODS= new HashMap<String, String>()
    {
        private static final long serialVersionUID = -689074032422788082L;
        {
            //  put("<METHOD_NAME>", 				"<REASON_NOT_ALLOWED>");

            put("printStackTrace", 	"Exceptions should be thrown to the top level and handled by log4j");
            put("executeQuery", 	"Low-level SQL should not be used. JdbcTemplate, HQL or Hibernate " +
                                    "should be used instead");
            put("executeUpdate", 	"Low-level SQL should not be used. JdbcTemplate, HQL or Hibernate " +
                                    "should be used instead");
        }
    };

    @Override
    public int[] getDefaultTokens()
    {
        return new int[]{TokenTypes.METHOD_CALL};
    }

    @Override
    public void visitToken(DetailAST ast)
    {
        count++;

        DetailAST methodCallSearch = ast.getFirstChild();

        if (methodCallSearch.getType() == TokenTypes.DOT)
        {
            methodCallSearch = methodCallSearch.getLastChild();
        }

        DetailAST methodNameIdent = methodCallSearch;

        if (methodNameIdent != null)
        {
            if (ILLEGAL_METHODS.keySet().contains(methodNameIdent.getText()))
            {
                log(methodNameIdent.getLineNo(), "ERROR: Invalid method being used in call to " + methodNameIdent.getText() +
                        "(). " + ILLEGAL_METHODS.get(methodNameIdent.getText()) +
                        " on line " + methodNameIdent.getLineNo() + ", column " + methodNameIdent.getColumnNo());
            }
        }
    }
}
