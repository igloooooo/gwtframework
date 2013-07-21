package com.iglooit.core.base.iface.command;

import com.clarity.commons.iface.type.AppX;

public class UpdateRequest<ResponseType extends Response> extends Request<ResponseType>
{
    private OpType opType;
    public static final String INSERT_STRING = "Insert";
    public static final String UPDATE_STRING = "Update";
    public static final String DELETE_STRING = "Delete";


    public UpdateRequest(OpType opType)
    {
        this.opType = opType;
    }

    public UpdateRequest()
    {
    }

    public boolean isUpdate()
    {
        return OpType.UPDATE.equals(opType);
    }

    public boolean isInsert()
    {
        return OpType.INSERT.equals(opType);
    }

    public boolean isDelete()
    {
        return OpType.DELETE.equals(opType);
    }

    public OpType getOpType()
    {
        return opType;
    }

    public String getOpTypeString()
    {
        switch (opType)
        {
            case INSERT:
                return INSERT_STRING;
            case DELETE:
                return DELETE_STRING;
            case UPDATE:
                return UPDATE_STRING;
            default:
                throw new AppX("Option Type is not set");
        }
    }

    public enum OpType
    {
        INSERT, UPDATE, DELETE
    }

}
