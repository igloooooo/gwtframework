package com.iglooit.commons.iface.type;

/**
 * A version of the option class that does not expect a serializable type.
 * @param <T>
 */
public abstract class NonSerOpt<T>
{
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

    static class None<T> extends NonSerOpt<T>
    {
        public T value()
        {
            throw new AppX("value() called on a NonSerOpt.None");
        }
    }

    static class Some<T> extends NonSerOpt<T>
    {
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

    public static <T> NonSerOpt<T> some(T t)
    {
        if (t == null)
            throw new AppX("NonSerOpt.some() may not be called with a null argument");

        return new Some<T>(t);
    }

    public static <T> NonSerOpt<T> none()
    {
        return new None<T>();
    }

    public static <T> NonSerOpt<T> option(T t)
    {
        if (t == null)
            return new None<T>();
        else
            return new Some<T>(t);
    }

    public static <T> T orDefault(NonSerOpt<T> nonSerOpt, T defaultValue)
    {
        if (nonSerOpt instanceof Some)
            return nonSerOpt.value();
        else
            return defaultValue;
    }
}


