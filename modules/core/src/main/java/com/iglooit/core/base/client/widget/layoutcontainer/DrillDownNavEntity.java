package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.commons.iface.type.NonSerOpt;
import com.clarity.commons.iface.type.Tuple2;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;

import java.util.ArrayList;
import java.util.List;

public class DrillDownNavEntity implements Breadcrumb<DrillDownNavEntity>
{
    private String nodeName;
    private String nodeType;
    private String displayName;
    private String entityId;
    private List<Tuple2<String, String>> attributes;
    private NonSerOpt<DrillDownNavEntity> parent;
    private Anchor anchor;
    private AbstractImagePrototype anchorSeparator;

    public DrillDownNavEntity(String entityId, String nodeName, String nodeType,
                              NonSerOpt<DrillDownNavEntity> parentDrillDownEntity)
    {
        this.entityId = entityId;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.displayName = nodeName + " (" + nodeType + ")";
        this.anchor = new Anchor(displayName);
        this.anchorSeparator = Resource.ICONS_SIMPLE.arrowPathRight();
        this.anchor.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                doOnClick();
            }
        });

        this.parent = parentDrillDownEntity;
        this.attributes = new ArrayList<Tuple2<String, String>>();
    }

    /**
     * Override this method when you want to do something once the anchor is clicked
     */
    public void doOnClick()
    {

    }

    public void addAttribute(String key, String value)
    {
        attributes.add(new Tuple2<String, String>(key, value));
    }

    public void removeAnchorSeparator()
    {
        this.anchorSeparator = null;
    }

    public void setAnchorSeparator(AbstractImagePrototype anchorSeparator)
    {
        this.anchorSeparator = anchorSeparator;
    }

    public NonSerOpt<DrillDownNavEntity> getParent()
    {
        return parent;
    }

    public void setParent(NonSerOpt<DrillDownNavEntity> parent)
    {
        this.parent = parent;
    }

    public Anchor getAnchor()
    {
        return anchor;
    }

    public AbstractImagePrototype getAnchorSeparator()
    {
        return anchorSeparator;
    }

    public String getEntityId()
    {
        return entityId;
    }

    @Override
    public String getText()
    {
        return anchor.getText();
    }

    public String getNodeName()
    {
        return nodeName;
    }

    public void setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
    }

    public String getNodeType()
    {
        return nodeType;
    }

    public void setNodeType(String nodeType)
    {
        this.nodeType = nodeType;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public List<Tuple2<String, String>> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(List<Tuple2<String, String>> attributes)
    {
        this.attributes = attributes;
    }

    /**
     * This enum is used to define "attributes" that each entity can have
     * e.g: some entity might have expanded nodes
     * POSITION, COLOR, COLLAPSED are potential attributes in future
     */
    public enum EntityAttribute
    {
        EXPANDED,
        COLLAPSED
    }
}
