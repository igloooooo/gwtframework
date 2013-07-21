package com.iglooit.core.base.client;

import com.extjs.gxt.ui.client.GXT;

public class MobileClientHelper
{
    private static final String[] MOBILE_SPECIFIC_SUBSTRING = {
        "iPhone", "Android", "MIDP", "Opera Mobi",
        "Opera Mini", "BlackBerry", "HP iPAQ", "IEMobile",
        "MSIEMobile", "Windows Phone", "HTC", "LG",
        "MOT", "Nokia", "Symbian", "Fennec",
        "Maemo", "Tear", "Midori", "armv",
        "Windows CE", "WindowsCE", "Smartphone", "240x320",
        "176x220", "320x320", "160x160", "webOS",
        "Palm", "Sagem", "Samsung", "SGH",
        "SIE", "SonyEricsson", "MMP", "UCWEB"
    };


    public static boolean isMobileClient()
    {
        String userAgent = GXT.getUserAgent().toUpperCase();

        for (String mobileStr : MOBILE_SPECIFIC_SUBSTRING)
        {
            if (userAgent.indexOf(mobileStr.toUpperCase()) != -1)
            {
                return true;
            }
        }
        return false;
    }
}
