package com.iglooit.core.base.iface.domain;

import com.clarity.commons.iface.type.UUID;

public interface UUIDFactory
{
    <DE extends JpaDomainEntity> UUID<DE> generate();
}
