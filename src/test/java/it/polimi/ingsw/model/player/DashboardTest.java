package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.MultiplayerGame;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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