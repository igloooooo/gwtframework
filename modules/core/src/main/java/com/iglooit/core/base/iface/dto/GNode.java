package com.iglooit.core.base.iface.dto;

import com.clarity.commons.iface.type.UUID;

import java.util.ArrayList;
import java.util.List;

public abstract class GNode extends GObject
{
    private List<GNode> children = new ArrayList<GNode>();
    private GNode parent;
    private long version;
    private boolean topLevelElement;

    private String graphName;
    private UUID graphId;


    public GNode()
    {

    }

    protected GNode(UUID id, boolean dirty, String name, String type, boolean editable, boolean mandatory)
    {
        super(id, dirty, name, type, editable, mandatory);

    }

    public List<GNode> getChildren()
    {
        return children;
    }

    public void setChildren(List<GNode> children)
    {
        this.children = children;
    }

    public GNode getParent()
    {
        return parent;
    }

    public void setParent(GNode parent)
    {
        this.parent = parent;
    }

    public int getChildrenSize()
    {
        if (children == null)
            return 0;
        else
            return children.size();
    }

    public long getVersion()
    {
        return version;
    }

    public void setVersion(long version)
    {
        this.version = version;
    }

    public boolean isTopLevelElement()
    {
        return topLevelElement;
    }

    public void setTopLevelElement(boolean topLevelElement)
    {
        this.topLevelElement = topLevelElement;
    }

    public String getGraphName()
    {
        return graphName;
    }

    public void setGraphName(String graphName)
    {
        this.graphName = graphName;
    }
}
