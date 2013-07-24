package com.iglooit.um.web.controller;

import com.iglooit.commons.iface.um.UserPasswordUpdateStatus;
import com.iglooit.um.server.UserSecurityService;
import com.iglooit.um.web.model.PasswordChangeData;
import com.iglooit.um.web.validator.PasswordValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import javax.annotation.Resource;

@Controller
@RequestMapping("/password.htm")
public class PasswordController
{
    private static final Log LOG = LogFactory.getLog(PasswordController.class);

    @Resource(name = "clientDefaultAccount")
    private String clientDefaultAccount;

    @Resource
    private PasswordValidator validator;

    @Resource
    private UserSecurityService userSecurityService;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model)
    {
        PasswordChangeData data = new PasswordChangeData();
        model.addAttribute("passwordchange", data);
        return "passwordChangeForm";
    }

    public String getClientDefaultAccount()
    {
        return clientDefaultAccount;
    }

    public void setClientDefaultAccount(String clientDefaultAccount)
    {
        this.clientDefaultAccount = clientDefaultAccount;
    }

    public UserSecurityService getUserSecurityService()
    {
        return userSecurityService;
    }

    public void setUserSecurityService(UserSecurityService userSecurityService)
    {
        this.userSecurityService = userSecurityService;
    }

    @Transactional(readOnly = false)
    @RequestMapping(method = RequestMethod.POST)
    protected String processSubmit(@ModelAttribute("passwordchange") PasswordChangeData data, BindingResult result,
                                   SessionStatus status) throws Exception
    {
        validator.setClientDefaultAccount(clientDefaultAccount);
        validator.validate(data, result);
        if (result.hasErrors())
            return "passwordChangeForm";
        String username = validator.getFinalUsername();
        String orgName = validator.getOrgName();
        LOG.info("Changing password for: " + orgName + " - " + username);
        UserPasswordUpdateStatus updateResult =
            userSecurityService.changePass(orgName, username, data.getPassword(), data.getNewPassword());
        data.setResult(updateResult);
        if (updateResult.equals(UserPasswordUpdateStatus.PASSWORD_VALID))
            return "passwordChangeSuccess";
        else
        {
            LOG.info("Failed changing password for: " + orgName + " - " + username + ", Error: " + updateResult);
            return "passwordChangeFailure";
        }
    }
}
