package com.iglooit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Checkstyle Check for a test class. The rules for a test class are:
 * <ul>
 * <li>A test class's name should end with "Test" or it's not considered as Test.
 * <li>A test class must extend either:<br>
 * [1] <code>AbstractClarityTest</code>, <code>AbstractServerTransactionalTest</code>,
 * <code>AbstractServerTransactionalMockTest</code> or <code>AbstractGwtTransactionalTest</code>;<br>
 * [2] any of the explicitly configured classes in the property <b>validTestSuperclasses</b>; <br>
 * [3] another test class (following the same naming convention).
 * </ul>
 * 
 * @author Michael Truong
 */
public class ClarityTestSuperclassCheck extends Check
{

    private static final String MOST_ABS_TESTCLASS = "AbstractClarityTest";
    private static final List<String> VALID_TEST_ABS_SUPERCLASSES = new ArrayList<String>(Arrays.asList(
            MOST_ABS_TESTCLASS, "AbstractServerTransactionalTest", "AbstractServerTransactionalMockTest",
            "AbstractGwtTransactionalTest", "AbstractGwtTest", "AbstractPresenterGwtTest"));

    private final Set<String> validTestSuperclasses = new HashSet<String>();

    /**
     * Sets all valid superclasses for a test class. Notice that all classes defined in
     * {@link #VALID_TEST_ABS_SUPERCLASSES} are included by default.
     * 
     * @param validTestSuperclasses Simple names of all valid classes, separated by commas. Example:
     *            "PerformanceRequestTest,HierarchyLevelRequestTest".
     */
    public void setValidTestSuperclasses(String validTestSuperclasses)
    {
        this.validTestSuperclasses.clear();
        this.validTestSuperclasses.addAll(VALID_TEST_ABS_SUPERCLASSES);

        if (validTestSuperclasses != null && !validTestSuperclasses.isEmpty())
        {
            String[] classes = validTestSuperclasses.split(",");
            for (String cls : classes)
            {
                this.validTestSuperclasses.add(cls.trim());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getDefaultTokens()
    {
        return new int[] { TokenTypes.CLASS_DEF };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitToken(DetailAST ast)
    {
        super.visitToken(ast);

        DetailAST className = ast.findFirstToken(TokenTypes.IDENT);

        if (isTestClassByConvention(className) && !isAbstract(ast))
        {
            DetailAST extendsClause = ast.findFirstToken(TokenTypes.EXTENDS_CLAUSE);
            if (extendsClause != null)
            {
                DetailAST superClass = findSuperClass(extendsClause);
                String superClassName = superClass.getText();
                if (!validTestSuperclasses.contains(superClassName) && !isTestClassByConvention(superClass))
                {
                    // test class doesn't extend a correct superclass -> violated
                    log(ast.getLine(), "ERROR: Invalid superclass. A test class must extend the " + MOST_ABS_TESTCLASS
                            + " or one of its subclasses");
                }
            }
            else
            {
                // test class doesn't extend anything -> violate
                log(ast.getLine(), "ERROR: No superclass. A test class must extend the " + MOST_ABS_TESTCLASS
                        + " or one of its subclasses");
            }
        }
    }

    private DetailAST findSuperClass(DetailAST extendsClause)
    {
        DetailAST dot = extendsClause.findFirstToken(TokenTypes.DOT);
        return (dot != null ? dot.findFirstToken(TokenTypes.IDENT) : extendsClause.findFirstToken(TokenTypes.IDENT));
    }

    private boolean isAbstract(DetailAST ast)
    {
        if (ast != null)
        {
            ast = ast.findFirstToken(TokenTypes.MODIFIERS);
            if (ast != null)
            {
                ast = ast.findFirstToken(TokenTypes.ABSTRACT);
            }
        }
        return ast != null && "abstract".equals(ast.getText());
    }

    private boolean isTestClassByConvention(DetailAST ast)
    {
        return ast != null && ast.getText().endsWith("Test");
    }
}
