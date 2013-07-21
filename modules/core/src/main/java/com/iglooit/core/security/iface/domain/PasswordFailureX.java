package com.iglooit.core.security.iface.domain;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.command.iface.LoginMessageConstants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class PasswordFailureX extends AppX
{
    private PasswordFailureInfo failureInfo;

    public PasswordFailureX()
    {
    }

    public PasswordFailureX(PasswordFailureInfo failureInfo)
    {
        this(failureInfo, null);
    }

    public PasswordFailureX(PasswordFailureInfo failureInfo, Throwable e)
    {
        super(failureInfo.getExceptionString(), e);
        this.failureInfo = failureInfo;
    }

    public PasswordFailureInfo getFailureInfo()
    {
        return failureInfo;
    }

    public void setFailureInfo(PasswordFailureInfo failureInfo)
    {
        this.failureInfo = failureInfo;
    }

    public static enum Code
    {
        PASSWORD_VALID
            {
                @Override
                public String getDisplayString()
                {
                    return LMC.accessGranted();
                }

                @Override
                public Option<String> getOracleExceptionCode()
                {
                    return Option.none();
                }
            },

        PASSWORD_INVALID
            {
                @Override
                public String getDisplayString()
                {
                    return LMC.passwordInvalid();
                }

                @Override
                public Option<String> getOracleExceptionCode()
                {
                    return Option.none();
                }
            },

        CANNOT_REUSE
            {
                @Override
                public String getDisplayString()
                {
                    return LMC.cannotReusePassword();
                }

                @Override
                public Option<String> getOracleExceptionCode()
                {
                    return Option.some("ORA-28007");
                }
            },

        PASSWORD_EXPIRED
            {
                @Override
                public String getDisplayString()
                {
                    return LMC.passwordExpired();
                }

                @Override
                public Option<String> getOracleExceptionCode()
                {
                    return Option.some("ORA-28001");
                }
            },

        VERIFICATION_FAILED
            {
                @Override
                public String getDisplayString()
                {
                    return "Error, should pass through from oracle exception code";
                }

                @Override
                public Option<String> getOracleExceptionCode()
                {
                    return Option.some("ORA-28003");
                }
            };

        public abstract String getDisplayString();

        public abstract Option<String> getOracleExceptionCode();

        private static final LoginMessageConstants LMC = I18NFactoryProvider.get(LoginMessageConstants.class);
    }

    // TODO mt: rename 'exceptionString' to exceptionMessage
    public static class PasswordFailureInfo implements Serializable, IsSerializable
    {
        private String originalExceptionMessage;
        private String exceptionString;
        private Code code;

        public PasswordFailureInfo()
        {
            this.code = Code.PASSWORD_INVALID;
        }

        public PasswordFailureInfo(String exceptionString)
        {
            this.originalExceptionMessage = exceptionString;

            this.code = Code.PASSWORD_INVALID;
            for (PasswordFailureX.Code code : PasswordFailureX.Code.values())
                if (code.getOracleExceptionCode().isSome()
                    && exceptionString.contains(code.getOracleExceptionCode().value()))
                    this.code = code;
            this.exceptionString = code.getDisplayString();

            // if it's a verification failure, just pass through the failure message.
            if (this.code.equals(Code.VERIFICATION_FAILED))
            {
                String exceptionCode = this.code.getOracleExceptionCode().value();
                int index = exceptionString.lastIndexOf(exceptionCode) + exceptionCode.length() + 2;
                String verificationString = exceptionString.substring(index).trim();
                this.exceptionString = verificationString;
            }
        }

        public PasswordFailureInfo(Code code)
        {
            this.code = code;
            exceptionString = code.getDisplayString();
        }

        public String getExceptionString()
        {
            return exceptionString;
        }

        public void setExceptionString(String exceptionString)
        {
            this.exceptionString = exceptionString;
        }

        public Code getCode()
        {
            return code;
        }

        public void setCode(Code code)
        {
            this.code = code;
        }

        /**
         * Returns the exception string originally used to instantiate this exception. Basically this is the cause
         * message.
         */
        public String getOriginalExceptionMessage()
        {
            return originalExceptionMessage;
        }
    }
}
