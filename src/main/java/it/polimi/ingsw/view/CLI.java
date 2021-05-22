package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.events.servermessages.InvalidNickname;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.ValidNickname;
import it.polimi.ingsw.network.NetworkHandler;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

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

    public String getInput(ArrayList<String> acceptedInputs) {
        String input = new Scanner(System.in).next();
        while(acceptedInputs.contains(input)){
            System.out.println("Invalid input, type <Help> to see options\n");
            input = new Scanner(System.in).next();
            if (input.equals("Help")) {
                for (String acceptedInput : acceptedInputs){
                    System.out.println(acceptedInput);
                }
            }
        }
        return input;
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
     * Finally it manages two cases: the creation of a lobby and the registration of a player in a lobby.
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
        System.out.println("- CREATE\n- JOIN");
        String choice = input.nextLine();
        switch (choice.toLowerCase()){
            case "create" ->{
                System.out.println("Insert the lobbyID:");
                String lobbyID = input.nextLine();
                System.out.println("Insert the number of players:");
                int numPlayers = input.nextInt();
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
        }
    }

    @Override
    public void handleLeaders() {
        //ArrayList<Integer> leaders = modelView.getMyDashboard().
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

    @Override
    public void selectBonusResource(int amount) {
        System.out.println("Select " + amount + " bonus resource.");
    }

    /**
     * CLI main method.
     * @param args main args.
     */
    public static void main(String[] args) {
        System.out.println("MASTERS OF RENAISSANCE - CLI");
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

    void showLeaderCards() {
        // show leader card and see if the player can/wants to play a leader card
    }

    //
    void playLeaderCard() {

    }


    void showMarket() {
        // show market and ask if player wants to take resources
    }
    //
    void takeResources() {

    }

    void arrangeShelves() {
        // edit shelves configuration
    }


    void showGrid() {
        // show grid see if the player can/wants to buy a card
    }
    //
    void buyCard() {

    }
    //
    void showAvailableProductions() {
        // show available productions and see if the player can/wants to start one
    }
    void startProduction() {

    }

    //
    void endTurn() {

    }
    void showResults() {

    }
    void updateFaithTrack() {

    }
}
