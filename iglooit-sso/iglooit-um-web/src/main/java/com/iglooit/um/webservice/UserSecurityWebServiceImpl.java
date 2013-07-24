package com.iglooit.um.webservice;

import com.iglooit.commons.iface.um.UserRoleDTO;
import com.iglooit.commons.iface.um.UserSecurityDetailsDTO;
import com.iglooit.um.server.UserSecurityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(endpointInterface = "com.iglooit.um.server.webservice.UserSecurityWebService")
@XmlSeeAlso(value = {UserSecurityDetailsDTO.class, UserRoleDTO.class})
public class UserSecurityWebServiceImpl implements UserSecurityWebService
{
    private static final Log LOG = LogFactory.getLog(UserSecurityWebServiceImpl.class);

    @Resource
    private UserSecurityService userSecurityService;

    public UserSecurityService getUserSecurityService()
    {
        return userSecurityService;
    }

    public void setUserSecurityService(UserSecurityService userSecurityService)
    {
        this.userSecurityService = userSecurityService;
    }

    public UserSecurityDetailsDTO checkPartyLogin(String orgName, String username, String password)
    {
        LOG.info("checkPartyLogin: " + orgName + " - " + username);
        UserSecurityDetailsDTO details = userSecurityService.checkPartyLogin(orgName, username, password);
        if (LOG.isDebugEnabled())
            LOG.debug("UserSecurityDetails: " + details);
        return new UserSecurityDetailsDTO(details.getUserLoginStatus(), details.getPrivilege(),
            details.getAuthorities(), details.getUserRole(), details.getErrorMessage());
    }
}
