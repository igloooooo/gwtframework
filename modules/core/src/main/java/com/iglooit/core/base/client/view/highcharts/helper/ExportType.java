package com.iglooit.core.base.client.view.highcharts.helper;

public enum ExportType
{
    PDF("application/pdf"),

    PNG("image/png"),

    JPG("image/jpeg"),

    SVG("image/svg+xml"),

    CSV("application/vnd.ms-excel");

    private String contentType;

    private ExportType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getContentType()
    {
        return contentType;
    }


    @Override
    public String toString()
    {
        return this.contentType;
    }
}
