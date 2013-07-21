package com.iglooit.core.iface;

import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.type.UUID;
import com.clarity.core.account.iface.domain.Individual;
import com.clarity.core.account.iface.domain.UserRole;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SecurityServiceAsync
{
    void getActiveParty(AsyncCallback<Option<Individual>> async);

    void getActiveUserId(AsyncCallback<Option<UUID<Individual>>> async);

    void getActiveUserId2(AsyncCallback<Option<UUID<UserRole>>> async);

    void setupHookUserId(Individual p, AsyncCallback<Void> async);

    void teardownHookUserId(AsyncCallback<Void> async);
}
