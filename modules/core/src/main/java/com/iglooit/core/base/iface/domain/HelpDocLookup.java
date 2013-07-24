package com.iglooit.core.base.iface.domain;

import java.util.HashMap;
import java.util.Map;

public class HelpDocLookup
{
    private static final Map<IglooitEntryPoint, String> HELP_DOC_MAP = new HashMap();

    static
    {

        HELP_DOC_MAP.put(IglooitEntryPoint.ADMIN, "help/admin.pdf");

    }

    public static String getDocUrl(IglooitEntryPoint entryPoint)
    {
        if (HELP_DOC_MAP.containsKey(entryPoint))
        {
            return HELP_DOC_MAP.get(entryPoint);
        }
        else
        {
            return entryPoint.name().toLowerCase() + ".pdf";
        }
    }
}
