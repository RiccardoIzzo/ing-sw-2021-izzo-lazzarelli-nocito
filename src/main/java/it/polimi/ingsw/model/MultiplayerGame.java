package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;


public class MultiplayerGame extends Game{
    private ArrayList<Player> players = new ArrayList<>();
    private Player currPlayer;
    private Player firstPlayer;
    private int playerIndex;

    public Player getCurrPlayer(){
        return currPlayer;
    }

    public void setCurrPlayer(Player player){
        currPlayer = player;
    }

    public Player getFirstPlayer(){
        return firstPlayer;
    }

    public void setFirstPlayer(){
        playerIndex = new Random().nextInt(getNumPlayers());
        setCurrPlayer(players.get(playerIndex));
        firstPlayer = currPlayer;
    }

    public int getNumPlayers(){
        return players.size();
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public Player getPlayerByName(String name){
        for(Player player : players){
            if(player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

    public void addPlayer(String name){
        players.add(new Player(name));
    }

    public void removePlayer(String name){
        players.remove(getPlayerByName(name));
    }

    public void nextPlayer(){
        if(playerIndex == players.size() - 1){
            playerIndex = 0;
            setCurrPlayer(players.get(0));
        }
        else{
            playerIndex++;
            setCurrPlayer(players.get(playerIndex));
        }
    }
}
