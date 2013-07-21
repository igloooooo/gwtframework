package com.iglooit.core.lib.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SearchOptions implements Serializable
{

    private static final int DEFAULT_MAX_ROWS = 200;
    private int maxRows = DEFAULT_MAX_ROWS;
    private List<String> sortColumnNames;
    private List<Boolean> sortAscendings;

    public SearchOptions()
    {
        sortColumnNames = new ArrayList<String>();
        sortAscendings = new ArrayList<Boolean>();
    }

    public SearchOptions(int maxRows)
    {
        this.maxRows = maxRows;
        sortColumnNames = new ArrayList<String>();
        sortAscendings = new ArrayList<Boolean>();
    }

    public SearchOptions(int maxRows, String sortColumnName, boolean sortAscending)
    {
        this.maxRows = maxRows;
        sortColumnNames = new ArrayList<String>();
        sortAscendings = new ArrayList<Boolean>();
        sortColumnNames.add(sortColumnName);
        sortAscendings.add(sortAscending);
    }

    public SearchOptions(String sortColumnName, boolean sortAscending)
    {
        sortColumnNames = new ArrayList<String>();
        sortAscendings = new ArrayList<Boolean>();
        sortColumnNames.add(sortColumnName);
        sortAscendings.add(sortAscending);
    }

    public SearchOptions(List<String> sortColumnNames, List<Boolean> sortAscendings)
    {
        this.sortAscendings = sortAscendings;
        this.sortColumnNames = sortColumnNames;
    }

    public SearchOptions(int maxRows, List<String> sortColumnNames, List<Boolean> sortAscendings)
    {
        this.maxRows = maxRows;
        this.sortAscendings = sortAscendings;
        this.sortColumnNames = sortColumnNames;
    }

    public SearchOptions(List<String> sortColumnNames)
    {
        this.sortColumnNames = sortColumnNames;

        this.sortAscendings = new ArrayList<Boolean>();

        for (int i = 0; i < sortColumnNames.size(); i++)
        {
            this.sortAscendings.add(Boolean.TRUE);
        }
    }

    public int getMaxRows()
    {
        return maxRows;
    }

    public void setMaxRows(int maxRows)
    {
        this.maxRows = maxRows;
    }

    public boolean isSortAscending()
    {
        return isSortAscending(0);
    }

    public boolean isSortAscending(int index)
    {
        boolean result = true;
        if (index < sortAscendings.size())
            result = sortAscendings.get(index).booleanValue();
        return result;
    }

    public void addSearchOptions(String sortColumnName, boolean sortAscending)
    {
        addSortAscending(sortAscending);
        addSortColumnName(sortColumnName);
    }

    public void addSortAscending(boolean sortAscending)
    {
        sortAscendings.add(sortAscending);
    }

    public void setSortAscending(boolean sortAscending)
    {
        setSortAscending(0, sortAscending);
    }

    public void setSortAscending(int index, boolean sortAscending)
    {
        if (index < sortAscendings.size())
            sortAscendings.set(index, sortAscending);
        else
            addSortAscending(sortAscending);
    }

    public String getSortColumnName()
    {
        return getSortColumnName(0);
    }

    public String getSortColumnName(int index)
    {
        String result = "";
        if (index < sortColumnNames.size())
            result = sortColumnNames.get(index);
        return result;
    }

    private void addSortColumnName(String sortColumnName)
    {
        sortColumnNames.add(sortColumnName);
    }

    public void setSortColumnNames(List<String> sortColumnNames)
    {
        this.sortColumnNames = sortColumnNames;

        // Default the direction to ascending if the directions are out of synch
        if (this.sortColumnNames.size() != this.sortAscendings.size())
        {

            this.sortAscendings = new ArrayList<Boolean>();

            for (int i = 0; i < sortColumnNames.size(); i++)
            {
                this.sortAscendings.add(Boolean.TRUE);
            }
        }
    }

    public List<Boolean> getSortAscendings()
    {
        return sortAscendings;
    }

    public List<String> getSortColumnNames()
    {
        return sortColumnNames;
    }

}
