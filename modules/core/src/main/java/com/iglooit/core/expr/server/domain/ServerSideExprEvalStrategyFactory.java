package com.iglooit.core.expr.server.domain;

import com.iglooit.commons.iface.type.AppX;
import com.iglooit.core.expr.iface.domain.ExprEvalStrategy;
import com.iglooit .core.expr.iface.domain.ExprEvalStrategyFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to map expression.eval() to the appropriate execution strategy
 * on the serverside.
 * <p/>
 * Note that this is required because most expressions can (and should) only
 * be performed on the server. This means that the eval() strategies that can
 * only be performed on the server, have to live in the server packages so that
 * there is no attempt by gwt to compile them.
 * <p/>
 * In order to add an addtional mapping, just make it a spring component,
 * and in the postconstruct event add it to the factory.
 */
@Component
public class ServerSideExprEvalStrategyFactory implements ExprEvalStrategyFactory
{
    private Map<Class, ExprEvalStrategy> map = new HashMap<Class, ExprEvalStrategy>();

    @Override
    public ExprEvalStrategy getExprEvalStrategy(Class clazz)
    {
        ExprEvalStrategy result = map.get(clazz);
        if (result == null)
            throw new AppX("Unable to find an expression eval stragegy for " + clazz.getName() +
                ". Ensure that it is mapped as a @Component, and has @PostConstruct method that " +
                "adds it to the ServerSideExprEvalFactory.");
        return result;
    }

    public void add(Class exprClass, ExprEvalStrategy strategy)
    {
        map.put(exprClass, strategy);
    }
}
