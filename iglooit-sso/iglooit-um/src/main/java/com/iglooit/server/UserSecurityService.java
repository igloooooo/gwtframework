package com.iglooit.server;

import com.iglooit.commons.iface.um.UserPasswordUpdateStatus;
import com.iglooit.commons.iface.um.UserSecurityDetailsDTO;

public interface UserSecurityService
{
    UserSecurityDetailsDTO checkPartyLogin(String orgName, String party, String password);

    UserPasswordUpdateStatus changePass(String orgName, String username, String password, String newPassword);

    UserPasswordUpdateStatus syncPass(String orgName, String username, String newPassword);
}
