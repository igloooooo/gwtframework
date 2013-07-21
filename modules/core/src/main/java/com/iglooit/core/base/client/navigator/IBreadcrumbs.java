package com.iglooit.core.base.client.navigator;

import java.util.Iterator;

public interface IBreadcrumbs
{
    void add(SimpleBreadcrumb breadcrumb);

    int size();

    Iterator<SimpleBreadcrumb> iterator();

    SimpleBreadcrumb truncate(int breadcrumbsIndex);

    void clear();

    // Not all implementations will support this method - only those that put breadcrumbs in the URL
    String toFragmentIdentifier();

    // Not all implementations will support this method - only those that put breadcrumbs in the URL.
    void fromFragmentIdentifier(String fragmentIdentifier);
}
