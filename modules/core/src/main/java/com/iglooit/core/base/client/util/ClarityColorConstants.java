package com.iglooit.core.base.client.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ClarityColorConstants
{
    ALICE_BLUE("ALICEBLUE", "#F0F8FF"),
    ANTIQUE_WHITE("ANTIQUEWHITE", "#FAEBD7"),
    AQUA("AQUA", "#00FFFF"),
    AQUA_MARINE("AQUAMARINE", "#7FFFD4", "LIGHTTEAL"),
    AZURE("AZURE", "#F0FFFF"),
    BEIGE("BEIGE", "#F5F5DC"),
    BISQUE("BISQUE", "#FFE4C4"),
    BLACK("BLACK", "#000000"),
    BLANCHED_ALMOND("BLANCHEDALMOND", "#FFEBCD"),
    BLUE("BLUE", "#0000FF"),
    BLUE_VIOLET("BLUEVIOLET", "#8A2BE2"),
    BROWN("BROWN", "#A52A2A"),
    BURLY_WOOD("BURLYWOOD", "#DEB887"),
    CADET_BLUE("CADETBLUE", "#5F9EA0"),
    CHARTREUSE("CHARTREUSE", "#7FFF00"),
    CHOCOLATE("CHOCOLATE", "#D2691E"),
    CORAL("CORAL", "#FF7F50"),
    CORNFLOWER_BLUE("CORNFLOWERBLUE", "#6495ED"),
    CORNSILK("CORNSILK", "#FFF8DC"),
    CRIMSON("CRIMSON", "#DC143C"),
    CYAN("CYAN", "#00FFFF"),
    DARK_BLUE("DARKBLUE", "#00008B"),
    DARK_CYAN("DARKCYAN", "#008B8B"),
    DARK_GOLDENROD("DARKGOLDENROD", "#B8860B"),
    DARK_GRAY("DARKGRAY", "#A9A9A9"),
    DARK_GREEN("DARKGREEN", "#006400"),
    DARK_KHAKI("DARKKHAKI", "#BDB76B"),
    DARK_MAGENTA("DARKMAGENTA", "#8B008B"),
    DARK_OLIVE_GREEN("DARKOLIVEGREEN", "#556B2F"),
    DARK_ORANGE("DARKORANGE", "#FF8C00"),
    DARK_ORCHID("DARKORCHID", "#9932CC"),
    DARK_RED("DARKRED", "#8B0000"),
    DARK_SALMON("DARKSALMON", "#E9967A"),
    DARK_SEA_GREEN("DARKSEAGREEN", "#8FBC8B"),
    DARK_SLATE_BLUE("DARKSLATEBLUE", "#483D8B"),
    DARK_SLATE_GRAY("DARKSLATEGRAY", "#2F4F4F"),
    DARK_TURQUOISE("DARKTURQUOISE", "#00CED1"),
    DARK_VIOLET("DARKVIOLET", "#9400D3"),
    DEEP_PINK("DEEPPINK", "#FF1493"),
    DEEP_SKY_BLUE("DEEPSKYBLUE", "#00BFFF"),
    DIM_GRAY("DIMGRAY", "#696969"),
    DODGER_BLUE("DODGERBLUE", "#1E90FF"),
    FIRE_BRICK("FIREBRICK", "#B22222"),
    FLORAL_WHITE("FLORALWHITE", "#FFFAF0"),
    FOREST_GREEN("FORESTGREEN", "#228B22"),
    FUCHSIA("FUCHSIA", "#FF00FF"),
    GAINSBORO("GAINSBORO", "#DCDCDC"),
    GHOST_WHITE("GHOSTWHITE", "#F8F8FF"),
    GOLD("GOLD", "#FFD700"),
    GOLDENROD("GOLDENROD", "#DAA520"),
    GRAY("GRAY", "#808080"),
    GREEN("GREEN", "#008000"),
    GREEN_YELLOW("GREENYELLOW", "#ADFF2F"),
    HONEYDEW("HONEYDEW", "#F0FFF0"),
    HOT_PINK("HOTPINK", "#FF69B4"),
    INDIAN_RED("INDIANRED", "#CD5C5C"),
    INDIGO("INDIGO", "#4B0082"),
    IVORY("IVORY", "#FFFFF0"),
    KHAKI("KHAKI", "#F0E68C"),
    LAVENDER("LAVENDER", "#E6E6FA"),
    LAVENDER_BLUSH("LAVENDERBLUSH", "#FFF0F5"),
    LAWN_GREEN("LAWNGREEN", "#7CFC00"),
    LEMON_CHIFFON("LEMONCHIFFON", "#FFFACD"),
    LIGHT_BLUE("LIGHTBLUE", "#ADD8E6"),
    LIGHT_CORAL("LIGHTCORAL", "#F08080"),
    LIGHT_CYAN("LIGHTCYAN", "#E0FFFF"),
    LIGHT_GOLDENROD_YELLOW("LIGHTGOLDENRODYELLOW", "#FAFAD2"),
    LIGHT_GRAY("LIGHTGRAY", "#D3D3D3"),
    LIGHT_GREEN("LIGHTGREEN", "#90EE90"),
    LIGHT_PINK("LIGHTPINK", "#FFB6C1"),
    LIGHT_SALMON("LIGHTSALMON", "#FFA07A"),
    LIGHT_SEA_GREEN("LIGHTSEAGREEN", "#20B2AA"),
    LIGHT_SKY_BLUE("LIGHTSKYBLUE", "#87CEFA"),
    LIGHT_SLATE_GRAY("LIGHTSLATEGRAY", "#778899"),
    LIGHT_STEEL_BLUE("LIGHTSTEELBLUE", "#B0C4DE"),
    LIGHT_YELLOW("LIGHTYELLOW", "#FFFFE0"),
    LIME("LIME", "#00FF00"),
    LIME_GREEN("LIMEGREEN", "#32CD32"),
    LINEN("LINEN", "#FAF0E6"),
    MAGENTA("MAGENTA", "#FF00FF"),
    MAROON("MAROON", "#800000"),
    MEDIUM_AQUAMARINE("MEDIUMAQUAMARINE", "#66CDAA"),
    MEDIUM_BLUE("MEDIUMBLUE", "#0000CD"),
    MEDIUM_ORCHID("MEDIUMORCHID", "#BA55D3"),
    MEDIUM_PURPLE("MEDIUMPURPLE", "#9370DB"),
    MEDIUM_SEA_GREEN("MEDIUMSEAGREEN", "#3CB371"),
    MEDIUM_SLATE_BLUE("MEDIUMSLATEBLUE", "#7B68EE"),
    MEDIUM_SPRING_GREEN("MEDIUMSPRINGGREEN", "#00FA9A"),
    MEDIUM_TURQUOISE("MEDIUMTURQUOISE", "#48D1CC"),
    MEDIUM_VIOLET_RED("MEDIUMVIOLETRED", "#C71585"),
    MIDNIGHT_BLUE("MIDNIGHTBLUE", "#191970"),
    MINT_CREAM("MINTCREAM", "#F5FFFA"),
    MISTY_ROSE("MISTYROSE", "#FFE4E1"),
    MOCCASIN("MOCCASIN", "#FFE4B5"),
    NAVAJO_WHITE("NAVAJOWHITE", "#FFDEAD"),
    NAVY("NAVY", "#000080"),
    OLD_LACE("OLDLACE", "#FDF5E6"),
    OLIVE("OLIVE", "#808000"),
    OLIVE_DRAB("OLIVEDRAB", "#6B8E23"),
    ORANGE("ORANGE", "#FFA500"),
    ORANGE_RED("ORANGERED", "#FF4500"),
    ORCHID("ORCHID", "#DA70D6", "LIGHTPURPLE"),
    PALE_GOLDENROD("PALEGOLDENROD", "#EEE8AA"),
    PALE_GREEN("PALEGREEN", "#98FB98"),
    PALE_TURQUOISE("PALETURQUOISE", "#AFEEEE"),
    PALE_VIOLET_RED("PALEVIOLETRED", "#DB7093"),
    PAPAYA_WHIP("PAPAYAWHIP", "#FFEFD5"),
    PEACH_PUFF("PEACHPUFF", "#FFDAB9"),
    PERU("PERU", "#CD853F"),
    PINK("PINK", "#FFC0CB", "LIGHTRED"),
    PLUM("PLUM", "#DDA0DD"),
    POWDER_BLUE("POWDERBLUE", "#B0E0E6"),
    PURPLE("PURPLE", "#800080"),
    RED("RED", "#FF0000"),
    ROSY_BROWN("ROSYBROWN", "#BC8F8F"),
    ROYAL_BLUE("ROYALBLUE", "#4169E1"),
    SADDLEB_ROWN("SADDLEBROWN", "#8B4513"),
    SALMON("SALMON", "#FA8072"),
    SANDY_BROWN("SANDYBROWN", "#F4A460"),
    SEA_GREEN("SEAGREEN", "#2E8B57"),
    SEA_SHELL("SEASHELL", "#FFF5EE"),
    SIENNA("SIENNA", "#A0522D"),
    SILVER("SILVER", "#C0C0C0"),
    SKY_BLUE("SKYBLUE", "#87CEEB"),
    SLATE_BLUE("SLATEBLUE", "#6A5ACD"),
    SLATE_GRAY("SLATEGRAY", "#708090"),
    SNOW("SNOW", "#FFFAFA"),
    SPRING_GREEN("SPRINGGREEN", "#00FF7F"),
    STEEL_BLUE("STEELBLUE", "#4682B4"),
    TAN("TAN", "#D2B48C", "LIGHTBROWN"),
    TEAL("TEAL", "#008080"),
    THISTLE("THISTLE", "#D8BFD8"),
    TOMATO("TOMATO", "#FF6347"),
    TRANSPARENT("TRANSPARENT", "#FFFFFF"),
    TURQUOISE("TURQUOISE", "#40E0D0"),
    VIOLET("VIOLET", "#EE82EE"),
    WHEAT("WHEAT", "#F5DEB3"),
    WHITE("WHITE", "#FFFFFF"),
    WHITE_SMOKE("WHITESMOKE", "#F5F5F5"),
    YELLOW("YELLOW", "#FFFF00"),
    YELLOW_GREEN("YELLOWGREEN", "#9ACD32");

    private static final Map<String, ClarityColorConstants> LOOKUP_BY_NAME
        = new HashMap<String, ClarityColorConstants>();

    private static final Map<String, ClarityColorConstants> LOOKUP_BY_MEANINGFUL_NAME
        = new HashMap<String, ClarityColorConstants>();

    private static final Map<String, ClarityColorConstants> LOOKUP_BY_HEX_CODE
        = new HashMap<String, ClarityColorConstants>();

    static
    {
        for (ClarityColorConstants msColor : EnumSet.allOf(ClarityColorConstants.class))
        {
            LOOKUP_BY_NAME.put(msColor.getName(), msColor);
            LOOKUP_BY_MEANINGFUL_NAME.put(msColor.getMeaningfulName(), msColor);
            LOOKUP_BY_HEX_CODE.put(msColor.getHexCode(), msColor);
        }
    }

    private String name;
    private String meaningfulName;
    private String hexCode;

    private ClarityColorConstants(String name, String hexCode)
    {
        this.name = name;
        this.hexCode = hexCode;
        this.meaningfulName = name;
    }

    private ClarityColorConstants(String name, String hexCode, String meaningfulName)
    {
        this(name, hexCode);
        this.meaningfulName = meaningfulName;
    }

    public String getName()
    {
        return name;
    }

    public String getMeaningfulName()
    {
        return meaningfulName;
    }

    public String getHexCode()
    {
        return hexCode;
    }

    public static ClarityColorConstants getByName(String name)
    {
        return LOOKUP_BY_NAME.get(name.toUpperCase());
    }

    public static ClarityColorConstants getByMeaningulName(String meaningfulName)
    {
        return LOOKUP_BY_MEANINGFUL_NAME.get(meaningfulName.toUpperCase());
    }

    public static ClarityColorConstants getByHexCode(String hexCode)
    {
        return LOOKUP_BY_HEX_CODE.get(hexCode);
    }

}

