package com.iglooit.core.iface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 30/04/13 3:21 PM
 */
public class GenericThemeStyleProvider
{
    private static final HashMap<String, GenericThemeStyle> STYLE_MAP =
        new HashMap<String, GenericThemeStyle>();

    private static final GenericThemeStyle DEFAULT_STYLE =
        new GenericThemeStyle("#5A6986", "#94A2bE");

    static
    {
        STYLE_MAP.put("INITIATED", new GenericThemeStyle("#5A6986", "#94A2bE"));
        STYLE_MAP.put("PROPOSED", new GenericThemeStyle("#B1365F", "#E67399"));
        STYLE_MAP.put("INFO", new GenericThemeStyle("#AB8B00", "#E0C240"));
        STYLE_MAP.put("CONCURRENCE", new GenericThemeStyle("#8D6F47", "#C4A883"));
        STYLE_MAP.put("READYFORAPPROVE", new GenericThemeStyle("#28754E", "#65AD89"));
        STYLE_MAP.put("APPROVED", new GenericThemeStyle("#0D7813", "#4CB052"));
        STYLE_MAP.put("REJECTED", new GenericThemeStyle("#A32929", "#D96666"));
        STYLE_MAP.put("INPROGRESS", new GenericThemeStyle("#528800", "#8CBF40"));
        STYLE_MAP.put("COMPLETED", new GenericThemeStyle("#2952A3", "#668CD9"));
        STYLE_MAP.put("CANCELLED", new GenericThemeStyle("#6E6E41", "#A7A77D"));
        STYLE_MAP.put("INCOMPLETE", new GenericThemeStyle("#7A367A", "#B373B3"));
        STYLE_MAP.put("CLOSED", new GenericThemeStyle("#4E5D6C", "#8997A5"));
    }

    public static GenericThemeStyle getThemeStyle(String state)
    {
        GenericThemeStyle style = STYLE_MAP.get(state);
        if (style == null)
            return DEFAULT_STYLE;
        else
            return style;
    }

    public static Set<Map.Entry<String, GenericThemeStyle>> getStyleMapEntries()
    {
        return STYLE_MAP.entrySet();
    }
}
