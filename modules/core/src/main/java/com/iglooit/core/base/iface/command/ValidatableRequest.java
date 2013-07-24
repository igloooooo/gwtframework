package com.iglooit.core.base.iface.command;

import com.iglooit.core.base.iface.domain.Validatable;

import java.util.List;

public interface ValidatableRequest
{
    List<Validatable> getValidatables();
}
