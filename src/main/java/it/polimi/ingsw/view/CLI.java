package it.polimi.ingsw.view;

import it.polimi.ingsw.constants.Colors;
import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.events.servermessages.InvalidNickname;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.ValidNickname;
import it.polimi.ingsw.model.JsonCardsCreator;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.model.card.ExtraShelfLeaderCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.network.NetworkHandler;

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
            case "refresh" ->{
                send(new GetLobbies());
            }
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
        System.out.println("It's your turn!");
        System.out.println("Select your next action: \n- TAKE_RESOURCE\n- BUY_CARD\n- ACTIVATE_PRODUCTION\n- ACTIVATE_LEADER\n- END_TURN");
        String action = getInput("take_resource|buy_card|activate_production|activate_leader|end_turn");
        switch(action){
            case "take_resource" -> handleTakeResource();
            case "buy_card" -> {

            }
            case "activate_production" -> {

            }
            case "activate_leader" -> {

            }
            case "end_turn" -> {

            }
        }
    }

    @Override
    public void handleTakeResource() {
        System.out.println("*** MARKET ***");
        System.out.println(modelView.getMarketTray().toString());
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
                 __  __           _             ___   __ ____                  _                             \s
                |  \\/  | __ _ ___| |_ ___ _ __ / _ \\ / _|  _ \\ ___ _ __   __ _(_)___ ___  __ _ _ __   ___ ___\s
                | |\\/| |/ _` / __| __/ _ \\ '__| | | | |_| |_) / _ \\ '_ \\ / _` | / __/ __|/ _` | '_ \\ / __/ _ \\
                | |  | | (_| \\__ \\ ||  __/ |  | |_| |  _|  _ <  __/ | | | (_| | \\__ \\__ \\ (_| | | | | (_|  __/
                |_|  |_|\\__,_|___/\\__\\___|_|   \\___/|_| |_| \\_\\___|_| |_|\\__,_|_|___/___/\\__,_|_| |_|\\___\\___|
                """);
        System.out.println("Insert server ip address:");
        String ip = new Scanner(System.in).next();
        System.out.println("Insert server port:");
        int port = new Scanner(System.in).nextInt();
        CLI cli = new CLI();
        cli.setupGame(ip, port);
    }

    void showDashboard(){
        // print dashboard
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


    void showMarket() {
        // show market and ask if player wants to take resources
    }

    void takeResources() {

    }

    void arrangeShelves() {
        // edit shelves configuration
    }

    void showGrid() {
        // show grid see if the player can/wants to buy a card
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

    void buyCard() {

    }

    void showAvailableProductions() {
        // show available productions and see if the player can/wants to start one
    }

    void startProduction() {

    }

    void endTurn() {

    }
}
