package server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameState {
    private final Set<ClientHandler> clients = new HashSet<>();
    private final Map<ClientHandler, Integer> playerScores = new HashMap<>();
    private final GameLogic gameLogic;  // Add GameLogic instance
// Track scores per player

    public GameState(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    // Add a new client to the game
    public synchronized void addClient(ClientHandler client) {
        clients.add(client);
        playerScores.put(client, 0);  // Initialize their score to 0
    }

    // Process a guess and increment score if correct
    public synchronized boolean makeGuess(ClientHandler player, int guess) {

        if (gameLogic.checkGuessCorrectness(guess)) {
            int currentScore = playerScores.get(player);
            playerScores.put(player, currentScore + 1);
            return true;  // Correct guess
        }
        return false;  // Incorrect guess
    }


    // Get all connected clients
    public synchronized Set<ClientHandler> getClients() {
        return clients;
    }

    // Get a specific player's score
    public synchronized int getPlayerScore(ClientHandler player) {
        return playerScores.getOrDefault(player, 0);
    }

    // Get all player scores for broadcasting
    public synchronized Map<String, Integer> getAllScores() {
        Map<String, Integer> scores = new HashMap<>();
        for (ClientHandler client : playerScores.keySet()) {
            scores.put(client.getPlayerName(), playerScores.get(client));
        }
        return scores;
    }

    //  Check if there's a winner or a draw
    public synchronized boolean checkForWinnerOrDraw() {
        if (playerScores.isEmpty()) {
            return false;  // No players, no winner
        }
        int maxScore = Integer.MIN_VALUE;
        // First Pass: Find the highest score
        for (int score : playerScores.values()) {
            if (score > maxScore) {
                maxScore = score;
            }
        }
        // Second Pass: Count how many players have the max score
        int countMax = 0;
        for (int score : playerScores.values()) {
            if (score == maxScore) {
                countMax++;
            }
        }
        return countMax == 1;
    }

    public synchronized void resetGame() {
        System.out.println("Resetting game for a new round...");

        for (ClientHandler client : playerScores.keySet()) {
            playerScores.put(client, 0);
        }
    }
}
