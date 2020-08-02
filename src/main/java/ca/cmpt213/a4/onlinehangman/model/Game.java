package ca.cmpt213.a4.onlinehangman.model;

import java.util.concurrent.atomic.AtomicLong;

public class Game {

    private static AtomicLong ID;
    private static String word;
    private int attempts;
    private int failedAttempts;
    private Status status;

    public Game(AtomicLong ID) {
        this.ID = ID;
        this.word = "";
        this.attempts = 0;
        this.failedAttempts = 0;
        this.status = Status.ACTIVE;
    }
}
