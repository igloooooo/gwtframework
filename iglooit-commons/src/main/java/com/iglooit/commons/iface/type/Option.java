package com.iglooit.commons.iface.type;

import java.io.Serializable;

public abstract class Option<T> implements Serializable
{
    public Option()
    {
        //unfortunately need this to be able to serialize
    }

    public boolean isSome()
    {
        return this instanceof Some;
    }

    public abstract T value();

    public boolean isNone()
    {
        return this instanceof None;
    }

    public T valueOrDefault(T defaultValue)
    {
        if (this instanceof Some)
            return value();
        else
            return (T)defaultValue;
    }

    public static <T> Option<T> createFromImplicitlySerializable(T nonSerOpt)
    {
        if (nonSerOpt instanceof Serializable)
        {
            return (Option<T>)Option.option((Serializable)nonSerOpt);
        }
        else throw new AppX("Failed attempt to convert non-serializable object to serializable");
    }

    static class None<T extends Serializable> extends Option<T> implements Serializable
    {
        public None()
        {
            //do not call - this is so it is serializable for gwt
        }

        public T value()
        {
            throw new AppX("value() called on a Option.None");
        }
    }

    static class Some<T extends Serializable> extends Option<T>
    {
        public Some()
        {
            //do not call - this is so its serializable for gwt
        }

        private T value;

        public Some(T t)
        {
            value = t;
        }

        public T value()
        {
            return value;
        }
    }

    public static <T extends Serializable> Option<T> some(T t)
    {
        if (t == null)
            throw new AppX("Option.some() may not be called with a null argument");

        return new Some<T>(t);
    }

    public static <T extends Serializable> Option<T> none()
    {
        return new None<T>();
    }

    public static <T extends Serializable> Option<T> option(T t)
    {
        if (t == null)
            return new None<T>();
        else
            return new Some<T>(t);
    }

    public static <T> T orDefault(Option<T> option, T defaultValue)
    {
        if (option instanceof Some)
            return option.value();
        else
            return defaultValue;
    }

    public boolean equals(Object other)
    {
        if (this instanceof None && other instanceof None)
            return true;
        if (this instanceof None || value() == null)
            return false;
        return value().equals(((Option<T>)other).value());
    }

    public int hashCode()
    {
        return 0;
    }
}
