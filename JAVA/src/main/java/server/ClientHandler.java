package server;
import game.GameLogic;


import game.GameLogic;
import java.io.*;
import java.net.*;

class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final GameLogic gameLogic;
    private final GameState gameState;
    private final String playerName;
    private PrintWriter out;

    public ClientHandler(Socket socket,  GameState gameState, String playerName) {
        this.clientSocket = socket;
        this.gameLogic = new GameLogic();
        this.gameState = gameState;
        gameState.addClient(this);
        this.playerName = playerName;
    }

    public void sendMessage(String message) {
        output.println(message);                   // Sends the message to the client's output stream
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            this.output = out;
            out.println("Welcome to the Guessing Game! Enter a number between 1 and 100.");
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                    handleClientRequest(inputLine);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

    private void handleClientRequest(String request) {
        try {
            int guess = gameLogic.validateGuess(request);         // Validate the guess (must be between 1-100)
            boolean isCorrect = gameState.makeGuess(this, guess); // Check if the guess is correct

            // Prepare the response based on the guess
            String response;
            if (isCorrect) {
                int updatedScore = gameState.getPlayerScore(this);
                response = "Correct! Your guess of " + guess + " was right. Your score: " + updatedScore;

                // Notify all players about the correct guess and updated scores
                broadcastToAllPlayers(playerName + " guessed correctly!the scores are: ");
                broadcastScores();

                // Check if the game has a winner or a draw
              //  if (gameState.checkForWinnerOrDraw()) {
              //      String winnerName = gameState.getWinnerName();
               //     broadcastToAllPlayers("Game Over! Winner: " + winnerName);
               //     gameState.resetGame();  // Reset the game for the next round
              //  }
            } else {
                response = "Incorrect! Your guess of " + guess + " was wrong. Try again!";
                broadcastToAllPlayers(playerName + " guessed " + guess + " but it was incorrect.");
            }

            // Send the response back to the player who made the guess
            sendMessage(response);

        } catch (IllegalArgumentException e) {
            // If the client's input is invalid, send an error message
            sendMessage("Invalid input: " + e.getMessage());
        }
    }

    private void broadcastScores() {
        StringBuilder scoresMessage = new StringBuilder("Current Scores:\n");
        for (Map.Entry<String, Integer> entry : gameState.getAllScores().entrySet()) {
            scoresMessage.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        for (ClientHandler client : gameState.getClients()) {
            client.sendMessage(scoresMessage.toString());
        }
    }

    // Notify all players about game events (like correct guesses or game over)
    private void broadcastToAllPlayers(String message) {
        for (ClientHandler client : gameState.getClients()) {
            client.sendMessage(message);
        }
    }


