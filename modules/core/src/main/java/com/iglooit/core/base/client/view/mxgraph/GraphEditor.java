package com.iglooit.core.base.client.view.mxgraph;

import com.clarity.commons.iface.type.AppX;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

public class GraphEditor extends Graph
{
    private int x = 100;
    private int y = 100;

    private JavaScriptObject editorObject;
    private ConnectionCallback connectionCallback;
    private boolean suspendCellConnections;

    private String holderSource = null;
    private EdgeFactory edgeFactory = new DefaultEdgeFactory();

    public GraphEditor()
    {
        super();
    }

    public boolean isSuspendCellConnections()
    {
        return suspendCellConnections;
    }

    public void setSuspendCellConnections(boolean suspendCellConnections)
    {
        this.suspendCellConnections = suspendCellConnections;
    }

    public void setConnectionCallback(ConnectionCallback connectionCallback)
    {
        this.connectionCallback = connectionCallback;
    }

    public void setEdgeFactory(EdgeFactory edgeFactory)
    {
        this.edgeFactory = edgeFactory;
    }

    private void handleAddVertex(JavaScriptObject vertex)
    {
        GWT.log("Added vertex", null);
    }

    public void addCellListener()
    {
        addCellListener(this, getGraphObject());
        GWT.log("Added cell listener..", null);
    }

    private native void addCellListener(GraphEditor e, JavaScriptObject graphObject)
        /*-{
           graphObject.addListener($wnd.mxEvent.ADD_VERTEX,
             function(sender, event)
             {
             var vertex = event.getProperty("vertex");
            e.
 @com.clarity.core.base.client.view.mxgraph.GraphEditor::handleAddVertex(Lcom/google/gwt/core/client/JavaScriptObject;)
     (vertex);
             });
        }-*/;

    public Vertex addVertex(String name)
    {

        beginUpdate();
        Vertex v = new Vertex(x, y, 60, 60, name, name);
        super.addVertex(v);

        GWT.log("Added vertex, code", null);
        x += 50;
        y += 50;

        //panToCell(v, true);

        endUpdate();
//        refresh();

        return v;
    }

    private void handleCellConnected(String source, JavaScriptObject edge, boolean isSource)
    {
        if (!suspendCellConnections)
        {
            if (holderSource == null)
            {
                holderSource = source;
            }
            else
            {
                GWT.log("Connected " + holderSource + ", is source: " + isSource, null);
                GWT.log("Edge: " + edge);

                Cell vStart = getCellByName(source);
                Cell vEnd = getCellByName(holderSource);

                Edge newEdge = edgeFactory.createEdge(vStart, vEnd);

                newEdge.setBrowserObject(edge);
                newEdge.setMemberGraph(this);
                setUUID(newEdge.getBrowserObject(), newEdge.getUUID().toString());

                addEdgeInternal(newEdge);

                String tmpSource = holderSource;
                holderSource = null;

                if (connectionCallback != null) connectionCallback.cellsConnected(tmpSource, source, newEdge);
            }
        }
    }

    public void handleCellMoved(String cell)
    {
        MXGraphObject eventObject = getObjectEventHash().get(cell);


        if (eventObject != null)
        {
            //Sync the location, to make it available to our users.
            ((Cell)eventObject).syncGeometry();
            eventObject.executeEvent(Event.CELL_MOVED_EVENT_TYPE);
        }
    }

    protected native JavaScriptObject setUUID(JavaScriptObject edge, String uuid)  /*-{
         edge.UUID = uuid;
    }-*/;


    public void handleDeletePressed(String cellId)
    {
        GWT.log("Delete pressed on " + cellId);
        MXGraphObject graphObject = getObjectEventHash().get(cellId);

        if (graphObject != null)
        {
            if (graphObject instanceof Edge)
            {
                GWT.log("Deleting edge");


                if (graphObject != null)
                {
                    graphObject.executeEvent(Event.EDGE_DELETED_EVENT_TYPE);
                }
                removeCell((Edge)graphObject);
            }
            else
            {
                GWT.log("Deleting vertex");
                if (graphObject != null)
                {
                    graphObject.executeEvent(Event.VERTEX_DELETED_EVENT_TYPE);
                }
                removeCell((Vertex)graphObject);
            }
        }
        else
        {
            throw new AppX("Could not find object to delete");
        }
    }

    public void deleteSelectedCells()
    {
        deleteSelectedCells(this, getGraphObject());
    }

    protected native void deleteSelectedCells(GraphEditor c, JavaScriptObject graph) /*-{
        var cells = graph.getSelectionCells();
        for (var i = 0; i < cells.length; i++)
        {
            c.@com.clarity.core.base.client.view.mxgraph.GraphEditor::handleDeletePressed(Ljava/lang/String;)
                (cells[i].UUID);
        }


    }-*/;


    @Override
    protected native JavaScriptObject runController(Graph c, Element container) /*-{

        if ($wnd.mxClient.IS_IE)
        {
              new $wnd.mxDivResizer(container);
        }

        var editor = new $wnd.mxEditor();
        var graph = editor.graph;
        editor.setGraphContainer(container);

        graph.getView().updateStyle = true;

        graph.setAutoSizeCells(true);
        graph.setPanning(true);
        graph.panningHandler.useLeftButtonForPanning = true;
        graph.setAllowDanglingEdges(false);
        graph.setAllowLoops(false);
        graph.dblClick = function(evt, cell) {
            if(!graph.isEnabled()) return;
            c.@com.clarity.core.base.client.view.mxgraph.Graph::doubleclickCallback(Ljava/lang/String;)(cell.UUID);
        };

        var rubberband = new $wnd.mxRubberband(graph);
        graph.addListener($wnd.mxEvent.CELLS_FOLDED,
              function(sender, event)
              {
                  var collapse = event.getProperty("collapse");
                  var cells = event.getProperty("cells");
                   for (m in event.getProperty("cells"))
                   {
                        var cell = cells[m];
                        c.@com.clarity.core.base.client.view.mxgraph.Graph::handleCellFolded(Ljava/lang/String;Z)
                                (cell.UUID, collapse);
                   }              });

        graph.addListener($wnd.mxEvent.CELL_CONNECTED,
              function(sender, event)
              {
                 var source = event.getProperty("source");
                 var terminal = event.getProperty("terminal");
                 var edge = event.getProperty("edge");

                 var terminalName = terminal.id;
                 c.@com.clarity.core.base.client.view.mxgraph.GraphEditor::handleCellConnected(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;Z)(terminalName, edge, source);
              });

        graph.addListener($wnd.mxEvent.MOVE_CELLS,
            function(sender, event)
            {
               var obj = graph.getSelectionCell();
               var UUID = obj.UUID;
               c.@com.clarity.core.base.client.view.mxgraph.GraphEditor::handleCellMoved(Ljava/lang/String;)(UUID);
            });

        graph.addListener($wnd.mxEvent.CLICK,
              function(sender, event)
              {
                c.@com.clarity.core.base.client.view.mxgraph.GraphEditor::focus()();
              });

        $wnd.mxEvent.addMouseWheelListener(function (evt, up)
        {
            if (c.@com.clarity.core.base.client.view.mxgraph.Graph::isVisible()())
            {
               if (!$wnd.mxEvent.isConsumed(evt))
               {
                   c.@com.clarity.core.base.client.view.mxgraph.Graph::mouseWheelCallback(Z)(up);
                   $wnd.mxEvent.consume(evt);
               }
            }
        });

        //removing annoying tooltips from appearing when mouse is not inside the graph
        $wnd.mxEvent.addListener(container, 'mouseout', $wnd.mxUtils.bind(graph, function (evt){
            var handler = graph.tooltipHandler;
            if(handler != null) handler.hide();
        }));

        //Define a vertex style
        var style = new Object();
        style[$wnd.mxConstants.STYLE_SHAPE] = $wnd.mxConstants.SHAPE_RECTANGLE;
        style[$wnd.mxConstants.STYLE_PERIMETER] = $wnd.mxPerimeter.RectanglePerimeter;
        style[$wnd.mxConstants.STYLE_ALIGN] = $wnd.mxConstants.ALIGN_CENTER;
        //style[$wnd.mxConstants.STYLE_VERTICAL_ALIGN] = $wnd.mxConstants.ALIGN_MIDDLE;
        style[$wnd.mxConstants.STYLE_GRADIENTCOLOR] = '#AAAAAA';
        style[$wnd.mxConstants.STYLE_FILLCOLOR] = '#DDDDDD';
        style[$wnd.mxConstants.STYLE_STROKECOLOR] = '#1B78C8';
        style[$wnd.mxConstants.STYLE_FONTCOLOR] = '#333333';
        style[$wnd.mxConstants.STYLE_ROUNDED] = true;
        style[$wnd.mxConstants.STYLE_OPACITY] = '80';
        style[$wnd.mxConstants.STYLE_FONTSIZE] = '11';
        style[$wnd.mxConstants.STYLE_FONTSTYLE] = 0;
        style[$wnd.mxConstants.STYLE_VERTICAL_ALIGN] = $wnd.mxConstants.ALIGN_TOP;
        style[$wnd.mxConstants.STYLE_VERTICAL_LABEL_POSITION] = $wnd.mxConstants.ALIGN_BOTTOM;
        //style[$wnd.mxConstants.STYLE_IMAGE_WIDTH] = '48';
        //style[$wnd.mxConstants.STYLE_IMAGE_HEIGHT] = '48';
        graph.getStylesheet().putDefaultVertexStyle(style);

        //Default to elbow connectors
	    style = graph.getStylesheet().getDefaultEdgeStyle();
		style[$wnd.mxConstants.STYLE_ROUNDED] = true;
		style[$wnd.mxConstants.STYLE_ELBOW] = $wnd.mxConstants.ELBOW_VERTICAL;
        style[$wnd.mxConstants.STYLE_EDGE] = $wnd.mxEdgeStyle.ElbowConnector;
        style[$wnd.mxConstants.STYLE_STARTARROW] = $wnd.mxConstants.NONE;
        style[$wnd.mxConstants.STYLE_ENDARROW] = $wnd.mxConstants.ARROW_CLASSIC;
        style[$wnd.mxConstants.STYLE_ROUTING_CENTER_Y] = '-0.5';
        style[$wnd.mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = '#FFFFFF';
        style[$wnd.mxConstants.STYLE_FONTCOLOR] = 'gray';
        style[$wnd.mxConstants.STYLE_STROKECOLOR] = '#444444';
        style[$wnd.mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = null;

        //Create

		graph.alternateEdgeStyle = 'elbow=vertical';


        return graph;
    }-*/;

    public void bindGraph()
    {
        super.bindGraph();
    }


    private native void setConnectable(JavaScriptObject graphObject, boolean state)
        /*-{
            graphObject.setConnectable(state);
            //graphObject.connectionHandler.setCreateTarget(state);
        }-*/;

    public void setConnectable(boolean state)
    {
        setConnectable(getGraphObject(), state);
    }

    private static class DefaultEdgeFactory implements EdgeFactory<Edge>
    {
        @Override
        public Edge createEdge(Cell vStart, Cell vEnd)
        {
            return new Edge(vStart, vEnd);
        }
    }
}
