package ca.cmpt213.a4.onlinehangman.model;

import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// By Kevin Tang (301357455, kta76@sfu.ca)

/**
 * The model that handles the logic of this application
 */
public class Game {

    private final long ID; // Id that identifies the instance of this particular game
    private final String word; // Word answer
    private String wordState; // Current state of completion on the word denoted by "_" and letters
    private final int allowedFailAttempts; // Number of fails allowed
    private int attempts, successfulAttempts, failedAttempts; // Attempts counter
    private ArrayList<Character> correctGuesses; // Previous correct guesses made
    private Status status; // Current status of the game, either active, won, or lost

    /**
     * Creates a new game with specified id
     * @param ID The intended id for the new game
     * @throws FileNotFoundException
     */
    public Game(long ID) throws FileNotFoundException {

        // Initialization of word answer randomly picked from commonWords.txt
        ArrayList<String> wordList = new ArrayList<>();
        Scanner scanner = new Scanner(new File("src/commonWords.txt"));
        while (scanner.hasNext()) wordList.add(scanner.nextLine());
        this.word = wordList.get(new Random().nextInt(wordList.size()));

        // Initialization of all other fields
        this.ID = ID;
        this.allowedFailAttempts = 8;
        this.attempts = 0;
        this.successfulAttempts = 0;
        this.failedAttempts = 0;
        this.correctGuesses = new ArrayList<>();
        this.wordState = StringUtils.repeat("_", word.length());;
        this.status = Status.ACTIVE;
    }

    /**
     * Guess a letter in the word and check if it is correct
     * @param letter The letter to be guessed
     */
    public void attempt(char letter) {

        if (letter == ' ') return;
        attempts++;
        if (word.indexOf(letter) == -1) failedAttempts++;
        else if (!correctGuesses.contains(letter)) {
            successfulAttempts++;
            correctGuesses.add(letter);
            revealLetters(letter);
        }
        updateStatus();
    }

    /**
     * Updates game status based on wordState
     */
    public void updateStatus() {

        if (!wordState.contains("_")) status = Status.WON;
        else if (failedAttempts >= allowedFailAttempts) status = Status.LOST;
        else status = Status.ACTIVE;
    }

    /**
     * Reveals correctly guessed letter in wordState
     * @param letter The correctly guessed letter
     */
    private void revealLetters(char letter) {

        char[] wordCharArray = word.toCharArray();
        char[] tempCharArray = wordState.toCharArray();
        for (int i = 0; i < wordCharArray.length; i++) {
            if (wordCharArray[i]==letter) tempCharArray[i]=letter;
        }
        wordState = new String(tempCharArray);
    }

    // Getters
    public long getID() {
        return ID;
    }

    public String getWord() {
        return word;
    }

    public String getWordState() {
        return wordState;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getSuccessfulAttempts() {
        return successfulAttempts;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public Status getStatus() {
        return status;
    }
}
