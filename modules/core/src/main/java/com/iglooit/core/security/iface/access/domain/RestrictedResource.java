package com.iglooit.core.security.iface.access.domain;

import com.clarity.core.expr.iface.domain.ExprEvalStrategyFactory;

public interface RestrictedResource
{
    boolean allowAccess(ExprEvalStrategyFactory exprEvalStrategyFactory, Subject subject,
                        SecurityZone subjectArea, String requiredPrivilegeName);
}


