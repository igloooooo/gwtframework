package com.iglooit.core.security.server.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * A thread-safe security context container.
 *
 * @author Michael Truong
 */
public class ClaritySecurityContextHolder
{
    private static final Log LOG = LogFactory.getLog(ClaritySecurityContextHolder.class);
    private static ThreadLocal<MutableClarityUserContext> threadLocalUserContext =
        new ThreadLocal<MutableClarityUserContext>()
        {
            @Override
            protected MutableClarityUserContext initialValue()
            {
                return new DefaultClarityUserContext();
            }
        };

    /**
     * Returns the current (or new) user context instance for the current thread.
     *
     * @return An existing {@link MutableClarityUserContext} for the current thread or a new one if none exists.
     */
    public static MutableClarityUserContext getUserContext()
    {
        return threadLocalUserContext.get();
    }

    public static boolean setAuthenticatedUser(Authentication currentAuth, String newDecryptedPassword)
    {
        Assert.notNull(currentAuth);
        Assert.state(currentAuth instanceof UsernamePasswordAuthenticationToken,
            "Authentication object must be a " + UsernamePasswordAuthenticationToken.class.getName());

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(currentAuth.getPrincipal(), newDecryptedPassword,
                currentAuth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return storeSecurityContextInSession();
    }

    /**
     * This step is important because basically the security context is retrieved from the HTTP session by Spring (See
     * {@link org.springframework.security.web.context.HttpSessionSecurityContextRepository
     * #containsContext(javax.servlet.http.HttpServletRequest)}).
     */
    private static boolean storeSecurityContextInSession()
    {
        RequestAttributes reqAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(reqAttributes != null);
        Assert.state(reqAttributes instanceof ServletRequestAttributes);

        HttpSession session = ServletRequestAttributes.class.cast(reqAttributes).getRequest().getSession();
        if (session != null)
        {
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
            return true;
        }
        else
        {
            LOG.warn("No HTTP session is available for the current HTTP request. Don't apply new security context");
            return false;
        }
    }
}
