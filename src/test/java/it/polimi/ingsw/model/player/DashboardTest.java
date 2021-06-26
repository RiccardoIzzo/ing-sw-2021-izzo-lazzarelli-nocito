package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.MultiplayerGame;

import org.junit.Before;

public class DashboardTest {
    Dashboard dashboard;

    /**
     * Method initialization create an instance of Dashboard
     */
    @Before
    public void initialization() {
        dashboard = new Dashboard(new MultiplayerGame());
    }
}