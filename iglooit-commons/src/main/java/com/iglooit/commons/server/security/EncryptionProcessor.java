package com.iglooit.commons.server.security;

import com.clarity.commons.iface.annotation.EncryptedString;
import com.clarity.commons.iface.domain.Encryptable;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.NonSerOpt;
import com.clarity.commons.server.util.MetaUtil;
import com.clarity.commons.server.util.ReflectionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EncryptionProcessor
{
    private static final Log LOG = LogFactory.getLog(EncryptionProcessor.class);

    private static StandardPBEStringEncryptor getEncryptor(Encryptable encryptable)
    {
        StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
        enc.setAlgorithm(encryptable.retrieveEncryptionCipher());
        enc.setPassword(encryptable.retrieveEncryptionKey());
        enc.initialize();
        return enc;
    }

    private static List<Field> getFieldsMatchingAnnotation(Class<?> type, Class<? extends Annotation> annotationClass)
    {
        List<Field> fieldsMatching = new ArrayList<Field>();
        for (Field field : ReflectionUtil.getAllFields(type))
        {
            if (LOG.isTraceEnabled()) LOG.trace("Iterating field: " + field.getName());
            field.setAccessible(true);
            if (field.getAnnotation(annotationClass) != null)
            {
                if (LOG.isTraceEnabled()) LOG.trace("Found annotation for field: " + field.getName());
                fieldsMatching.add(field);
            }
        }
        return fieldsMatching;
    }

    private static <T extends Encryptable> boolean isFieldEncrypted(T de,
                                                                    Field field,
                                                                    StandardPBEStringEncryptor encryptor)
    {
        try
        {
            String str = null;
            NonSerOpt<Method> getter = MetaUtil.findGetter(de.getClass(), field);
            if (getter.isSome())
                str = (String)getter.value().invoke(de);
            else
                throw new AppX("Cannot find setter for field: " + field.getName());
            decryptString(encryptor, str);
        }
        catch (EncryptionOperationNotPossibleException e)
        {
            return false;
        }
        catch (IllegalAccessException e)
        {
            throw new AppX("Cannot access field value for encrypted type: ", e);
        }
        catch (InvocationTargetException e)
        {
            throw new AppX("Cannot invoke getter ", e);
        }
        return true;
    }

    protected static String decryptString(StandardPBEStringEncryptor encryptor, String encryptedValue)
    {
        try
        {
            return encryptor.decrypt(encryptedValue);
        }
        catch (EncryptionOperationNotPossibleException e)
        {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

    // is this a good public contract to send out?
    // assumes they know what fields are encryptable in advance. which is fair i guess...
    // it should force the user to think when they are obtaining decrypted values
    public static <T extends Encryptable> String decryptString(T de, String encryptedValue)
    {
        return decryptString(getEncryptor(de), encryptedValue);
    }

    protected static String encryptString(StandardPBEStringEncryptor encryptor, String unencryptedValue)
    {
        try
        {
            return encryptor.encrypt(unencryptedValue);
        }
        catch (EncryptionOperationNotPossibleException e)
        {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

    private static <T extends Encryptable> void encryptField(T de,
                                                             Field field,
                                                             StandardPBEStringEncryptor encryptor)
    {
        try
        {
            NonSerOpt<Method> getter = MetaUtil.findGetter(de.getClass(), field);
            NonSerOpt<Method> setter = MetaUtil.findSetter(de.getClass(), field, field.getType());
            if (getter.isNone() || setter.isNone())
                throw new AppX("Cannot find getter and setter for field: " + field.getName());

            final String unencryptedValue = (String)getter.value().invoke(de);
            final String encryptedString = encryptString(encryptor, unencryptedValue);
            setter.value().invoke(de, encryptedString);
        }
        catch (IllegalAccessException e)
        {
            throw new AppX("Could not access field to encrypt: ", e);
        }
        catch (InvocationTargetException e)
        {
            throw new AppX("Cannot invoke getter or setter ", e);
        }
    }

    public static <T extends Encryptable> T encrypt(T de, boolean skipCheck)
    {
        final Class encryptableClass;
        encryptableClass = de.getClass();

        if (LOG.isTraceEnabled()) LOG.trace("Scanning class: " + encryptableClass);
        StandardPBEStringEncryptor encryptor = getEncryptor(de);

        List<Field> encryptableFields = getFieldsMatchingAnnotation(encryptableClass, EncryptedString.class);
        for (Field field : encryptableFields)
            if (skipCheck || !isFieldEncrypted(de, field, encryptor))
                encryptField(de, field, encryptor);

        return de;
    }

    public static <T extends Encryptable> T encrypt(T de)
    {
        return encrypt(de, false);
    }

    public static void main(String[] args)
    {
        if (args.length < 3)
        {
            System.err.println("Usage: EncryptionProcessor key input_string enc/dec [algorithm]");
            System.exit(1);
        }
        String key = args[0];
        String inputString = args[1];
        String operation = args[2];
        String algorithm = "PBEWithMD5AndTripleDES";
        if (args.length > 3)
            algorithm = args[3];

        StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
        enc.setAlgorithm(algorithm);
        enc.setPassword(key);
        enc.initialize();


        if (operation.equals("enc"))
        {
            String encryptedString = EncryptionProcessor.encryptString(enc, inputString);
            System.out.println("Encrypted string: " + encryptedString);
        }
        else if (operation.equals("dec"))
        {
            String decryptedString = EncryptionProcessor.decryptString(enc, inputString);
            System.out.println("Decrypted string: " + decryptedString);
        }
    }
}
