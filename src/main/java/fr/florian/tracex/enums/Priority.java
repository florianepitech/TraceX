package fr.florian.tracex.enums;

import fr.florian.tracex.utils.ConsoleColors;

public enum Priority {

    ALL("ALL", "ALL  ", ConsoleColors.BLACK, 0),
    TRACE("TRACE", "TRACE", ConsoleColors.WHITE, 1),
    DEBUG("DEBUG", "DEBUG", ConsoleColors.BLUE, 2),
    INFO("INFO", "INFO ", ConsoleColors.YELLOW, 3),
    WARN("WARN", "WARN ", ConsoleColors.RED, 4),
    ERROR("ERROR", "ERROR", ConsoleColors.RED, 5),
    FATAL("FATAL", "FATAL", ConsoleColors.RED_BOLD, 6);

    private String name, prefix, color;
    private int level;

    Priority(String name, String prefix, String color, int level) {
        this.name = name;
        this.prefix = prefix;
        this.color = color;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public String getPrefixColored() {
        return getColor() + getPrefix() + ConsoleColors.RESET;
    }
}