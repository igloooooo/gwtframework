package com.iglooit.core.base.client.widget.layoutcontainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BreadcrumbTrail<T extends Breadcrumb<T>>
{
    private List<T> breadcrumbList;

    public BreadcrumbTrail()
    {
        this.breadcrumbList = new ArrayList<T>();
    }

    public void add(T t)
    {
        breadcrumbList.add(t);
    }

    public T getBreadcrumb(String breadcrumbString)
    {
        for (T t : breadcrumbList)
        {
            if (t.getText().trim().equalsIgnoreCase(breadcrumbString.trim()))
            {
                return t;
            }
        }
        return null;
    }

    public T getLast()
    {
        return breadcrumbList.get(breadcrumbList.size() - 1);
    }

    public List<T> getBreadcrumbsInOrder(final T lastBreadcrumb)
    {
        List<T> breadcrumbListInOrder = new ArrayList<T>();

        if (lastBreadcrumb == null)
        {
            return breadcrumbListInOrder;
        }

        breadcrumbListInOrder.add(lastBreadcrumb);

        T parentBreadcrumb = lastBreadcrumb;

        while (parentBreadcrumb.getParent().isSome())
        {
            parentBreadcrumb = parentBreadcrumb.getParent().value();
            breadcrumbListInOrder.add(parentBreadcrumb);
        }

        Collections.reverse(breadcrumbListInOrder);

        return breadcrumbListInOrder;
    }

    /**
     * This method will remove any other breadcrumbs after the selectedBreadcrumb
     *
     * @param selectedBreadcrumb
     */
    public void rearrangeBreadcrumbsInOrder(final T selectedBreadcrumb)
    {
        clear();
        add(selectedBreadcrumb);
        T parentEntity = selectedBreadcrumb;
        boolean parentExist = selectedBreadcrumb.getParent().isSome();

        if (parentExist)
        {
            parentEntity = selectedBreadcrumb.getParent().value();
        }

        while (parentExist)
        {
            add(parentEntity);
            parentExist = parentEntity.getParent().isSome();
            if (parentExist)
            {
                parentEntity = parentEntity.getParent().value();
            }
        }

        reverseBreadcrumbList();
    }

    /**
     * This is used when user click the backbutton to traverse through the breadcrumbs
     *
     * @return
     */
    public T getSecondLastBreadcrumb()
    {
        int noOfBreadcrumbs = breadcrumbList.size();

        if (noOfBreadcrumbs >= 2)
        {
            return breadcrumbList.get(noOfBreadcrumbs - 2);
        }
        else return breadcrumbList.get(0);
    }

    public void reverseBreadcrumbList()
    {
        Collections.reverse(breadcrumbList);
    }

    public List<T> getBreadcrumbList()
    {
        return breadcrumbList;
    }

    public void clear()
    {
        breadcrumbList.clear();
    }
}

