package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.mvp.Display;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Panel that implements the 'Master-Detail' UI patteen.
 * <p/>
 * Position of master panel can be set to the top, right, bottom or left side of the layout. The detail panel
 * is always positions in the center (via BorderLayout)
 */
public class MasterDetailDisplay extends GPanel implements Display
{
    private ContentPanel master;
    private LayoutContainer detail;
    private MasterPosition masterPosition;
    private BorderLayoutData masterLayoutData;

    public MasterDetailDisplay()
    {
        this(MasterPosition.TOP);
    }

    public MasterDetailDisplay(MasterPosition masterPosition)
    {
        this(masterPosition, null, null);
    }

    public MasterDetailDisplay(MasterPosition masterPosition, Margins masterMargins, Margins detailMargins)
    {
        setLayout(new BorderLayout());
        // fix borderlayout positioning issues
        setStyleAttribute("position", "relative");
        this.masterPosition = masterPosition;

        switch (masterPosition)
        {
            case TOP:
                masterLayoutData = new BorderLayoutData(Style.LayoutRegion.NORTH);
                masterLayoutData.setMargins(masterMargins == null ? new Margins(0, 0, 5, 0) : masterMargins);
                break;
            case RIGHT:
                masterLayoutData = new BorderLayoutData(Style.LayoutRegion.EAST);
                masterLayoutData.setMargins(masterMargins == null ? new Margins(0, 0, 0, 5) : masterMargins);
                break;
            case BOTTOM:
                masterLayoutData = new BorderLayoutData(Style.LayoutRegion.SOUTH);
                masterLayoutData.setMargins(masterMargins == null ? new Margins(5, 0, 0, 0) : masterMargins);
                break;
            case LEFT:
                masterLayoutData = new BorderLayoutData(Style.LayoutRegion.WEST);
                masterLayoutData.setMargins(masterMargins == null ? new Margins(0, 5, 0, 0) : masterMargins);
                break;
            default:
                break;
        }
        master = new ContentPanel(new FitLayout());
        master.setHeaderVisible(false);
        master.setBorders(false);
        master.setBodyBorder(false);
        add(master, masterLayoutData);

        detail = new LayoutContainer(new FitLayout());
        BorderLayoutData centerData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        centerData.setMargins(detailMargins == null ? new Margins(0, 0, 0, 0) : detailMargins);
        add(detail, centerData);
    }

    public void setMaster(Widget widget)
    {
        master.removeAll();
        master.add(widget);
    }

    public void setDetail(Widget widget)
    {
        detail.removeAll();
        detail.add(widget);
    }

    public void setMasterSize(float size)
    {
        masterLayoutData.setSize(size);
    }

    public float getMasterSize()
    {
        return masterLayoutData.getSize();
    }

    public void setMasterMaxSize(int maxSize)
    {
        masterLayoutData.setMaxSize(maxSize);

    }

    public void setMasterMinSize(int minSize)
    {
        masterLayoutData.setMinSize(minSize);
    }

    public void setSplit(boolean split)
    {
        masterLayoutData.setSplit(split);
    }

    public void setMasterVisible(boolean isVisible)
    {
        master.setVisible(isVisible);
    }

    public void setCollapsible(boolean collapsible)
    {
        master.setHeaderVisible(true);
        masterLayoutData.setCollapsible(collapsible);
//        masterLayoutData.setHideCollapseTool(!collapsible);
    }

    public ContentPanel getMasterContainer()
    {
        return master;
    }

    public LayoutContainer getDetailContainer()
    {
        return detail;
    }

    public Widget asWidget()
    {
        return null;
    }

    public void doOnRender(Element element, int i)
    {

    }

    public String getLabel()
    {
        return null;
    }

    public static enum MasterPosition
    {
        TOP, RIGHT, BOTTOM, LEFT;
    }
}
