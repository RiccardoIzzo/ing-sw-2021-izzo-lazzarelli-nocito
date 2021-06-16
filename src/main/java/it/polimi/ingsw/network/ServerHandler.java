package it.polimi.ingsw.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ServerHandler class handles multi-client connections.
 *
 * @author Riccardo Izzo
 */
public class ServerHandler implements Runnable{
    private final Server server;
    private ServerSocket serverSocket;
    private final ExecutorService executors;

    /**
     * Constructor ServerHandler creates a new instance of ServerHandler and initializes a thread pool.
     * Finally it creates a serverSocket that waits for client connections.
     * @param server instance of Server.
     * @param port server port to which clients must connect.
     */
    public ServerHandler(Server server, int port){
        this.server = server;
        executors = Executors.newCachedThreadPool();
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            System.err.println("Cannot initialize the server socket!");
            System.exit(0);
        }
    }

    /**
     * Method acceptConnection waits for client connection and creates a thread for each connection accepted.
     */
    public void acceptConnection(){
        while(true){
            try{
                Socket socket = serverSocket.accept();
                ClientConnection clientConnection = new ClientConnection(server, socket);
                clientConnection.setStatus(true);
                executors.execute(clientConnection);
                System.out.println("Connection established with " + socket.getRemoteSocketAddress().toString());
            } catch (IOException e){
                System.err.println("Can't accept connection anymore");
            }
        }
    }

    /**
     * Runnable class method that accepts new connections from the client.
     */
    @Override
    public void run() {
        System.out.println("Server ready!");
        acceptConnection();
    }
}
