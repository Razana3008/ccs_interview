package server;

import java.io.*;
import java.net.*;

public class GameServer {
    public static void main(String[] args) {
        int port = 8080;
        GameLogic gameLogic = new GameLogic();
        GameState gameState = new GameState(gameLogic);
        int playerCount = 0;



        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                playerCount++;

                String playerName = "Player " + playerCount;

                new ClientHandler(clientSocket, gameState, playerName).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
