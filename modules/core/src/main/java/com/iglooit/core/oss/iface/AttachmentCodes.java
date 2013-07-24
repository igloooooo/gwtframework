package com.iglooit.core.oss.iface;

import java.util.HashMap;
import java.util.Map;

public class AttachmentCodes
{
    public static final String RESULT_START_TAG = "<result>";
    public static final String RESULT_END_TAG = "</result>";
    public static final String RESULT_ENCODED_START_TAG = "&lt;result&gt;";
    public static final String RESULT_ENCODED_END_TAG = "&lt;/result&gt;";

    public static final String ATTACHMENT_ID = "ATTACHMENT_ID:";


    public static enum UPLOAD_MODE
    {
        APP_SERVER, DB, MAPPED_DRIVE, INVALID // INVALID is being used internally
    }

    public static enum RESULT
    {
        SUCCESS, FAILURE, WARNING, INVALID_CONVENTION, EXCEEDS_MAX_SIZE, FEATURE_DISABLED, INVALID_FILE_TYPE,
        NO_FILE, INVALID_ENTITY_TYPE, INVALID_REQUEST, FILE_EXISTS, INVALID_CONFIG, FORCE_UPLOAD;


        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append(RESULT_START_TAG);
            builder.append(super.toString());
            builder.append(RESULT_END_TAG);
            return builder.toString();
        }
    }

    public static String getAttachmentIdResult(Long attachmentId)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(RESULT_START_TAG);
        sb.append(ATTACHMENT_ID);
        sb.append(attachmentId);
        sb.append(RESULT_END_TAG);

        return sb.toString();
    }

    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<String, String>();

    static
    {
        // reference http://www.iana.org/assignments/media-types/index.html
        // MS office
        CONTENT_TYPE_MAP.put("doc", "application/msword");
        CONTENT_TYPE_MAP.put("xls", "application/vnd.ms-excel");
        CONTENT_TYPE_MAP.put("ppt", "application/vnd.ms-powerpoint");

        // for ODF, PDF, RTF, Text shtml html htm
        CONTENT_TYPE_MAP.put("oda", "application/oda");
        CONTENT_TYPE_MAP.put("pdf", "application/pdf");
        CONTENT_TYPE_MAP.put("rtx", "application/rtf");
        CONTENT_TYPE_MAP.put("txt", "text/plain");
        CONTENT_TYPE_MAP.put("shtml", "text/html");
        CONTENT_TYPE_MAP.put("html", "text/html");
        CONTENT_TYPE_MAP.put("htm", "text/html");

        // for zip
        CONTENT_TYPE_MAP.put("zip", "application/zip");

        // for CSS & JS
        CONTENT_TYPE_MAP.put("css", "text/css");
        CONTENT_TYPE_MAP.put("js", "text/javascript");

        // for video
        CONTENT_TYPE_MAP.put("mov", "application/video");
        CONTENT_TYPE_MAP.put("mxu", "video/vnd.mpegurl");
        CONTENT_TYPE_MAP.put("avi", "video/x-msvideo");
        CONTENT_TYPE_MAP.put("qt", "video/quicktime");

        // for image
        CONTENT_TYPE_MAP.put("bmp", "application/bmp");
        CONTENT_TYPE_MAP.put("gif", "application/gif");
        CONTENT_TYPE_MAP.put("jpeg ", "application/jpeg");
        CONTENT_TYPE_MAP.put("jpe ", "application/jpeg");
        CONTENT_TYPE_MAP.put("jpg ", "application/jpeg");
        CONTENT_TYPE_MAP.put("png", "application/png");
        CONTENT_TYPE_MAP.put("tiff", "application/tiff");
        CONTENT_TYPE_MAP.put("tif", "application/tiff");


        //browser files
        CONTENT_TYPE_MAP.put("xul", "application/vnd.mozilla.xul+xml");
        CONTENT_TYPE_MAP.put("xml ", "application/xml ");

        // audio files audio/mpeg	 mpga mp2 mp3
        CONTENT_TYPE_MAP.put("mp3", "application/audio");
        CONTENT_TYPE_MAP.put("au", "application/audio");
        CONTENT_TYPE_MAP.put("mp", "audio/mpeg");
        CONTENT_TYPE_MAP.put("mpga", "audio/mpeg");
        CONTENT_TYPE_MAP.put("wav", "audio/x-wav");

        // for binaries application/octet-stream	 bin dms lha lzh exe class so dl
        CONTENT_TYPE_MAP.put("bin", "application/octet-stream");
        CONTENT_TYPE_MAP.put("dms", "application/octet-stream");
        CONTENT_TYPE_MAP.put("lha", "application/octet-stream");
        CONTENT_TYPE_MAP.put("lzh", "application/octet-stream");
        CONTENT_TYPE_MAP.put("exe", "application/octet-stream");
        CONTENT_TYPE_MAP.put("class", "application/octet-stream");
        CONTENT_TYPE_MAP.put("so", "application/octet-stream");
        CONTENT_TYPE_MAP.put("dll", "application/octet-stream");

    }

    public static Map<String, String> getContentTypeMap()
    {
        return CONTENT_TYPE_MAP;
    }


}
