package com.iglooit.sso.cas;

import org.jasig.cas.authentication.AbstractPasswordPolicyEnforcer;

/**
 * Simple password policy enforcer for testing.
 */
public class DummyPasswordPolicyEnforcer extends AbstractPasswordPolicyEnforcer
{
    private static final int PASSWORD_STATUS_PASS = -1;

    @Override
    public void afterPropertiesSet() throws Exception
    {
    }

    /**
     * @return Number of days left to the expiration date, or {@value #PASSWORD_STATUS_PASS}
     */
    public long getNumberOfDaysToPasswordExpirationDate(final String userId)
    {
        String[] organizationIndividual = userId.split("/");
        int daysToExpire = PASSWORD_STATUS_PASS;
        if (organizationIndividual.length >= 2)
            daysToExpire = Integer.parseInt(organizationIndividual[organizationIndividual.length - 1]);
        return daysToExpire;
    }
}
