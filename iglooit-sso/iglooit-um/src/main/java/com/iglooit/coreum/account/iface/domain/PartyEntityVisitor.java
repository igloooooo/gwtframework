package com.iglooit.coreum.account.iface.domain;

public interface PartyEntityVisitor
{
    void visit(Individual individual);

    void visit(Organization organization);
}
