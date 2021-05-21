package it.polimi.ingsw;

import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.gui.GUI;

import java.util.Scanner;

/**
 * MastersOfRenaissance class represents the main class of the game.
 * This class allows the user to start the server/cli/gui based on the input.
 *
 * @author Riccardo Izzo, Gabriele Lazzarelli, Andrea Nocito
 */
public class MastersOfRenaissance {
    /**
     * MastersOfRenaissance main method.
     * @param args main args.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Masters Of Renaissance!");
        System.out.println("Please, select an option:");
        System.out.println("- SERVER\n- CLI\n- GUI");
        switch (new Scanner(System.in).next().toLowerCase()) {
            case "server" -> {
                System.out.println("Starting server...");
                Server.main(null);
            }
            case "cli" -> {
                System.out.println("Starting CLI...");
                CLI.main(null);
            }
            case "gui" -> {
                System.out.println("Starting GUI...");
                GUI.main(null);
            }
            default -> {
                System.out.println("Invalid argument, restart the application.");
                System.exit(0);
            }
        }
    }
}
