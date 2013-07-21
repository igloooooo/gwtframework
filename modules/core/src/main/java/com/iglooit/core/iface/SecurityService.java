package com.iglooit.core.iface;

import com.iglooit.commons.iface.type.Option;
import com.iglooit.commons.iface.type.UUID;
import com.iglooit.commons.iface.um.UserRoleDTO;
import com.iglooit.commons.iface.um.UserSecurityDetailsDTO;
import com.iglooit.core.account.iface.domain.Individual;
import com.iglooit.core.account.iface.domain.UserRole;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import javax.servlet.http.HttpServletRequest;

@RemoteServiceRelativePath("SecurityService")
public interface SecurityService extends RemoteService
{
    String BEAN_NAME = "securityService";
    String SESSION_PARAM_USER_ROLE = "session_user_role";

    UserSecurityDetailsDTO authenticateUser(String orgName, String username, String password);

    UserRoleDTO getLoginInfo(HttpServletRequest request);

    Option<Individual> getActiveIndividual();

    /**
     * Returns the individual attached to the current (i.e. active) user role. If there is no active user role, return
     * {@link Option#none()}.
     */
    Option<Individual> getActiveIndividualWeak();

    Option<UUID> getActiveUserRoleId(HttpServletRequest request);

    Option<UUID> getActiveUserRoleId();

    Option<UserRole> getActiveUserRole(HttpServletRequest request);

    Option<UserRole> getActiveUserRole();

    Option<UUID<Individual>> getActiveIndividualId();

    String getClientDefaultAccount();

    void setupNonSessionUserId(Individual individual);
}
