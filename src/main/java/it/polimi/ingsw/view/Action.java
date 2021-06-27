package it.polimi.ingsw.view;

/**
 * Enum Action provides an enumeration of all the possible user actions.
 */
public enum Action {
    TAKE_RESOURCE(false),
    BUY_CARD(false),
    ACTIVATE_PRODUCTION(false),
    ACTIVATE_LEADER(false),
    DISCARD_LEADER(false),
    SHOW_DASHBOARD(false),
    SHOW_GRID(false),
    END_TURN(false);

    boolean enabled;

    /**
     * Constructor Action creates a new Action instance.
     * @param enabled action status.
     */
    Action(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Method toString returns a string representation for the action.
     * @return the string representation.
     */
    @Override
    public String toString() {
        return this.ordinal() + ") " + this.name();
    }
}
