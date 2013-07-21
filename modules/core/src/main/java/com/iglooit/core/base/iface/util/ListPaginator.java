package com.iglooit.core.base.iface.util;

import com.clarity.commons.iface.domain.SearchPaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class ListPaginator
{
    public static <T> ArrayList<T> getPaginatedSublist(SearchPaging searchPaging,
                                                       List<T> unsortedList,
                                                       Comparator<T> comparator)
    {
        ArrayList<T> sortedFullList = new ArrayList<T>(unsortedList);
        Collections.sort(sortedFullList, comparator);
        return getPaginatedSublist(searchPaging, sortedFullList);
    }

    public static <T> ArrayList<T> getPaginatedSublist(SearchPaging searchPaging, List<T> sortedList)
    {
        searchPaging.setTotalHits(sortedList.size());
        searchPaging.setTotalHitsFound(true);

        int firstHit = searchPaging.getFirstHitIndex();
        if (firstHit < 0)
        {
            firstHit = 0;
            searchPaging.setFirstHitIndex(0);
        }
        int lastHit = Math.min(sortedList.size(), firstHit + searchPaging.getPageSize());

        ArrayList<T> sortedSegment = new ArrayList<T>();
        for (int i = firstHit; i < lastHit; i++)
            sortedSegment.add(sortedList.get(i));

        return sortedSegment;
    }
}
