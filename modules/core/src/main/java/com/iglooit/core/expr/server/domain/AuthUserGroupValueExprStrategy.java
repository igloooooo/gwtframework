package com.iglooit.core.expr.server.domain;

import com.iglooit.core.account.server.domain.UserHome;
import com.iglooit.core.expr.iface.domain.*;
import com.iglooit.core.iface.SecurityService;
import com.iglooit.core.oss.server.OSSHome;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Component
public class AuthUserGroupValueExprStrategy implements ExprEvalStrategy
{
    @Resource
    private ExprEvalStrategyFactory exprEvalStrategyFactory;

    @Resource
    private OSSHome ossHome;

    @Resource
    private UserHome userHome;

    @Resource
    private SecurityService securityService;


    @PostConstruct
    private void init()
    {
        exprEvalStrategyFactory.add(AuthUserGroupExpr.class, this);
    }

    @Override
    public ExprResult eval(Expr expr, Map<String, Object> expressionContext)
    {
        expr.ensureCorrectNumberofChildren(0);

        //to get authenticated users groups
        String employeeId = userHome.getUser(securityService.getActiveUserRoleId().value()).getOssEmployeeId();
        List<String> workGroupList = ossHome.getUserWorkgroupNames(employeeId);

        return new ExprResult(workGroupList);
    }
}