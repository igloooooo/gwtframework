package com.iglooit.commons.iface.type;

public class AppX extends RuntimeException
{
    //unfortunately GWT serialization requires that there is a _public_ default constructor
    //makes it hard to enforce the rule that all exception constructors should contain an error
    //message...
    public AppX()
    {
    }

    public AppX(String message)
    {
        super(message);
    }

    public AppX(String message, Throwable cause)
    {
        super(message, cause);
    }
}
