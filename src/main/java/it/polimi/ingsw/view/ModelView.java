package it.polimi.ingsw.view;

import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.constants.GameConstants.*;
import static it.polimi.ingsw.constants.PlayerConstants.*;

public class ModelView {
    ArrayList<DashboardView> dashboards;
    private String myNickname;
    private String currPlayer;
    private ArrayList<Integer> grid;
    private MarbleColor slideMarble;
    private ArrayList<MarbleColor> marketTray;
    private Integer tokenDrawn;

    public ModelView(ArrayList<String> players, String myNickname) {
        this.dashboards = new ArrayList<>();
        for (String player: players) {
            dashboards.add(new DashboardView(player));
        }
        this.myNickname = myNickname;
        this.currPlayer = "";
        this.grid = new ArrayList<>(12);
        this.slideMarble = null;
        this.marketTray = new ArrayList<>(12);
        this.tokenDrawn = null;
    }

    public String getMyNickname() {
        return myNickname;
    }

    public String getCurrPlayer() {
        return currPlayer;
    }

    public MarbleColor getSlideMarble() {
        return slideMarble;
    }

    public Integer getTokenDrawn() {
        return tokenDrawn;
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

    public DashboardView getMyDashboard() {
        return dashboards.stream().filter(dashboardView -> dashboardView.getNickname().equals(myNickname)).findAny().orElse(null);
    }

    public void updateModelView(String playerSource, String propertyName, Object objectToUpdate) {
        if (playerSource != null) {
            DashboardView dashboardView = dashboards.stream().filter(dashboard -> dashboard.getNickname().equals(playerSource)).findAny().orElse(null);
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

    private class DashboardView{
        private String nickname;
        private Map<Integer, Boolean> leaderCards;
        private ArrayList<Integer> developmentCards;
        private ResourceMap strongbox;
        private ArrayList<Resource> warehouse;
        private ArrayList<Resource> extraShelfResources;
        private ArrayList<Integer> faithTrack;

        public DashboardView(String nickname) {
            this.nickname = nickname;
            this.leaderCards = new HashMap<>();
            this.developmentCards = new ArrayList<>();
            this.strongbox = new ResourceMap();
            this.warehouse = new ArrayList<>();
            this.extraShelfResources = new ArrayList<>();
            this.faithTrack = new ArrayList<>();
        }

        public String getNickname() {
            return nickname;
        }

        public Map<Integer, Boolean> getLeaderCards() {
            return leaderCards;
        }

        public ArrayList<Integer> getDevelopmentCards() {
            return developmentCards;
        }

        public ResourceMap getStrongbox() {
            return strongbox;
        }

        public ArrayList<Resource> getWarehouse() {
            return warehouse;
        }

        public ArrayList<Resource> getExtraShelfResources() {
            return extraShelfResources;
        }

        public ArrayList<Integer> getFaithTrack() {
            return faithTrack;
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
}
