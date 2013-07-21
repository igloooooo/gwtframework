package com.iglooit.core.base.client.view.mxgraph.overlay;

import com.clarity.core.base.client.view.mxgraph.CellOverlay;
import com.google.gwt.core.client.JavaScriptObject;

public class ImageCellOverlay extends CellOverlay
{
    protected ImageCellOverlay()
    {
    }

    public static ImageCellOverlay create(String filename, int width, int height, String tooltip)
    {
        ImageCellOverlay overlay = new ImageCellOverlay();
        overlay.setBrowserObject(createInternal(filename, width, height, tooltip));

        return overlay;
    }

    private static native JavaScriptObject createInternal(String filename, int width, int height, String tooltip) /*-{
        return new $wnd.mxCellOverlay(new $wnd.mxImage(filename, width, height), tooltip );
    }-*/;

    public final native String getImageSource() /*-{
        return this.image.src;
    }-*/;


}
