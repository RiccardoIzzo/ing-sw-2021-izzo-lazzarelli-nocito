package it.polimi.ingsw.view;

import it.polimi.ingsw.constants.Colors;
import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.events.servermessages.InvalidNickname;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.ValidNickname;
import it.polimi.ingsw.model.JsonCardsCreator;
import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.ResourceRequirement;
import it.polimi.ingsw.network.NetworkHandler;

import java.lang.reflect.Array;
import java.util.*;

/**
 * CLI class manages the game with a Command Line Interface.
 */
public class CLI implements View{
    private final ActionHandler actionHandler;
    private String nickname;
    private ModelView modelView;
    private NetworkHandler network;
    private final Scanner input;

    /**
     * Constructor CLI creates a new CLI instance.
     */
    public CLI() {
        actionHandler = new ActionHandler(this);
        input = new Scanner(System.in);
    }

    /**
     * Method getNickname returns the player's nickname.
     * @return player's nickname.
     */
    @Override
    public String getNickname() {
        return nickname;
    }

    /**
     * Method setNickname asks for a nickname and sends a SetNickname message to the server with the selected nickname.
     */
    @Override
    public void setNickname() {
        System.out.println("Choose your nickname:");
        nickname = new Scanner(System.in).next();
        send(new SetNickname(nickname));
    }

    /**
     * Method setModelView sets the modelView for the current view.
     * @param modelView the modelView.
     */
    @Override
    public void setModelView(ModelView modelView) {
        this.modelView = modelView;
    }

    /**
     * Method getInput gets the user input and checks the validity by comparing the input with the available options.
     * @param check available options.
     * @return the valid user input.
     */
    @Override
    public String getInput(String check) {
        String option = new Scanner(System.in).nextLine().toLowerCase();
        while(!option.matches(check)){
            System.out.println("This option is not available, try again.");
            option = input.nextLine();
        }
        return option;
    }

    /**
     * Method handleNickname manages the two possible answers to a SetNickname message, ValidNickname and InvalidNickname.
     * @param message ServerMessage to handle.
     */
    @Override
    public void handleNickname(ServerMessage message){
        if(message instanceof ValidNickname){
            System.out.println("The selected nickname is valid and has been successfully registered.");
            send(new GetLobbies());
        }
        else if(message instanceof InvalidNickname){
            System.out.println("This nickname is not valid! Try again.");
            setNickname();
        }
    }

    /**
     * Method handleLobbies manages the lobby system.
     * First of all it prints the list of available lobbies with the corresponding maximum number of players.
     * Finally it manages three cases: create a lobby, register a player to a lobby and refresh the list of available lobbies.
     * @param lobbies map that associates the lobby id to the maximum number of players for that lobby.
     */
    @Override
    public void handleLobbies(Map<String, Integer> lobbies){
        System.out.println("*** LOBBIES ***");
        for(String lobbyID : lobbies.keySet()){
            System.out.println("--------------------");
            System.out.println("ID: " + lobbyID);
            System.out.println("Number of players: " + lobbies.get(lobbyID));
            System.out.println("--------------------");
        }
        System.out.println("\nDo you want to create your own lobby or join an existing one?");
        System.out.println("- CREATE\n- JOIN\n- REFRESH");
        String choice = getInput("create|join|refresh");
        switch (choice){
            case "create" ->{
                System.out.println("Insert the lobbyID:");
                String lobbyID = input.nextLine();
                System.out.println("Insert the number of players:");
                int numPlayers = input.nextInt();
                while(numPlayers > 4){
                    System.out.println("The maximum number of players is four, choose again.");
                    numPlayers = input.nextInt();
                }
                if(!lobbies.containsKey(lobbyID)){
                    send(new CreateLobby(lobbyID, numPlayers));
                }
                else {
                    System.out.println("Already exists a lobby with this id! Try again.");
                    send(new GetLobbies());
                }
            }
            case "join" ->{
                System.out.println("Insert the lobbyID of the lobby you want to join:");
                String lobbyID = input.nextLine();
                if(lobbies.containsKey(lobbyID)){
                    send(new JoinLobby(lobbyID));
                }
                else{
                    System.out.println("Error, this lobby doesn't exist.");
                    send(new GetLobbies());
                }
            }
            case "refresh" -> send(new GetLobbies());
        }
    }

    /**
     * Method handleLeaders manages the selection of the leader cards at the beginning of the game.
     */
    @Override
    public void handleLeaders() {
        Set<Integer> ids = modelView.getMyDashboard().getLeaderCards().keySet();
        System.out.println("Select two out of four leader cards...\n");
        for(Integer id : ids){
            LeaderCard card = JsonCardsCreator.generateLeaderCard(id);
            System.out.println(card.toString());
        }

        System.out.println("Select the first card to discard by typing the id: ");
        int firstId = input.nextInt();
        while(!ids.contains(firstId)){
            System.out.println("Id not valid, choose again.");
            firstId = input.nextInt();
        }

        System.out.println("Select the second card to discard by typing the id: ");
        int secondId = input.nextInt();
        while(!ids.contains(secondId)){
            System.out.println("Id not valid, choose again.");
            secondId = input.nextInt();
        }
        send(new DiscardLeaderCard(firstId, secondId));
    }

    /**
     * Method handleBonusResources manages the bonus resource system based on the players order at the beginning of the game.
     * @param amount amount of bonus resources that the player can choose.
     */
    @Override
    public void handleBonusResource(int amount) {
        ResourceMap bonusResources = new ResourceMap();
        for(int n = 0; n < amount; n++){
            System.out.println("Select a bonus resource: \n- STONE\n- COIN\n- SHIELD\n- SERVANT\n");
            String resource = getInput("stone|coin|shield|servant");
            switch (resource){
                case "stone" -> bonusResources.modifyResource(Resource.STONE, 1);
                case "coin" -> bonusResources.modifyResource(Resource.COIN, 1);
                case "shield" -> bonusResources.modifyResource(Resource.SHIELD, 1);
                case "servant" -> bonusResources.modifyResource(Resource.SERVANT, 1);
            }
        }
        send(new SendBonusResources(bonusResources));
    }

    /**
     * Method handleTurn manages a player's turn and the available user actions.
     */
    @Override
    public void handleTurn() {
        boolean[] availableActions = new boolean[]{true, true, true, true};
        String[] actions = new String[]{"BASIC_ACTION", "ACTIVATE_LEADER", "DISCARD_LEADER", "END_TURN"};
        System.out.println("Now it's your turn!");

        while(availableActions[3]){
            System.out.println("Select your next action: ");
            for(int i = 0; i < 4; i++){
                if(availableActions[i]) System.out.println(i + ") " + actions[i]);
            }

            int action = input.nextInt();
            while(action < 0 || action > 3){
                System.out.println("Action not valid, try again.");
                action = input.nextInt();
            }

            switch(action){

                //TAKE_RESOURCE, BUY_CARD, ACTIVATE_PRODUCTION
                case 0 -> {
                    System.out.println("Select your basic action: ");
                    System.out.println("0) TAKE_RESOURCE\n1) BUY_CARD\n2) ACTIVATE_PRODUCTION");
                    action = input.nextInt();
                    while(action < 0 || action > 2){
                        System.out.println("Basic action not valid, try again.");
                        action = input.nextInt();
                    }
                    switch(action){
                        case 0 -> handleTakeResource();
                        case 1 -> handleBuyCard();
                        case 2 -> handleActivateProduction();
                    }
                    availableActions[0] = false;
                }

                //ACTIVATE_LEADER
                case 1 ->{
                    handleActivateLeader();
                    availableActions[1] = false;
                }

                //DISCARD_LEADER
                case 2 -> {
                    handleDiscardLeader();
                    availableActions[2] = false;
                }

                //END_TURN
                case 3 -> {
                    handleEndTurn();
                    availableActions[3] = false;
                }
            }
        }
    }

    /**
     * Method handleTakeResource manages the "TAKE_RESOURCE" action with the player that takes resources from the market.
     */
    @Override
    public void handleTakeResource() {
        showMarket(modelView.getMarketTray(), modelView.getSlideMarble());
        System.out.println("Insert the row/column index:");
        int index;
        index = new Scanner(System.in).nextInt();
        while(index < 1 || index > 7){
            System.out.println("Row/Column index must be between 1 and 7! Try again.");
            index = new Scanner(System.in).nextInt();
        }
        if(index > 4) send(new TakeResources(index - 4, 1));
        else send(new TakeResources(index, 2));
    }

    /**
     * Method handleBuyCard manages the "BUY_CARD" action, if the requirements are met the player buys a card from the grid.
     */
    @Override
    public void handleBuyCard() {
        int id, index;
        ArrayList<Integer> grid = modelView.getGrid();
        showCards(grid);
        System.out.println("Select the card that you want to buy by typing the id: ");

        while(true){
            id = input.nextInt();
            if(grid.contains(id)){
                index = grid.indexOf(id);
                if(!checkRequirements(id)){
                    System.out.println("Requirements not met, not enough resources.");
                    break;
                }
            }
            else System.out.println("Id not valid, choose again.");
        }

        send(new BuyCard(index / 4, index % 4));
    }

    /**
     * Method checkRequirements checks that the total amount of resources owned by the player is enough to meet the card requirements.
     * @param cardID id of the selected card.
     * @return true if the requirements are met, false otherwise.
     */
    private boolean checkRequirements(int cardID){
        Map<Resource, Integer> myResources = new HashMap<>();
        ArrayList<Resource> cardRequirement = ((ResourceRequirement) JsonCardsCreator.generateDevelopmentCard(cardID).getRequirement()).getResources().asList();
        for(Resource resource : Resource.values()){
            int amount = 0;
            amount += Collections.frequency(modelView.getMyDashboard().getWarehouse(), resource);
            amount += Collections.frequency(modelView.getMyDashboard().getStrongbox().asList(), resource);
            myResources.put(resource, amount);
        }

        for(Resource resource : Resource.values()){
            if(myResources.get(resource) < Collections.frequency(cardRequirement, resource)) return false;
        }
        return true;
    }

    @Override
    public void handleTemporaryShelf() {
        //TODO
        System.out.println("Place your resources on the shelves");
        int firstSlot, secondSlot;
        while(true){
            showWarehouse(modelView.getMyDashboard().getWarehouse(),modelView.getMyDashboard().getExtraShelfResources());
            System.out.println("Select two slots: ");
            firstSlot = new Scanner(System.in).nextInt();
            secondSlot = new Scanner(System.in).nextInt();
            if(firstSlot >= 0 && firstSlot < 15 && secondSlot >= 0 && secondSlot < 15){
                modelView.getMyDashboard().swapResources(firstSlot, secondSlot);
            }
            if(modelView.getMyDashboard().getWarehouse().subList(10, 14).size() > 0){
                if(modelView.getMyDashboard().getWarehouse().subList(3, 5).size() > 0){
                    //modelView.getMyDashboard().getWarehouse().subList(3, 5).stream().reduce((a, b) -> a)
                }
            }
        }
    }

    @Override
    public void handleActivateProduction() {

    }

    @Override
    public void handleActivateLeader() {

    }

    @Override
    public void handleDiscardLeader() {

    }

    /**
     * Method handleEndTurn manages the "END_TURN" action with the player that decides to end his turn.
     */
    @Override
    public void handleEndTurn() {
        System.out.println("Your turn is finished.");
        send(new EndTurn());
    }

    /**
     * Method printText prints the given text on the terminal.
     * @param text text to print.
     */
    @Override
    public void printText(String text){
        System.out.println(text);
    }

    /**
     * Method send dispatch a message to the server.
     * @param message the ClientMessage to send.
     */
    @Override
    public void send(ClientMessage message) {
        network.sendToServer(message);
    }

    /**
     * Method setupGame manages the initial phase of the game.
     * @param ip server ip address.
     * @param port server port.
     */
    private void setupGame(String ip, int port) {
        this.network = new NetworkHandler(ip, port);
        network.setConnection(actionHandler);
        setNickname();
    }

    /**
     * CLI main method.
     * @param args main args.
     */
    public static void main(String[] args) {
        System.out.println(
                """
                 __  __           _                 ___   __ ____                  _                             \s
                |  \\/  | __ _ ___| |_ ___ _ __ ___ / _ \\ / _|  _ \\ ___ _ __   __ _(_)___ ___  __ _ _ __   ___ ___\s
                | |\\/| |/ _` / __| __/ _ \\ '__/ __| | | | |_| |_) / _ \\ '_ \\ / _` | / __/ __|/ _` | '_ \\ / __/ _ \\
                | |  | | (_| \\__ \\ ||  __/ |  \\__ \\ |_| |  _|  _ <  __/ | | | (_| | \\__ \\__ \\ (_| | | | | (_|  __/
                |_|  |_|\\__,_|___/\\__\\___|_|  |___/\\___/|_| |_| \\_\\___|_| |_|\\__,_|_|___/___/\\__,_|_| |_|\\___\\___|
                """);
        System.out.println("Insert server ip address:");
        String ip = new Scanner(System.in).next();
        System.out.println("Insert server port:");
        int port = new Scanner(System.in).nextInt();
        CLI cli = new CLI();
        cli.setupGame(ip, port);
    }

    void showDashboard(ModelView.DashboardView dashboardView){
        System.out.println("\n*** FAITHTRACK ***");
        showFaithTrack(dashboardView.getFaithMarker(), dashboardView.getBlackMarker(), dashboardView.getPopesFavorTiles());
        System.out.println("\n*** WAREHOUSE ***");
        showWarehouse(dashboardView.getWarehouse(), dashboardView.getExtraShelfResources());
        System.out.println("\n*** LEADER CARDS ***");
        showCards(dashboardView.getLeaderCards().keySet());
        System.out.println("\n*** AVAILABLE PRODUCTION ***");
        showCards(dashboardView.getAvailableProduction());
    }

    void chooseAction() {
        // ask whichAction

        // showLeaderCards
        // showMarker
        // showGrid
        // arrangeShelves
        // showAvailableProductions

        // execute actions and then show updated dashboard
    }

    void playLeaderCard() {

    }


    public static void showMarket(ArrayList<MarbleColor> marketTray, MarbleColor slideMarble) {
        System.out.printf("Slide marble = %s\n\n", slideMarble.toString());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(marketTray.get(i+j).toString() + "\t");
            }
            System.out.printf("<-- %d\n", 7 - i);
        }
        System.out.println(
                """
                ^   ^   ^   ^
                |   |   |   |
                1   2   3   4
                """
        );
    }

    void takeResources() {

    }

    void arrangeShelves() {
        // edit shelves configuration
    }

    void showCards(Collection<Integer> cards) {
        for (Integer card: cards) {
            System.out.println(JsonCardsCreator.generateCard(card).toString());
        }
    }

    public void showWarehouse(ArrayList<Resource> warehouse, ArrayList<Resource> extraShelfResources){
        for (int shelf = 1; shelf <=5; shelf++) {
            int index = (shelf*shelf - shelf +2)/2 - 1;
            System.out.print("  ".repeat(5 - shelf));
            if (shelf == 4) { //leaderCards' shelves
                for (int resourceSlot = index; resourceSlot < index + shelf; resourceSlot++){
                    Resource resourceExtraShelf = null;
                    if (resourceSlot < 8) {
                        if (extraShelfResources.size() > 0) resourceExtraShelf = extraShelfResources.get(0);
                    } else {
                        if (extraShelfResources.size() > 1) resourceExtraShelf = extraShelfResources.get(1);
                        else resourceExtraShelf = null;
                    }
                    Colors color, r = Colors.ANSI_RESET;
                    if (resourceExtraShelf == Resource.STONE) {
                        color = Colors.ANSI_WHITE;
                    } else if (resourceExtraShelf == Resource.SERVANT) {
                        color = Colors.ANSI_PURPLE;
                    } else if (resourceExtraShelf == Resource.SHIELD) {
                        color = Colors.ANSI_CYAN;
                    } else if (resourceExtraShelf == Resource.COIN) {
                        color = Colors.ANSI_YELLOW;
                    } else {
                        color = Colors.ANSI_BLACK;
                    }
                    if (warehouse.get(resourceSlot) != null) {
                        System.out.print(color+"⎣"+r + String.format("%s", warehouse.get(resourceSlot).toString()) + color+"⎦"+r);
                    } else {
                        System.out.print(color+"⎣"+r + " " + color+"⎦"+r);
                    }
                }
            }
            else {
                for (int resourceSlot = index; resourceSlot < index + shelf; resourceSlot++){
                    if (warehouse.get(resourceSlot) != null) {
                        System.out.printf("⎣%s⎦", warehouse.get(resourceSlot).toString());
                    } else {
                        System.out.print("⎣  ⎦");
                    }
                }
            }
            System.out.print("\n");
        }
    }

    /*
    f = faithMarker
    b = blackMarker
    p = popesTile
     */
    public static void showFaithTrack(int f, int b, Boolean[] p) {
        System.out.printf("""
                        +---------+----+----+----+----+----+----+----+---------+----+----+----+----+----+----+----+----+
                        |         | %s | %s | %s | %s | %s | %s |    |   %s   |    | %s | %s | %s | %s | %s | %s | %s |
                        |         +----+----+----+----+----+----+    |         |    +----+----+----+----+----+----+----+
                        |         | %s |    |   %s   |    | %s |    |         |    | %s |         |   %s   |         |
                        +----+----+----+    |         |    +----+----+----+----+----+----+         |         |         |
                        | %s | %s | %s |    |         |    | %s | %s | %s | %s | %s | %s |         |         |         |
                        +----+----+----+----+---------+----+----+----+----+----+----+----+---------+---------+---------+
                        %n""", f(4,f,b),f(5,f,b),f(6,f,b),f(7,f,b),f(8,f,b),f(9,f,b),p(p[1],2),
                                f(18,f,b),f(19,f,b),f(20,f,b),f(21,f,b),f(22,f,b),f(23,f,b),f(24,f,b),
                                f(3,f,b),p(p[0], 2),f(10,f,b),f(17,f,b),p(p[2],4),f(0,f,b),f(1,f,b),
                                f(2,f,b),f(11,f,b),f(12,f,b),f(13,f,b),f(14,f,b),f(15,f,b),f(16,f,b));
    }
    private static String f(int tile, int faithMarker, int blackMarker){
        Colors backgroundColor = (tile == blackMarker) ? Colors.ANSI_BLACK_BACKGROUND: Colors.ANSI_RESET;
        Colors numberColor = (tile == faithMarker) ? Colors.ANSI_CYAN : Colors.ANSI_BLACK;
        String tileColor;
        if (tile < 10) {
            tileColor = String.format("%s%s0%d%s", backgroundColor, numberColor, tile, Colors.ANSI_RESET);
        } else {
            tileColor = String.format("%s%s%d%s", backgroundColor, numberColor, tile, Colors.ANSI_RESET);
        }
        return tileColor;
    }
    private static String p(boolean popeTileActive, int points) {
        Colors numberColor = popeTileActive ? Colors.ANSI_YELLOW_BACKGROUND : Colors.ANSI_RED_BACKGROUND;
        return String.format("%s%sVP%s", numberColor, points, Colors.ANSI_RESET);
    }

    void startProduction() {

    }
}
