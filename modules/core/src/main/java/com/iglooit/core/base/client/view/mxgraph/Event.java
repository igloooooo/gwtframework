package com.iglooit.core.base.client.view.mxgraph;

public interface Event
{
    void onEvent(MXGraphObject object, EventData eventData);

    String DOUBLE_CLICK_EVENT_TYPE = "MX_Graph_Double_Click";
    String CELL_FOLDED_EVENT_TYPE = "MX_Graph_Cell_Folded";
    String CELL_MOVED_EVENT_TYPE = "MX_Graph_Cell_Moved";
    String EDGE_DELETED_EVENT_TYPE = "MX_Graph_Edge_Deleted";
    String VERTEX_DELETED_EVENT_TYPE = "MX_Graph_Vertex_Deleted";
    String LEFT_CLICK_EVENT_TYPE = "MX_Graph_Left_Click";
}
 