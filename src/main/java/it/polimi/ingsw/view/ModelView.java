package it.polimi.ingsw.view;

import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;

import java.util.ArrayList;
import java.util.Map;

import static it.polimi.ingsw.constants.GameConstants.*;
import static it.polimi.ingsw.constants.PlayerConstants.*;

public class ModelView {
    ArrayList<DashboardView> dashboards;

    private class DashboardView{
        private String nickname;
        private Map<Integer, Boolean> leaderCards;
        private ArrayList<Integer> developmentCards;
        private ResourceMap strongbox;
        private ArrayList<Resource> warehouse;
        private ArrayList<Resource> extraShelfResources;
        private ArrayList<Integer> faithTrack;

        public DashboardView(String nickname, Map<Integer, Boolean> leaderCards, ArrayList<Integer> developmentCards, ResourceMap strongbox, ArrayList<Resource> warehouse, ArrayList<Resource> extraShelfResources, ArrayList<Integer> faithTrack) {
            this.nickname = nickname;
            this.leaderCards = leaderCards;
            this.developmentCards = developmentCards;
            this.strongbox = strongbox;
            this.warehouse = warehouse;
            this.extraShelfResources = extraShelfResources;
            this.faithTrack = faithTrack;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void updateDashboard(String propertyName, Object objectToUpdate) {
            switch (propertyName) {
                case LEADER_ACTIVATION -> leaderCards.put((Integer) objectToUpdate, true);
                case SELECT_LEADERS, DISCARD_LEADER -> leaderCards = (Map<Integer, Boolean>) objectToUpdate;
                case STRONGBOX_CHANGE -> strongbox = (ResourceMap) objectToUpdate;
                case TEMPORARY_SHELF_CHANGE, SHELF_CHANGE -> warehouse = (ArrayList<Resource>) objectToUpdate;
                case FAITH_MARKER_POSITION, BLACK_MARKER_POSITION -> faithTrack = (ArrayList<Integer>) objectToUpdate;
            }
        }
    }

    private String currPlayer;
    private ArrayList<Integer> grid;
    private MarbleColor slideMarble;
    private ArrayList<MarbleColor> marketTray;
    private Integer tokenDrawn;

    public ModelView() {
        this.dashboards = new ArrayList<>();
        this.currPlayer = "";
        this.grid = new ArrayList<>(12);
        this.slideMarble = null;
        this.marketTray = new ArrayList<>(12);
        this.tokenDrawn = null;
    }

    /*
    Retrieve the model view after loosing connection
     */
    public ModelView(ModelView modelView) {
        this.dashboards = modelView.dashboards;
        this.currPlayer = modelView.currPlayer;
        this.grid = modelView.grid;
        this.slideMarble = modelView.slideMarble;
        this.marketTray = modelView.marketTray;
        this.tokenDrawn = modelView.tokenDrawn;
    }

    public ArrayList<DashboardView> getDashboards() {
        return dashboards;
    }

    public ArrayList<Integer> getGrid() {
        return grid;
    }

    public ArrayList<MarbleColor> getMarketTray() {
        return marketTray;
    }

    public void setDashboards(ArrayList<DashboardView> dashboards) {
        this.dashboards = dashboards;
    }

    public void setGrid(ArrayList<Integer> grid) {
        this.grid = grid;
    }

    public void setMarketTray(ArrayList<MarbleColor> marketTray) {
        this.marketTray = marketTray;
    }

    public void updateModelView(String nickname, String propertyName, Object objectToUpdate) {
        if (nickname != null) {
            DashboardView dashboardView = dashboards.stream().filter(dashboard -> dashboard.getNickname().equals(nickname)).findAny().orElse(null);
            dashboardView.updateDashboard(propertyName, objectToUpdate);
        } else {
            switch (propertyName) {
                case END_TURN -> currPlayer = (String) objectToUpdate;
                case TOKEN_DRAWN -> tokenDrawn = (Integer) objectToUpdate;
                case MARKET_CHANGE -> marketTray = (ArrayList<MarbleColor>) objectToUpdate;
                case SLIDE_MARBLE -> slideMarble = (MarbleColor) objectToUpdate;
            }
        }
    }
}
