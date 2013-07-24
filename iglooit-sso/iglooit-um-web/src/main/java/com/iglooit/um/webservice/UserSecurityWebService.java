package com.iglooit.um.webservice;

import com.iglooit.commons.iface.um.UserRoleDTO;
import com.iglooit.commons.iface.um.UserSecurityDetailsDTO;

import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService
@XmlSeeAlso(value = {UserSecurityDetailsDTO.class, UserRoleDTO.class})
public interface UserSecurityWebService
{
    UserSecurityDetailsDTO checkPartyLogin(String orgName, String username, String password);
}
