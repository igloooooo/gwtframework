package com.iglooit.core.command.iface;

import com.google.gwt.i18n.client.Messages;

public interface LoginMessageConstants extends Messages
{
    String loginTitle();

    String accountLabel();

    String userLabel();

    String passwordLabel();

    String loginFailMsg();

    String loginReasonHeading();

    String submitButton();

    String passwordInvalid();

    String cannotReusePassword();

    String accessGranted();

    String invalidCredentials();

    String noLoginPrivilege();

    String passwordExpired();

    String accountLocked();

    String unknownError();

    String currentPasswordIncorrect();

    String passwordSoonToExpire();

    String loadingAppMessage();

    String refreshButton();

    String sessionTimeoutTitle();

    String sessionTimeoutMessage();
}
