package it.polimi.ingsw.view;

import it.polimi.ingsw.constants.Colors;
import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.events.servermessages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.network.NetworkHandler;

import java.util.*;

/**
 * CLI class manages the game with a Command Line Interface.
 *
 * @author Riccardo Izzo, Gabriele Lazzarelli
 */
public class CLI implements View{
    private final ActionHandler actionHandler;
    private String nickname;
    private ModelView modelView;
    private NetworkHandler network;

    /**
     * Constructor CLI creates a new CLI instance.
     */
    public CLI() {
        actionHandler = new ActionHandler(this);
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
                String lobbyID = getString();
                if(lobbies.containsKey(lobbyID)){
                    System.out.println("Already exists a lobby with this id! Try again.");
                    send(new GetLobbies());
                }
                else {
                    System.out.println("Insert the number of players:");
                    int numPlayers = getInt();
                    while(numPlayers > 4){
                        System.out.println("The maximum number of players is four, choose again.");
                        numPlayers = getInt();
                    }
                    send(new CreateLobby(lobbyID, numPlayers));
                }
            }
            case "join" ->{
                System.out.println("Insert the lobbyID of the lobby you want to join:");
                String lobbyID = getString();
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
        int firstId = getInt();
        while(!ids.contains(firstId)){
            System.out.println("Id not valid, choose again.");
            firstId = getInt();
        }

        System.out.println("Select the second card to discard by typing the id: ");
        int secondId = getInt();
        while(!ids.contains(secondId)){
            System.out.println("Id not valid, choose again.");
            secondId = getInt();
        }
        send(new SelectLeaderCards(firstId, secondId));
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
        boolean[] performedActions = new boolean[]{false, false, false, false};
        String[] actions = new String[]{"BASIC_ACTION", "ACTIVATE_LEADER", "DISCARD_LEADER", "END_TURN"};
        System.out.println("Now it's your turn!");

        while (!performedActions[3]) {
            System.out.println("Select your next action: ");
            for (int i = 0; i < 4; i++) {
                if (!performedActions[i]) System.out.println(i + ") " + actions[i]);
            }
            int action = getInt();
            while (action < 0 || action > 3) {
                System.out.println("Action not valid, try again.");
                action = getInt();
            }

            switch (action) {

                //TAKE_RESOURCE, BUY_CARD, ACTIVATE_PRODUCTION
                case 0 -> {
                    System.out.println("Select your basic action: ");
                    System.out.println("0) TAKE_RESOURCE\n1) BUY_CARD\n2) ACTIVATE_PRODUCTION");
                    action = getInt();
                    while (action < 0 || action > 2) {
                        System.out.println("Basic action not valid, try again.");
                        action = getInt();
                    }
                    switch (action) {
                        case 0 -> performedActions[0] = handleTakeResource();
                        case 1 -> performedActions[0] = handleBuyCard();
                        case 2 -> performedActions[0] = handleActivateProduction();
                    }
                }

                //ACTIVATE_LEADER
                case 1 -> performedActions[1] = handleActivateLeader();

                //DISCARD_LEADER
                case 2 -> performedActions[2] = handleDiscardLeader();

                //END_TURN
                case 3 -> performedActions[3] = handleEndTurn();
            }
        }
    }

    /**
     * Method handleTakeResource manages the "TAKE_RESOURCE" action with the player that takes resources from the market.
     * @return true if the action is completed, false otherwise.
     */
    @Override
    public boolean handleTakeResource() {
        showMarket(modelView.getMarketTray(), modelView.getSlideMarble());
        System.out.println("Insert the row/column index:");
        int index = new Scanner(System.in).nextInt();
        while(index < 1 || index > 7){
            System.out.println("Row/Column index must be between 1 and 7! Try again.");
            if(changeAction()) return false;
            index = new Scanner(System.in).nextInt();
        }
        if(index > 4) send(new TakeResources(index - 4, 1));
        else send(new TakeResources(index, 2));
        return true;
    }

    /**
     * Method handleBuyCard manages the "BUY_CARD" action, if the requirements are met the player buys a card from the grid.
     * @return true if the action is completed, false otherwise.
     */
    @Override
    public boolean handleBuyCard() {
        int id, index;
        ArrayList<Integer> grid = modelView.getGrid();
        showCards(grid);
        System.out.println("Select the card that you want to buy by typing the id: ");

        while(true){
            id = getInt();
            if(grid.contains(id)){
                index = grid.indexOf(id);
                if(!checkRequirements(id)){
                    System.out.println("Requirements not met, not enough resources.");
                    return false;
                }
                else break;
            }
            else System.out.println("Id not valid, choose again.");
        }
        send(new BuyCard(index / 4, index % 4));
        return true;
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
            } else {
                System.out.println("Slot number(s) out of index");
            }
            if (modelView.getMyDashboard().checkWarehouse()) break;
        }
    }

    /**
     * Method handleActivateProduction manages the "ACTIVATE_PRODUCTION" action, the player activate the production.
     * @return true if the action is completed, false otherwise.
     */
    @Override
    public boolean handleActivateProduction() {
        return true;
    }

    /**
     * Method handleActivateLeader manages the "ACTIVATE_LEADER" action, the player activates a leader card.
     * @return true if the action is completed, false otherwise.
     */
    @Override
    public boolean handleActivateLeader() {
        return true;
    }

    /**
     * Method handleDiscardLeader manages the "DISCARD_LEADER" action, the player select a leader card to discard.
     * @return true if the action is completed, false otherwise.
     */
    @Override
    public boolean handleDiscardLeader() {
        Set<Integer> ids = modelView.getMyDashboard().getLeaderCards().keySet();
        if(ids.size() == 0) return true;
        else{
            for(Integer id : ids){
                LeaderCard card = JsonCardsCreator.generateLeaderCard(id);
                System.out.println(card.toString());
            }
        }

        System.out.println("Select the leader card to discard by typing the id: ");
        int id = getInt();
        while(!ids.contains(id)){
            System.out.println("Id not valid, choose again.");
            if(changeAction()) return false;
            id = getInt();
        }
        send(new DiscardLeaderCard(id));
        return true;
    }

    /**
     * Method handleEndTurn manages the "END_TURN" action with the player that decides to end his turn.
     * @return true if the action is completed, false otherwise.
     */
    @Override
    public boolean handleEndTurn() {
        System.out.println("Your turn is finished.");
        send(new EndTurn());
        return true;
    }

    /**
     * Method changeAction asks the user if he wants to change action.
     * @return true if the user wants to change action, false otherwise.
     */
    private boolean changeAction(){
        System.out.println("Do you want to change action? y/n");
        String option = getInput("y|n");
        if(option.equals(("y"))) return true;
        else{
            System.out.println("Continue with your current action.");
            return false;
        }
    }

    /**
     * Method getInput gets the user input and checks the validity by comparing the input with the available options.
     * @param check available options.
     * @return the valid user input.
     */
    @Override
    public String getInput(String check) {
        String option = getString().toLowerCase();
        while(!option.matches(check)){
            System.out.println("This option is not available, try again.");
            option = getString();
        }
        return option;
    }

    /**
     * Method getString creates a new Scanner and reads a string.
     * @return the string.
     */
    private String getString(){
        return new Scanner(System.in).nextLine();
    }

    /**
     * Method getInt creates a new Scanner and reads an integer.
     * @return the integer.
     */
    private int getInt(){
        return new Scanner(System.in).nextInt();
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

    void showCards(Collection<Integer> cards) {
        for (Integer card: cards) {
            System.out.println(JsonCardsCreator.generateCard(card).toString());
        }
    }

    public static void showStrongbox(ResourceMap strongbox){
        System.out.printf("""
                            ╔══
                            ║   %s
                            ║   %s
                            ║   %s
                            ║   %s
                            ╚══
                        %n""", Resource.STONE + " x " + strongbox.getResource(Resource.STONE).toString(),
                        Resource.SERVANT + " x " + strongbox.getResource(Resource.SERVANT).toString(),
                        Resource.SHIELD + " x " + strongbox.getResource(Resource.SHIELD).toString(),
                        Resource.COIN + " x " + strongbox.getResource(Resource.COIN).toString());
    }

    public static void showWarehouse(ArrayList<Resource> warehouse, ArrayList<Resource> extraShelfResources){
        String firstExtraShelf = "", firstIndexPair = "", secondExtraShelf = "", secondIndexPair = "";
        if (extraShelfResources.size() > 0){
            firstIndexPair = "6   7";
            firstExtraShelf = String.format("⎣%s⎦ ⎣%s⎦  <-- 1st Leader card shelf {%s}", warehouse.get(6),warehouse.get(7), extraShelfResources.get(0));
        }
        if (extraShelfResources.size() > 1){
            secondIndexPair = "8   9";
            secondExtraShelf = String.format("⎣%s⎦ ⎣%s⎦  <-- 2nd Leader card shelf {%s}", warehouse.get(8),warehouse.get(9), extraShelfResources.get(1));
        }
        System.out.printf("""
                                                 +-0-+             %s
                                                 | %s |            %s
                                               +-1-+-2-+
                                               | %s | %s |           %s
                                             +-3-+-4-+-5-+        %s
                                             | %s | %s | %s |
                                             +---+---+---+
                                            10  11   12  13
                                            ⎣%s⎦ ⎣%s⎦ ⎣%s⎦ ⎣%s⎦
                        %n""", firstIndexPair, warehouse.get(0), firstExtraShelf, warehouse.get(1), warehouse.get(2), secondIndexPair, secondExtraShelf, warehouse.get(3), warehouse.get(4), warehouse.get(5),
                warehouse.get(10), warehouse.get(11), warehouse.get(12), warehouse.get(13));
    }

    /*
    f = faithMarker
    b = blackMarker
    p = popesTile
     */
    public static void showFaithTrack(int f, int b, Boolean[] p) {
        System.out.printf(
                """
                          +----╔═══════════════════╗----+    ╔═════════╗    +----╔═════════════════════════════╗
                          | %s ║ %s | %s | %s | %s ║ %s |    ║   %s   ║    | %s ║ %s | %s | %s | %s | %s | %s ║
                          +----╚════╗----+----╔════╝----+    ║         ║    +----╚═════════╗----+----╔═════════╝
                          | %s |    ║   %s   ║    | %s |    ║         ║    | %s |         ║   %s   ║         
                +----+----+----+    ║         ║    +----╔════╝----+----╚═════════╗         ║         ║         
                | %s | %s | %s |    ║         ║    | %s ║ %s | %s | %s | %s | %s ║         ║         ║         
                +----+----+----+    ╚═════════╝    +----╚════════════════════════╝         ╚═════════╝
                %n""", f(4,f,b),f(5,f,b),f(6,f,b),f(7,f,b),f(8,f,b),f(9,f,b),p(p[1],2),
                        f(18,f,b),f(19,f,b),f(20,f,b),f(21,f,b),f(22,f,b),f(23,f,b),f(24,f,b),
                        f(3,f,b),p(p[0], 2),f(10,f,b),f(17,f,b),p(p[2],4),f(0,f,b),f(1,f,b),
                        f(2,f,b),f(11,f,b),f(12,f,b),f(13,f,b),f(14,f,b),f(15,f,b),f(16,f,b));
    }

    private static String f(int tile, int faithMarker, int blackMarker){
        Colors backgroundColor = (tile == blackMarker) ? Colors.ANSI_BLACK_BACKGROUND: Colors.ANSI_RESET;
        Colors numberColor = (tile == faithMarker) ? Colors.ANSI_RED : Colors.ANSI_WHITE;
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
}
