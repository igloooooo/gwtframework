package com.iglooit.core.base.client.view.resource;

import com.clarity.commons.iface.type.Tuple2;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import java.util.HashMap;
import java.util.Map;

public class FileIconResource
{
    private static final Map<String, Tuple2<String, AbstractImagePrototype>> FILE_ICON_MAP = new HashMap<String,
        Tuple2<String, AbstractImagePrototype>>();

    static
    {
        // reference http://www.iana.org/assignments/media-types/index.html
        // MS office
        FILE_ICON_MAP.put("doc", new Tuple2<String, AbstractImagePrototype>("application/msword",
            Resource.ICONS.fileTypeDoc()));
        FILE_ICON_MAP.put("xls", new Tuple2<String, AbstractImagePrototype>("application/vnd.ms-excel",
            Resource.ICONS.fileTypeXls()));
        FILE_ICON_MAP.put("ppt", new Tuple2<String, AbstractImagePrototype>("application/vnd.ms-powerpoint",
            Resource.ICONS.fileTypePpt()));

        // for ODF, PDF, RTF, Text shtml html htm
        FILE_ICON_MAP.put("oda", new Tuple2<String, AbstractImagePrototype>("application/oda",
            Resource.ICONS.fileTypeDefault()));
        FILE_ICON_MAP.put("pdf", new Tuple2<String, AbstractImagePrototype>("application/pdf",
            Resource.ICONS.fileTypePdf()));
        FILE_ICON_MAP.put("rtx", new Tuple2<String, AbstractImagePrototype>("application/rtf",
            Resource.ICONS.fileTypeTxt()));
        FILE_ICON_MAP.put("txt", new Tuple2<String, AbstractImagePrototype>("text/plain",
            Resource.ICONS.fileTypeTxt()));
        FILE_ICON_MAP.put("shtml", new Tuple2<String, AbstractImagePrototype>("text/html",
            Resource.ICONS.fileTypeHtml()));
        FILE_ICON_MAP.put("html", new Tuple2<String, AbstractImagePrototype>("text/html",
            Resource.ICONS.fileTypeHtml()));
        FILE_ICON_MAP.put("htm", new Tuple2<String, AbstractImagePrototype>("text/html",
            Resource.ICONS.fileTypeHtml()));

        // for CSS & JS
        FILE_ICON_MAP.put("css", new Tuple2<String, AbstractImagePrototype>("text/css",
            Resource.ICONS.fileTypeCss()));
        FILE_ICON_MAP.put("js", new Tuple2<String, AbstractImagePrototype>("text/javascript",
            Resource.ICONS.fileTypeJs()));

        // for zip
        FILE_ICON_MAP.put("zip", new Tuple2<String, AbstractImagePrototype>("application/zip",
            Resource.ICONS.fileTypeZip()));

        // for video
        FILE_ICON_MAP.put("mov", new Tuple2<String, AbstractImagePrototype>("application/video",
            Resource.ICONS.fileTypeMp4()));
        FILE_ICON_MAP.put("mxu", new Tuple2<String, AbstractImagePrototype>("video/vnd.mpegurl",
            Resource.ICONS.fileTypeMpg()));
        FILE_ICON_MAP.put("avi", new Tuple2<String, AbstractImagePrototype>("video/x-msvideo",
            Resource.ICONS.fileTypeAvi()));
        FILE_ICON_MAP.put("qt", new Tuple2<String, AbstractImagePrototype>("video/quicktime",
            Resource.ICONS.fileTypeAvi()));

        // for image
        FILE_ICON_MAP.put("bmp", new Tuple2<String, AbstractImagePrototype>("application/bmp",
            Resource.ICONS.fileTypeJpg()));
        FILE_ICON_MAP.put("gif", new Tuple2<String, AbstractImagePrototype>("application/gif",
            Resource.ICONS.fileTypeGif()));
        FILE_ICON_MAP.put("jpeg ", new Tuple2<String, AbstractImagePrototype>("application/jpeg",
            Resource.ICONS.fileTypeJpg()));
        FILE_ICON_MAP.put("jpe ", new Tuple2<String, AbstractImagePrototype>("application/jpeg",
            Resource.ICONS.fileTypeJpg()));
        FILE_ICON_MAP.put("jpg ", new Tuple2<String, AbstractImagePrototype>("application/jpeg",
            Resource.ICONS.fileTypeJpg()));
        FILE_ICON_MAP.put("png", new Tuple2<String, AbstractImagePrototype>("application/png",
            Resource.ICONS.fileTypePng()));
        FILE_ICON_MAP.put("tiff", new Tuple2<String, AbstractImagePrototype>("application/tiff",
            Resource.ICONS.fileTypeJpg()));
        FILE_ICON_MAP.put("tif", new Tuple2<String, AbstractImagePrototype>("application/tiff",
            Resource.ICONS.fileTypeJpg()));


        //browser files
        FILE_ICON_MAP.put("xul", new Tuple2<String, AbstractImagePrototype>("application/vnd.mozilla.xul+xml",
            Resource.ICONS.fileTypeXml()));
        FILE_ICON_MAP.put("xml ", new Tuple2<String, AbstractImagePrototype>("application/xml",
            Resource.ICONS.fileTypeXml()));

        // audio files audio/mpeg	 mpga mp2 mp3
        FILE_ICON_MAP.put("mp3", new Tuple2<String, AbstractImagePrototype>("application/audio",
            Resource.ICONS.fileTypeMp3()));
        FILE_ICON_MAP.put("au", new Tuple2<String, AbstractImagePrototype>("application/audio",
            Resource.ICONS.fileTypeMp3()));
        FILE_ICON_MAP.put("mp", new Tuple2<String, AbstractImagePrototype>("audio/mpeg",
            Resource.ICONS.fileTypeMpg()));
        FILE_ICON_MAP.put("mpga", new Tuple2<String, AbstractImagePrototype>("audio/mpeg",
            Resource.ICONS.fileTypeMpg()));
        FILE_ICON_MAP.put("wav", new Tuple2<String, AbstractImagePrototype>("audio/x-wav",
            Resource.ICONS.fileTypeDefault()));

        // for binaries application/octet-stream	 bin dms lha lzh exe class so dl
        FILE_ICON_MAP.put("bin", new Tuple2<String, AbstractImagePrototype>("application/octet-stream",
            Resource.ICONS.fileTypeDefault()));
        FILE_ICON_MAP.put("dms", new Tuple2<String, AbstractImagePrototype>("application/octet-stream",
            Resource.ICONS.fileTypeDefault()));
        FILE_ICON_MAP.put("lha", new Tuple2<String, AbstractImagePrototype>("application/octet-stream",
            Resource.ICONS.fileTypeDefault()));
        FILE_ICON_MAP.put("lzh", new Tuple2<String, AbstractImagePrototype>("application/octet-stream",
            Resource.ICONS.fileTypeDefault()));
        FILE_ICON_MAP.put("exe", new Tuple2<String, AbstractImagePrototype>("application/octet-stream",
            Resource.ICONS.fileTypeDefault()));
        FILE_ICON_MAP.put("class", new Tuple2<String, AbstractImagePrototype>("application/octet-stream",
            Resource.ICONS.fileTypeDefault()));
        FILE_ICON_MAP.put("so", new Tuple2<String, AbstractImagePrototype>("application/octet-stream",
            Resource.ICONS.fileTypeDefault()));
        FILE_ICON_MAP.put("dll", new Tuple2<String, AbstractImagePrototype>("application/octet-stream",
            Resource.ICONS.fileTypeDefault()));

    }

    public static Map<String, Tuple2<String, AbstractImagePrototype>> getContentTypeMap()
    {
        return FILE_ICON_MAP;
    }

}
