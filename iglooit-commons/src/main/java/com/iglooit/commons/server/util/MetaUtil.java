package com.iglooit.commons.server.util;

import com.clarity.commons.iface.annotation.NoMetaAccess;
import com.clarity.commons.iface.domain.meta.DelegatingMetaEntity;
import com.clarity.commons.iface.type.NonSerOpt;
import com.clarity.commons.iface.type.Option;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MetaUtil
{
    private static final Log LOG = LogFactory.getLog(MetaUtil.class);

    protected static NonSerOpt<Method> findMethod(Class c, String fieldName, Class<?>... args)
    {
        for (Class cl = c; cl != Object.class; cl = cl.getSuperclass())
        {
            try
            {
                Method getter = cl.getDeclaredMethod(fieldName, args);
                if (getter != null)
                    return NonSerOpt.some(getter);
            }
            catch (NoSuchMethodException e)
            {
                // could mean for this class, not the super.s
            }
        }
        return NonSerOpt.none();
    }

    protected static NonSerOpt<Method> findMethodCaseInsensitive(Class c, String methodName, Class<?>... args)
    {
        for (Class cl = c; cl != Object.class; cl = cl.getSuperclass())
        {
            for (Method method : cl.getDeclaredMethods())
            {
                boolean nameMatches = methodName.equalsIgnoreCase(method.getName());

                boolean argsMatches = true;
                if (method.getParameterTypes().length == args.length)
                {
                    for (int i = 0; i < args.length; i++)
                        if (!isSameArgType(method.getParameterTypes()[i], args[i]))
                            argsMatches = false;
                }

                if (argsMatches && nameMatches)
                {
                    return NonSerOpt.some(method);
                }
            }
        }
        return NonSerOpt.none();
    }

    //TODO: find a better way to compromise the conflicts between primitive type and wrapper type
    public static boolean isSameArgType(Class type1, Class type2)
    {
        if (type1.equals(type2))
            return true;
        if ((type1.equals(Boolean.class) && type2.equals(boolean.class))
            || (type2.equals(Boolean.class) && type1.equals(boolean.class)))
            return true;
        if ((type1.equals(Integer.class) && type2.equals(int.class))
            || (type2.equals(Integer.class) && type1.equals(int.class)))
            return true;
        if ((type1.equals(Double.class) && type2.equals(double.class))
            || (type2.equals(Double.class) && type1.equals(double.class)))
            return true;
        if ((type1.equals(Float.class) && type2.equals(float.class))
            || (type2.equals(Float.class) && type1.equals(float.class)))
            return true;
        if ((type1.equals(Long.class) && type2.equals(long.class))
            || (type2.equals(Long.class) && type1.equals(long.class)))
            return true;
        if ((type1.equals(Character.class) && type2.equals(char.class))
            || (type2.equals(Character.class) && type1.equals(char.class)))
            return true;
        return false;
    }


    public static NonSerOpt<Method> findSetter(Class c, Field field, Class<?>... args)
    {
        NonSerOpt<Method> setter = findMethod(c, setterName(field, true), args);
        if (setter.isSome())
            return setter;
        setter = findMethod(c, setterName(field, false), args);
        if (setter.isSome())
            return setter;
        setter = findMethodCaseInsensitive(c, setterName(field, true), args);
        if (setter.isSome())
            return setter;
        setter = findMethodCaseInsensitive(c, setterName(field, false), args);
        if (setter.isSome())
            return setter;
        return NonSerOpt.none();
    }

    public static NonSerOpt<Method> findGetter(Class c, Field field, Class<?>... args)
    {
        NonSerOpt<Method> getter = findMethod(c, getterName(field, true), args);
        if (getter.isSome())
            return getter;
        // stupid isIsBooleanType stuff in fsm web services
        getter = findMethod(c, getterName(field, false), args);
        if (getter.isSome())
            return getter;
        getter = findMethodCaseInsensitive(c, getterName(field, true), args);
        if (getter.isSome())
            return getter;
        getter = findMethodCaseInsensitive(c, getterName(field, false), args);
        if (getter.isSome())
            return getter;
        return NonSerOpt.none();
    }


    public static boolean isMetaDelegate(Class c)
    {
        if (!c.isInterface())
            for (Class interfaceOfClass : getAllInterfaces(c))
                if (interfaceOfClass.getSimpleName().equals("MetaDelegate"))
                    return true;
        return false;
    }

    public static boolean isMetaClass(Class c)
    {
        if (!c.isInterface())
            for (Class interfaceOfClass : getAllInterfaces(c))
                if (interfaceOfClass.getSimpleName().equals("Meta"))
                    return true;
        return false;
    }

    public static boolean isMetaField(Field field)
    {
        if (Modifier.isStatic(field.getModifiers()))
            return false;
        for (Annotation annotation : field.getAnnotations())
            if (annotation.annotationType().equals(NoMetaAccess.class))
                return false;
        return true;
    }

    public static Set<Field> getAllDeclaredFields(Class<?> clas)
    {
        Set<Field> declaredFields = new HashSet<Field>();

        for (Class cl = clas; cl != Object.class && cl != null; cl = cl.getSuperclass())
            for (Field field : cl.getDeclaredFields())
                if (isMetaField(field))
                    declaredFields.add(field);
        return declaredFields;
    }

    public static String toStaticName(String instanceName)
    {
        StringBuilder sb = new StringBuilder();
        char[] chars = instanceName.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];
            char prevC = (i > 0) ? chars[i - 1] : 'A';
            //char nextC = (i < chars.length - 1) ? chars[i + 1] : 'A';
            if (Character.isUpperCase(c) && Character.isLowerCase(prevC) && i > 0)
                sb.append("_");
            sb.append(Character.toUpperCase(c));
        }
        return sb.toString();
    }

    public static String toInstanceName(String staticName)
    {
        StringBuilder sb = new StringBuilder();
        char[] chars = staticName.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];
            char prevC = (i > 0) ? chars[i - 1] : 'A';
            if (c != '_')
            {
                if (prevC == '_')
                {
                    sb.append(Character.toUpperCase(c));
                }
                else
                {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }

    public static String staticMetaPropertyName(Field field)
    {
        return toStaticName(field.getName()) + "_PROPERTYNAME";
    }

    // get the field name, removing one 'if' from the start
    private static String camelFieldName(Field field, boolean removeIfPrefix)
    {
        String fieldName = field.getName();
        if (removeIfPrefix
            && (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class))
            && fieldName.startsWith("is") && Character.isUpperCase(fieldName.charAt(2)))
            fieldName = fieldName.replaceFirst("is", "");
        return fieldName.substring(0, 1).toUpperCase(Locale.getDefault()) +
            fieldName.substring(1, fieldName.length());
    }

    public static String getterName(Field field)
    {
        return getterName(field, true);
    }

    private static String getterName(Field field, boolean removeIfPrefix)
    {
        String prefix = "get";
        if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class))
            prefix = "is";
        return prefix + camelFieldName(field, removeIfPrefix);
    }

    public static String setterName(Field field)
    {
        return setterName(field, true);
    }

    private static String setterName(Field field, boolean removeIfPrefix)
    {
        String fieldName = field.getName();
        return "set" + camelFieldName(field, removeIfPrefix);
    }


    // NB: be careful, it doesn't play nice with the Meta delegate inheritance
    public static String getPropertyName(Class metaClass, Field field)
    {
        return (metaClass.getName() + "." + field.getName()).replaceAll("\\.", "-");
    }

    public static Option<String> autoGenerateMetaMethods(Class metaClass, String instance, Set<String> imports)
    {
        Set<Field> declaredFields = new HashSet<Field>();

        for (Field field : metaClass.getDeclaredFields())
            if (MetaUtil.isMetaField(field))
                declaredFields.add(field);

        //Set<Field> declaredFields = getAllDeclaredFields(metaClass);

        if (declaredFields.size() == 0)
            return Option.none();

        StringBuilder sb = new StringBuilder();
        // public static field names
        for (Field field : declaredFields)
        {
            sb.append("\tpublic static final String ");
            sb.append(MetaUtil.staticMetaPropertyName(field));
            sb.append(" = ");
            sb.append("\n\t\t\"");
            sb.append(getPropertyName(metaClass, field));
            sb.append("\";\n");
        }

        // list
        sb.append("\n\tprivate static final String[] PROPERTYNAMES = \n\t\t{\n");
        for (Field field : declaredFields)
        {
            sb.append("\t\t");
            sb.append(MetaUtil.staticMetaPropertyName(field));
            sb.append(",\n");
        }
        sb.append("\t};\n\n");
        sb.append("\n\tprivate static List<String> propertyNames = null;\n\n");

        // list method
        sb.append("\n\t@Override");
        sb.append("\n\tpublic synchronized List<String> getPropertyNames() \n\t{");
        sb.append("\n\t\tif (propertyNames == null) \n\t\t{");
        sb.append("\n\t\t\tpropertyNames = new ArrayList<String>(Arrays.asList(PROPERTYNAMES));");
        sb.append("\n\t\t\tpropertyNames.addAll(super.getPropertyNames());");
        sb.append("\n\t\t}");
        sb.append("\n\t\treturn propertyNames;");
        sb.append("\n\t}\n\n");

        // get
        sb.append("\n\t@Override");
        sb.append("\n\tpublic <X> X get(String propertyName) \n\t{\n\t\t");
        for (Field field : declaredFields)
        {
            sb.append("if (propertyName.equals(");
            sb.append(staticMetaPropertyName(field));
            sb.append("))");
            sb.append("\n\t\t\treturn (X)");
            sb.append(getCast(field, imports));
            sb.append(instance);
            sb.append(".");
            sb.append(getterName(field));
            sb.append("();");
            sb.append("\n\t\telse ");
        }
        sb.append("\n\t\t\treturn (X)super.get(propertyName);\n\t}\n\n");

        // set
        sb.append("\n\t@Override");
        sb.append("\n\tpublic void set(String propertyName, Object value) \n\t{\n\t\t");
        for (Field field : declaredFields)
        {
            String primitiveNullCheck = "";
//            if (primitiveNullCheck(field))
//                primitiveNullCheck = " && value != null";
            sb.append("if (propertyName.equals(");
            sb.append(staticMetaPropertyName(field));
            sb.append(")");
            sb.append(primitiveNullCheck);
            sb.append(")");
            sb.append("\n\t\t\t");
            sb.append(instance);
            sb.append(".");
            sb.append(setterName(field));
            sb.append("(");
            sb.append(getCast(field, imports));
            sb.append("value);");
            sb.append("\n\t\telse ");
        }
        sb.append("\n\t\t\tsuper.set(propertyName, value);\n\t}\n\n");

        // set
        sb.append("\n\t@Override");
        sb.append("\n\tpublic String getPropertyTypeName(String propertyName) \n\t{\n\t\t");
        for (Field field : declaredFields)
        {
            sb.append("if (propertyName.equals(");
            sb.append(staticMetaPropertyName(field));
            sb.append("))");

            sb.append("\n\t\t\treturn \"");
            sb.append(getType(field));
            sb.append("\";");
            sb.append("\n\t\telse ");
        }
        sb.append("\n\t\t\treturn super.getPropertyTypeName(propertyName);\n\t}\n\n");


        return Option.some(sb.toString());
    }

    private static boolean primitiveNullCheck(Field field)
    {
        return field.getType().isPrimitive();
    }

    private static String getType(Field field)
    {
        return field.getType().getName().replaceAll("\\$", ".");
    }

    private static String getCast(Field field, Set<String> imports)
    {
        if (field.getType().equals(boolean.class))
            return "(" + Boolean.class.getName() + ")";
        else if (field.getType().equals(int.class))
            return "(" + Integer.class.getName() + ")";
        else if (field.getType().equals(long.class))
            return "(" + Long.class.getName() + ")";
        else if (field.getType().equals(double.class))
            return "(" + Double.class.getName() + ")";
        else
            return "(" + addToImportsAndStripPackage(getType(field), imports) + ")";
    }

    public static List<Class> getAllInterfaces(Class<?> type)
    {
        List<Class> classes = new ArrayList<Class>();
        List<Class<?>> declaredInterfaces = Arrays.asList(type.getInterfaces());
        classes.addAll(declaredInterfaces);
        for (Class<?> declaredInterface : declaredInterfaces)
            classes.addAll(getAllInterfaces(declaredInterface));
        if (type.getSuperclass() != null)
            classes.addAll(getAllInterfaces(type.getSuperclass()));
        return classes;
    }

    public static Set<Annotation> getAllAnnotations(Class clas)
    {
        Set<Annotation> annotations = new HashSet<Annotation>();

        for (Class cl = clas; cl != Object.class && cl != null; cl = cl.getSuperclass())
            Collections.addAll(annotations, cl.getAnnotations());
        return annotations;
    }

    public static boolean classHasAnnotation(Class<?> c, Class<? extends Annotation> annotation)
    {
        for (Annotation a : getAllAnnotations(c))
            if (a.annotationType().equals(annotation))
                return true;
        return false;
    }

    public static boolean isGenerateMetaForClass(Class c)
    {
        if (DelegatingMetaEntity.class.isAssignableFrom(c))
            if (isMetaClass(c) && !isMetaDelegate(c))
                if (!classHasAnnotation(c, NoMetaAccess.class))
                    return true;
        return false;
    }

    public static String addToImportsAndStripPackage(String fqn, Set<String> imports)
    {
        if (!fqn.contains("ObjectMeta") && !fqn.startsWith("java.lang"))
            imports.add("import " + fqn + ";\n");

        return fqn.substring(fqn.lastIndexOf('.') + 1);

    }
}

