package it.polimi.ingsw.constants;

/**
 * Class Colors contains all the ANSI colors used in the game.
 */
public enum Colors {
    ANSI_RESET("\u001B[0m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_CYAN("\u001B[36m"),
    ANSI_WHITE("\u001B[37m"),
    WHITE_BRIGHT("\033[0;97m"),
    CYAN_BRIGHT("\033[0;96m"),
    BLACK_BRIGHT("\033[0;90m"),
    ANSI_YELLOW_BACKGROUND("\u001B[43m"),
    ANSI_BLACK_BACKGROUND("\u001B[40m"),
    ANSI_RED_BACKGROUND("\u001B[41m"),
    ANSI_BLUE_BACKGROUND("\u001B[44m"),
    ANSI_GREEN_BOLD("\u001B[32;1m");

    private final String escape;

    Colors(String escape) {
        this.escape = escape;
    }

    @Override
    public String toString() {
        return this.escape;
    }
}
