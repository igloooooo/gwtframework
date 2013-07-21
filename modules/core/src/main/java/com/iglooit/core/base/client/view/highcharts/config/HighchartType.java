package com.iglooit.core.base.client.view.highcharts.config;

public enum HighchartType
 {
     AREA("area"),
     AREASPLINE("areaspline"),
     BAR("bar"),
     COLUMN("column"),
     LINE("line"),
     PIE("pie"),
     SCATTER("scatter"),
     SPLINE("spline"),
     GAUGE("gauge");

     private String name;

     HighchartType(String name)
     {
         this.name = name;
     }

     @Override
     public String toString()
     {
         return name;
     }
 }
