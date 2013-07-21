package com.iglooit.core.expr.iface.domain;

//to be replaced by ExprDef only when it is a database entity and subclass determined by a discriminator
public enum ExprDefType
{
    //common
    ROOT,
    ATTRIB_VALUE,
    NAMED_ATTRIB_VALUE,

    //validation
    EQUALS,
    NOT_EQUALS,
    REGEX_MATCHES,
    NOT_REGEX_MATCHES,
    MANDATORY,
    //validation values
    STRING_VALUE,
    REGEX_VALUE,
    IPV4_VALUE,
    IPV6_VALUE,
    EMAIL_VALUE,
    NUMBER_VALUE,
    CONSTANT_REGEX_VALUE,
    BOOLEAN_VALUE,

    //access
    USER,
    IN,
    NOT_IN,
    OWNINGUSER,
    //access values
    USER_LIST,
    GROUP_LIST,

    //content
    AUTH_USER,
    AUTH_USER_GROUPS,

    //rating
    IF_EXPR,

    //compound boolean
    AND,
    OR,
    NOT;

}
