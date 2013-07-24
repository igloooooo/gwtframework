package com.iglooit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class DiamondSyntaxCheck extends Check
{
    @Override
    public int[] getDefaultTokens()
    {
        return new int[]{TokenTypes.GENERIC_START};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitToken(DetailAST ast)
    {
        super.visitToken(ast);

        if (ast.getType() == TokenTypes.GENERIC_START)
        {
            processStart(ast);
        }
    }


    private void processStart(DetailAST ast)
    {

        final String line = getLines()[ast.getLineNo() - 1];

        for (int nextCharIdx = ast.getColumnNo() + 1; nextCharIdx < line.length(); nextCharIdx++)
        {
            char nextCharacter = line.charAt(nextCharIdx);
            if (Character.compare(nextCharacter, '>') == 0)
            {
                log(ast.getLineNo(), nextCharIdx - 1, "illegal use of diamond syntax");
            }
            else if (!Character.isSpaceChar(nextCharacter))
            {
                return;
            }
        }
    }
}
