package com.iglooit.commons.iface.um;

import com.iglooit.commons.iface.type.Option;

import java.io.Serializable;
import java.util.Arrays;

public enum UserLoginStatus implements Serializable
{
    GRANTED(null),
    BAD_CREDENTIALS("ORA-01017"),
    NO_LOGIN_PRIVILEGE("ORA-01045"),
    PASSWORD_EXPIRED("ORA-28001"),
    PASSWORD_WILL_EXPIRE("ORA-28002"),
    PASSWORD_WILL_EXPIRE_SOON("ORA-28011"),
    ACCOUNT_LOCKED("ORA-28000"),
    OTHER_ERROR(null);

    UserLoginStatus(String errorCodeString)
    {
        if (errorCodeString == null)
            errorCode = Option.none();
        else
            errorCode = Option.some(errorCodeString);
    }

    private final Option<String> errorCode;

    public boolean isGranted()
    {
        return Arrays.asList(UserLoginStatus.GRANTED, UserLoginStatus.PASSWORD_WILL_EXPIRE,
            UserLoginStatus.PASSWORD_WILL_EXPIRE_SOON).contains(this);
    }

    public boolean isExpired()
    {
        return Arrays.asList(UserLoginStatus.PASSWORD_EXPIRED).contains(this);
    }

    public Option<String> getErrorCode()
    {
        return errorCode;
    }
}
