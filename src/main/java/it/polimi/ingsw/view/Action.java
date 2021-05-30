package it.polimi.ingsw.view;

public enum Action {
    TAKE_RESOURCE(false),
    BUY_CARD(false),
    ACTIVATE_PRODUCTION(false),
    ACTIVATE_LEADER(false),
    DISCARD_LEADER(false),
    SHOW_DASHBOARD(false),
    END_TURN(false);

    boolean enabled;

    Action(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return this.ordinal() + ") " + this.name();
    }
}
