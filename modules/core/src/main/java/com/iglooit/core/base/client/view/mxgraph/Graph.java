package com.iglooit.core.base.client.view.mxgraph;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.clarity.commons.iface.type.AppX;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class

Graph extends GPanel
{
    protected static final int LEFT_MOUSE_BUTTON = 0;
    protected static final int RIGHT_MOUSE_BUTTON = 2;

    private Element graphElement;
    private JavaScriptObject graphObject;
    private LayoutContainer viewport, navigationPanel;

    private HashMap<String, Style> styleRegister = new HashMap<String, Style>();
    private HashMap<String, Cell> cellHash = new HashMap<String, Cell>();
    private HashMap<String, MXGraphObject> objectEventHash = new HashMap<String, MXGraphObject>();
    private int mouseClickButton = LEFT_MOUSE_BUTTON;
    private HashSet<CellSelectionListener> cellSelectionListeners = new HashSet<CellSelectionListener>();

    private DefaultFoldableChecker defaultFoldableCheckerInstance = new DefaultFoldableChecker();

    protected HashMap<String, MXGraphObject> getObjectEventHash()
    {
        return objectEventHash;
    }

    private HashMap<String, Style> styleMap = new HashMap<String, Style>();

    private ArrayList<Cell> cellList = new ArrayList<Cell>();
    private static final int PAN_PIXELS = 20;
    private static final int DIRECTION_ARROW_SIZE = 16;

    /**
     * Wraps the graph into a FlowLayout container which is contained within a FitLayout container
     *
     * @return The outer LayoutContainer (with FitLayout)
     */
    public LayoutContainer embedInFitFlowContainer()
    {
        final LayoutContainer outer = new LayoutContainer(new FitLayout());
        final LayoutContainer inner = new LayoutContainer(new FlowLayout());
        outer.add(inner);
        inner.add(this);
        return outer;
    }


    public LayoutContainer embedInFitFlowContainerOverFlowHidden()
    {
        LayoutContainer outer = embedInFitFlowContainer();
        outer.setStyleAttribute("overflow", "hidden");
        outer.setStyleAttribute("top", "auto");
        outer.setStyleAttribute("left", "auto");
        return outer;
    }

    public void useRightMouseButtonForCellClick()
    {
        mouseClickButton = RIGHT_MOUSE_BUTTON;
    }

    public void useLeftMouseButtonForCellClick()
    {
        mouseClickButton = LEFT_MOUSE_BUTTON;
    }

    public boolean isZoomOnMouseWheel()
    {
        return zoomOnMouseWheel;
    }

    public void setZoomOnMouseWheel(boolean zoomOnMouseWheel)
    {
        this.zoomOnMouseWheel = zoomOnMouseWheel;
    }

    private boolean zoomOnMouseWheel = true;

    protected void mouseWheelCallback(boolean up)
    {
        if (zoomOnMouseWheel)
        {
            if (up)
                zoomIn();
            else
                zoomOut();
        }
    }

    public void doubleclickCallback(String cUUID)
    {
        MXGraphObject eventObject = objectEventHash.get(cUUID);

        if (eventObject != null)
        {
            eventObject.executeEvent(Event.DOUBLE_CLICK_EVENT_TYPE);
        }
    }

    //this method is called when any part of the graph is clicked

    public void cellClicked(String cUUID, int x, int y)
    {
        MXGraphObject eventObject = objectEventHash.get(cUUID);
        if (eventObject != null)
        {
            eventObject.executeEvent(Event.LEFT_CLICK_EVENT_TYPE, new CellClickedEventData(x, y));
        }
    }

    public Cell getCellByName(String name)
    {
        return cellHash.get(name);
    }

    public void handleCellFolded(String cUUID, boolean collapse)
    {
        MXGraphObject eventObject = objectEventHash.get(cUUID);
        if (eventObject != null)
        {
            eventObject.executeEvent(Event.CELL_FOLDED_EVENT_TYPE, new CellFoldedEventData(collapse));
        }
    }

    public void cellLeftClicked(String cUUID)
    {
        MXGraphObject eventObject = objectEventHash.get(cUUID);
        if (eventObject != null)
        {
            eventObject.executeEvent(Event.LEFT_CLICK_EVENT_TYPE);
        }
    }

    /**
     * @param mouseX The absolute position of the mouse X coord
     * @param mouseY The absolute position of the mouse Y coord
     * @return Whether the graph should respond to the mousewheel event - this is needed as mxGraph catches the mouse
     *         wheel event at the document (global) level instead of localizing it to just the graph.  A bug report will
     *         be sent to the mxGraph team - this method would be deprecated once fixed.
     */
    private boolean shouldHandleMouseWheel(int mouseX, int mouseY)
    {
        if (!isVisible()) return false;

        BoxComponent boxComponent = getBoundedParentBoxComponent(this);
        return boxComponent == null || boxComponent.getBounds(false).contains(mouseX, mouseY);
        //we might want to cache this ancestor - but this might be error-prone if the graph is moved around
    }

    /**
     * Find a parent LayoutContainer that has defined bounds (inclusive of param)
     *
     * @param widget
     * @return
     */
    private BoxComponent getBoundedParentBoxComponent(Widget widget)
    {
        Widget w = widget;
        if (w == null) return null;

        if (!(w instanceof BoxComponent))
        {
            w = getBoundedParentBoxComponent(w.getParent());
        }

        BoxComponent boxComponent = (BoxComponent)w;

        Rectangle bounds = boxComponent.getBounds(false);
        if ((bounds.height == 0 || bounds.width == 0))
        {
            boxComponent = getBoundedParentBoxComponent(boxComponent.getParent());
        }
        return boxComponent;
    }

    protected native JavaScriptObject runController(Graph c, Element container) /*-{

        if ($wnd.mxClient.IS_IE)
        {
              new $wnd.mxDivResizer(container);
        }

        var graph = new $wnd.mxGraph(container);
        graph.setAutoSizeCells(true);
        graph.setPanning(true);
        graph.panningHandler.useLeftButtonForPanning = true;
        graph.setAllowDanglingEdges(false);

        var timerId = -1;
        var cellMoveFlag = 0;
        var cellId = null;
        var clickX = null;
        var clickY = null;
        graph.dblClick = function(evt, cell) {
//            alert('clearing timer, id: ' + timerId);
            clearTimeout(timerId);
            timerId = -1;
            cellId = null;
            if(!graph.isEnabled()) return;
            c.@com.clarity.core.base.client.view.mxgraph.Graph::doubleclickCallback(Ljava/lang/String;)(cell.UUID);
        }
        graph.click = function(me) {
            var cell = me.getCell();
            clickX = me.getX();
            clickY = me.getY();
            cellId = cell == null || cell == undefined ? null: cell.UUID;
            var event = me.getEvent();

            if (cellId != null)
            {
//                alert('timerID=' + timerId + '    flag=' + cellMoveFlag);

                if (timerId == -1 && cellMoveFlag == 0)
                {
                    //check if the correct button is pressed.
                    var clickedButton = c.@com.clarity.core.base.client.view.mxgraph.Graph::mouseClickButton;
                    if (event.button == clickedButton)
                    {
                        timerId = setTimeout(cellClick, 240);
                    }
                }
                else
                {
//                    alert('clearing timer, id: ' + timerId);
                    clearTimeout(timerId);
                    cellId = null;
                    timerId = -1;
                }
            }
            cellMoveFlag = 0;
        };
        function cellMoveTimerInterval ()
        {
            cellMoveFlag = 0;
        }
        function cellClick ()
        {
//            alert('single click -- cell id: ' + cellId + ';x: ' + clickX + ';y: ' + clickY);
            timerId = -1;
			c.@com.clarity.core.base.client.view.mxgraph.Graph::cellClicked(Ljava/lang/String;II)
                (cellId, clickX, clickY);
			cellId = null;
        }

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
                    }
              });


//        graph.addListener($wnd.mxEvent.CELLS_MOVED,
//            function(sender, event)
//            {
//                cellMoveFlag = 1;
//                clearTimeout(timerId);
//                timerId = -1;
//                cellId = null;
//                setTimeout(cellMoveTimerInterval, 500);
//            });
//
        $wnd.mxEvent.addMouseWheelListener(function (evt, up)
        {
            if(evt == null || evt == undefined || evt.target.namespaceURI.indexOf("/svg") < 0) return;

            if (c.@com.clarity.core.base.client.view.mxgraph.Graph::shouldHandleMouseWheel(II)
                (evt.clientX, evt.clientY))
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

        //override graph isCellFoldable to fix expand cell for alarm/performance overlay problem
        graph.defaultCellFoldableCheck = function(cell)
        {
            var myStyle = cell.getStyle();
            if (myStyle != null && myStyle.toString() == "EQUIPMENT_STYLE_KEY")
            {
                return false;
            }
            else if (this.model.getChildCount(cell) > 1)
            {
                return true
            }
            else if (this.model.getChildCount(cell) < 1)
            {
                return false;
            }
            else
            {
                var child = cell.getChildAt(0);
                var style = child.getStyle();
                if (!style)
                    return true;
                var styleStr = style.toString();
                var subStr = styleStr.substring(0, 12);
                if (subStr == "marker-style")
                    return false;
                else
                    return true;
            }
        }
        graph.isCellFoldable = function(cell)
        {
            if (!cell.isVertex())
                return false;
            var callable = c.@com.clarity.core.base.client.view.mxgraph.Graph::hasCellFoldableHandler()();
            if (callable)
                return c.@com.clarity.core.base.client.view.mxgraph.Graph::isCellFoldableCallback(Ljava/lang/String;)
                        (cell.UUID);
            else
                return graph.defaultCellFoldableCheck(cell);
        }

        //Define a vertex style
        var style = new Object();
        style[$wnd.mxConstants.STYLE_SHAPE] = $wnd.mxConstants.SHAPE_RECTANGLE;
        style[$wnd.mxConstants.STYLE_PERIMETER] = $wnd.mxPerimeter.RectanglePerimeter;
        style[$wnd.mxConstants.STYLE_ALIGN] = $wnd.mxConstants.ALIGN_CENTER;
        //style[$wnd.mxConstants.STYLE_VERTICAL_ALIGN] = $wnd.mxConstants.ALIGN_MIDDLE;
        style[$wnd.mxConstants.STYLE_GRADIENTCOLOR] = '#41B9F5';
        style[$wnd.mxConstants.STYLE_FILLCOLOR] = '#8CCDF5';
        style[$wnd.mxConstants.STYLE_STROKECOLOR] = '#1B78C8';
        style[$wnd.mxConstants.STYLE_FONTCOLOR] = '#333333';
        style[$wnd.mxConstants.STYLE_ROUNDED] = true;
        style[$wnd.mxConstants.STYLE_OPACITY] = '80';
        style[$wnd.mxConstants.STYLE_FONTSIZE] = 8;
//        style[$wnd.mxConstants.STYLE_FONTSTYLE] = 0;
        style[$wnd.mxConstants.STYLE_VERTICAL_ALIGN] = $wnd.mxConstants.ALIGN_TOP;
        style[$wnd.mxConstants.STYLE_VERTICAL_LABEL_POSITION] = $wnd.mxConstants.ALIGN_BOTTOM;
        //style[$wnd.mxConstants.STYLE_IMAGE_WIDTH] = '48';
        //style[$wnd.mxConstants.STYLE_IMAGE_HEIGHT] = '48';
        graph.getStylesheet().putDefaultVertexStyle(style);       

        //Default to elbow connectors
	    var style = graph.getStylesheet().getDefaultEdgeStyle();
		style[$wnd.mxConstants.STYLE_ROUNDED] = true;
		style[$wnd.mxConstants.STYLE_ELBOW] = $wnd.mxConstants.ELBOW_VERTICAL;
		//		style[$wnd.mxConstants.STYLE_ENDARROW] = null;
          style[$wnd.mxConstants.STYLE_ROUTING_CENTER_Y] = '-0.5';
//      style[$wnd.mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = '#FFFFFF';
        style[$wnd.mxConstants.STYLE_FONTSIZE] = 10;
//        style[$wnd.mxConstants.STYLE_FONTSTYLE] = 0;
        style[$wnd.mxConstants.STYLE_FONTCOLOR] = 'gray';
        style[$wnd.mxConstants.STYLE_STROKECOLOR] = '#444444';
        style[$wnd.mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = null;
        graph.getStylesheet().putDefaultEdgeStyle(style);

        //Create


//		graph.alternateEdgeStyle = 'elbow=vertical';

        graph.setTooltips(true);
        graph.setHtmlLabels(true);

        return graph;
    }-*/;

    private native JavaScriptObject addStyle(JavaScriptObject graphCanvas, String name, JavaScriptObject style) /*-{
        graphCanvas.getStylesheet().putCellStyle(name, style);
    }-*/;


    //--------------------   CELL SELECTION --------------------------------------------//

    private native JavaScriptObject setDefaultVertexSelectionStyle(
        String color, int strokeWidth, int isDashed) /*-{
        if(color != null) $wnd.mxConstants.VERTEX_SELECTION_COLOR = color;
        if(strokeWidth >= 0) $wnd.mxConstants.VERTEX_SELECTION_STROKEWIDTH = strokeWidth;
        if(isDashed >= 0) $wnd.mxConstants.VERTEX_SELECTION_DASHED = (isDashed == 1);
    }-*/;

    /**
     * Sets the default vertex selection style
     *
     * @param color       - The color of the selection frame (if null, color is left unchanged)
     * @param strokeWidth - The width of the frame, (if null, it is left unchanged)
     * @param isDashed    - Whether the line is dashed, (if null, it is left unchanged)
     */
    public void setDefaultVertexSelectionStyle(String color, Integer strokeWidth, Boolean isDashed)
    {
        setDefaultVertexSelectionStyle(color, strokeWidth == null ? -1 : strokeWidth.intValue(),
            isDashed == null ? -1 : (isDashed ? 1 : 0));
    }

    private native void selectCell(JavaScriptObject graph, JavaScriptObject cell)/*-{
        graph.addSelectionCell(cell);
    }-*/;

    /**
     * Clears the current selection and selects the given cell
     */
    public void selectCell(Cell cell)
    {
        selectCell(getGraphObject(), cell == null ? null : cell.getBrowserObject());
    }

    private native void setSingleSelection(JavaScriptObject graph, boolean single)/*-{
         graph.getSelectionModel().setSingleSelection(single);
     }-*/;

    /**
     * @param single If true, sets the selection mode to single (default is false)
     */
    public void setSingleSelection(boolean single)
    {
        setSingleSelection(graphObject, single);
    }

    private native void addSelectionListener(Graph c, JavaScriptObject graph)/*-{
        //see function mxGraph(container, model, renderHint) 
        graph.getSelectionModel().addListener($wnd.mxEvent.CHANGE, function(sender, evt){
            var cellsSelected = sender.cells;
            //this will hold the UUIDs of the selected cells 
            var uuids = new Array();
            if(cellsSelected != null)
            {
                var i;
                for(i = 0; i< cellsSelected.length; i++)
                {
                    uuids.push(cellsSelected[i].UUID);
                }
            }
            c.@com.clarity.core.base.client.view.mxgraph.Graph::cellsSelected(Ljava/lang/String;)(uuids.join(":"));
        });
    }-*/;

    /**
     * Ideally this should take in an array of UUIDs but can't send array of Strings from JS*
     */
    private void cellsSelected(String uuids)
    {
        final String[] cellIds = uuids.split(":");
        List<Edge> edges = null;
        List<Vertex> vertices = null;
        if (cellIds.length > 0)
        {
            int i = 0;
            for (String cellId : cellIds)
            {
                Cell cell = (Cell)objectEventHash.get(cellId);
                if (cell instanceof Vertex)
                {
                    if (vertices == null) vertices = new ArrayList<Vertex>();
                    vertices.add((Vertex)cell);
                }
                else if (cell instanceof Edge)
                {
                    if (edges == null) edges = new ArrayList<Edge>();
                    edges.add((Edge)cell);
                }
                //add an else for all other cell types
            }
        }

        final CellSelectionEvent event = new CellSelectionEvent(this, vertices, edges);

        for (CellSelectionListener listener : cellSelectionListeners)
        {
            listener.cellsSelected(event);
        }
    }

    public void addSelectionListener(CellSelectionListener listener)
    {
        //if someone is going to add a removeSelectionListener then this condition will not work. we'll have
        //to have a flag to indicate whether we have previously registered a selection listener with the Graph JS object
        if (cellSelectionListeners.isEmpty()) addSelectionListener(this, getGraphObject());
        cellSelectionListeners.add(listener);
    }

    private native void setGridSize(JavaScriptObject graph, int size) /*-{
        graph.setGridSize(size);
    }-*/;


    public void setGridSize(int size)
    {
        setGridSize(graphObject, size);
    }


    private native int getGridSize(JavaScriptObject graph) /*-{
        return graph.getGridSize();
    }-*/;

    public int getGridSize()
    {
        return getGridSize(graphObject);
    }

    private native void snapToGrid(JavaScriptObject graph) /*-{
        graph.snap(graph.getGridSize());
    }-*/;

    public void snapToGrid()
    {
        snapToGrid(graphObject);
    }


    @Deprecated
    public void registerStyle(Style style)
    {
        styleMap.put(style.getStyleName(), style);
        style.processStyleJS(this);
        addStyle(getGraphObject(), style.getStyleName(), style.getStyleObject());
    }

    public Style getStyleByName(String name)
    {
        return styleMap.get(name);
    }

    public void refresh(Cell cell)
    {
        refresh(getGraphObject(), cell.getBrowserObject());
    }

    private static native void refresh(JavaScriptObject graphObject, JavaScriptObject cell) /*-{
        graphObject.refresh(cell);
    }-*/;

    private native void refresh(JavaScriptObject graphCanvas) /*-{
        graphCanvas.refresh();
    }-*/;

    public void refresh()
    {
        refresh(this.getGraphObject());
    }

    private native JavaScriptObject lockGraph(JavaScriptObject graphCanvas) /*-{
        graphCanvas.setCellsLocked(true);
        graphCanvas.setConnectable(false);
    }-*/;

    private native JavaScriptObject unlockGraph(JavaScriptObject graphCanvas) /*-{
        graphCanvas.setCellsLocked(false);    
        graphCanvas.setConnectable(true);
    }-*/;

    public void setCellsLocked(boolean locked)
    {
        if (locked)
            lockGraph();
        else
            unlockGraph();
    }

    public void lockGraph()
    {
        lockGraph(graphObject);
    }

    public void unlockGraph()
    {
        unlockGraph(graphObject);
    }

    public void useHtmllabels(boolean value)
    {
        useHtmllabels(graphObject, value);
    }

    private static native void useHtmllabels(JavaScriptObject graphCanvas, boolean useHtmllabels) /*-{
        graphCanvas.setHtmlLabels(useHtmllabels);
    }-*/;

    public void doOnRender(Element element, int i)
    {
        viewport = new LayoutContainer(new FitLayout());
        add(viewport);

        graphElement = element;

//        graphElement = getElement();
//        setStyleAttribute("overflow", "hidden");
//        setStyleAttribute("top", "auto");
//        setStyleAttribute("left", "auto");

        layout();
        if (mouseClickButton == RIGHT_MOUSE_BUTTON)
        {
            disableContextMenu(graphElement);
        }

    }

    protected native void disableContextMenu(Element container) /*-{
        $wnd.mxEvent.disableContextMenu(container);
    }-*/;


    public String getLabel()
    {

        return "MXGraph";
    }

    private native void beginUpdate(JavaScriptObject graphCanvas) /*-{
        graphCanvas.getModel().beginUpdate();
    }-*/;

    public void beginUpdate()
    {
        beginUpdate(graphObject);
    }

    private native void endUpdate(JavaScriptObject graphCanvas) /*-{
        graphCanvas.getModel().endUpdate();
    }-*/;

    public void endUpdate()
    {
        endUpdate(graphObject);
    }

    public void panToCompleteCell(Cell cell, int border)
    {
        //panToCell(cell, true);
        panToCompleteCell(graphObject, cell == null ? null : cell.getBrowserObject(), border);
    }

    private static native void panToCompleteCell(JavaScriptObject graph,
                                                 JavaScriptObject cell, int border) /*-{
        var geo = cell.getGeometry();
        //var origin = graph.model.getOrigin(cell);
        var origin = graph.view.getState(cell);
        var w = graph.container.clientWidth;
        var h = graph.container.clientHeight;
        if (origin.width + 2 * border > w || origin.height + 2 * border > h)
        {
            var scaleX = (w - 2 * border) / geo.width;
            var scaleY = (h - 2 * border) / geo.height;
            graph.view.setScale($wnd.Math.min(scaleX, scaleY));
            origin = graph.view.getState(cell);
        }
        var tx = -graph.view.translate.x;
        var ty = -graph.view.translate.y;

        // Note: this bit won't work for internal expansion since we don't know the actual old screen offset before
        // the nodes were expanded 
        //if (origin.x > 0 && origin.x + origin.width < w && origin.y > 0 && origin.y + origin.height < h)
        //    return;

        var rect = new $wnd.mxRectangle(tx + origin.getCenterX() - w / 2, ty + origin.getCenterY() - h / 2, w, h);
        if (graph.scrollRectToVisible(rect))
        {
            graph.view.setTranslate(graph.view.translate.x, graph.view.translate.y);
        }
    }-*/;

    public void panToCell(Cell cell, boolean centre)
    {
        panToCell(graphObject, cell == null ? null : cell.getBrowserObject(), centre);
    }

    public MXRectangle getGeometry(Cell cell)
    {
        return new MXRectangle(getGeometry(cell.getBrowserObject()));
    }

    private static native JavaScriptObject getGeometry(JavaScriptObject cell) /*-{
        return cell.getGeometry();
    }-*/;

    private native JavaScriptObject panToCell(JavaScriptObject graphCanvas, JavaScriptObject cell, boolean centre) /*-{
        if (cell == null)
            graphCanvas.scrollCellToVisible(graphCanvas.getDefaultParent(), centre);
        else        
            graphCanvas.scrollCellToVisible(cell, centre);
    }-*/;

    private native void panGraph(JavaScriptObject graphCanvas, int x, int y) /*-{
        graphCanvas.view.setTranslate(x, y);        
    }-*/;

    private native int getTranslateX(JavaScriptObject graphCanvas) /*-{
        var trans = graphCanvas.view.getTranslate();
        return trans.x;
    }-*/;

    private native int getTranslateY(JavaScriptObject graphCanvas) /*-{
        var trans = graphCanvas.view.getTranslate();
        return trans.y;    
    }-*/;

    public void panLeft()
    {
        panGraph(graphObject, getTranslateX(graphObject) - PAN_PIXELS, getTranslateY(graphObject));
    }

    public void panLeft(int panPixels)
    {
        panGraph(graphObject, getTranslateX(graphObject) - panPixels, getTranslateY(graphObject));
    }

    public void panRight()
    {
        panGraph(graphObject, getTranslateX(graphObject) + PAN_PIXELS, getTranslateY(graphObject));
    }

    public void panUp()
    {
        panGraph(graphObject, getTranslateX(graphObject), getTranslateY(graphObject) - PAN_PIXELS);
    }

    public void panUp(int panPixels)
    {
        panGraph(graphObject, getTranslateX(graphObject), getTranslateY(graphObject) - panPixels);
    }

    public void panDown()
    {
        panGraph(graphObject, getTranslateX(graphObject), getTranslateY(graphObject) + PAN_PIXELS);
    }

    private boolean anyVisibleCaptionedCells()
    {
        Iterator<Cell> visibleCellIterator = getVisibleCaptionedCellsIt().iterator();
        return visibleCellIterator.hasNext();
    }

    public void centerGraph()
    {
        List<Cell> visibleCellList = getVisibleCaptionedCells();
        if (visibleCellList.size() == 0) return;

        MXRectangle[] geometries = new MXRectangle[visibleCellList.size()];
        for (int i = 0; i < visibleCellList.size(); i++)
        {
            geometries[i] = getGeometry(visibleCellList.get(i));
        }

        Cell firstCell = visibleCellList.get(0);
        double minX = geometries[0].getCenterX();
        double minY = geometries[0].getCenterY();
        double maxX = minX;
        double maxY = minY;

        for (MXRectangle rect : geometries)
        {
            minX = Math.min(minX, rect.getCenterX());
            maxX = Math.max(maxX, rect.getCenterX());
            minY = Math.min(minY, rect.getCenterY());
            maxY = Math.max(maxY, rect.getCenterY());
        }

        final double centerX = (minX + maxX) / 2;
        final double centerY = (minY + maxY) / 2;

        Cell centerCell = firstCell;
        Double bestDistanceSqr = null;
        for (int i = 0; i < visibleCellList.size(); i++)
        {
            final Cell cell = visibleCellList.get(i);
            double dx = geometries[i].getCenterX() - centerX;
            double dy = geometries[i].getCenterY() - centerY;
            double distSqr = dx * dx + dy * dy;
            if (bestDistanceSqr == null || distSqr < bestDistanceSqr)
            {
                bestDistanceSqr = distSqr;
                centerCell = cell;
            }
        }

        panToCell(centerCell, true);
    }

    public void panDownToCenter()
    {
        int oldTranslateX = getTranslateX(graphObject);
        centerGraph();
        int newTranslateY = getTranslateY(graphObject);
        panGraph(graphObject, oldTranslateX, newTranslateY);
    }

    public void installTooltips()
    {
        for (CaptionableMXGraphObject captionedObject : getVisibleCaptionedCellsIt())
            captionedObject.installTooltipPanel();
    }

    public void initialiseCaptions()
    {
        for (CaptionableMXGraphObject captionedObject : getVisibleCaptionedCellsIt())
            captionedObject.initialiseCaption();
    }

    public void shortenCollidingCaptions(double scale)
    {
        List<Cell> visibleCellList = getVisibleCaptionedCells();

        boolean[] colliding = new boolean[visibleCellList.size()];
        for (int i = 0; i < visibleCellList.size(); i++)
        {
            CaptionableMXGraphObject captionedObjectA = visibleCellList.get(i);
            MXRectangle captionBoundsA = captionedObjectA.getCaptionBounds();
            for (int j = i + 1; j < visibleCellList.size(); j++)
            {
                CaptionableMXGraphObject captionedObjectB = visibleCellList.get(j);
                MXRectangle captionBoundsB = captionedObjectB.getCaptionBounds();
                boolean collision = captionBoundsA.intersects(captionBoundsB, 10, -5);
                colliding[i] |= collision;
                colliding[j] |= collision;
            }
        }

        for (int i = 0; i < visibleCellList.size(); i++)
            if (colliding[i])
                visibleCellList.get(i).shortenCaptionToWidthScale(scale);
    }

    private native JavaScriptObject addVertex(JavaScriptObject graphCanvas, String style, int x, int y, int width,
                                              int height, String name, Object userObject, String cUUID,
                                              JavaScriptObject parent) /*-{
         // Adds the root vertex of the tree

        if (parent == null)
            parent = graphCanvas.getDefaultParent();

        // var parent = graphCanvas.getDefaultParent();
        var root = null;
        try
        {
            //var w = graphCanvas.container.offsetWidth;
            if (style == null)
                root = graphCanvas.insertVertex(parent, name, userObject, x, y, width, height);
            else
                root = graphCanvas.insertVertex(parent, name, userObject, x, y, width, height, style);
            root.UUID = cUUID;
            root.name = name;
        }
        finally
        {            
        }

        return root;      
    }-*/;

    private native JavaScriptObject addEdge(JavaScriptObject graphCanvas, JavaScriptObject v1,
                                            JavaScriptObject v2, String style, Object caption, String cUUID) /*-{

        //Add an edge

        var parent = graphCanvas.getDefaultParent();    
        var edge = null;
        try
        {
            //var w = graphCanvas.container.offsetWidth;

            if (style == null)
                edge = graphCanvas.insertEdge(parent, caption, caption, v1, v2);
            else
                edge = graphCanvas.insertEdge(parent, caption, caption, v1, v2, style);
                
            edge.UUID = cUUID;
        }
        finally
        {
        }

        return edge;
    }-*/;

    private native JavaScriptObject addGroup(JavaScriptObject graphCanvas, String name, String caption, int width,
                                             int height, String cUUID, String styleName,
                                             JavaScriptObject parentGroup) /*-{
       //Add a group

        var group = null;
        try
        {
            var parent = graphCanvas.getDefaultParent();

            //Create two dummy cells, because we can't create a group without any.
            var cellArray = new Array();
            cellArray[0] = graphCanvas.insertVertex(parent, 'dummy1', '', 2, 2, 2, 2);
            cellArray[1] = graphCanvas.insertVertex(parent, 'dummy2', '', 2, 2, 2, 2);

            //TODO: Add border handling code
            if (parentGroup == null)
                parentGroup = parent; 

            group = graphCanvas.insertVertex(parentGroup, name, caption, 0, 0, 2, 2);
            graphCanvas.groupCells(group, 1, cellArray);            
            group.UUID = cUUID;

            //Now remove the dummy cells
            graphCanvas.removeCells(cellArray, true);

            if (width > 0 && height > 0)
            {
                var geo = new $wnd.mxGeometry(group.geometry.x,  group.geometry.y, width, height);
                geo.alternateBounds = new $wnd.mxRectangle(group.geometry.x,  group.geometry.y, width, height);
                group.setGeometry(geo);
            }

            if (styleName != null)
            {
                group.setStyle(styleName);
            }
        }
        finally
        {
        }

        return group;
    }-*/;

    private native void removeCell(JavaScriptObject graphCanvas, JsArray<JavaScriptObject> cells) /*-{
       //Remove a vertex

        try
        {
            graphCanvas.removeCells(cells, true);

        }
        finally
        {
        }
        
    }-*/;

    private native void clear(JavaScriptObject graphCanvas) /*-{
       //Clear the graph;

        try
        {
            graphCanvas.getModel().clear();

        }
        finally
        {
        }

    }-*/;


    public Edge addEdge(Edge edge)
    {

        addEdgeInternal(edge);

        String style = null;
        if (edge.getStyle() != null)
        {
            style = edge.getStyle().getStyleName();
        }

        checkAndRegisterStyle(edge.getStyle());

        JavaScriptObject jsEdge = addEdge(graphObject, edge.getStart().getBrowserObject(),
            edge.getEnd().getBrowserObject(), style, edge.getCaption(), edge.getUUID());

        edge.setBrowserObject(jsEdge);

        return edge;
    }

    protected void addEdgeInternal(Edge edge)
    {
        edge.setMemberGraph(this);

        //getVisibleCaptionedCells().add(edge);
        cellList.add(edge);
        objectEventHash.put(edge.getUUID(), edge);
    }

    /**
     * Returns the parents of the given vertex by checking the edges point to it
     * Note that Vertex.getParentCell() refers to the group it belongs to, which is not what we want
     *
     * @param child
     * @return
     */
    public List<Vertex> findVerticesLinkedTo(final Vertex child)
    {
        final List<Vertex> parents = new ArrayList<Vertex>();
        for (Cell cell : cellList)
        {
            if (!(cell instanceof Edge)) continue;
            final Edge edge = (Edge)cell;
            final Linkable start = edge.getStart();
            if (edge.getEnd().equals(child) && (start instanceof Vertex))
            {
                parents.add((Vertex)start);
            }
        }
        return parents;
    }

    public void checkAndRegisterStyle(Style style)
    {
        //If we have no style to register, don't try
        if (style == null)
            return;

        if (!styleRegister.containsKey(style.getStyleName()))
        {
            registerStyle(style);
        }
    }

    public Group addGroup(Group group)
    {
        //Tell the group what graph it's in.
        GWT.log("Graph.addGroup - begin");
        group.setMemberGraph(this);
        objectEventHash.put(group.getUUID(), group);

        //Add this to the hash
        cellHash.put(group.getName(), group);
//        cellList.add(group);
        //getVisibleCaptionedCells().add(group);

        String styleName = null;
        checkAndRegisterStyle(group.getStyle());
        if (group.getStyle() != null)
        {
            styleName = group.getStyle().getStyleName();
        }

        GWT.log("Graph.addGroup - js");

        JavaScriptObject parent = group.getParentCell() == null ? null : group.getParentCell().getBrowserObject();

        //JavaScriptObject[] jsVertices = group.getCellArray();
        JavaScriptObject jsGroup = addGroup(graphObject, group.getName(), group.getCaption(),
            group.getWidth(), group.getHeight(), group.getUUID(), styleName, parent);

        group.setBrowserObject(jsGroup);

        return group;
    }

    public Cell addCell(Cell c)
    {
        if (c instanceof Vertex)
            addVertex((Vertex)c);
        else if (c instanceof Group)
            addGroup((Group)c);

        cellList.add(c);

        return c;
    }

    public Vertex addVertex(final Vertex vertex)
    {
        //Tell the vertex what graph it's in
        vertex.setMemberGraph(this);

        String style = null;
        if (vertex.getStyle() != null)
        {
            style = vertex.getStyle().getStyleName();
        }

        cellHash.put(vertex.getName(), vertex);
//        cellList.add(vertex);
        //getVisibleCaptionedCells().add(vertex);
        checkAndRegisterStyle(vertex.getStyle());

        objectEventHash.put(vertex.getUUID(), vertex);

        JavaScriptObject parent = vertex.getParentCell() == null ? null : vertex.getParentCell().getBrowserObject();

        JavaScriptObject jsVertex = addVertex(graphObject, style, vertex.getX(), vertex.getY(), vertex.getWidth(),
            vertex.getHeight(),
            vertex.getName(), vertex.getCaption(), vertex.getUUID(), parent);

        vertex.setBrowserObject(jsVertex);

        vertex.processOverlays();

        return vertex;
    }

    public int getCellCount()
    {
        return cellList.size();
    }

    public Cell getCellAt(int i)
    {
        return cellList.get(i);
    }

    public void removeCell(Cell cell)
    {
        cell.setMemberGraph(null);
        cell.setParentCell(null);

        JavaScriptObject newArrayJSO = JavaScriptObject.createArray();
        JsArray<JavaScriptObject> newArray = newArrayJSO.cast();
        newArray.push(cell.getBrowserObject());

        //JavaScriptObject[] cells = new JavaScriptObject[1];
        //cells[0] = cell.getBrowserObject();

        //Remove from the name hash
        cellHash.remove(cell.getName());
        cellList.remove(cell);

        //Remove from the UUID hash
        objectEventHash.remove(cell.getUUID());

        removeCell(graphObject, newArray);
    }

    public void removeEdge(Edge e)
    {
//        JavaScriptObject[] cells = new JavaScriptObject[1];
//        cellsJSO[0] = e.getBrowserObject();

        cellList.remove(e);
        objectEventHash.remove(e.getUUID());
        e.setMemberGraph(null);

        JavaScriptObject cellsJSO = JavaScriptObject.createArray();
        JsArray<JavaScriptObject> cellsArray = cellsJSO.cast();
        cellsArray.push(e.getBrowserObject());

        removeCell(graphObject, cellsArray);
    }

    public Cell getDefaultParent()
    {
        return cellHash.get(getDefaultParentName(graphObject));
    }

    private native String getDefaultParentName(JavaScriptObject graph) /*-{
        return graph.getDefaultParent().getId();
    }-*/;

    private native void zoomIn(JavaScriptObject graph) /*-{
        graph.zoomIn();
    }-*/;

    private native void zoomOut(JavaScriptObject graph) /*-{
        graph.zoomOut();
    }-*/;

    public void zoomIn()
    {
        zoomIn(graphObject);
    }

    public void zoomOut()
    {
        zoomOut(graphObject);
    }

    public void clear()
    {
        cellHash.clear();
        objectEventHash.clear();
        cellList.clear();
        clear(graphObject);
    }

    public ArrayList<Cell> getChildren(Group group)
    {
        JsArrayString childrenIds = getChildrenId(group.getBrowserObject());
        ArrayList<Cell> result = new ArrayList<Cell>();

        for (int i = 0; i < childrenIds.length(); i++)
        {
            Cell cell = (Cell)objectEventHash.get(childrenIds.get(i));
            if (cell != null)
                result.add(cell);
        }

        return result;
    }

    private static native JsArrayString getChildrenId(JavaScriptObject group)
        /*-{
            var result = new Array();
            for (child in group.children)
                result.push(child.UUID);
            return result;
        }-*/;


    private Iterable<Cell> getVisibleCaptionedCellsIt()
    {
        return new Iterable<Cell>()
        {
            @Override
            public Iterator<Cell> iterator()
            {
                return new Iterator<Cell>()
                {
                    private int i = -1;
                    private final int count = cellList.size();
                    private boolean hasNextCalled = false;

                    @Override
                    public boolean hasNext()
                    {
                        hasNextCalled = true;
                        int temp = i + 1;
                        while (temp < count)
                        {
                            Cell c = cellList.get(temp);
                            if (c.hasCellState() && c.isVisible())
                            {
                                return true;
                            }
                            temp++;
                            i++;
                        }

                        return false;
                    }

                    @Override
                    public Cell next()
                    {
                        if (!hasNextCalled)
                            hasNext();

                        hasNextCalled = false;
                        i++;
                        return cellList.get(i);
                    }

                    @Override
                    public void remove()
                    {
                        throw new AppX("getVisibleCaptionedCellsIt: remove not supported");
                    }
                };
            }
        };

    }

    @Override
    public void focus()
    {
        super.focus();
        final LayoutContainer navPanel = getNavigationPanel();
        if (navPanel != null && navPanel.getItemCount() > 0)
        {
            //focusing on a layout container does not do anything; we have to focus on an actual widget
            //here we are assuming that the navigation panel only contains buttons (which it should)
            navPanel.getItem(0).focus();
        }
    }

    public List<Cell> getVisibleCaptionedCells()
    {
        ArrayList<Cell> vcs = new ArrayList<Cell>();
        for (Cell cell : cellList)
            if (cell.hasCellState() && cell.isVisible())
                vcs.add(cell);
        return vcs;
    }

    /**
     * Creates the Navigation panel - it is no longer included as the graph - should instead be added
     * as required by other components.
     *
     * @return
     */
    public LayoutContainer getNavigationPanel()
    {
        if (this.navigationPanel != null)
            return this.navigationPanel;

        navigationPanel = new LayoutContainer();
        navigationPanel.addStyleName(ClarityStyle.GRAPH_NAV_CONTAINER);
        navigationPanel.setLayout(new TableLayout(3));

        Button up = new Button();
        up.addListener(Events.OnClick, new Listener<ComponentEvent>()
        {
            @Override
            public void handleEvent(ComponentEvent componentEvent)
            {
                Graph.this.panDown();
            }
        });
        up.setIcon(Resource.ICONS.arrowUp());
        up.setHeight(DIRECTION_ARROW_SIZE);
        up.setWidth(DIRECTION_ARROW_SIZE);
        up.setStyleName(ClarityStyle.BUTTON_NOSTYLE);

        Button down = new Button();
        down.addListener(Events.OnClick, new Listener<ComponentEvent>()
        {
            @Override
            public void handleEvent(ComponentEvent componentEvent)
            {
                Graph.this.panUp();
            }
        });
        down.setIcon(Resource.ICONS.arrowDown());
        down.setHeight(DIRECTION_ARROW_SIZE);
        down.setWidth(DIRECTION_ARROW_SIZE);
        down.setStyleName(ClarityStyle.BUTTON_NOSTYLE);

        LayoutContainer leftPanel = new LayoutContainer();
        leftPanel.setLayout(new CenterLayout());
        Button left = new Button();
        left.addListener(Events.OnClick, new Listener<ComponentEvent>()
        {
            @Override
            public void handleEvent(ComponentEvent componentEvent)
            {
                Graph.this.panRight();
            }
        });
        left.setIcon(Resource.ICONS.arrowLeft());
        left.setHeight(DIRECTION_ARROW_SIZE);
        left.setWidth(DIRECTION_ARROW_SIZE);
        left.setStyleName(ClarityStyle.BUTTON_NOSTYLE);
        leftPanel.add(left);
        leftPanel.setWidth(DIRECTION_ARROW_SIZE);
        leftPanel.setHeight(DIRECTION_ARROW_SIZE);

        Button right = new Button();
        right.addListener(Events.OnClick, new Listener<ComponentEvent>()
        {
            @Override
            public void handleEvent(ComponentEvent componentEvent)
            {
                Graph.this.panLeft();
            }
        });
        right.setIcon(Resource.ICONS.arrowRight());
        right.setHeight(DIRECTION_ARROW_SIZE);
        right.setWidth(DIRECTION_ARROW_SIZE);
        right.setStyleName(ClarityStyle.BUTTON_NOSTYLE);

        TableData singleRowTableData = new TableData(
            com.extjs.gxt.ui.client.Style.HorizontalAlignment.CENTER,
            com.extjs.gxt.ui.client.Style.VerticalAlignment.MIDDLE);
        singleRowTableData.setColspan(3);

        navigationPanel.add(up, singleRowTableData);
        navigationPanel.add(left);
        navigationPanel.add(new Html("&nbsp;"));
        navigationPanel.add(right);
        navigationPanel.add(down, singleRowTableData);

        Button zoomIn = new Button();
        zoomIn.addListener(Events.OnClick, new Listener<ComponentEvent>()
        {
            @Override
            public void handleEvent(ComponentEvent componentEvent)
            {
                Graph.this.zoomIn();
            }
        });
        zoomIn.setIcon(Resource.ICONS.plusWhite());
        zoomIn.setHeight(DIRECTION_ARROW_SIZE);
        zoomIn.setWidth(DIRECTION_ARROW_SIZE);
        zoomIn.setStyleName(ClarityStyle.BUTTON_NOSTYLE);

        Button zoomOut = new Button();
        zoomOut.addListener(Events.OnClick, new Listener<ComponentEvent>()
        {
            @Override
            public void handleEvent(ComponentEvent componentEvent)
            {
                Graph.this.zoomOut();
            }
        });
        zoomOut.setIcon(Resource.ICONS.minusWhite());
        zoomOut.setHeight(DIRECTION_ARROW_SIZE);
        zoomOut.setWidth(DIRECTION_ARROW_SIZE);
        zoomOut.setStyleName(ClarityStyle.BUTTON_NOSTYLE);

        singleRowTableData.setPadding(4);
        navigationPanel.add(zoomIn, singleRowTableData);
        navigationPanel.add(zoomOut, singleRowTableData);

        return navigationPanel;
    }

    public Graph()
    {
    }

    public void bindGraph()
    {
        graphObject = runController(this, graphElement);
    }

    private String canvasName;

    public JavaScriptObject getGraphObject()
    {
        return graphObject;
    }

    public void setGraphObject(JavaScriptObject graphObject)
    {
        this.graphObject = graphObject;
    }


    public Cell getParent(Cell cell)
    {
        return (Cell)objectEventHash.get(getParent(cell.getBrowserObject()));
    }

    private static native String getParent(JavaScriptObject cell) /*-{
        var parent = cell.getParent();
        return parent == null ? null : parent.UUID;
    }-*/;

    public void fit()
    {
        fit(graphObject);
    }

    private native void fit(JavaScriptObject graphObject) /*-{
        graphObject.fit();
    }-*/;

    public void fit(int border)
    {
        fit(graphObject, border);
    }

    private native void fit(JavaScriptObject graphObject, int border) /*-{
        graphObject.fit(border);
    }-*/;

    public void fit(int border, boolean keepOrigin)
    {
        fit(graphObject, border, keepOrigin);
    }

    private native void fit(JavaScriptObject graphObject, int border, boolean keepOrigin) /*-{
        graphObject.fit(border, keepOrigin);
    }-*/;

    public void zoom(double zoomlevel)
    {
        zoom(graphObject, zoomlevel);
    }

    private native void zoom(JavaScriptObject graphObject, double zoom) /*-{
        graphObject.zoom(zoom);
    }-*/;

    public double getScale()
    {
        return getScale(graphObject);
    }

    private native double getScale(JavaScriptObject graphObject)        /*-{
        return graphObject.getView().getScale();
    }-*/;

    private native JavaScriptObject getMaximumGraphBounds(JavaScriptObject graphObject) /*-{
        return graphObject.getMaximumGraphBounds();
    }-*/;

    private MXRectangle getGraphBounds()
    {
        return new MXRectangle(getMaximumGraphBounds(graphObject));
    }


    private native void connectOrphanedCell(JavaScriptObject parent, JavaScriptObject graph,
                                            JsArray<JavaScriptObject> allNodes, boolean edgeVisible)
        /*-{
            var visited = new Array();
            var validNodes = new Array();
            var childCount = allNodes.length;
            if (childCount <= 1)
                return;

            for (var i = 0; i < childCount; i++)
            {
               if (allNodes[i].isVertex())
                  validNodes.push(allNodes[i]);
               visited.push(false);
            }

            var queue = new Array();

            var pointer = 0;
            queue.push(0);
            while (queue.length > 0)
            {
                var nodeIdx = queue.shift();
                if (!visited[nodeIdx])
                {
                    var node = allNodes[nodeIdx];
                    var edgeCount = node.getEdgeCount();
                    for (var j = 0; j < edgeCount; j++)
                    {
                        var edge = node.getEdgeAt(j);
                        var partner = edge.getTerminal(true);
                        if (partner == node)
                        {
                            partner = edge.getTerminal(false);
                            if (partner == node)
                                continue;
                        }

                        for (var k = 0; k < allNodes.length; k++)
                        {
                            if (partner == allNodes[k] && !visited[k])
                            {
                               queue.push(k);
                               break;
                            }
                        }
                    }
                }
                visited[nodeIdx] = true;

                if (queue.length <= 0)
                {
                    var next = -1;
                    for (pointer++; pointer < childCount; pointer++)
                    {
                        if (!visited[pointer])
                        {
                            next = pointer;
                            break;
                        }
                    }

                    if (next >= 0)
                    {
                        var edge = graph.insertEdge(parent, null, '-o-',
                            allNodes[Math.floor((next - 1) / 2)], allNodes[next]);
                        edge.setVisible(edgeVisible);
                        edge.isGuide = true;
                        queue.push(next);
                    }
                }
            }
        }-*/;

    public void connectOrphanedCell(Cell parent, ArrayList<? extends Cell> groupArray, boolean edgeVisible)
    {
        JavaScriptObject jsoArray = JsArray.createArray();
        JsArray<JavaScriptObject> jsArray = jsoArray.cast();

        for (Cell c : groupArray)
        {
            jsArray.push(c.getBrowserObject());
        }

        JavaScriptObject parentJSO = parent == null ? null : parent.getBrowserObject();

        connectOrphanedCell(parentJSO, getGraphObject(), jsArray, edgeVisible);
    }

    public boolean areConnected(Cell commonChildA, Cell commonChildB)
    {
        return areConnected(graphObject, commonChildA.getBrowserObject(), commonChildB.getBrowserObject());
    }

    public void removeGuideEdges()
    {
        ArrayList<Edge> removalList = new ArrayList<Edge>();
        for (Cell cell : cellList)
        {
            if (cell instanceof Edge)
            {
                Edge edge = (Edge)cell;
                if (edge.isGuide())
                    removalList.add(edge);
            }
        }

        for (Edge edge : removalList)
            removeEdge(edge);

        removeGuideEdgeInternal(getGraphObject());
    }

    private static native void removeGuideEdgeInternal(JavaScriptObject graphObject) /*-{
        var model = graphObject.model;
        var guides = [];

        function accumulateEdges(start)
        {
            if (model.isEdge(start) && start.isGuide)
                guides.push(start);

            var childCount = model.getChildCount(start);
            for (var i = 0; i < childCount; i++)
            {
                var cell = model.getChildAt(start, i);
                accumulateEdges(cell);
            }
        }

        accumulateEdges(graphObject.root);
        graphObject.removeCells(guides, true);
    }-*/;

    private static native boolean areConnected(JavaScriptObject graph, JavaScriptObject cellA,
                                               JavaScriptObject cellB) /*-{
        return graph.getEdgesBetween(cellA, cellB, false).length > 0;

    }-*/;

    public void orderCell(boolean back, Cell g)
    {
        orderCell(graphObject, back, g.getBrowserObject());
    }

    private static native void orderCell(JavaScriptObject graph, boolean back, JavaScriptObject cell) /*-{
        graph.orderCells(back, [cell]);

    }-*/;

    private class DefaultFoldableChecker implements IsCellFoldable
    {
        @Override
        public boolean check(Cell c, IsCellFoldable defaultChecker)
        {
            return defaultFoldableCheck(graphObject, c.getBrowserObject());
        }
    }

    private static native boolean defaultFoldableCheck(JavaScriptObject graph, JavaScriptObject cell) /*-{
        return graph.defaultCellFoldableCheck(cell);
    }-*/;

    protected native String getSelectedCell(Graph c, JavaScriptObject graph) /*-{
        var cells = graph.getSelectionCells();
        for (var i = 0; i < cells.length; i++)
        {
            return cells[i].UUID;
        }
    }-*/;

    protected native String getSelectedCellName(Graph c, JavaScriptObject graph) /*-{
        var cells = graph.getSelectionCells();
        for (var i = 0; i < cells.length; i++)
        {
            return cells[i].name;
        }
    }-*/;

    public MXGraphObject getSelectedCell()
    {
        return objectEventHash.get(getSelectedCell(this, this.getGraphObject()));
    }

    public String getSelectedCellName()
    {
        return getSelectedCellName(this, getGraphObject());
    }


    protected native void setCellsSelectable(JavaScriptObject graph, boolean selectable) /*-{
        graph.setCellsSelectable(selectable);
    }-*/;

    public void setCellsSelectable(boolean selectable)
    {
        setCellsSelectable(getGraphObject(), selectable);
    }


    protected native void setGraphEnabled(JavaScriptObject graph, boolean enabled) /*-{
        graph.setEnabled(enabled);
    }-*/;

    public Element getGraphElement()
    {
        return graphElement;
    }

    public void setGraphElement(Element graphElement)
    {
        this.graphElement = graphElement;
    }

    public int getMouseClickButton()
    {
        return mouseClickButton;
    }

    public void setMouseClickButton(int mouseClickButton)
    {
        this.mouseClickButton = mouseClickButton;
    }

    public void setGraphEnabled(boolean enabled)
    {
        setGraphEnabled(getGraphObject(), enabled);
    }

    private IsCellFoldable cellFoldableHandler;

    public IsCellFoldable getCellFoldableHandler()
    {
        return cellFoldableHandler;
    }

    public boolean hasCellFoldableHandler()
    {
        return getCellFoldableHandler() != null;
    }

    public void setFoldableCheckHandler(IsCellFoldable handler)
    {
        cellFoldableHandler = handler;
    }

    public boolean isCellFoldableCallback(String cUUID)
    {
        Cell cell = (Cell)objectEventHash.get(cUUID);
        if (cellFoldableHandler != null)
            return cellFoldableHandler.check(cell, defaultFoldableCheckerInstance);
        else
            return defaultFoldableCheckerInstance.check(cell, null);
    }

    public interface CellSelectionListener
    {
        void cellsSelected(CellSelectionEvent event);
    }

    public static class CellSelectionEvent
    {
        private Graph source;
        private List<Vertex> selectedVertices;
        private List<Edge> selectedEdges;

        public CellSelectionEvent(Graph source, List<Vertex> selectedVertices, List<Edge> selectedEdges)
        {
            this.source = source;
            this.selectedVertices = selectedVertices;
            this.selectedEdges = selectedEdges;
        }

        public List getSelectedEdges()
        {
            return selectedEdges == null ? null : Collections.unmodifiableList(selectedEdges);
        }

        public List getSelectedVertices()
        {
            return selectedVertices == null ? null : Collections.unmodifiableList(selectedVertices);
        }

        public Vertex getSelectedVertex()
        {
            return selectedVertices == null ? null : selectedVertices.get(0);
        }

        public Edge getSelectedEdge()
        {
            return selectedEdges == null ? null : selectedEdges.get(0);
        }

        public Graph getSource()
        {
            return source;
        }
    }
}
