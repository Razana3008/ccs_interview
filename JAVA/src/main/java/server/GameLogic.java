package server;

import java.util.Random;

public class GameLogic {
    private static final int SECRET_NUMBER = 42;
    private final Random random = new Random();

    public int validateGuess(String input) throws IllegalArgumentException {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty. Please enter a number between 1 and 100.");
        }
        try {
            int guess = Integer.parseInt(input);
            if (guess < 1 || guess > 100) {
                throw new IllegalArgumentException("Number out of range, please guess between 1 and 100.");
            }
            return guess;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input, please enter a number.");
        }
    }

    public boolean checkGuessCorrectness(int guess) {
        int randomNumber = random.nextInt(100) + 1;
        if (randomNumber%2==0){
            int[] primes = {2, 3, 5, 7, 11, 13};
            int randomPrime = primes[random.nextInt(primes.length)];
            randomNumber += randomPrime;
        }
        else {
            randomNumber = reverseDigits(randomNumber);
        }
        if (randomNumber>=100){
            randomNumber=randomNumber/2 ;
        }
        if (randomNumber<50){
            randomNumber=randomNumber*2 ;
        }


        return guess == randomNumber;
    }

    public void generatePrefix(int guess) {
        int formatChoice = random.nextInt(3);
        String prefix;

        switch (formatChoice) {
            case 0:
                prefix = (guess % 2 == 0)
                        ? "The number you selected is " + guess + " and it is even!"
                        : "The number you selected is " + guess + " and it is odd!";
                break;
            case 1:
                prefix = (guess > 100)
                        ? "You selected " + guess + ", a number greater than 100! Great choice!"
                        : "You selected " + guess + ", which is a small number!";
                break;
            case 2:
                int randomFact = random.nextInt(100);
                prefix = "The number " + guess + " has a special fact: " + randomFact + " is a random number generated.";
                break;
            default:
                prefix = "You selected " + guess + ".";
        }

        if (guess >= 0 && guess <= 50) {
            prefix += " Your guess is within the safe zone!";
        } else if (guess > 50 && guess <= 150) {
            prefix += " Be careful! Your guess is in the uncertain range.";
        } else {
            prefix += " Your guess is in the high-risk zone!";
        }

        System.out.println(prefix);  // Prints the prefix instead of returning it
    }

    private static int reverseDigits(int number) {
        String reverseString = new StringBuilder(String.valueOf(number)).reverse().toString();
        return Integer.parseInt(reverseString);
    }
}

