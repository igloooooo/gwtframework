package com.iglooit.core.base.client.view.mxgraph.demo;

import com.clarity.core.base.client.view.mxgraph.Cell;
import com.clarity.core.base.client.view.mxgraph.Edge;
import com.clarity.core.base.client.view.mxgraph.Event;
import com.clarity.core.base.client.view.mxgraph.EventData;
import com.clarity.core.base.client.view.mxgraph.Graph;
import com.clarity.core.base.client.view.mxgraph.Group;
import com.clarity.core.base.client.view.mxgraph.MXGraphObject;
import com.clarity.core.base.client.view.mxgraph.Vertex;
import com.clarity.core.base.client.view.mxgraph.layout.CircleLayout;
import com.clarity.core.base.client.view.mxgraph.layout.OrganicLayout;
import com.clarity.core.base.client.view.mxgraph.layout.StackLayout;
import com.clarity.core.base.client.view.mxgraph.overlay.ImageCellOverlay;
import com.clarity.core.base.client.view.mxgraph.style.LineStyle;
import com.clarity.core.base.client.view.mxgraph.style.StyleConstants;
import com.clarity.core.base.client.view.mxgraph.style.VertexStyle;
import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

import java.util.ArrayList;

public class MXGraphTest extends GPanel
{
    public void doOnRender(Element element, int i)
    {
        final Graph graph = new Graph();

//        setLayout(new BorderLayout());

        LayoutContainer hlayout = new LayoutContainer(new RowLayout(Style.Orientation.HORIZONTAL));
        hlayout.setHeight(60);
        hlayout.add(new Button("hello"));

//        add(hlayout, new BorderLayoutData(Style.LayoutRegion.NORTH, 0.05f));
//        add(graph, new BorderLayoutData(Style.LayoutRegion.CENTER));

        add(hlayout);
        add(graph);

        layout();
        graph.bindGraph();


        final StackLayout layout = new StackLayout(graph);
        layout.setSpacing(40);
        final OrganicLayout oLayout = new OrganicLayout(graph);
        final CircleLayout cLayout = new CircleLayout(graph);

        hlayout.add(new Button("setLabels", new SelectionListener<ButtonEvent>()
        {
            public void componentSelected(ButtonEvent ce)
            {
                for (int i = 0; i < graph.getCellCount(); i++)
                {
                    Cell cell = graph.getCellAt(i);
                    //
                    // cell.setLabel(cell.getCaption() + ".....");
                }
            }
        }));

        hlayout.add(new Button("Circle Layout", new SelectionListener<ButtonEvent>()
        {
            public void componentSelected(ButtonEvent buttonvent)
            {
                cLayout.execute();
            }
        }));
        hlayout.add(new Button("Organic Layout", new SelectionListener<ButtonEvent>()
        {
            public void componentSelected(ButtonEvent buttonvent)
            {
                oLayout.execute();
            }
        }));
        hlayout.add(new Button("Stack Layout", new SelectionListener<ButtonEvent>()
        {
            public void componentSelected(ButtonEvent buttonvent)
            {
                layout.execute();
            }
        }));

        //Add a dotted line style
        LineStyle dottedLine = new LineStyle("dashedEdge");
        dottedLine.addElement(StyleConstants.STYLE_EDGE, "mxEdgeStyle.ElbowConnector");
        dottedLine.addElement(StyleConstants.STYLE_ENDARROW, StyleConstants.ARROW_BLOCK);
//        dottedLine.addElement(StyleConstants.STYLE_ROUNDED, true);
        dottedLine.addElement(StyleConstants.STYLE_FONTCOLOR, "green");
        dottedLine.addElement(StyleConstants.STYLE_STROKECOLOR, "black");
//        dottedLine.addElement(StyleConstants.STYLE_DASHED, true);
        //graph.registerStyle(dottedLine);

        //Add a different vertex style
        VertexStyle boringBox = new VertexStyle("boringBox", graph);
        boringBox.addElement(StyleConstants.STYLE_SHAPE, StyleConstants.SHAPE_RECTANGLE);
//        boringBox.addElement(StyleConstants.STYLE_ROUNDED, false);
        boringBox.addElement(StyleConstants.STYLE_VERTICAL_LABEL_POSITION, StyleConstants.ALIGN_BOTTOM);
        boringBox.addElement(StyleConstants.STYLE_FILLCOLOR, "#1C06F5");
        boringBox.addElement(StyleConstants.STYLE_IMAGE, "#1C06F5");
        //graph.registerStyle(boringBox);

        //Add a different vertex style
        VertexStyle coolBox = new VertexStyle("coolBox", graph);
        coolBox.addElement(StyleConstants.STYLE_SHAPE, StyleConstants.SHAPE_IMAGE);
        coolBox.addElement(StyleConstants.STYLE_IMAGE_ALIGN, StyleConstants.ALIGN_CENTER);
        coolBox.addElement(StyleConstants.STYLE_IMAGE_VERTICAL_ALIGN, StyleConstants.ALIGN_MIDDLE);
//        coolBox.addElement(StyleConstants.STYLE_ROUNDED, false);
        coolBox.addElement(StyleConstants.STYLE_VERTICAL_LABEL_POSITION, StyleConstants.ALIGN_BOTTOM);
        coolBox.addElement(StyleConstants.STYLE_FILLCOLOR, "#1C06F5");
        coolBox.addElement(StyleConstants.STYLE_IMAGE, "images/icons/earth.png");
        // graph.registerStyle(coolBox);

        graph.beginUpdate();

        //Basic vertices
        Vertex v1 = graph.addVertex(new Vertex(20, 60, 60, 40, "treeRoot", "Root"));
        Vertex v2 = graph.addVertex(new Vertex(100, 60, 60, 40, "treeRoot2", "Node 2"));

        //Edge
        Edge e1 = graph.addEdge(new Edge(v1, v2, "Test"));

        e1.addEvent(Event.DOUBLE_CLICK_EVENT_TYPE, new Event()
        {
            public void onEvent(MXGraphObject e, EventData eventData)
            {
                GWT.log("This is a test of an edge event", null);
            }
        });

        //Grouping
        Vertex v3 = graph.addVertex(new Vertex(50, 80, 30, 30, "treeRoot", "Node"));
        Vertex v4 = graph.addVertex(new Vertex(50, 120, 30, 20, "treeRoot2", "Node"));


        v4.addEvent(Event.DOUBLE_CLICK_EVENT_TYPE, new Event()
        {
            public void onEvent(MXGraphObject e, EventData eventData)
            {
                GWT.log("This is a test of a vertex click event", null);
            }
        });

        ArrayList<Cell> vertices = new ArrayList<Cell>();
        vertices.add(v3);
        vertices.add(v4);


        Group g1 = new Group("Group Caption");
        g1.setWidth(150);
        g1.setHeight(90);
        g1 = graph.addGroup(g1);

        g1.addCell(v1);
        g1.addCell(v2);

        Vertex v5 = new Vertex(200, 260, 60, 40, "treeRoot3", "More Nodes");
        v5.setStyle(boringBox);
        v5 = graph.addVertex(v5);

        Vertex v6 = new Vertex(230, 260, 60, 40, "treeRoot3", "Image Node");
        v6.setStyle(coolBox);
        v6 = graph.addVertex(v6);

        final Group g2 = new Group("outer Group");
        g2.setWidth(300);
        g2.setHeight(300);
        graph.addGroup(g2);

        g2.addCell(v3);
        g2.addCell(v4);
        g2.addCell(new Vertex(50, 5, 20, 20, "layout1", "Layout 1"));
        g2.addCell(new Vertex(80, 5, 20, 20, "layout2", "Layout 2"));
        g2.addCell(new Vertex(50, 40, 60, 40, "layout3", "Layout 3"));

        Group group3 = new Group("Sub grouper");
        group3.setWidth(250);
        group3.setHeight(190);
        graph.addGroup(group3);
        group3.setStyle(boringBox);

        group3.addOverlay(ImageCellOverlay.create("/images/icons/check.png", 16, 16, "Overlay tooltip"));

        Group group4 = new Group("Sub group 1");
        group4.setWidth(100);
        group4.setHeight(100);
        group4.setY(15);
        graph.addGroup(group4);

        Group group5 = new Group("Sub group 2");
        group5.setWidth(100);
        group5.setHeight(100);
        group5.setX(105);
        group5.setY(15);
        graph.addGroup(group5);

        group4.addCell(new Vertex(10, 20, 20, 20, "V1", "V1"));
        group4.addCell(new Vertex(30, 20, 20, 20, "V2", "V2"));

        group5.addCell(new Vertex(10, 20, 20, 20, "V3", "V3"));
        group5.addCell(new Vertex(30, 20, 20, 20, "V4", "V4"));

        group3.addCell(group4);
        group3.addCell(group5);


        //Edge connected from vertex to group
        Edge e2 = new Edge(v2, g2, "Group Link");
        e2.setStyle(dottedLine);
        e2 = graph.addEdge(e2);


        //Don't know why this is needed at the moment..layout related?  FIXME
        g2.pushGeometry();
        group3.pushGeometry();
        group4.pushGeometry();
        group5.pushGeometry();

        //Execute the stack layout        
        layout.execute();

        //Execute the cell circle layout
        CircleLayout icclayout = new CircleLayout(graph);
        icclayout.setStartCell(g2);
        icclayout.execute();

        graph.endUpdate();

        //Lock graph
        //graph.lockGraph();

        //Pan the first group to the centre
        graph.panToCell(g1, true);

        final CircleLayout cclayout = new CircleLayout(graph);
        hlayout.add(new Button("Circle Layout on a group node", new SelectionListener<ButtonEvent>()
        {
            public void componentSelected(ButtonEvent buttonvent)
            {
                cclayout.setStartCell(g2);
                cclayout.execute();
            }
        }));

        hlayout.add(new Button("+", new SelectionListener<ButtonEvent>()
        {
            public void componentSelected(ButtonEvent buttonvent)
            {
                graph.zoomIn();
            }
        }));

        hlayout.add(new Button("-", new SelectionListener<ButtonEvent>()
        {
            public void componentSelected(ButtonEvent buttonvent)
            {
                graph.zoomOut();
            }
        }));

        hlayout.add(new Button("Lock Graph", new SelectionListener<ButtonEvent>()
        {
            public void componentSelected(ButtonEvent buttonvent)
            {
                graph.lockGraph();
            }
        }));

        hlayout.add(new Button("UnLock Graph", new SelectionListener<ButtonEvent>()
        {
            public void componentSelected(ButtonEvent buttonvent)
            {
                graph.unlockGraph();
            }
        }));

        hlayout.layout(true);
        layout(true);
    }

    public String getLabel()
    {
        return "MX Graph Test";
    }
}
