package by.eparmon.boostnoteutils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum Colour {

    STRONG_BLUE("#1278BD", "strong blue"),
    BRIGHT_BLUE("#2BA5F7", "bright blue"),
    CYAN("#30D5C8", "cyan"),
    LIME_GREEN("#3FD941", "lime green"),
    MAGENTA("#B013A4", "magenta"),
    PINK("#E10051", "pink"),
    YELLOW("#E8D252", "yellow"),
    ORANGE("#FF8E00", "orange");

    private String code;
    private String name;

    public static Colour get(String name) {
        for (Colour colour : values())
            if (colour.name.equals(name))
                return colour;
        if (StringUtils.isNumeric(name)) {
            int ordinal = Integer.parseInt(name);
            if (ordinal >= 0 && ordinal < values().length)
                return values()[ordinal];
        }
        return null;
    }
}
