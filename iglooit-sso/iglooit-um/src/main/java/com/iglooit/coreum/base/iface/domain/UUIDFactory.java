package com.iglooit.coreum.base.iface.domain;

import com.iglooit.commons.iface.type.UUID;

public interface UUIDFactory
{
    <DE extends JpaDomainEntity> UUID<DE> generate();
}
