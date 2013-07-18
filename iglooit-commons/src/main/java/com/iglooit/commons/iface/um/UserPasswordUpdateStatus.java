package com.iglooit.commons.iface.um;

import com.iglooit.commons.iface.type.Option;

import java.io.Serializable;

public enum UserPasswordUpdateStatus implements Serializable
{
    PASSWORD_VALID(null),
    PASSWORD_INVALID(null),
    CANNOT_REUSE("ORA-28007"),
    PASSWORD_EXPIRED("ORA-28001"),
    VERIFICATION_FAILED("ORA-28003"),
    INSUFFICIENT_PRIVILEGES("ORA-01031");

    UserPasswordUpdateStatus(String errorCodeString)
    {
        if (errorCodeString == null)
            errorCode = Option.none();
        else
            errorCode = Option.some(errorCodeString);
    }

    private final Option<String> errorCode;

    public Option<String> getErrorCode()
    {
        return errorCode;
    }

    public static UserPasswordUpdateStatus getByException(String exceptionString)
    {
        for (UserPasswordUpdateStatus status : UserPasswordUpdateStatus.values())
            if (status.getErrorCode().isSome() && exceptionString.contains(status.getErrorCode().value()))
                return status;
        return PASSWORD_INVALID;
    }
}
